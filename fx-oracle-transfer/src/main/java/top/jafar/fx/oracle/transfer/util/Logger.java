package top.jafar.fx.oracle.transfer.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void log(String msg) {
        System.out.println(String.format("[%s] %s", DATE_FORMATTER.format(new Date()), msg));
    }

}
