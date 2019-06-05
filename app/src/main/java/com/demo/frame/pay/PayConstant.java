package com.demo.frame.pay;

/**
 * 说明：PayConstant
 */
public class PayConstant {
    public final static String PAY_MONEY ="PAY_MONEY";
    public final static String PAY_ORDER_ID ="PAY_ORDER_ID";

    public final static String WX_APP_ID = "wx3fae79024e14aea9";
    //
    public final static int PAY_RESULT_OK = 2;// 已成功的付款记录

    public interface Result{
        int SUCCESS = 1;
        int CANCEL = -1;
        int FAIL = -2;
    }
}
