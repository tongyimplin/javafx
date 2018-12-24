import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatePatternTest {

    @Test
    public void testRange() {
        String timeRangeStr = "2018-10-29 10:00:32~2018-12-29 10:00:32";


    }


    @Test
    public void testValid() {
        // 要求时间为1945-04-02日到2014-06-15日
        StringBuffer patternBuffer = new StringBuffer();
        patternBuffer
                .append("(")
                //=== 年
                // 开始的部分
                .append("1945-")
                    //=== 月
                    .append("(")
                    .append("04-(0[2-9])|([1-3]\\d)")
                    .append(")|(")
                    .append("(0[5-9])|(1[12])-\\d{2}")
                    .append(")")
                .append(")|(")
                // 中间部分1946-01-01, 2013-12-31
                .append("(194[6-9])|(19[5-9]\\d)|(200\\d)|(201[1-3])-\\d{2}-\\d{2}")
                .append(")|(")
                // 结尾部分
                .append("2014-")
                    //=== 月
                    .append("(")
                    .append("[0][1-5]-\\d{2}")
                    .append(")|(")
                    .append("06-0\\d")
                    .append(")|(")
                    .append("06-1[0-5]")
                    .append(")")
                .append(")");
        String patternStr = patternBuffer.toString();
        String dateStr = "1945-04-15";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(dateStr);
        if (matcher.find()) {
            System.out.println("合法!");
        }else {
            System.out.println("不合法!");
        }
    }

    @Test
    public void numberCompare() {
        String start = "1845";
        String end = "1964";
        System.out.println("start: "+start);
        System.out.println("end: "+end);
        DateStrComparator comparator = new DateStrComparator(start, end);
        String patternStr = comparator.dealWith();
        System.out.println("结果: "+patternStr);
    }



}
class DateStrComparator {
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
        /*if (diffCount == 1) {
            return dealWithDiffOne();
        } else if (diffCount == 2) {
            return dealWithDiffTwo();
        } else if (diffCount == 3) {
            return dealWithDiffThree();
        } else if (diffCount == 4) {
            return dealWithDiffFour();
        }*/
        return String.join("|", patternString);
//        throw new RuntimeException("不符的diffCount: "+diffCount);
    }

    private String dealWithDiffFour() {
        /*for (int i = 0; i < variableLength; i++) {
            // 是否是结尾
            boolean isSuffix = i == variableLength - 1;
            // 是否是开头
            boolean isPreffix = i == 0 && !isSuffix;
            if (isSuffix) {
                String prefix = end.substring(0, compareIndex);
                String endVariableNumber = end.substring(compareIndex, compareIndex+1);
                matchPrefixList[i] = String.format("(%s[0-%s])", prefix, endVariableNumber);
            } else if (isPreffix) {
                String prefix = start.substring(0, compareIndex);
                String variableNumber = start.substring(compareIndex, compareIndex+1);
                matchPrefixList[i] = String.format("(%s[%s-9])", prefix, variableNumber);
            }else {
                // 中间部分
            }
        }*/
        return "";
    }

    private String dealWithDiffThree() {
        // 字符串长度
        int strLen = start.length();
        String prefix = start.substring(0, diffIndex);
        int prefixLen = prefix.length();
        int startVariableNumber = Integer.parseInt(start.substring(diffIndex, diffIndex + 1));
        int endVariableNumber = Integer.parseInt(end.substring(diffIndex, diffIndex + 1));
        int variableNumberGap = endVariableNumber - startVariableNumber;
        int matchPrefixListLength = variableNumberGap > 1 ? 3 : 2;
        String[] matchPrefixList = new String[matchPrefixListLength];
        // 这里分两种情况
        // 1. 差异位第1位的数字相差等于1的情况
        // 2. 差异位第1位的数字相差大于1的情况
        /*for (int i = 0; i < matchPrefixListLength; i++) {
            // 是否结尾
            boolean isSuffix = i == matchPrefixListLength - 1;
            // 是否开始
            boolean isPreffix = i == 0;
            StringBuffer buffer = new StringBuffer(prefix);
            if (isPreffix) {
                buffer
                        .append(startVariableNumber)
                        .append(start.charAt(prefixLen+1))
                ;
            } else if (isSuffix) {
                buffer.append(endVariableNumber)
                        .append(end.charAt(prefixLen + 1))
                        ;
            }else {
                // 中间部分
                // 差异化的第一位
                if (variableNumberGap > 2) {
                    buffer.append("[")
                            .append(startVariableNumber+1)
                            .append("-")
                            .append(endVariableNumber-1)
                            .append("]");
                }else {
                    buffer.append(startVariableNumber + 1);
                }
                ;
            }
        }*/
//        int variableLength = strLen - compareIndex;
        // 总体分为3种情况
        return String.join("|", matchPrefixList);
    }

    private String dealWithDiffTwo() {
        // 字符串长度
        /*int strLen = start.length();
        String prefix = start.substring(0, diffIndex);
        int startVariableNumber = Integer.parseInt(start.substring(diffIndex, diffIndex + 1));
        int endVariableNumber = Integer.parseInt(end.substring(diffIndex, diffIndex + 1));
        int variableNumberGap = endVariableNumber - startVariableNumber;
        int matchPrefixListLength = variableNumberGap > 1 ? 3 : 2;
        String[] matchPrefixList = new String[matchPrefixListLength];
        // 这里分两种情况
        // 1. 差异位第1位的数字相差等于1的情况
        // 2. 差异位第1位的数字相差大于1的情况
        for (int i = 0; i < matchPrefixListLength; i++) {
            // 是否结尾
            boolean isSuffix = i == matchPrefixListLength - 1;
            // 是否开始
            boolean isPreffix = i == 0;
            StringBuffer buffer = new StringBuffer("(").append(prefix);
            if (isPreffix) {
                buffer
                        .append(startVariableNumber)
                        .append("[")
                        .append(start.charAt(strLen-1))
                        .append("-9]")
                ;
            } else if (isSuffix) {
                buffer.append(endVariableNumber)
                        .append("[0-")
                        .append(end.charAt(strLen - 1))
                        .append("]");
            }else {
                // 中间部分
                // 差异化的第一位
                if (variableNumberGap > 2) {
                    buffer.append("[")
                            .append(startVariableNumber+1)
                            .append("-")
                            .append(endVariableNumber-1)
                            .append("]");
                }else {
                    buffer.append(startVariableNumber + 1);
                }
                buffer.append("\\d");
            }
            buffer.append(")");
            matchPrefixList[i] = buffer.toString();
        }
//        int variableLength = strLen - compareIndex;
        // 总体分为3种情况
        return String.join("|", matchPrefixList);*/
        return "";
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
            // 最少的部分
            innerDealPrefix(prefix+startVariableNumber, startStr.substring(1), endStr.substring(1, 2)+ StringUtils.repeat("0", startStr.length()-2), patternStrList);
            // 最多的部分
            innerDealPrefix(prefix+endVariableNumber, startStr.substring(1), endStr.substring(1), patternStrList);
            // 中间的部分
            if (variableNumberGap > 2) {
                patternStrList.add("(" + prefix + "[" + (startVariableNumber + 1) + "-" + (endVariableNumber - 1) + "]\\d{"+(startStr.length()-1)+"}" + "])");
            }
        }
    }

    private String dealWithDiffOne() {
        int diffIndex = strLength - 1;
        return start.substring(0, diffIndex)+"["+start.substring(diffIndex,strLength)+"-"+end.substring(diffIndex,strLength)+"]";
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
