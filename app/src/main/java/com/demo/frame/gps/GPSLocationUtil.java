package com.demo.frame.gps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.fast.library.utils.LogUtils;

import java.util.Iterator;

public class GPSLocationUtil {

    private static final long REFRESH_TIME = 1000L;
    private static final float METER_POSITION = 1.0f;
    private static ILocationListener mLocationListener;
    private static LocationListener listener = new MyLocationListener();
    private static Context mContext = null;

    private static class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {//定位改变监听
            LogUtils.e("onLocationChanged:GPS location =" + location.toString());
//            RxToast.success(location.toString());
            if (mLocationListener != null) {
                mLocationListener.onSuccessLocation(location);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            LogUtils.e("onStatusChanged:GPS provider =" + provider + ",status=" + status);
        }

        @Override
        public void onProviderEnabled(String provider) {
            LogUtils.e("onProviderEnabled:GPS provider =" + provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            LogUtils.e("onProviderDisabled:GPS provider =" + provider);
        }
    }

    /**
     * GPS获取定位方式
     */
    public static Location getGPSLocation(@NonNull Context context) {
        Location location = null;
        mContext = context;
        LocationManager manager = getLocationManager(context);
        //高版本的权限检查
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

//        manager.addGpsStatusListener(gpsStatusListener);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {//是否支持GPS定位
            //获取最后的GPS定位信息，如果是第一次打开，一般会拿不到定位信息，一般可以请求监听，在有效的时间范围可以获取定位信息
            location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        return location;
    }

    /**
     * network获取定位方式
     */
    public static Location getNetWorkLocation(Context context) {
        Location location = null;
        mContext = context;
        LocationManager manager = getLocationManager(context);

        //高版本的权限检查
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //请求权限
            return null;
        }

//        manager.addGpsStatusListener(gpsStatusListener);
        if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {//是否支持Network定位
            //获取最后的network定位信息
            location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        return location;
    }

    /**
     * 获取最好的定位方式
     */
    public static Location getBestLocation(Context context, Criteria criteria) {
        Location location;
        mContext = context;
        LocationManager manager = getLocationManager(context);
        if (criteria == null) {
            criteria = new Criteria();
        }
        String provider = manager.getBestProvider(criteria, true);
        if (TextUtils.isEmpty(provider)) {
            //如果找不到最适合的定位，使用network定位
            location = getNetWorkLocation(context);
        } else {
            //高版本的权限检查
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }

//            manager.addGpsStatusListener(gpsStatusListener);
            //获取最适合的定位方式的最后的定位权限
            location = manager.getLastKnownLocation(provider);
        }
        return location;
    }

    /**
     * 定位监听
     */
    public static void addLocationListener(Context context, String provider, ILocationListener locationListener) {

        addLocationListener(context, provider, REFRESH_TIME, METER_POSITION, locationListener);
    }

    /**
     * 定位监听
     */
    public static void addLocationListener(Context context, String provider, long time, float meter, ILocationListener locationListener) {
        if (locationListener != null) {
            mLocationListener = locationListener;
        }
        if (listener == null) {
            listener = new MyLocationListener();
        }
        LocationManager manager = getLocationManager(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.requestLocationUpdates(provider, time, meter, listener);
    }

    /**
     * 取消定位监听
     */
    public static void unRegisterListener(Context context) {
        if (listener != null) {
            LocationManager manager = getLocationManager(context);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //移除定位监听
            manager.removeUpdates(listener);
        }

        mContext = null;
    }

    private static LocationManager getLocationManager(@NonNull Context context) {
        return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * 自定义接口
     */
    public interface ILocationListener {
        void onSuccessLocation(Location location);
    }

    private static GpsStatus.Listener gpsStatusListener = new GpsStatus.Listener() {
        @Override
        public void onGpsStatusChanged(int event) {
            switch (event) {
                //卫星状态改变
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:

                    LocationManager manager = getLocationManager(mContext);
                    @SuppressLint("MissingPermission")
                    GpsStatus gpsStatus = manager.getGpsStatus(null);
                    //获取卫星颗数的默认最大值
                    int maxSatellites = gpsStatus.getMaxSatellites();
                    //获取所有的卫星
                    Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
                    //卫星颗数统计
                    int count = 0;
                    StringBuilder sb = new StringBuilder();
                    while (iters.hasNext() && count <= maxSatellites) {
                        count++;
                        GpsSatellite s = iters.next();

                        //                        float azimuth = s.getAzimuth(); //方向角
                        //                        float elevation = s.getElevation();//高度角
                        //根据方向角和高度角计算出，卫星显示的位置
//                        Point point = new Point();
//                        int x = mEarthHeartX; //左边地球圆形的圆心位置X坐标
//                        int y = mEarthHeartY; //左边地球圆形的圆心位置Y坐标
//                        int r = mEarthR;
//                        x+=(int)((r*elevation*Math.sin(Math.PI*azimuth/180)/90));
//                        y-=(int)((r*elevation*Math.cos(Math.PI*azimuth/180)/90));
//                        point.x = x;
//                        point.y = y;//point就是你需要绘画卫星图的起始坐标

                        //卫星的信噪比
                        float snr = s.getSnr();
                        sb.append("第").append(count).append("颗").append("：").append(snr).append("\n");
                    }
                    LogUtils.e("卫星信噪比：", sb.toString());
                    break;
                case GpsStatus.GPS_EVENT_STARTED: // 定位启动
                    break;
                case GpsStatus.GPS_EVENT_STOPPED: // 定位结束
                    break;
                default:
                    break;
            }
        }
    };
}
