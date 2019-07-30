package com.enterprise.util;

import com.enterprise.base.vo.dto.DayDTO;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * DateUtil
 *
 * @Author ZKT
 * @CreateDate 2014年9月20日
 */
public class DateUtil {

    private static Log log = LogFactory.getLog(DateUtil.class);

    private static final String FORMAT_YM = "yyyyMM";

    private static final String FORMAT_Y_M = "yyyy-MM";

    public static final String FORMAT_YMD = "yyyyMMdd";

    private static final String FORMAT_Y_M_D = "yyyy-MM-dd";

    private static final String FORMAT_YearMonthDay = "yyyy年MM月dd日";

    private static final String FORMAT_D = "dd";

    private static final String FORMAT_YMD_HMS = "yyyy-MM-dd HH:mm:ss";

    private static final String FORMAT_YMDHMS = "yyyyMMddHHmmss";

    private static final String FORMAT_YMDHMSS = "yyyyMMddHHmmssSSS";

    private static final String FORMAT_YMD_H = "yyyy-MM-dd HH";

    private static final String FORMAT_YMDHMS5 = "yyyy-MM-dd HH:mm:ss.SSS";

    private static final String FORMAT_MD_HM = "MM/dd HH:mm";

    private static final String FORMAT_YMDTHMS = "yyyy-MM-dd'T'HH:mm:ss";

    public static final String FORMAT_YYYYMMDD = "yyyy/MM/dd";

    public static final String FORMAT_YYYY_MM_DD = "yyyy.MM.dd";

    public static final String FORMAT_MM_DD = "MM.dd";

    public static final String FORMATER_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static final String REGX_TIME_YYYY0mm0dd_SIMPLE_1 = "[0-9]{4}[0-9]{2}[0-9]{2}";
    public static final String REGX_TIME_YYYY0mm0dd_SIMPLE_2 = "^[1-9][0-9]{3}-(0[1-9]|1[0-2]|[1-9])-([0-2][1-9]|3[0-1]|[1-9])$";
    public static final String REGX_TIME_YYYY0mm0dd_SIMPLE_3 = "^[1-9][0-9]{3}\\.(0[1-9]|1[0-2]|[1-9])\\.([0-2][1-9]|3[0-1]|[1-9])$";
    public static final String REGX_TIME_YYYY0mm0dd_SIMPLE_4 = "^[1-9][0-9]{3}/(0[1-9]|1[0-2]|[1-9])/([0-2][1-9]|3[0-1]|[1-9])$";
    public static final String REGX_TIME_YYYY0mm0dd_SIMPLE_5 = "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-)) (20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d$";
    public static final String REGX_TIME_YYYY0mm0dd_SIMPLE_6 = "^((((1[6-9]|[2-9]\\d)\\d{2})(0?[13578]|1[02])(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})(0?[13456789]|1[012])(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-)) (20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d$";

    /**
     * 将日期转成  yyyy/MM/dd
     *
     * @author shisan
     * @date 2017/10/11 下午5:48
     */
    public static String getYMD(Date date) {
        return new SimpleDateFormat(FORMAT_YYYYMMDD).format(date);
    }

    /**
     * 将日期转成  yyyy.MM.dd
     *
     * @author anso
     * @date 2017/12/15 上午11:29
     */
    public static String getDateFromet(Date date) {
        return new SimpleDateFormat(FORMAT_YYYY_MM_DD).format(date);
    }

    /**
     * 将日期转成  MM.dd
     *
     * @author anso
     * @date 2017/12/15 上午11:29
     */
    public static String getDateFrometMD(Date date) {
        return new SimpleDateFormat(FORMAT_MM_DD).format(date);
    }

    /**
     * 将日期转成  MM.dd
     *
     * @author anso
     * @date 2017/12/15 上午11:29
     * //
     */
//    public static List<String> getDateFrometMDList(Date date) {
//
//        return new SimpleDateFormat(FORMAT_MM_DD).format(date);
//    }
    public static String getYearMonthDay(Date date) {
        return new SimpleDateFormat(FORMAT_YearMonthDay).format(date);
    }

    /**
     * 获取服务器当前时间
     *
     * @return 服务器当前时间
     */
    public static Date getCurrentDate() {
        Date now = getCurrentDateTime();
        return strToDate(dateToStr(now));
    }

    public static String dateToStr(Date date) {
        String s = "";
        if (date == null) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            s = sdf.format(date);
        } catch (Exception e) {

        }

