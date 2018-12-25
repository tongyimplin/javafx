package top.jafar.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 根据日期时间生成正则匹配
 * top.jafar.util.DateStrComparator comparator = new top.jafar.util.DateStrComparator("1998-01-06", "2012-04-06");
 * String dateRangeRegexp = comparator.dealWith();
 * Pattern pattern = Pattern.compile(dateRangeRegexp);
 * boolean isValid = pattern.matcher("2013-04-06").find();
 *
 * 或者
 * top.jafar.util.DateStrComparator comparator = new top.jafar.util.DateStrComparator("1998-01-06", "2012-04-06");
 * boolean isValid = comparator.isValid("2013-04-06");
 */
public class DateStrComparator {
    private int strLength;
    private int diffIndex;
    private int diffCount;
    private String start;
    private String end;
    private String dateRangeReg;

    public DateStrComparator(String start, String end) {
        this.start = start;
        this.end = end;
        this.diffIndex = stringCompareIndex();
        this.strLength = start.length();
        this.diffCount = this.strLength - this.diffIndex;
    }

    /**
     * 校验日期是否合法
     * @param dateStr
     * @return
     */
    public boolean isValid(String dateStr) {
        if (dateRangeReg == null) {
            dateRangeReg = dealWith();
        }
        return Pattern.compile(dateRangeReg).matcher(dateStr).find();
    }

    public String dealWith() {
        System.out.println("diffCount: "+diffCount);
        int numberLen = start.length();
        List<String> patternString = new ArrayList<>();
        innerDealPrefix(start.substring(0, diffIndex), start.substring(diffIndex, numberLen), end.substring(diffIndex, numberLen), patternString);
        dateRangeReg = String.join("|", patternString);
        return dateRangeReg;
    }

    private void innerDealPrefix(String prefix, String startStr, String endStr, List<String> patternStrList) {
        // 计算prefix
        boolean flag = true;
        String startNextChar, endNextChar;
        int nextCharIndex = 0;
        do {
            try {
                startNextChar = String.valueOf(startStr.charAt(nextCharIndex));
                endNextChar = String.valueOf(endStr.charAt(nextCharIndex));
                flag = (prefix + startNextChar).equals(prefix + endNextChar);
                if (flag) {
                    prefix = prefix + startNextChar;
                    startStr = startStr.substring(1);
                    endStr = endStr.substring(1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (flag);
        int
                // 起始变化数字的第一位
                startVariableNumber = Integer.parseInt(String.valueOf(startStr.charAt(0)))
                // 结束变化数字的第一位
                , endVariableNumber = Integer.parseInt(String.valueOf(endStr.charAt(0)))
                // 变化数字之间的差
                , variableNumberGap = endVariableNumber - startVariableNumber;
        // 是否是最终的数字
        boolean isLastNumDeal = startStr.length() == 1;
        // 如果变化数字之间的差仅仅为1，那么表示只需要prefix和suffix
        // 如果变化数字之间的差大于1，那么表示需要prefix,suffix和中间部分
        if (isLastNumDeal) {
            if (variableNumberGap == 0) {
                // 相等
                patternStrList.add("(" + prefix+startVariableNumber + ")");
            }else {
                patternStrList.add("(" + prefix+"["+startVariableNumber+"-"+endVariableNumber + "])");
            }
        }else {
          int startIndex = 1;
            String startStrSub = startStr.substring(startIndex);
            String endStrSub = endStr.substring(startIndex);
            // 最少的部分
            innerDealPrefix(prefix+startVariableNumber, startStrSub, replaceAllNumber(startStrSub, "9"), patternStrList);
            // 最多的部分
            innerDealPrefix(prefix+endVariableNumber, replaceAllNumber(endStrSub, "0"), endStrSub, patternStrList);
            // 中间的部分
            if (variableNumberGap > 1) {
                // 将所有的字符串替换为\\d
                String addPatternStr = "(" + prefix + "[" + (startVariableNumber + 1) + "-" + (endVariableNumber - 1) + "]" + replaceAllNumber(startStrSub, "\\\\d") + ")";
                patternStrList.add(addPatternStr);
            }
        }
    }

    private String replaceAllNumber(String string, String replaceChar) {
        if ("9".equals(replaceChar) && string.length() > 1) {
            // 最大值
            String tpl = "9999-12-31";
            String substring = tpl.substring(tpl.length() - string.length());
            if (string.length() == 4) {
                return "9-31";
            }
            return substring;
            // 取最大值时，仅在
        } else if ("0".equals(replaceChar) && string.length() == 6) {
            return "-01-01";
        }
        return Pattern.compile("\\d").matcher(string).replaceAll(replaceChar);
    }

    /**
     * 获取两个数字字符串不相等的位置
     * @return
     */
    private int stringCompareIndex() {
        char[] chars1 = start.toCharArray();
        char[] chars2 = end.toCharArray();
        int idx = 0;
        while (idx < chars1.length) {
            if (chars1[idx] != chars2[idx]) {
                return idx;
            }
            idx++;
        }
        return -1;
    }
}