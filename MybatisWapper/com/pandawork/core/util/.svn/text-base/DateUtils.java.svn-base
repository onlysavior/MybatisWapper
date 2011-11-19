package com.pandawork.core.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author qq
 * @date 2010-10-20
 * @sine 1.0.0
 */
public class DateUtils {

    /**
     * 计算两个时间之间相隔天数
     * 
     * @param startday 开始时间
     * @param endday 结束时间
     * @return
     */
    public static int getIntervalDays(Calendar startday, Calendar endday) {
        //确保startday在endday之前
        if (startday.after(endday)) {
            Calendar cal = startday;
            startday = endday;
            endday = cal;
        }
        //分别得到两个时间的毫秒数
        long sl = startday.getTimeInMillis();
        long el = endday.getTimeInMillis();

        long ei = el - sl;
        //根据毫秒数计算间隔天数
        return (int) (ei / (1000 * 60 * 60 * 24));
    }

    /** */
    /**
     * 计算两个时间之间相隔天数
     * 
     * @param startday 开始时间
     * @param endday 结束时间
     * @return
     */
    public static int getIntervalDays(Date startday, Date endday) {
        //确保startday在endday之前
        if (startday.after(endday)) {
            Date cal = startday;
            startday = endday;
            endday = cal;
        }
        //分别得到两个时间的毫秒数
        long sl = startday.getTime();
        long el = endday.getTime();

        long ei = el - sl;
        //根据毫秒数计算间隔天数
        return (int) (ei / (1000 * 60 * 60 * 24));
    }

    /**
     * 计算两个时间之间相隔天数
     * 
     * @param d1 开始时间
     * @param d2 结束时间
     * @return
     */
    public static  int getDaysBetween(Calendar d1, Calendar d2) {
        if (d1.after(d2)) { // swap dates so that d1 is start and d2 is end
            java.util.Calendar swap = d1;
            d1 = d2;
            d2 = swap;
        }
        int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
            d1 = (Calendar) d1.clone();
            do {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);//得到当年的实际天数
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }
        return days;
    }
    
    /** 
     * date到calendar的转化
     * @param date
     * @return
     */
    public static Calendar dateToCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
    
    public static Date calendarToDate(Calendar cal){
        return cal.getTime();
    }
    
    /**
     * 时间格式化输出
     * 
     * @param date          需要进行格式化的时间
     * @param formate
     * 日期和时间模式  结果
    * "yyyy.MM.dd G 'at' HH:mm:ss z"  2001.07.04 AD at 12:08:56 PDT
    * "EEE, MMM d, ''yy"  Wed, Jul 4, '01
    * "h:mm a"    12:08 PM
    * "hh 'o''clock' a, zzzz"     12 o'clock PM, Pacific Daylight Time
    * "K:mm a, z"     0:08 PM, PDT
    * "yyyyy.MMMMM.dd GGG hh:mm aaa"  02001.July.04 AD 12:08 PM
    * "EEE, d MMM yyyy HH:mm:ss Z"    Wed, 4 Jul 2001 12:08:56 -0700
    * "yyMMddHHmmssZ"     010704120856-0700
    * "yyyy-MM-dd'T'HH:mm:ss.SSSZ"    2001-07-04T12:08:56.235-0700
     * 
     * @return
     * @throws Exception
     */
    public static String dateFormateToStr(Date date, String formate) throws Exception {
        String str;
        SimpleDateFormat formater = new SimpleDateFormat(formate);
        str = formater.format(date);
        return str;
    }
    
    public static Date parseStrToDate(String date, String formate) throws Exception {
        SimpleDateFormat formater = new SimpleDateFormat(formate);
        Date _date = formater.parse(date);
        return _date;
    }
    
    public static void main(String[] args) throws Exception{
        System.out.println(parseStrToDate("2011-01-25", "yyyy-MM-hh"));
    }

}
