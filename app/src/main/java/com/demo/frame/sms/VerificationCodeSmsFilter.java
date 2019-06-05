package com.demo.frame.sms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 短信验证码过滤器
 */
public class VerificationCodeSmsFilter implements SmsFilter {
    /**
     * 需要过滤的发短信的人
     */
    private String filterAddress;

    public VerificationCodeSmsFilter(String filterAddress) {
        this.filterAddress = filterAddress;
    }

    @Override
    public String filter(String address, String smsContent) {
        if (address.endsWith(filterAddress)) {
            Pattern pattern = Pattern.compile("(\\d{4,8})");//匹配4-8位的数字
            Matcher matcher = pattern.matcher(smsContent);
            if (matcher.find()) {
                return matcher.group(0);
            }
        }
        return null;
    }
}
