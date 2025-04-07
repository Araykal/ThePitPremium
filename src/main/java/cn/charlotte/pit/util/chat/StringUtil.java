package cn.charlotte.pit.util.chat;

import java.text.DecimalFormat;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/3 22:44
 */
public class StringUtil {
    private static final long million = 100000000;
    private static final long tenkilo = 10000;
    public static DecimalFormat decimalFormat = new java.text.DecimalFormat("0.00");

    public static String getFormatLong(long input) {
        boolean negative = input < 0;
        input = Math.abs(input);

        if (input > million) {
            return (negative ? "-" : "") + decimalFormat.format((double) input / (double) million) + " 亿";
        } else if (input > tenkilo) {
            return (negative ? "-" : "") + decimalFormat.format((double) input / (double) tenkilo) + " 万";
        } else {
            return (negative ? "-" : "") + input + "";
        }

    }
}
