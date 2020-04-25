package com.muye.rocket.tools.filter;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditTextFilter implements InputFilter {
    private final int mMax;

    Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\u4E00-\\u9FA5_,.?!:;…~_\\-\"\"/@*+'()<>{}/[/]()<>{}\\[\\]=%&$|\\/♀♂#¥£¢€\"^` ，。？！：；……～“”、“（）”、（——）‘’＠‘·’＆＊＃《》￥《〈〉》〈＄〉［］￡［］｛｝｛｝￠【】【】％〖〗〖〗／〔〕〔〕＼『』『』＾「」「」｜﹁﹂｀．]");

    public EditTextFilter(int max) {
        mMax = max;
    }

    public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                               int dstart, int dend) {
        if (source.equals(" ")) {
            return "";
        }

        if (dest.toString().trim().length() > mMax) {
            return "";
        }

        Matcher matcher = pattern.matcher(source);
        if (!matcher.find()) {
            return null;
        } else {
            return "";
        }
    }

    public int getMax() {
        return mMax;
    }
}
