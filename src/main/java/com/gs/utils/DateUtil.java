package org.nocheater.paycenterserver.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 处理时间的工具类
 * @Author：guozhenyu(guozhenyu112@163.com)
 * @date: 2018/9/15.
 */
public class DateUtil {

    /**
     * 获取系统当前时间
     * @return
     */
    public static Date getCurrDate() {
        // 获取系统当前时间
        SimpleDateFormat std = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 将字符串转换为Date格式
        Date date = null;
        try {
            date = std.parse(std.format(new Date()).toString());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 给定格式和参数格式要统一不然就会抛出异常
        return date;
    }

    public static String getCurrDateStr() {
        // 获取系统当前时间
        SimpleDateFormat std = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 将字符串转换为Date格式
        return std.format(new Date()).toString();
        // 给定格式和参数格式要统一不然就会抛出异常
    }

    /**
     * 获取系统当前年份
     *
     * @return
     */
    public static String getCurrYear() {
        // 获取系统当前时间
        SimpleDateFormat std = new SimpleDateFormat("yyyy");
        // 将字符串转换为Date格式
        System.out.println(std.format(new Date()).toString());
        return std.format(new Date()).toString();
    }

    /**
     * 获取两时间差
     *
     * @throws ParseException
     */
    public static long getshijiancha(Date da) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date now = df.parse(df.format(new Date()).toString());
        java.util.Date date = df.parse(df.format(da).toString());
        long l = now.getTime() - date.getTime();
        return l;
    }
    //生成唯一文件名
    public static String getSHC() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str = sdf.format(date);
        return str;
    }

    public static Date getBeginDayOfWeek() {
        Date date = new Date();
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek);
        return cal.getTime();
    }

    public static Date getEndDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return weekEndSta;
    }

    /**
     * 获取本周的开始日期
     * @return java.lang.String
     */
    public static String getBeginDayWeek() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(getBeginDayOfWeek());
    }

    /**
     * 获取本周的结束日期
     * @return java.lang.String
     */
    public static String getEndDayWeek() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(getEndDayOfWeek());
    }


    /**
     * 收集起始时间到结束时间之间所有的日期并以集合的方式返回
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<String> CollectLocalDates(String startTime, String endTime) {
        return collectLocalDates(LocalDate.parse(startTime), LocalDate.parse(endTime));
    }

    /**
     * 计算时间差
     * @return
     */
    public static String getDatePoor(Date startDate, Date endDate) {
        if (null == startDate || null == endDate) {
            return "";
        }
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        // 获得两个时间的毫秒时间差异
        Long diff = endDate.getTime() - startDate.getTime();
        // 计算差多少天
//        Long day = diff / nd;
        // 计算差多少小时
        Long hour = diff % nd / nh;
        // 计算差多少分钟
        Long min = diff % nd % nh / nm;
        // 计算差多少秒
        Long sec = diff % nd % nh % nm / ns;

        return (hour.toString().length() >1 ? "" : "0") + hour + ":"
                + (min.toString().length() > 1 ? "" : "0") + min + ":"
                + (sec.toString().length() > 1 ? "" : "0") + sec;
    }

    /**
     * 收集起始时间到结束时间之间所有的日期并以集合的方式返回
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<String> collectLocalDates(LocalDate startTime, LocalDate endTime) {
        return Stream.iterate(startTime, LocalDate -> LocalDate.plusDays(1))
                .limit(ChronoUnit.DAYS.between(startTime, endTime) + 1)
                .map(LocalDate::toString)
                .collect(Collectors.toList());
    }

    /**
     * 格式化时间
     * @return
     */
    public static String getDatePoorS(Long s) {
        if (null == s) {
            return "";
        }
        long nd = 24 * 60 * 60;
        long nh = 60 * 60;
        long nm = 60;
        long ns = 1;
        // 获得两个时间的毫秒时间差异
        Long diff = s;
        // 计算差多少天
//        Long day = diff / nd;
        // 计算差多少小时
        Long hour = diff % nd / nh;
        // 计算差多少分钟
        Long min = diff % nd % nh / nm;
        // 计算差多少秒
        Long sec = diff % nd % nh % nm / ns;

        return (hour.toString().length() >1 ? "" : "0") + hour + ":"
                + (min.toString().length() > 1 ? "" : "0") + min + ":"
                + (sec.toString().length() > 1 ? "" : "0") + sec;
    }

    public static void main(String[] args) {

        System.out.println(getBeginDayWeek());
        System.out.println(getEndDayWeek());
        System.out.println(CollectLocalDates(getBeginDayWeek(), getEndDayWeek()));
        System.out.println(getFormatAge(1995,3,12));
    }

    /**
     * 生成订单号
     */
    public static String generateOutTradeNo() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str = sdf.format(date);
        return str.concat("1010");
    }

    /**
     * 格式化出生日期并得到年龄
     * @param year
     * @param month
     * @param day
     */
    public static Integer getFormatAge(Integer year, Integer month, Integer day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        if (null == year || null == month || null == day
                || 0 == year || 0 == month || 0 == day) {
            return null;
        }
        try {
            Date birthDay = sdf.parse(year + "-" + month + "-" + day);
            return getAge(birthDay);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 由出生日期获得年龄
     * @param birthDay
     * @return
     */
    public static int getAge(Date birthDay) throws Exception {
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) {
            throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            }else{
                age--;
            }
        }
        return age;
    }
}

