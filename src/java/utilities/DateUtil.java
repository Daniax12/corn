/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilities;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * THIS CLASS WILL PERFOM ALL THAT REGARDING TO DATE_CALENDAR_TIME -> WE MUST EXPLOITE MORE JAVA.SQL
 * @author rango
 */
public class DateUtil {
    
    // Adding minutes to a timestamp
    public static Timestamp add_minutes_to_timestamp(Timestamp timestamp, int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);
        cal.add(Calendar.MINUTE, minutes);
        return new Timestamp(cal.getTime().getTime());
    }
    
    
    // Adding time_data to a timestamp
    public static Timestamp add_time_data_in_timestamp(Timestamp timestamp, Time time){
        LocalDate localDate = timestamp.toLocalDateTime().toLocalDate();
        LocalTime localTime = time.toLocalTime();
        
        // Combine LocalDate and LocalTime to LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        // Convert LocalDateTime to Timestamp
        Timestamp result = Timestamp.valueOf(localDateTime);
        
        return result;
    }
    
    
    // Adding a day into a timestamp
    public static Timestamp add_day_to_timestamp(Timestamp timestamp, int day){
        LocalDateTime localDateTime = timestamp.toLocalDateTime();

        // Adding a day
        LocalDateTime newDateTime = localDateTime.plusDays(day);

        Timestamp newTimestamp = Timestamp.valueOf(newDateTime);
        return newTimestamp;
    }
    
    // Get the day of a specific timestamp
    public static int day_of_timestamp(Timestamp timestamp){
        LocalDate date = timestamp.toLocalDateTime().toLocalDate();
        return date.getDayOfMonth();
    }
    
    // Get the month of a specific timestamp
    public static int month_of_timestamp(Timestamp timestamp){
        LocalDate date = timestamp.toLocalDateTime().toLocalDate();
        return date.getMonthValue();
    }
    
    // Get the month of a specific timestamp
    public static int year_of_timestamp(Timestamp timestamp){
        LocalDate date = timestamp.toLocalDateTime().toLocalDate();
        return date.getYear();
    }  
    
    // Here, we must consider that the date must be the same
    public static int duration_minutes_two_intervalle(Timestamp debut, Timestamp end, Timestamp bornInf, Timestamp bornSup){
        int trig = 0;
        boolean first_in = is_between_timestamps(debut, bornInf, bornSup);
        boolean last_in = is_between_timestamps(end, bornInf, bornSup);
        if(first_in == true && last_in == true){
            return difference_minutes_two_timestamp(debut, end);
        } else if( first_in == true && last_in == false){
            return difference_minutes_two_timestamp(debut, bornSup);
        } else if(first_in == false && last_in == true){
            return difference_minutes_two_timestamp(bornInf, end);
        } else if(is_between_timestamps(bornInf, debut, end) == true && is_between_timestamps(bornSup, debut, end) == true){
            return difference_minutes_two_timestamp(bornInf, bornSup);
        }
        return 0;
    }
    
    
    public static int difference_minutes_two_timestamp(Timestamp debut, Timestamp end){
        String deb = timestamp_to_hour_min(debut);
        System.out.println("deb is "+deb);
        String en = timestamp_to_hour_min(end);
        System.out.println("end is "+en);
        
        return calculateHourDifference(en, deb);
    }
    
    
    public static String timestamp_to_hour_min(Timestamp timestamp){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(timestamp);
    }
    
    // Difference entre 17:30 et 20:30
    public static int calculateHourDifference(String hour1, String hour2) {
        LocalTime time1 = LocalTime.parse(hour1);
        LocalTime time2 = LocalTime.parse(hour2);

        long minutes = Math.abs(ChronoUnit.MINUTES.between(time1, time2));
        return (int) minutes;
    }
    
    // String from HTML -> TIMESTAMP
    public static Timestamp string_to_timestamp(String timestampString) throws Exception {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            java.util.Date parsedDate = dateFormat.parse(timestampString);
            return new Timestamp(parsedDate.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Parsing impossible. Error: "+e.getMessage());
        }
    }
    
    
    /**
     * CHange a milliseconde into Time
     * @param milliseconds
     * @return 
     */
    public static Time millisecond_to_Time(long milliseconds) {
        long totalSeconds = milliseconds / 1000;
        int hours = (int) (totalSeconds / 3600);
        int minutes = (int) ((totalSeconds % 3600) / 60);
        int seconds = (int) (totalSeconds % 60);

        String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        return Time.valueOf(timeString);
    }
    
    
    /**
     * COnver a time into millisecond
     * @param time
     * @return 
     */
    public static long time_in_millisecond(Time time) {
        long timeInMillis = time.getTime();
        long midnightInMillis = Time.valueOf("00:00:00").getTime();

        return timeInMillis - midnightInMillis;
    }
    
    /**
     * If a main timestamp is between 2 timestamps
     * @param main_date
     * @param debut
     * @param end
     * @return 
     */
    public static boolean is_between_timestamps(Timestamp main_date, Timestamp debut, Timestamp end) {
        return main_date.after(debut) && main_date.before(end);
    }
    
    /**
     * Adding a time to a timestamp
     * @param toAdd
     * @param adding
     * @return 
     */
    public static Timestamp add_time_to_timestamp(Timestamp toAdd, Time adding) {
        long timeInMillisToAdd = toAdd.getTime();
        long timeInMillisAdding =  time_in_millisecond(adding);
       // System.out.println(timeInMillisAdding);

        long newTimeInMillis = timeInMillisToAdd + timeInMillisAdding;

        // Create a new Timestamp object with the added time
        Timestamp newTimestamp = new Timestamp(newTimeInMillis);

        return newTimestamp;
    }
    
    public static Timestamp add_time_to_timestamp(Timestamp toAdd, long adding) {
        long timeInMillisToAdd = toAdd.getTime();
       // System.out.println(timeInMillisAdding);

        long newTimeInMillis = timeInMillisToAdd + adding;

        // Create a new Timestamp object with the added time
        Timestamp newTimestamp = new Timestamp(newTimeInMillis);

        return newTimestamp;
    }
    
    
    public static String long_to_string(long duration) {
        long days = TimeUnit.MILLISECONDS.toDays(duration);
        duration -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(duration);
        duration -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        duration -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration);

        String differenceString = String.format("%d/%02d/%04d %02d:%02d:%02d", days, 0, 0, hours, minutes, seconds);
        return differenceString;
    }
    
    /**
     * Get the difference between 2 timestamp into time
     * @param debut
     * @param end
     * @return 
     */
    public static Time time_difference_two_timestamp(Timestamp debut, Timestamp end) {
        long timeInMillis1 = debut.getTime();
        long timeInMillis2 = end.getTime();

        long differenceInMillis = Math.abs(timeInMillis2 - timeInMillis1);

        // Create a new Time object based on the difference in milliseconds
        Time timeDifference = millisecond_to_Time(differenceInMillis);

        return timeDifference;
    }
    
    public static long time_difference_two_timestamp_long(Timestamp debut, Timestamp end) {
        long timeInMillis1 = debut.getTime();
        long timeInMillis2 = end.getTime();

        long differenceInMillis = Math.abs(timeInMillis2 - timeInMillis1);

        return differenceInMillis;
    }
    public  static long difference_two_timestamp_long(Timestamp debut, Timestamp end){
        long milliseconds1 = debut.getTime();
        long milliseconds2 = end.getTime();
        return milliseconds2 - milliseconds1;
    }
    
    /**
     * TO CHECK IF A SQL.DATE IS BETWEEN 2 SQL.DATE
     * @param mainDate
     * @param debut
     * @param fin
     * @return 
     */
    public static boolean isSqlDateBetween(java.sql.Date mainDate, java.sql.Date debut, java.sql.Date fin){
        java.util.Date  dateMain = new java.util.Date(mainDate.getTime());
        java.util.Date dateDebut = new java.util.Date(debut.getTime());
        java.util.Date dateFin = new java.util.Date(fin.getTime());
        
        return isDateBetweem(dateMain, dateDebut, dateFin);
    }
    
    /**
     * String to -> java.sql.Timestamp
     * @param sqlDateType The type of sql.date needed
     * @param dateString
     * @param datePattern
     * @return
     * @throws Exception 
     */
    public static <T> T stringToSqlDate(Class<T> sqlDateType, String dateString, DatePattern datePattern) throws Exception{
        T result = null;
    
        try {
            Date temp = stringToDate(dateString, datePattern);
            if(sqlDateType == java.sql.Timestamp.class){
                return (T) utilDateToSqlDate(java.sql.Timestamp.class, temp);
            }else if(sqlDateType == java.sql.Date.class){
                return (T) utilDateToSqlDate(java.sql.Date.class, temp);
            }
            return result;
        } catch (Exception e) {
            throw new Exception("Error on parsing string date to java.sql.Timestamp or java.sql.Date");
        }
    }
    
    
    /**
     * String to Time -> BUT Format should be 11:00:00
     * @param timeString
     * @return
     * @throws Exception 
     */
    public static java.sql.Time stringToSqlTime(String timeString) throws Exception{
        try {
            return java.sql.Time.valueOf(timeString);
        } catch (Exception e) {
            throw new Exception("Error on casting string to time");
        }
    }
    
    
    /**
     * CASTING UTIL/CALENDAR -> JAVA.SQL type
     * @param <T>
     * @param sqlDateType -> the sql.class date requested
     * @param aDate -> maybe a calendar ofr date or localdate -> you can add some class if possible
     * @return
     * @throws Exception 
     */
    public static <T> T utilDateToSqlDate(Class<T> sqlDateType,Object aDate) throws Exception{
        T result = null;
        
        try {
            Date date = null;

            if(aDate.getClass() == java.util.GregorianCalendar.class){             // If field -> Calendar
//                System.out.println("Hello");
                date = new Date(((Calendar) aDate).getTimeInMillis());
            } else if(aDate.getClass() == Date.class){                             // If field -> Date
                date = (Date) aDate;
            } else if(aDate.getClass() == LocalDateTime.class){                     // If field -> LocalDateTime
                Instant insant = ((LocalDateTime) aDate).toInstant(ZoneOffset.UTC);
                date = Date.from(insant);
            }
                
            if(sqlDateType == java.sql.Timestamp.class){        // If requested -> timestamp sql
                java.sql.Timestamp sqlStamp = new java.sql.Timestamp(date.getTime());
                return (T) sqlStamp;
            } else if(sqlDateType == java.sql.Date.class){      // If requested -> dqte sql
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                return (T) sqlDate;
            }
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on casting the date to timestap data type");
        }
    }
    
    
    /**
     * TO CHECK IF A DATE IS BETWEEN TWO CALENDAR
     * @param mainDate the date to verify
     * @param debut
     * @param end
     * @return 
     */
    public static boolean isDateBetweem(Date mainDate, Date debut, Date end){
        Calendar main = Calendar.getInstance();
        main.setTime(mainDate);
        
        Calendar calDebut = Calendar.getInstance();
        calDebut.setTime(debut);
        
        Calendar calFin = Calendar.getInstance();
        calFin.setTime(end);
        
        return isCalendarBetween(main, calDebut, calFin);
    }
    
    /**
     * TO CHECK IF A CALENDAR IS BETWEEN TWO CALENDAR
     * @param mainDate the calendar to verify
     * @param debut
     * @param end
     * @return 
     */
    public static boolean isCalendarBetween(Calendar mainDate, Calendar debut, Calendar end){
        if((mainDate.after(debut) == true && mainDate.before(end) == true) || (mainDate.getTime().compareTo(debut.getTime()) == 0) || 
        (mainDate.getTime().compareTo(end.getTime()) == 0)){
            return true;
        }
        return false;
    }
    
    
    /**
     * DIFFERENCE BETWEEN TWO DATES TO SECOND - MINUTS - etc
     * @param debut
     * @param end
     * @param timeType
     * @return 
     */
    public static double diffBetweenDates(Date debut, Date end, TimeType timeType){
        Calendar calDebut = Calendar.getInstance();
        calDebut.setTime(debut);
        
        Calendar calFin = Calendar.getInstance();
        calFin.setTime(end);
        
        return diffBetweenCalendars(calDebut, calFin, timeType);
    }
    
    /**
     * GET THE DIFFERENCE BETWEEN TWO DATES TO SECONDE OR MINUTES OR DAY OR ... 
     * @param debut
     * @param end
     * @param timeType enum class -> Choosing
     * @return 
     */
    public static double diffBetweenCalendars(Calendar debut, Calendar end, TimeType timeType){

//        int differenceSeconde = (int) Duration.between(debut.toInstant(), end.toInstant()).toSeconds();     // Get the difference in second
        int differenceSeconde = (int)Duration.between(debut.toInstant(), end.toInstant()).getSeconds();
        
        if(timeType.equals(TimeType.SECOND) == true) return (double) differenceSeconde/1;
        if(timeType.equals(TimeType.MINUTE) == true) return (double) differenceSeconde/60;
        if(timeType.equals(TimeType.HOUR) == true) return (double) differenceSeconde/3600;
        if(timeType.equals(TimeType.DAY) == true) return (double) differenceSeconde/86400;
        if(timeType.equals(TimeType.MONTH) == true) return (double) differenceSeconde/2629746;
        if(timeType.equals(TimeType.YEAR) == true) return (double) differenceSeconde/31556952;
        return 0.0;
    }
    
    /**
     * CONVERTING STRING TO DATE
     * @param dateString
     * @param pattern
     * @return
     * @throws Exception 
     */  
    public static Date stringToDate(String dateString, DatePattern pattern) throws Exception{
        try {
            return new SimpleDateFormat(pattern.value()).parse(dateString);  
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on parsing string into Date "+dateString + " with the fornat "+pattern.value());
        }
    }
    
    /**
     * CONVERTING A STRING TO CALENDAR
     * @param dateString
     * @param pattern
     * @return 
     */
    public static Calendar stringToCalendar(String dateString, DatePattern pattern) throws Exception{
        Calendar calendar = Calendar.getInstance();
        
        try {
            Date date = stringToDate(dateString, pattern);
            calendar.setTime(date);
            return calendar;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on parsing a string to calendar");
        }
    }
    
    /**
     * CONVERTING A DATE TO STRING
     * @param date
     * @param pattern enum class => We have to choose
     * @return 
     */
    public static String dateToString(Date date, DatePattern pattern){
        DateFormat dateFormat = new SimpleDateFormat(pattern.value());
        String strDate = dateFormat.format(date);
        return strDate;
    }
    
    
    /**
     * CONVERTING A CALENDAR => STRING( pattern i.e: dd-MM-yyyy hh:mi:ss)
     * @param calendar
     * @param pattern  enum class => We have to choose
     * @return 
     */
    public static String calendarToString(Calendar calendar, DatePattern pattern){
        Date date = calendar.getTime();
        return dateToString(date, pattern);
    }
}
