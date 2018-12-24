import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class DateStrComparator {
    private int strLength;
    private int diffIndex;
    private int diffCount;
    private String start;
    private String end;

    public DateStrComparator(String start, String end) {
        this.start = start;
        this.end = end;
        this.diffIndex = stringCompareIndex();
        this.strLength = start.length();
        this.diffCount = this.strLength - this.diffIndex;
    }

    public String dealWith() {
        System.out.println("diffCount: "+diffCount);
        int numberLen = start.length();
        List<String> patternString = new ArrayList<>();
        innerDealPrefix(start.substring(0, diffIndex), start.substring(diffIndex, numberLen), end.substring(diffIndex, numberLen), patternString);
        return String.join("|", patternString);
    }

    private void innerDealPrefix(String prefix, String startStr, String endStr, List<String> patternStrList) {
        int prefixLen = prefix.length()
                , subStart = 0
                , subEnd = 1
                // 起始变化数字的第一位
                , startVariableNumber = Integer.parseInt(startStr.substring(subStart, subEnd))
                // 结束变化数字的第一位
                , endVariableNumber = Integer.parseInt(endStr.substring(subStart, subEnd))
                // 变化数字之间的差
                , variableNumberGap = endVariableNumber - startVariableNumber;
        // 是否是最终的数字
        boolean isLastNumDeal = startStr.length() == 1;
        // 如果变化数字之间的差仅仅为1，那么表示只需要prefix和suffix
        // 如果变化数字之间的差大于1，那么表示需要prefix,suffix和中间部分
        StringBuffer prefixBuffer = new StringBuffer(prefix).append(startVariableNumber);
        if (isLastNumDeal) {
            if (variableNumberGap == 0) {
                // 相等
                patternStrList.add("(" + prefix+startVariableNumber + ")");
            }else {
                patternStrList.add("(" + prefix+"["+startVariableNumber+"-"+endVariableNumber + "])");
            }
        }else {
            int startFirstNum = Integer.parseInt(startStr.substring(0, 1))
                    , endFirstNum = Integer.parseInt(endStr.substring(0, 1))
                    , padLen = startStr.length() - 1;
            int startIndex = 1;
            String padStr = "";
            // 下一个串如果是-
            if (startStr.length() > 1 && "-".equals(startStr.substring(1, 2))) {
                startIndex ++;
                padLen--;
                padStr = "-";
            }
            String startStrSub = startStr.substring(startIndex);
            String endStrSub = endStr.substring(startIndex);
            // 最少的部分
            innerDealPrefix(prefix+startFirstNum+padStr, startStrSub, replaceAllNumber(startStrSub, "9"), patternStrList);
            // 最多的部分
            innerDealPrefix(prefix+endFirstNum+padStr, replaceAllNumber(endStrSub, "0"), endStrSub, patternStrList);
            // 中间的部分
            if (variableNumberGap > 1) {
                // 将所有的字符串替换为\\d
                String addPatternStr = "(" + prefix + "[" + (startVariableNumber + 1) + "-" + (endVariableNumber - 1) + "]"+padStr + replaceAllNumber(startStrSub, "\\\\d") + ")";
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