        return s;
    }

    public static Date strToDate(String s) {
        Date d = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            d = sdf.parse(s);
        } catch (Exception e) {

        }

        return d;
    }

    public static Date getCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    /**
     * 获取指定日期的前一天开始时间
     *
     * @return 指定日期的前一天开始时间
     * @Author hehao
     * @CreateDate 2016年3月12日
     */
    public static Date getBeforeDayStartDateTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date dBefore = calendar.getTime();
        return getDateStartDateTime(dBefore);
    }

    /**
     * 获取指定日期的前一天开始时间
     *
     * @return 指定日期的前一天开始时间
     * @Author hehao
     * @CreateDate 2016年3月12日
     */
    public static Date getBeforeDayEndDateTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date dBefore = calendar.getTime();
        return getDateEndDateTime(dBefore);
    }


    /**
     * 根据本次发放日期获取下个代金券的发放的日期
     *
     * @param date 本次发放日期
     * @return 下个代金券的发放的日期
     * @Author ZKT
     * @CreateDate 2015年12月30日
     */
    public static Date getNextCashCouponDate(Date date) {
        if (Integer.parseInt(new SimpleDateFormat(FORMAT_D).format(date)) <= getNextMonthDay(date, 1)) {
            return getSeveralMonthsLaterDate(date, 1);
        } else {
            return getNextMonthStartDay(date, 2);
        }
    }

    /**
     * 根据日期获取该日期的开始日期时间点
     *
     * @return 日期的开始日期时间点(示例:2016-9-26 00:00:00)
     * @author ZKT
     * @createTime 2012-9-26
     */
    public static Date getDateStartDateTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 根据日期获取该日期的结束日期时间点
     *
     * @return 日期的结束日期时间点(示例:2016-9-26 23:59:59)
     * @author ZKT
     * @createTime 2012-9-26
     */
    public static Date getDateEndDateTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取今天日期的开始日期时间点
     *
     * @return 日期的开始日期时间点(示例:2016-9-26 00:00:00)
     * @author ZKT
     * @createTime 2012-9-26
     */
    public static Date getTodayStartDateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 是否在当前日期之前
     *
     * @param date 日期
     * @return True:是
     * @Author ZKT
     * @CreateDate 2015年8月13日
     */
    public static boolean isBeforeCurrentDate(Date date) {
        if (date == null) {
            return false;
        }
        return date.before(new Date());
    }

    /**
     * 是否在当前日期之后
     *
     * @param date 日期
     * @return True:是
     * @Author ZKT
     * @CreateDate 2015年8月13日
     */
    public static boolean isAfterCurrentDate(Date date) {
        if (date == null) {
            return false;
        }
        return date.after(new Date());
    }

    public static boolean isAfter(Date afterDate, Date beforeDate) {
        if (afterDate == null || beforeDate == null) {
            return false;
        }
        return afterDate.after(beforeDate);
    }

    /**
     * 获取当前服务器时间的几天后的时间
     *
     * @return 几天后的时间
     */
    public static Date getSeveralDaysLaterDate(int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + days);
        return c.getTime();
    }

    /**
     * 获取当前服务器时间的几个月后的时间
     *
     * @return 几个月后的时间
     */
    public static Date getSeveralMonthsLaterDate(Date date, int months) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, months);
        return c.getTime();
    }

    /**
     * 获取指定日期几天后的时间
     *
     * @param date 指定日期(为空则为当前服务器时间)
     * @param days 天数
     * @return 几天后的时间
     */
    public static Date getSeveralDaysLaterDate(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date == null ? new Date() : date);
        c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + days);
        return c.getTime();
    }

    /**
     * 获取几小时后的时间
     *
     * @param hours 小时数
     * @return 几小时后的时间
     */
    public static Date getSeveralHoursLaterDate(int hours) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.HOUR, c.get(Calendar.HOUR) + hours);
        return c.getTime();
    }

    /**
     * 获取几分钟后的时间
     *
     * @return 几分钟后的时间
     */
    public static Date getSeveralMinutesLaterDate(int minutes) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + minutes);
        return c.getTime();
    }

    /**
     * 根据指定的时间来获取指定的秒数之后的时间
     *
     * @return 指定的秒数之后的时间
     */
    public static Date getSecondLaterDate(Date date, Integer second) {
        if (date == null) {
            date = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.SECOND, c.get(Calendar.SECOND) + second);
        return c.getTime();
    }

    /**
     * 获取当前月份的天数
     *
     * @return 天数
     * @Author dengzhengjun
     * @CreateDate 2015年9月9日
     */
    public static int getCurrentMonthDay() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        return a.get(Calendar.DATE);
    }

    /**
     * 获取指定日期对应月份的天数
     *
     * @param date 指定日期
     * @return 天数
     * @Author ZKT
     * @CreateDate 2015年9月9日
     */
    public static int getMonthDay(Date date) {
        Calendar a = Calendar.getInstance();
        a.setTime(date);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        return a.get(Calendar.DATE);
    }

    /**
     * 获取指定日期下N个月的天数
     *
     * @param date  指定日期
     * @param month 月数
     * @return 天数
     * @Author ZKT
     * @CreateDate 2015年12月30日
     */
    public static int getNextMonthDay(Date date, int month) {
        Calendar a = Calendar.getInstance();
        a.setTime(date);
        a.set(Calendar.MONTH, a.get(Calendar.MONTH) + month);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        return a.get(Calendar.DATE);
    }

    /**
     * 获取当前日期下N个月的开始的日期
     *
     * @param date  指定日期
     * @param month 月数
     * @return 下N个月的开始的日期
     * @Author ZKT
     * @CreateDate 2015年12月30日
     */
    public static Date getNextMonthStartDay(Date date, int month) {
        Calendar a = Calendar.getInstance();
        a.setTime(date);
        a.set(Calendar.MONTH, a.get(Calendar.MONTH) + month);
        a.set(Calendar.DAY_OF_MONTH, 1);
        return a.getTime();
    }

    /**
     * 获取当前日期周的开始的日期
     *
     * @return 本周开始时间
     * @Author anson
     * @CreateDate 14
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        return c.getTime();
    }

    /**
     * 获取当前日期周的结束的日期
     *
     * @return 本周结束时间
     * @Author anson
     * @CreateDate 14
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
        return c.getTime();
    }


    public static Date getWeekStartTime(){
        Calendar currentDate = new GregorianCalendar();
        currentDate.setFirstDayOfWeek(Calendar.MONDAY);

        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return (Date)currentDate.getTime().clone();
    }

    public static Date getWeekEndTime(){
        Calendar currentDate = new GregorianCalendar();
        currentDate.setFirstDayOfWeek(Calendar.MONDAY);
        currentDate.set(Calendar.HOUR_OF_DAY, 23);
        currentDate.set(Calendar.MINUTE, 59);
        currentDate.set(Calendar.SECOND, 59);
        currentDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return (Date)currentDate.getTime().clone();
    }

    /**
     * 根据年月获取对应月份的天数
     *
     * @param year  年
     * @param month 月
     * @return 天数
     * @Author dengzhengjun
     * @CreateDate 2015年9月9日
     */
    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        return a.get(Calendar.DATE);
    }

    /**
     * 获得本年有多少天
     *
     * @return 本年有多少天
     * @Author dengzhengjun
     * @CreateDate 2015年9月14日
     */
    public static int getCurrentYearDay() {
        Calendar cd = Calendar.getInstance();
        cd.set(Calendar.DAY_OF_YEAR, 1);
        cd.roll(Calendar.DAY_OF_YEAR, -1);
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
        return MaxYear;
    }

    /**
     * 将日期的时分秒修改为指定的HH:mm:ss
     *
     * @Author dengzhengjun
     * @CreateDate 2015年9月17日
     */
    public static Date modifyDateHHmmss(Date targetDate, int HH, int mm, int ss) {
        HH = (0 <= HH && HH < 24) ? HH : 0;
        mm = (0 <= mm && mm < 60) ? mm : 0;
        ss = (0 <= ss && ss < 60) ? ss : 0;
        if (targetDate != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(targetDate);
            c.set(Calendar.HOUR_OF_DAY, HH);
            c.set(Calendar.MINUTE, mm);
            c.set(Calendar.SECOND, ss);
            c.set(Calendar.MILLISECOND, 0);
            return c.getTime();
        }
        return null;
    }

    /**
     * 根据结束时间获取当前时间到结束时间剩余分钟数
     *
     * @param endDate 结束时间
     * @return 当前时间到结束时间剩余分钟数
     */
    public static long getRemainMinutes(Date endDate) {
        // 小于60秒返回0
        if ((endDate.getTime() - getCurrentDate().getTime()) <= (60 * 1000L)) {
            return 0;
        }
        return getDiffMillis(endDate, getCurrentDate()) / (60 * 1000L);
    }

    /**
     * 计算两个日期之间的分钟数</br>
     * 任何一个参数传空都会返回-1</br>
     * 返回两个日期的时间差，不关心两个日期的先后</br>
     *
     * @param dateStart
     * @param dateEnd
     * @return
     * @author Saber
     * Date 2017/4/18 14:07
     */
    public static long getMinutesBetweenTwoDate(Date dateStart, Date dateEnd) {
        if (null == dateStart || null == dateEnd) {
            return -1;
        }
        long l = Math.abs(dateStart.getTime() - dateEnd.getTime());
        l = l / (1000 * 60);
        return l;
    }

    /**
     * 根据结束时间和开始时间获取间隔毫秒数
     *
     * @param endDate   结束时间
     * @param beginDate 开始时间
     * @return 间隔毫秒数
     */
    public static long getDiffMillis(Date endDate, Date beginDate) {
        Assert.notNull(endDate, "结束时间不能为Null.");
        Assert.notNull(beginDate, "开始时间不能为Null.");

        long diffMillis = Math.abs(endDate.getTime() - beginDate.getTime());
        return diffMillis == 0 ? 1 : diffMillis;
    }

    /**
     * 根据结束时间和开始时间获取间隔秒数
     *
     * @param endDate   结束时间
     * @param beginDate 开始时间
     * @return 间隔秒数
     */
    public static long getDiffSeconds(Date endDate, Date beginDate) {
        return getDiffMillis(endDate, beginDate) / 1000L;
    }

    /**
     * 根据结束时间和开始时间获取间隔分钟数
     *
     * @param endDate   结束时间
     * @param beginDate 开始时间
     * @return 间隔分钟数
     */
    public static long getDiffMinutes(Date endDate, Date beginDate) {
        return getDiffMillis(endDate, beginDate) / (60 * 1000L);
    }

    /**
     * 根据结束时间和开始时间获取间隔小时数
     *
     * @param endDate   结束时间
     * @param beginDate 开始时间
     * @return 间隔小时数
     */
    public static long getDiffHours(Date endDate, Date beginDate) {
        return getDiffMillis(endDate, beginDate) / (60 * 60 * 1000L);
    }

    /**
     * 根据结束时间和开始时间获取间隔天数
     *
     * @param endDate   结束时间
     * @param beginDate 开始时间
     * @return 间隔天数
     */
    public static long getDiffDays(Date endDate, Date beginDate) {
        return getDiffMillis(endDate, beginDate) / (24 * 60 * 60 * 1000L);
    }

    /**
     * 根据结束时间和开始时间获取间隔年数
     *
     * @param endDate   结束时间
     * @param beginDate 开始时间
     * @return 间隔年数
     */
    public static long getDiffYears(Date endDate, Date beginDate) {
        Assert.notNull(endDate, "结束时间不能为Null.");
        Assert.notNull(beginDate, "开始时间不能为Null.");

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        int endYear = endCalendar.get(Calendar.YEAR);
        int endMonth = endCalendar.get(Calendar.MONTH);
        int endDay = endCalendar.get(Calendar.DAY_OF_MONTH);
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(beginDate);
        int beginYear = beginCalendar.get(Calendar.YEAR);
        int beginMonth = beginCalendar.get(Calendar.MONTH);
        int beginDay = beginCalendar.get(Calendar.DAY_OF_MONTH);
        int years;
        if (beginDate.before(endDate)) {
            if ((endMonth > beginMonth) || ((endMonth == beginMonth) && (endDay >= beginDay))) {
                years = endYear - beginYear;
            } else {
                years = endYear - beginYear - 1;
            }
        } else {
            if ((beginMonth > endMonth) || ((endMonth == beginMonth) && (beginDay >= endDay))) {
                years = beginYear - endYear;
            } else {
                years = beginYear - endYear - 1;
            }
            years = 0 - years;
        }
        return years;
    }

    /**
     * 计算两个时间间隔的月数
     *
     * @param firstDate  第一个时间
     * @param secondDate 第二个时间
     * @author shisan
     * @date 2018/2/1 下午1:55
     */
    public static int getDiffMonths(Date firstDate, Date secondDate) {
        if (firstDate == null || secondDate == null) {
            return -100;
        }
        Calendar bef = Calendar.getInstance();
        Calendar aft = Calendar.getInstance();
        bef.setTime(firstDate);
        aft.setTime(secondDate);
        int result = aft.get(Calendar.MONTH) - bef.get(Calendar.MONTH);
        int month = (aft.get(Calendar.YEAR) - bef.get(Calendar.YEAR)) * 12;

        return Math.abs(month + result);
    }

    public static Date getMonthFirstDayYYYYMMDD(Date date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_Y_M_D);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.MONTH, 0);
            c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
            String first = sdf.format(c.getTime());
            Date firstDate = sdf.parse(first);
            return firstDate;
        } catch (Exception e) {
            log.error("时间转换异常", e);
        }
        return new Date();
    }

    public static Date getMonthLastDayYYYYMMDD(Date date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_Y_M_D);
            Calendar ca = Calendar.getInstance();
            ca.setTime(date);
            ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
            String last = sdf.format(ca.getTime());
            Date lastDate = sdf.parse(last);
            return lastDate;
        } catch (Exception e) {
            log.error("时间转换异常", e);
        }
        return new Date();
    }

    public static List<String> findDates(Date dBegin, Date dEnd) {
        List lDate = Lists.newArrayList();
        Calendar calBegin = Calendar.getInstance();
        dBegin = getDateStartDateTime(dBegin);
        dEnd = getDateStartDateTime(dEnd);
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(dEnd);
        while (dEnd.after(calBegin.getTime())) {
            lDate.add(DateUtil.getDateFrometMD(calBegin.getTime()));
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
        }
        lDate.add(DateUtil.getDateFrometMD(calBegin.getTime()));

        return lDate;
    }

    /**
     * 获取指定日期的下几个月
     *
     * @param date   日期
     * @param months 个数
     * @author shisan
     * @date 2018/2/1 下午2:25
     */
    public static String getPreMonth(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(cal.MONTH, months);
        SimpleDateFormat dft = new SimpleDateFormat(FORMAT_Y_M);
        String preMonth = dft.format(cal.getTime());
        return preMonth;
    }

    /**
     * 判断是否是当天
     *
     * @param date
     * @return 是否是当天(True:是当天, False:不是当天)
     */
    public static boolean isToday(Date date) {
        if (null == date) {
            return false;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Calendar curr = Calendar.getInstance();
        if ((cal.get(Calendar.YEAR) == curr.get(Calendar.YEAR)) && (cal.get(Calendar.MONTH) == curr.get(Calendar.MONTH))
                && (cal.get(Calendar.DAY_OF_MONTH) == curr.get(Calendar.DAY_OF_MONTH))) {
            return true;
        }
        return false;
    }

    /**
     * 获得系统日期时间的yyyyMM格式字符串
     *
     * @return yyyyMM格式字符串
     */
    public static String getCurrentDateYM() {
        return new SimpleDateFormat(FORMAT_YM).format(new Date());
    }

    /**
     * 获得指定日期时间的yyyyMM格式字符串
     *
     * @param date 指定日期时间
     * @return yyyyMM格式字符串
     */
    public static String getDateYM(Date date) {
        return new SimpleDateFormat(FORMAT_YM).format(date);
    }

    /**
     * 获得指定日期时间的yyyy-MM格式字符串
     *
     * @param date 指定日期时间
     * @return yyyyMM格式字符串
     */
    public static String getDateY_M(Date date) {
        return new SimpleDateFormat(FORMAT_Y_M).format(date);
    }

    /**
     * 获得指系统日期时间的yyyyMMdd格式字符串
     *
     * @return 系统日期时间的yyyyMMdd格式字符串
     */
    public static String getCurrentDateYMD1() {
        return new SimpleDateFormat(FORMAT_YMD).format(new Date());
    }

    /**
     * 获得指系统日期时间的yyyy-MM-dd格式字符串
     *
     * @return 系统日期时间的yyyy-MM-dd格式字符串
     */
    public static String getCurrentDateYMD() {
        return new SimpleDateFormat(FORMAT_Y_M_D).format(new Date());
    }

    /**
     * 获得date时间
     *
     * @param date
     * @return yyyyMMdd
     */
    public static String getDisplayYMD(Date date) {
        return new SimpleDateFormat(FORMAT_YMD).format(date);
    }

    /**
     * 获得date时间
     *
     * @param date
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getDisplayYMDHMS(Date date) {
        return new SimpleDateFormat(FORMAT_YMD_HMS).format(date);
    }

    public static String getDate_Y_M_D_H_M_S() {
        return new SimpleDateFormat(FORMAT_YMD_HMS).format(new Date());
    }

    /**
     * 获得date时间
     *
     * @param date
     * @return yyyy-MM-dd HH:mm:ss:SSS
     */
    public static String getDisplayYMDHMSMS(Date date) {
        return new SimpleDateFormat(FORMAT_YMDHMS5).format(date);
    }

    /**
     * 获得date时间
     *
     * @param date
     * @return MM/dd HH:ss
     */
    public static String getMDHM(Date date) {
        return new SimpleDateFormat(FORMAT_MD_HM).format(date);
    }

    /**
     * 根据日期获得yyyy-MM-dd格式字符串
     *
     * @param date 日期
     * @return yyyy-MM-dd
     */
    public static String getDateYMD(Date date) {
        return new SimpleDateFormat(FORMAT_Y_M_D).format(date);
    }

    /**
     * 获得当前date时间
     *
     * @return yyyyMMddHHmmss
     */
    public static String getDisplayYMDHHMMSS() {
        return new SimpleDateFormat(FORMAT_YMDHMS).format(new Date());
    }

    /**
     * 根据日期获得当前日期字符串
     *
     * @param date 日期
     * @return yyyyMMddHHmmss
     */
    public static String getDisplayYMDHHMMSS(Date date) {
        return new SimpleDateFormat(FORMAT_YMDHMS).format(date);
    }

    /**
     * 获得当前日期时间字符串
     *
     * @return yyyyMMddHHmmssSSS
     */
    public static String getDisplayYMDHMS3() {
        return new SimpleDateFormat(FORMAT_YMDHMSS).format(new Date());
    }

    /**
     * 获得当前日期时间字符串
     *
     * @return yyyy-MM-dd HH
     */
    public static String getDisplayYMDHMS4(Date date) {
        return new SimpleDateFormat(FORMAT_YMD_H).format(date);
    }

    public static boolean compareTime(Date date, int field, int amount) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.add(field, amount);

            Calendar c = Calendar.getInstance();
            c.setTime(date);

            return cal.before(c);
        } catch (Exception e) {
            return false;
        }
    }

    public static Date getDate(String date) {
        try {
            return new SimpleDateFormat(FORMAT_Y_M_D).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date getDate2(String date) {
        try {
            return new SimpleDateFormat(FORMAT_YMD_HMS).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date getDateTime(String date) {
        try {
            if (StringUtils.isBlank(date)) {
                return null;
            }
            return new SimpleDateFormat(FORMAT_YMDTHMS).parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date getDateTime(String date, String formatString) {
        try {
            if (StringUtils.isBlank(date)) {
                return null;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(formatString);
            return sdf.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getCompareTime(Date d1, Date d2) {
        try {
            SimpleDateFormat DF = new SimpleDateFormat("Z");
            Calendar calendar = Calendar.getInstance();
            String zone = ((SimpleDateFormat) DF.clone()).format(calendar.getTime());
            if (!StringUtils.isBlank(zone) && (zone.length() >= 5)) {
                Integer z = Integer.valueOf(new String(zone.substring(1)));
                if (zone.indexOf("-") != -1) {
                    z += 800;
                } else {
                    z = 800 - z;
                }
                int t1 = z / 100;
                int t2 = z % 100;
                calendar.add(Calendar.HOUR_OF_DAY, t1);
                calendar.add(Calendar.MINUTE, t2);
            }
            long currTime = calendar.getTimeInMillis();
            long time = currTime - d1.getTime();
            long day = time / (1000 * 60 * 60 * 24);
            if (day > 0) {
                long year = day / 365;
                if (year > 0) {
                    return year + "y";
                }
                long month = day / 30;
                if (month > 0) {
                    return month + "M";
                }
                return day + "d";
            }
            long hour = time / (1000 * 60 * 60);
            if (hour > 0) {
                return hour + "h";
            }
            return (time / (1000 * 60)) + "m";
        } catch (Exception e) {
            return "";
        }
    }

    public static String getCurrentDateStr() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS");
        return simpleDateFormat.format(new Date());
    }

    public static String getCurrentDayStr() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(new Date());
    }

    /**
     * 时间戳转换为yyyy-MM-dd HH:mm:ss 格式日期
     *
     * @param timestamp 时间戳
     * @return 日期
     * @Author 邓政军
     * @CreateDate 2015年8月5日
     */
    public static Date getDateFromTimestamp(Long timestamp) {
        try {
            return new SimpleDateFormat(FORMAT_YMD_HMS).parse(new SimpleDateFormat(FORMAT_YMD_HMS).format(timestamp));
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 时间戳转换为yyyy-MM-dd HH:mm:ss:SSS 格式日期
     *
     * @param timestamp 时间戳
     * @return 日期
     * @Author 邓政军
     * @CreateDate 2015年8月5日
     */
    public static Date getDateFromTimestamp5(Long timestamp) {
        try {
            return new SimpleDateFormat(FORMAT_YMDHMS5).parse(new SimpleDateFormat(FORMAT_YMDHMS5).format(timestamp));
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 检测时间间隔，可指定间隔单位
     *
     * @param dateFir
     * @param dateSec
     * @param timeUnit 间隔单位
     * @return
     * @Author guowei.wang
     * @CreateDate 2016年3月18日
     */
    public static long interval(Date dateFir, Date dateSec, TimeUnit timeUnit) {
        return timeUnit.convert(Math.abs(dateFir.getTime() - dateSec.getTime()), TimeUnit.MILLISECONDS);
    }

    /**
     * 获得指定日期所在月份的第一天
     */
    public static Date getMonthFirstDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // 把日期设置为当月第一天
        c.set(Calendar.DATE, 1);
        return getDateStartDateTime(c.getTime());
    }

    /**
     * 获得指定日期所在月份的最后一天
     */
    public static Date getMonthLastDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // 把日期设置为当月最后一天
        c.set(Calendar.DATE, getMonthDay(date));
        return getDateEndDateTime(c.getTime());
    }

    /**
     * 获得指定日期所在月份的指定天数的日期
     */
    public static Date getMonthAssignDay(Date date, int dayNum) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, dayNum);
        return getDateStartDateTime(c.getTime());
    }

    public static Date transformTime(String time, TimeZone timeZone) {
        DateFormat formatCur = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatCur.setTimeZone(timeZone);
        DateFormat formatNew = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return getDate2(formatNew.format(formatCur.parse(time)));
        } catch (ParseException e) {
            throw new RuntimeException("时区时间转换异常", e);
        }
    }

    public static Date parseDate(String date) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        try {
            return new SimpleDateFormat(FORMAT_Y_M_D).parse(date);
        } catch (ParseException e) {
            log.error("时间转换异常", e);
            return null;
        }
    }

    public static Date parseDateYMD(String date) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        try {
            return new SimpleDateFormat(FORMAT_YMD).parse(date);
        } catch (ParseException e) {
            log.error("时间转换异常", e);
            return null;
        }
    }

    public static boolean checkFormat(String date, String format) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return formatter.format(formatter.parse(date)).equals(date);
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * 正则匹配日期时间并返回date对象格式化
     *
     * @param dateStr
     * @return
     */
    public static Date convertStrToDate(String dateStr) {

        if (StringUtils.isBlank(dateStr)) return null;
        dateStr = dateStr.split("\\.")[0];//处理末尾的.0
        SimpleDateFormat sdf;

        if (Pattern.compile(REGX_TIME_YYYY0mm0dd_SIMPLE_4).matcher(dateStr).matches()) {
            sdf = new SimpleDateFormat("yyyy/MM/dd");
        } else if (Pattern.compile(REGX_TIME_YYYY0mm0dd_SIMPLE_3).matcher(dateStr).matches()) {
            sdf = new SimpleDateFormat("yyyy.MM.dd");
        } else if (Pattern.compile(REGX_TIME_YYYY0mm0dd_SIMPLE_2).matcher(dateStr).matches()) {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
        } else if (Pattern.compile(REGX_TIME_YYYY0mm0dd_SIMPLE_1).matcher(dateStr).matches()) {
            sdf = new SimpleDateFormat("yyyyMMdd");
        } else if (Pattern.compile(REGX_TIME_YYYY0mm0dd_SIMPLE_5).matcher(dateStr).matches()) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else if (Pattern.compile(REGX_TIME_YYYY0mm0dd_SIMPLE_6).matcher(dateStr).matches()) {
            sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        } else {
            return null;
        }

        Date result = null;

        try {
            result = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * 获取已经过去的本月的日期时间
     *
     * @return
     */
    public static List<Date> getMonthPastDate(String paramYm) {

        List<Date> result = new ArrayList<>();

        if (StringUtils.isBlank(paramYm)) return result;

        Date today = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String todayStr = sdf.format(today);

        Calendar c = Calendar.getInstance();

        int pastDays = 0;

        try {
            if (paramYm.trim().equals(todayStr)) {
                c.setTime(today);
                pastDays = c.get(Calendar.DAY_OF_MONTH);
            } else {
                c.setTime(sdf.parse(paramYm));
                pastDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i = 1; i <= pastDays; i++) {
            c.set(Calendar.DAY_OF_MONTH, i);
            result.add(c.getTime());
        }
        return result;
    }

    /**
     * 　　* 判断时间是否在时间段内 *
     */

    public static boolean isInDate(Date date, Date dateBegin, Date dateEnd) {

        if (date.before(dateBegin) && date.after(dateEnd)) {
            return true;
        }
        return false;
    }

    /**
     * 获取当前时间的UTC时间
     *
     * @return
     */
    public static Date getCurrentUTCTime() {
        return DateUtil.GMT82UTC(new Timestamp(System.currentTimeMillis()));
    }


    /**
     * GMT8--->UTC
     *
     * @param sourceTime Date
     * @return
     */
    public static Date GMT82UTC(Date sourceTime) {
        if (sourceTime == null) return null;
        DateFormat formatter = new SimpleDateFormat(FORMATER_YYYY_MM_DD_HH_MM_SS);
        //Date date = Calendar.getInstance().getTime();
        TimeZone srcTimeZone = TimeZone.getTimeZone("GMT+8");
        //TimeZone srcTimeZone = TimeZone.getTimeZone("CST");
        TimeZone destTimeZone = TimeZone.getTimeZone("UTC");
        return StringToDate(dateTransformBetweenTimeZone(sourceTime, formatter, srcTimeZone, destTimeZone));
    }

    /**
     * 字符串转化为日期</br>
     *
     * @param str 需要被转换为日期的字符串
     * @return java.util.Date，如果出错会返回null
     */
    public static Date StringToDate(String str) {
        return StringToDate(str, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 字符串转化为日期</br>
     *
     * @param str    需要被转换为日期的字符串
     * @param format 格式，常用的为 yyyy-MM-dd HH:mm:ss
     * @return java.util.Date，如果出错会返回null
     */
    public static Date StringToDate(String str, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            if (log.isErrorEnabled()) {
                log.error("将字符串转换成日期时出错", e);
            }
        }
        return date;
    }

    /**
     * 两个时区转换
     *
     * @param sourceDate     源时间
     * @param formatter      转换的时间类型
     * @param sourceTimeZone 源时区
     * @param targetTimeZone 要转换的时区
     * @return 新时区的时间
     */
    public static String dateTransformBetweenTimeZone(Date sourceDate, DateFormat formatter,
                                                      TimeZone sourceTimeZone, TimeZone targetTimeZone) {
        Long targetTime = sourceDate.getTime() - sourceTimeZone.getRawOffset() + targetTimeZone.getRawOffset();
        return getTime(new Date(targetTime), formatter);
    }

    public static String getTime(Date date, DateFormat formatter) {
        return formatter.format(date);
    }


    /**
     * 获取当前时间 （yyyy-MM-dd HH:mm:ss）
     *
     * @return 当前时间 （yyyy-MM-dd HH:mm:ss）
     */
    public static String current() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);

    }

    private static long oneDayMilliSeconds = 24 * 3600 * 1000;


    public static boolean isDayToday(Date date) {
        Date now = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        //获取今天的日期
        String nowDay = sf.format(now);
        //对比的时间
        String day = sf.format(date);
        return day.equals(nowDay);
    }

    public static boolean isYesterday(Date date) {
        Calendar now = Calendar.getInstance();
        long ms = 1000 * (now.get(Calendar.HOUR_OF_DAY) * 3600 + now.get(Calendar.MINUTE) * 60 + now.get(Calendar.SECOND));//毫秒数
        long ms_now = now.getTimeInMillis();
        if (ms_now - date.getTime() > ms && ms_now - date.getTime() < (ms + oneDayMilliSeconds)) {
            return true;
        }
        return false;
    }

    public static boolean isDayBeofre(Date date) {
        Calendar now = Calendar.getInstance();
        long ms = 1000 * (now.get(Calendar.HOUR_OF_DAY) * 3600 + now.get(Calendar.MINUTE) * 60 + now.get(Calendar.SECOND));//毫秒数
        long ms_now = now.getTimeInMillis();
        if (ms_now - date.getTime() < (ms + 2 * oneDayMilliSeconds) && (ms + oneDayMilliSeconds) < ms_now - date.getTime()) {
            return true;
        }
        return false;
    }

    /**
     * 获取当前时间，指定前面多少小时的时间
     *
     * @param ihour 几个小时
     * @author shisan
     * @date 2018/1/23 下午6:51
     */
    public static Date getDateBeforeHourTime(Date date, int ihour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - ihour);
        return calendar.getTime();
    }

    public static boolean isValidDate(String str) {
        boolean convertSuccess = true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (str.length() > 10) {
            return false;
        }
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }


    public static List<DayDTO> getDaysBetween(Date startDate, Date endDate) {
        List<DayDTO> al = new ArrayList<DayDTO>();
        if (startDate.getYear() == endDate.getYear() && startDate.getMonth() == endDate.getMonth() && startDate.getDay() == endDate.getDay()) {
            //IF起始日期等于截止日期,仅返回起始日期一天
            al.add(new DayDTO(startDate, endDate));
        } else if (startDate.compareTo(endDate) < 0) {
            //IF起始日期早于截止日期,返回起止日期的每一天
            while (startDate.compareTo(endDate) < 0) {
                if (startDate.getDay() == endDate.getDay() && startDate.getMonth() == endDate.getMonth()) {
                    Date startTimeOfDay = getDateStartDateTime(endDate);
                    al.add(new DayDTO(startTimeOfDay, endDate));
                } else {
                    Date endTimeOfDay = getDateEndDateTime(startDate);
                    al.add(new DayDTO(startDate, endTimeOfDay));
                }
                try {
                    DateUtil.getDateStartDateTime(startDate);
                    startDate = new Date(DateUtil.getDateStartDateTime(startDate).getTime() + 3600 * 24 * 1000);//+1天
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            Date endTimeOfDay = getDateEndDateTime(startDate);
            //IF起始日期晚于截止日期,仅返回起始日期一天
            al.add(new DayDTO(startDate, endTimeOfDay));
        }
        return al;

    }

    public static void main(String[] args) {
//        System.out.println(getMonthDay(new Date()));
//        System.out.println(getMonthFirstDay(new Date()));
//        System.out.println(getMonthLastDay(new Date()));
//        System.out.println(getMonthAssignDay(new Date(), 31));
//        System.out.println(getDate_Y_M_D_H_M_S());

//        List<Date> dateList = getMonthPastDate("201711");
//        for (Date date:dateList){
//            System.out.println(date);
//        }
        //1514505600000&endTime=1514534400000&pageNum=1
//        Date date11 = new Date(new Long(1514505600));
//        Date date2 = new Date(new Long(1514534400));
//        System.out.println(date11);
//        System.out.println(date2);

//        String dateStr = "20171207";
//        Date date = StringToDate(dateStr, DateUtil.FORMAT_YMD);
//        System.out.println(date);
//          String qdate = dateToStr(getDate("2017-12-21 15:25:00"));
//        System.out.println(qdate);
//          System.out.println(isDayToday(qdate));
//          System.out.println(isYesterday(qdate));
//          System.out.println(isDayBeofre(qdate));

//        System.out.println(GMT82UTC(getDateStartDateTime(getSeveralDaysLaterDate(new Date(),-30))));
//        Date qdate = convertStrToDate("2018-03-06 07:50:00");
//        System.out.println(qdate.getTime());
//        Date zdate = convertStrToDate("2018-03-06 20:10:00");
//        System.out.println(zdate.getTime());
//        System.out.println(zdate.getTime());
//        String date1 = "2017-09-01";
//        String date2 = "2017/09/01";
//        String date3 = "2017-09-01 11:00:00";
//        System.out.println(date1.length());
//
//        boolean result1 = isValidDate(date1);
//        boolean result2 = isValidDate(date2);
//        boolean result3 = isValidDate(date3);
//        System.out.println(result1);
//        System.out.println(result2);
//        System.out.println(result3);

//        Date begin = getCurrentWeekBeginday();
//        Date end = getCurrentWeekEndday();
//        System.out.println(begin);
//        System.out.println(end);

//        String lastMonth = DateUtil.getDateY_M(DateUtil.getSeveralMonthsLaterDate(new Date(), -1));
//        System.out.println(lastMonth);
//        Date date = new Date();
//        Date begin = getDateStartDateTime(getFirstDayOfWeek(date));
//        Date end = getDateEndDateTime(getLastDayOfWeek(date));
//        System.out.println(begin);
//        System.out.println(end);
//        Date dayBeofreYesterDayBegin = DateUtil.getBeforeDayStartDateTime(DateUtil.getDateBeforeHourTime(new Date(), 24));
//        Date dayBeofreYesterDayEnd = DateUtil.getBeforeDayEndDateTime(DateUtil.getDateBeforeHourTime(new Date(), 24));
//        System.out.println(dayBeofreYesterDayBegin);
//        System.out.println(dayBeofreYesterDayEnd);

//        Date sevenDayBeofreYesterDayBegin = DateUtil.getBeforeDayStartDateTime(DateUtil.getDateBeforeHourTime(new Date(), 5 * 24));
//        System.out.println(sevenDayBeofreYesterDayBegin);
//        Date qdate = convertStrToDate("2018-05-01 18:00:00");
//        System.out.println(getDiffDays(new Date(),qdate));
//        System.out.println(getDiffDays(qdate,new Date()));
//        Date zdate = convertStrToDate("2018-05-05 23:50:00");
//        List<DayDTO> dayList = getDaysBetween(qdate,zdate);
//        for (int i=0;i<dayList.size();i++){
//            DayDTO dayDTO = dayList.get(i);
//            System.out.println(dayDTO.getStartDate() +"-" + dayDTO.getEndDate());
//        }
        //获取上个月
//        Date date = DateUtil.getSeveralMonthsLaterDate(new Date(),-1);
//        System.out.println(date);
//        System.out.println(getDate_Y_M_D_H_M_S());
//        System.out.println(getCurrentDayStr());
//        System.out.println(getWeekStartTime());
//        System.out.println(getWeekEndTime());

        Date qdate = convertStrToDate("1969-01-01 18:00:00");
        System.out.println(qdate.getTime());

    }
}