package top.jafar.fx.oracle.transfer.util;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class DateHelper {

    /**
     * 无分隔符: 年与日时分秒毫秒
     */
    public static DateTimeFormatter DATE_TIME_FORMATTER_PURE_NUMBER = DateTimeFormat.forPattern("yyyyMMddHHmmssSSS");
    public static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    static {
        DATE_TIME_FORMATTER.withLocale(Locale.US);
        DATE_TIME_FORMATTER_PURE_NUMBER.withLocale(Locale.US);
    }

    public static String nowTimeStamp() {
        return DATE_TIME_FORMATTER.print(LocalTime.now());
    }
    public static String nowTimeStampPureNumber() {
//        return DATE_TIME_FORMATTER_PURE_NUMBER.print(LocalTime.now());
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }

}
