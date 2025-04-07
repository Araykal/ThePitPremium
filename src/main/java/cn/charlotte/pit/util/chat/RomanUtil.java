package cn.charlotte.pit.util.chat;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/3 23:52
 */
public class RomanUtil {
    public static final String[][] roman = {
            {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"},  // 个位数举例
            {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"},  // 十位数举例
            {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"},  // 百位数举例
            {"", "M", "MM", "MMM"}  // 千位数举例
    };


    /**
     * @param number input
     * @return roman number
     * <p>
     * 思路是：千位先除以1000然后模10，百位先除以100然后模10，十位先除以10然后模10，个位直接模10.
     */
    public static String convert(int number) {

        return roman[3][number / 1000 % 10] +
                roman[2][number / 100 % 10] +
                roman[1][number / 10 % 10] +
                roman[0][number % 10];
    }
}
