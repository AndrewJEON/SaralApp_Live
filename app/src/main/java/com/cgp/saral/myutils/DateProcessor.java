package com.cgp.saral.myutils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateProcessor {

	public static String getNow() {

		try{
			String DATE_FORMAT_NOW = "yyyyMMddhhmmss";
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
			String timeNow = sdf.format(calendar.getTime());
			return timeNow;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	// get Today date. Return Value as String
	public static String getToday() {
		try{
			String DATE_FORMAT_NOW = "MM/dd/yyyy";
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
			String timeNow = sdf.format(calendar.getTime());
			return timeNow;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	// get Tommorow date . Return Value as String
	public static String getTomorrow() {
		try{
			String DATE_FORMAT_NOW = "MM/dd/yyyy";
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_WEEK, 1);
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
			String timeNow = sdf.format(calendar.getTime());
			return timeNow;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	// get Yesterday date . Return Value as String
	public static String getnextYesterdayDate() {
		try{
			String DATE_FORMAT_NOW = "MM/dd/yyyy";
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -2);
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
			String timeNow = sdf.format(calendar.getTime());
			return timeNow;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static String getnextTomorrow() {
		try{
			String DATE_FORMAT_NOW = "MM/dd/yyyy";
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_WEEK, 2);
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
			String timeNow = sdf.format(calendar.getTime());
			return timeNow;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	// get Tommorow date . Return Value as String
	public static String getWeek() {
		try{
			String DATE_FORMAT_NOW = "MM/dd/yyyy";
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_YEAR, 8);
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
			String timeNow = sdf.format(calendar.getTime());
			return timeNow;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	// get Last week date . Return Value as String
	public static String getLastWeek() {
		try{
			String DATE_FORMAT_NOW = "MM/dd/yyyy";
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_YEAR, -8);
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
			String timeNow = sdf.format(calendar.getTime());
			return timeNow;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	// get Tommorow date . Return Value as String
	public static String getTodayPlus30Days() {
		try{
			String DATE_FORMAT_NOW = "MM/dd/yyyy";
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_YEAR, 30);
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
			String timeNow = sdf.format(calendar.getTime());
			return timeNow;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;


		/*	Calendar calendar = Calendar.getInstance();
			calendar.setTime(myDate);
			calendar.add(Calendar.DAY_OF_YEAR, -7);
			Date newDate = calendar.getTime();

			String date = dateFormat.format(newDate);*/
	}

	public static String getYesterdayPlus30Days() {
		try{
			String DATE_FORMAT_NOW = "MM/dd/yyyy";
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_YEAR, -30);
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
			String timeNow = sdf.format(calendar.getTime());
			return timeNow;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
		}
	
	// get Tommorow date . Return Value as String
	public static String getTomorrowDate() {
		try{
			String DATE_FORMAT_NOW = "yyyy/MM/dd";
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_WEEK, 1);
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
			String timeNow = sdf.format(calendar.getTime());
			return timeNow;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	// Convert String in yymmdd format in MM/DD/YY format . Return Value as String
	public static String getDateInFormat(String dateValue) {
		String format = dateValue.substring(6, 8) + "/" + dateValue.substring(4, 6) + "/" + dateValue.substring(0, 4);
		return format;
	}
	// get Yesterday date . Return Value as String
	public static String getYesterdayDate() {
		try{
			String DATE_FORMAT_NOW = "yyyy/MM/dd";
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -1);
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
			String timeNow = sdf.format(calendar.getTime());
			return timeNow;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	// Convert yymmdd format in dd/mm/yy . Return Value as String
	public static String getYMDDateInString(String value) {
		String[] dateYYMMDD = value.split("/");
		return dateYYMMDD[0] + dateYYMMDD[1] + dateYYMMDD[2];
	}



	public static boolean checkTimeValue(long startTime ,long endTime){
		long currentTime = convertTimeInMilliSec();
		if(startTime < currentTime && endTime > currentTime){
			return true;
		}
		return false;
	}

	// validate date between range . Return Value as boolean
	public static boolean validateDateRange(String dateValue,String minDateValue, String maxDateValue) {
		// yyyyMMdd format for date
		long dateValueL, minDateValueL, maxDateValueL;
		try {
			dateValueL = Long.parseLong(dateValue);
			minDateValueL = Long.parseLong(minDateValue);
			maxDateValueL = Long.parseLong(maxDateValue);
			if(maxDateValueL == 0){
				return true;
			}
			return (dateValueL >= minDateValueL && dateValueL < maxDateValueL);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return true;
		}
	}

	// Convert Datetime in convertTimeInMilliSec format. return  Long value

	public static long convertTimeInMilliSec(){
		try{
			String timeformat = "HH:mm:ss";
			SimpleDateFormat obDateFormat = new SimpleDateFormat(timeformat);
			Calendar time = Calendar.getInstance();
			String res[] = (obDateFormat.format(time.getTime())).split(":");
			int hr = Integer.parseInt(res[0]);
			int min = Integer.parseInt(res[1]);
			int sec = Integer.parseInt(res[2]);
			long ms = (hr*60*60 + min*60 + sec)* 1000;	
			return ms;	
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return (Long) null;
	}


	// get Date in MM/dd/yyyy format. return Date in string

	public static String getDateMMDDYYYY() {
		try{
			String DATE_FORMAT_NOW = "MM/dd/yyyy";
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
			String timeNow = sdf.format(calendar.getTime());
			return timeNow;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}


	public static String getDateValue(String timeStamp) {
		String[] date = timeStamp.split(" ");
		return date[0];
	}

	// get Date in dd/MM//yyyy format. return Date in string

	public static String getDateDDMMYYYY() {
		try{
			String DATE_FORMAT_NOW = "dd/MM/yyyy";
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
			String timeNow = sdf.format(calendar.getTime());
			return timeNow;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static String getDateYYYYMMDD() {
		try{
			String DATE_FORMAT_NOW = "yyyy/MM/dd";
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
			String timeNow = sdf.format(calendar.getTime());
			return timeNow;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}


	// Convert Date in dd/MM/yyyy format. return  String value

	public static String convertStringValueInDateFormat(String timeStamp){
		String timeNow ="";
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date converDate;
		try {
			converDate = (Date) df.parse(timeStamp);
			timeNow = df.format(converDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return timeNow;
	}

	// Convert Date Time in yyyy/MM/dd HH:mm format. return  value in Date

	public static String convertDate(String timeStamp){
		String timeNow ="";
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		Date converDate;
		try {
			converDate = (Date) df.parse(timeStamp);
			timeNow = df.format(converDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return timeNow;

	}

	public static Date convertDateTime(String timeStamp){
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			return df.parse(timeStamp);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// get Date Time in MM/dd/yyyy hh:mm:ss a format. return Date in string

	public static String getDateTime_MDYHMSA() {
		try{
			String DATE_FORMAT_NOW = "MM/dd/yyyy hh:mm:ss a";
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
			String timeNow = sdf.format(calendar.getTime());
			return timeNow;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}


	// get Date Time in yyyyMMddhhmmss a format. return Date in string

	public static String getDateTime_YMDHMS() {
		try{
			String DATE_FORMAT_NOW = "yyyyMMdd";
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
			String timeNow = sdf.format(calendar.getTime());
			return timeNow;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static String getDateTime_YMDHMSforcasekey() {
		try{
			String DATE_FORMAT_NOW = "yyyyMMddHHmmss";
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
			String timeNow = sdf.format(calendar.getTime());
			return timeNow;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	// Validate Date Time in MM/dd/yyyy hh:mm:ss a format. return boolean value



	public static Date converStringValueInDateTimeFormat(String timeStamp) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			Date converDate = (Date) df.parse(timeStamp);
			return converDate;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static boolean compareTwoDates(String currentDate,String lastLoginDateTime) {
		try {
			Date todayDate = converStringValueInDateTimeFormat(currentDate);
			Date lastLoginDate = converStringValueInDateTimeFormat(lastLoginDateTime);
			if (todayDate.after(lastLoginDate)) {
				Log.e("todayDate  astLoginDate", "");
				return true;
			}
			if (todayDate.equals(lastLoginDate)) {
				System.out.println("todayDate is equal lastLoginDate");
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}


	public static boolean checkDatesAreEqual(String currentDate,String lastLoginDateTime) {
		try {
			Date todayDate = converStringValueInDateTimeFormat(currentDate);
			Date lastLoginDate = converStringValueInDateTimeFormat(lastLoginDateTime);
			if (todayDate.equals(lastLoginDate)) {
				System.out.println("todayDate is equal lastLoginDate");
				return true;
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return false;
	}
	public static long dateInCurrentTimeMiles(String timeStamp){
		long timeNow =0;
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		Date converDate;
		try {
			converDate = (Date) df.parse(timeStamp);
			timeNow = converDate.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return timeNow;
	}
	public static String removeSecondfromDateTime(String datetime){

		String output = null;
		try {
			DateFormat inputFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			Date date;

			date = inputFormatter.parse(datetime);


			DateFormat outputFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm a");
			output = outputFormatter.format(date); // 
		} catch (ParseException e) {

			e.printStackTrace();
		}


		return output;

	}
}
