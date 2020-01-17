/** 
 * @Package qlc.utils 
 * @Description 
 * @author yfhuang521@gmail.com
 * @date 2019年12月5日 上午10:41:19 
 * @version V1.0 
 */ 
package qlc.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class TimeUtil {
	
	// Beijing time
    public static String getBeijingTime(){
        return getFormatedDateString(8);
    }
    
	// Bangalore time
    public static String getBangaloreTime(){
        return getFormatedDateString(5.5f);
    }
    
    // New York time
    public static String getNewyorkTime(){
        return getFormatedDateString(-5);
    }
    
    public static String getFormatedDateString(float timeZoneOffset){
        if (timeZoneOffset > 13 || timeZoneOffset < -12) {
            timeZoneOffset = 0;
        }
        
        int newTime=(int)(timeZoneOffset * 60 * 60 * 1000);
        TimeZone timeZone;
        String[] ids = TimeZone.getAvailableIDs(newTime);
        if (ids.length == 0) {
            timeZone = TimeZone.getDefault();
        } else {
            timeZone = new SimpleTimeZone(newTime, ids[0]);
        }
    
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(timeZone);
        return sdf.format(new Date());
    }
    
    public static long getTimeSeconds(float timeZoneOffset){
        try {
			if (timeZoneOffset > 13 || timeZoneOffset < -12) {
			    timeZoneOffset = 0;
			}
			
			int newTime=(int)(timeZoneOffset * 60 * 60 * 1000);
			TimeZone timeZone;
			String[] ids = TimeZone.getAvailableIDs(newTime);
			if (ids.length == 0) {
			    timeZone = TimeZone.getDefault();
			} else {
			    timeZone = new SimpleTimeZone(newTime, ids[0]);
			}
   
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sdf.setTimeZone(timeZone);
			String dateStr = sdf.format(new Date());
			Date timeZoneDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
			return timeZoneDate.getTime()/1000;
		} catch (ParseException e) {
			e.printStackTrace();
			return System.currentTimeMillis()/1000;
		}
    }
    
    public static void main(String[] args) {
		System.out.println(getTimeSeconds(8));
		System.out.println(System.currentTimeMillis()/1000);
	}
}
