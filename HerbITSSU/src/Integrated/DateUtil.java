package Integrated; 
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
 
/**
 * 날짜 계산 관련 기능들을 모아놓은 유틸리티 클래스
 * 
 * 다음의 4가지 형식에 대하여 상호 변환 및 현재 시각을 반환한다.
 * SimpleString - yyyy/MM/dd
 * UserTypedString - ??
 * Calendar
 * TimeValue
 * 
 * @author Claude
 * @see Original From Croute, http://croute.me/397
 * 
 */
public class DateUtil
{
	//
	// Current Time(4)
	//
	public static String getSimpleString() {
		return getUserTypedString("yyyy/MM/dd");
	}
	public static String getUserTypedString(String format) {
		SimpleDateFormat sdfFormatter = new SimpleDateFormat(format);
		return sdfFormatter.format(Calendar.getInstance().getTime());
	}
	public static long getTimeValue() {
		return (Calendar.getInstance().getTimeInMillis());
	}
	public static Calendar getCalendar() {
		return (Calendar.getInstance());
	}
	
	//
	// Convert into String(4)
	//
	public static String convTimeValueToSimpleString(long timeValue) {
		return convTimeValueToUserTypedString(timeValue, "yyyy/MM/dd");
	}
	public static String convTimeValueToUserTypedString(long timeValue, String format) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeValue);
		return convCalendarToUserTypedString(cal, format);
	}
	public static String convCalendarToSimpleString(Calendar cal) {
		return convCalendarToUserTypedString(cal, "yyyy/MM/dd");
	}
	public static String convCalendarToUserTypedString(Calendar cal, String format) {
		SimpleDateFormat sdfFormatter = new SimpleDateFormat(format);
		return sdfFormatter.format(cal.getTime());
	}
	
	//
	// Convert into Types(4)
	//
	public static long convSimpleStringToTimeValue(String date) {
        return convUserTypedStringToTimeValue(date, "yyyy/MM/dd");
	}
	public static Calendar convSimpleStringToCalendar(String date) {
		return convUserTypedStringToCalendar(date, "yyyy/MM/dd");
	}
	public static long convUserTypedStringToTimeValue(String date, String format) {
		Calendar cal = Calendar.getInstance();
        
        try
        {
        	SimpleDateFormat sdfFormatter = new SimpleDateFormat(format);
            cal.setTime(sdfFormatter.parse(date));
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }
        return cal.getTimeInMillis();
	}
	public static Calendar convUserTypedStringToCalendar(String date, String format) {
		Calendar cal = Calendar.getInstance();
        
        try
        {
        	SimpleDateFormat sdfFormatter = new SimpleDateFormat(format);
            cal.setTime(sdfFormatter.parse(date));
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }
        return cal;
	}
	
	/**
	 * 주어진 Milliseconds 당뉘의 시각에서부터 현재까지 몇초가 지났는지 계산한다.
	 * 
	 * @param timeValue
	 * @return
	 */
	public static long getSecondsPassedFrom(long timeValue) {
		return ((getTimeValue() - timeValue) / 1000);
	}
}
