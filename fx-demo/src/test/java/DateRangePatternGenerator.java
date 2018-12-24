public class DateRangePatternGenerator {

    private DateModel startDate;
    private DateModel endDate;

    public DateRangePatternGenerator(String startDateStr, String endDateStr) {
        this.startDate = DateModel.getInstance(startDateStr);
        this.endDate = DateModel.getInstance(endDateStr);
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
