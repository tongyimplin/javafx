import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
        String start = "1845-06-04";
        String end = "1966-10-25";
        System.out.println("start: "+start);
        System.out.println("end: "+end);
        DateStrComparator comparator = new DateStrComparator(start, end);
        String patternStr = comparator.dealWith();
        System.out.println("结果: "+patternStr);
        Pattern pattern = Pattern.compile(patternStr);
        long oneDays = TimeUnit.DAYS.toMillis(1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startDate = dateFormat.parse(start);
            Date endDate = dateFormat.parse(end);
            long startDateMillis = startDate.getTime();
            long endDateMillis = endDate.getTime();
            for (long i = startDateMillis; i <= endDateMillis; i += oneDays) {
                Date tempDate = new Date(i);
                String format = dateFormat.format(tempDate);
                if (!pattern.matcher(format).find()) {
                    System.out.println(format+" -> 不合法");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }



}

