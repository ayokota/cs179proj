package com.jusu.hangout.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**   
 *  将时间戳转为字符串 
 * 项目：聊天对话效果界面
 * 包名：com.junhua.ceo.util
 * @author Yuanjunhua
 *
 * 2014-9-4下午2:07:33
 */
public class DateFomats {
	

	/** 转换获取出入的字符串时间值  */
	public static String getStringTime(String strTime){
		SimpleDateFormat sd = new SimpleDateFormat("MM-dd"+"\0\0"+"HH:"+"mm");
		long sTime = Long.valueOf(strTime);
		
		return sd.format(new Date(sTime * 1000));
	}
	
	
	/** 获取并格式化当前时间值  */
	public static String getCurrentTime(long date){
		SimpleDateFormat sd = new SimpleDateFormat("MM-dd"+"\t"+"HH:"+"mm");

//		System.out.println("simpletime: " + sd.format(date));

		return sd.format(date);
	}
}
