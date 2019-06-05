package  com.demo.frame.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.demo.frame.common.http.BaseResponse;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户基本信息
 */
public class UserBean extends BaseResponse implements Serializable{

    public DataBean data;

    public static UserBean objectFromData(String str) {

        return new Gson().fromJson(str, UserBean.class);
    }

    public static List<UserBean> arrayUserBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<UserBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static class DataBean implements Serializable {
        public int id;
        public String name;
        public String id_card;
        public String mobile;
        public String autograph;
        public String portrait;
        public int is_complete;
        public int organization_id;
        public int company_id;
        public String company_name;
        public String organization_name;

        public static DataBean objectFromData(String str) {

            return new Gson().fromJson(str, DataBean.class);
        }

        public static List<DataBean> arrayDataBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<DataBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }
    }
}
