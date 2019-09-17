package top.ruandb.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.shiro.crypto.hash.Md5Hash;

public class StringUtils {

	/**
	 * 判断是否是空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str.trim())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否不是空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		if ((str != null) && !"".equals(str.trim())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 *  md5 加盐加密
	 * @param str
	 * @param salt
	 * @return
	 */
	public static  String md5(String str, String salt) {
		return new Md5Hash(str, salt).toString();
	}
	
	/**
	    *   计算增量开始时间
	 * @return
	 */
	public static String getStartTime(String increaseTime) {
		LocalDate nowDate = LocalDate.now();
		LocalTime inTime = LocalTime.parse(increaseTime, DateTimeFormatter.ofPattern("HH:mm:ss"));
		//LocalDateTime localDateTime =  LocalDateTime.of(nowDate.minusDays(1), inTime); //往前推一天
		//LocalDateTime localDateTime =  LocalDateTime.of(nowDate.minusMonths(1), inTime);
		LocalDateTime localDateTime =  LocalDateTime.of(nowDate.minusWeeks(1), inTime); //往前退一周
		return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) ;
		//return "2019-01-30 13:00:00";
	}
	
	/**
	 * 计算增量结束时间
	 * @return
	 */
	public static String getEndTime(String increaseTime) {
		LocalDate nowDate = LocalDate.now();
		LocalTime inTime = LocalTime.parse(increaseTime, DateTimeFormatter.ofPattern("HH:mm:ss"));
		LocalDateTime localDateTime =  LocalDateTime.of(nowDate, inTime);
		return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) ;
	}
	/**
	 * 日期转字符串
	 */
	public static String date2Str(Date date) {
		 LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
		return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) ;
	}
	
	/**
	 * 计算两个时间相差得s数
	 * @return
	 */
	public static String twoDateS(Date date1,Date date2) {
		Long m = date2.getTime() - date1.getTime();
		float r =  ((float)m)/1000;
		return r+"";
	}
	
	/**
	 *       获取当前时间字符串
	 * @return
	 */
	public static String getNow() {
		LocalDateTime localDateTime = LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault());
		return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) ;
	}
	
	/**
	 * 	时间返回1天,冗余1天,防止数据丢失
	 * @return
	 */
	public static String redundant1s(String oldTime) {
		 LocalDateTime dt = LocalDateTime.parse(oldTime,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		 //LocalDateTime localDateTime = dt.minusSeconds(1);
		 LocalDateTime localDateTime = dt.minusDays(1);
		 return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) ;
	}
	
	public static void main(String[] args) {
		//System.out.println(StringUtils.getStartTime("04:00:00") + "   "+StringUtils.getEndTime("04:00:00"));
		System.out.println(StringUtils.redundant1s("2019-04-16 14:32:05"));
	}
}
