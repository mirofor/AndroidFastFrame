package com.fast.library.span;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.widget.TextView;

import com.fast.library.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class SpanTextUtils {

    private static float fontScale = 0.0f;

    public static void setText(TextView tv, SpanSetting... settings) {
        if (settings.length > 0) {
            List<SpanSetting> list = new ArrayList<SpanSetting>();
            for (int i = 0; i < settings.length; i++) {
                list.add(settings[i]);
            }
            setText(tv, list);
        }
    }

    public static void setText(TextView tv, List<SpanSetting> charSequences) {
        if (tv == null) {
            return;
        }
        fontScale = tv.getContext().getResources().getDisplayMetrics().scaledDensity;
        SpannableStringBuilder builder = new SpannableStringBuilder();
        if (check(charSequences)) {
            for (int i = 0; i < charSequences.size(); i++) {
                final SpanSetting setting = charSequences.get(i);
                if (setting == null){
                    continue;
                }
//                设置图片
                if (setting.getPic() > 0){
                    setting.setCharSequence("icon");
                    SpannableString spannableString = new SpannableString(
                            setting.getCharSequence());
                    VerticalImageSpan imgSpan = new VerticalImageSpan(tv.getContext(),setting.getPic());
                    spannableString.setSpan(imgSpan,0, setting.getCharSequence().length(),
                            Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    builder.append(spannableString);
                    continue;
                }
//                设置文字
                if (setting.getCharSequence() == null
                        || TextUtils.isEmpty(setting.getCharSequence()
                        .toString())) {
                    continue;
                }
                SpannableString spannableString = new SpannableString(
                        setting.getCharSequence());
                // 设置字体大小
                float fontSize = setting.getFontSize();
                if (fontSize != 0.0f) {
                    fontSize = UIUtils.sp2px(fontSize);
                } else {
                    fontSize = tv.getTextSize();
                }
                spannableString.setSpan(
                        new AbsoluteSizeSpan((int) fontSize), 0, setting
                                .getCharSequence().length(),
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                // 设置属性
                if (setting.getColor() == Color.TRANSPARENT) {
                    setting.setColor(tv.getCurrentTextColor());
                }
                // 设置点击事件
                settingOnClickSpan(spannableString, setting, i);
                builder.append(spannableString);
            }
        }
        if (builder.length() > 0) {
            tv.setHighlightColor(Color.TRANSPARENT);
            tv.setClickable(true);
            tv.setMovementMethod(LinkMovementMethod.getInstance());
            tv.setText(builder);
        }
    }

    private static void settingOnClickSpan(SpannableString spannableString,
                                           final SpanSetting setting, int position) {
        spannableString.setSpan(new CustomClickSpan(setting, position), 0,
                setting.getCharSequence().length(),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    private static boolean check(List<SpanSetting> charSequences) {
        return !(charSequences == null || charSequences.isEmpty());
    }

}
