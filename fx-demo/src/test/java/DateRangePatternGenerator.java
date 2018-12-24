import java.util.regex.Pattern;

public class DateRangePatternGenerator {

    // 校验日期的合法性
    private Pattern DATE_PATTERN = Pattern.compile("(1[7-9]\\d{2})|(2[012]\\d\\d)-((0[1,3,5,7,8])|(1[02])-([012]\\d)|(3[01]))|((0[4,6,9])|(11)-([012]\\d)|(30))");
    // 校验时间的合法性
    private Pattern TIME_PATTERN = Pattern.compile("([01]\\d)|(2[0-3]):[0-5]\\d:[0-5]\\d");
    private DateModel startDate;
    private DateModel endDate;

    public DateRangePatternGenerator(String startDateStr, String endDateStr) {
        // 校验合法性
        boolean isStartValid = false;
        boolean isEndValid = false;
        boolean isStartValid1 = true;
        boolean isEndValid1 = true;
        String[] startDateStrSplit = startDateStr.split(" ");
        String[] endDateStrSplit = endDateStr.split(" ");
        if (startDateStrSplit.length == 1 && endDateStrSplit.length == 1) {
            isStartValid = DATE_PATTERN.matcher(startDateStrSplit[0]).find();
            isEndValid = DATE_PATTERN.matcher(endDateStrSplit[0]).find();
        }else {
            isStartValid1 = TIME_PATTERN.matcher(startDateStrSplit[1]).find();
            isEndValid1 = TIME_PATTERN.matcher(endDateStrSplit[1]).find();
        }
        if (isStartValid && isEndValid && isStartValid1 && isEndValid1) {
            this.startDate = DateModel.getInstance(startDateStr);
            this.endDate = DateModel.getInstance(endDateStr);
        }else {
            throw new RuntimeException("日期格式不合法");
        }
    }

    /**
     * 日期模型
     */
    static class DateModel {
        private String year;
        private String month;
        private String day;

        /**
         * 获取实例
         * @param dateStr
         * @return
         */
        public static DateModel getInstance(String dateStr) {
            return new DateModel(dateStr.substring(0, 4), dateStr.substring(5, 7), dateStr.substring(8, 10));
        }

        public DateModel(String year, String month, String day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }
    }

    /**
     * 获取正则匹配表达式
     * @return
     */
    public String getPatternStr() {
        StringBuffer buffer = new StringBuffer();

        return buffer.toString();
    }

    /**
     * 重置起始日期
     * @param startDateStr
     * @param endDateStr
     */
    public DateRangePatternGenerator resetRange(String startDateStr, String endDateStr) {
        this.startDate = DateModel.getInstance(startDateStr);
        this.endDate = DateModel.getInstance(endDateStr);
        return this;
    }
}
