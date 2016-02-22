package com.clockworks.android.tablet.bigture.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil
{
	private static String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
	private static String dateFormat = "yyyy-MM-dd";

	public static int getCurrentYear()
	{
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}

	public static int getCurrentMonth()
	{
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH) + 1;
	}

	public static int getCurrentDay()
	{
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public static int getCurrentHour()
	{
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	public static int getCurrentMinute()
	{
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MINUTE);
	}

	public static long getCurrentTimeMillis()
	{
		return System.currentTimeMillis();
	}

	public static String getTimeString(Date date)
	{
		String result = "";

		try
		{
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			long t1 = cal.getTimeInMillis();
			long t2 = System.currentTimeMillis();

			if (t2 - t1 < 48 * 60 * 60 * 1000){
				if (t2 - t1 > 24 * 60 * 60 * 1000)
				{
					result = "1 day ago";
				}
				else if (t2 - t1 > 60 * 60 * 1000){
					result = String.format("%d hours ago", (t2-t1)/60/60/1000);
				}
				else if (t2 - t1 > 60 * 1000){
					result = String.format("%d mins ago", (t2-t1)/60/1000);
				}else{
					result = String.format("%d seconds ago", (t2-t1)/1000);
				}
			}
			else
			{
				SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
				result = sdf2.format(date);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return result;		
	}
	public static String getTimeString(String dateString)
	{
		String result = "";

		SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat, Locale.getDefault());
		try {
			Date date = sdf.parse(dateString);
			result = getTimeString(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static String getTimeString2(String dateString)
	{
		String result = "";
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat, Locale.getDefault());
			Date date = sdf.parse(dateString);

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);

			SimpleDateFormat sdf2 = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);
			result = sdf2.format(date);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return result;
	}

	public static Date addYear(Date date, int years)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, years);

		return cal.getTime();
	}
	public static Date addMonth(Date date, int amount)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, amount);

		return cal.getTime();
	}
	public static Date addDay(Date date, int amount)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, amount);

		return cal.getTime();
	}
	public static Date addHour(Date date, int amount)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, amount);

		return cal.getTime();
	}
	public static Date addMinute(Date date, int amount)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, amount);

		return cal.getTime();
	}
	public static Date addSecond(Date date, int amount)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, amount);

		return cal.getTime();
	}

	public static int getYear(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return cal.get(Calendar.YEAR);
	}
	public static int getMonth(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return cal.get(Calendar.MONTH) + 1;
	}
	public static int getDay(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return cal.get(Calendar.DAY_OF_MONTH);
	}
	public static int getHour(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return cal.get(Calendar.HOUR);
	}
	public static int getMinute(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return cal.get(Calendar.MINUTE);
	}
	public static int getSecond(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return cal.get(Calendar.SECOND);
	}

	/**
	 * ������ ������ ��´�. ���� ù�� ° ������ �Ͽ����̸� 1�� ��Ÿ����.
	 * @return ����� ����
	 */
	public static int getDayOfWeek()
	{
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * �־��� ������ ������ ��´�.
	 * @param date
	 * @return �־��� ������ ����
	 */
	public static int getDayOfWeek(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * ������ �� °�ֿ� ���ϴ����� �ǵ�����.
	 * @return
	 */
	public static int getWeekOfMonth()
	{
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.WEEK_OF_MONTH);
	}

	/**
	 * �־��� ���ڰ� �� ���� �� °�ֿ� ���ϴ����� �ǵ�����.
	 * @param date
	 * @return
	 */
	public static int getWeekOfMonth(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_MONTH);
	}

	public static Date createDate()
	{
		return new Date();
	}

	/**
	 * ������� �̿��Ͽ� ���� ��ü�� ���Ѵ�.
	 * @param year ��
	 * @param month ��(1-12)
	 * @param date ��(1 - 31)
	 * @return
	 */
	public static Date createDate(int year, int month, int date)
	{
		Calendar cal = Calendar.getInstance();
		cal.set(year, month-1, date);
		return cal.getTime();
	}

	/**
	 * ���� ��ü�� ���Ѵ�.
	 * @param year
	 * @param month
	 * @param date
	 * @param hour �ð�(0 - 23)
	 * @param minute ��(0 - 59)
	 * @param second ��(0 - 59)
	 * @return
	 */
	public static Date createDate(int year, int month, int date, int hour, int minute, int second)
	{
		Calendar cal = Calendar.getInstance();
		cal.set(year, month-1, date, hour, minute, second);

		return cal.getTime();
	}

	/**
	 * �̴޿� �� �ְ� �����ϴ� ���� �ǵ�����.
	 * @return �̴��� �ּ�
	 */
	public static int getWeekCount()
	{
		Calendar cal = Calendar.getInstance();

		// ù° ���� ������ ��´�.
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

		// ������ ���� ��´�.
		int lastDay = cal.getMaximum(Calendar.DAY_OF_MONTH);

		// �� ���� �ָ� ��´�.
		int count = (dayOfWeek + lastDay - 1) / 7;

		if((dayOfWeek + lastDay - 1) % 7 > 0)
			count++;

		return count;
	}

	/**
	 * �־��� ��� �� �ְ� �����ϴ� ���� �ǵ�����.
	 * @param year
	 * @param month
	 * @return �ش� ����� �� ��
	 */
	public static int getWeekCount(int year, int month)
	{
		Calendar cal = Calendar.getInstance();
		cal.set(year, month-1, 1);

		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		int count = (dayOfWeek + lastDay - 1) / 7;

		if((dayOfWeek + lastDay - 1) % 7 > 0)
			count++;

		return count;
	}

	public static int getLastDayOfMonth()
	{
		Calendar cal = Calendar.getInstance();
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public static int getLastDayOfMonth(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	
	public static String getCurrentDateTime()
	{
		Calendar cal = Calendar.getInstance();
		return getDateFormatString(cal.getTime(), dateTimeFormat);
	}

	public static String getCurrenntDate()
	{
		Calendar cal = Calendar.getInstance();
		return getDateFormatString(cal.getTime(), dateFormat);
	}

	public static String getCurrentDate(String dateTimeFormat)
	{
		Calendar cal = Calendar.getInstance();
		return getDateFormatString(cal.getTime(), dateTimeFormat);
	}

	public static String getDateFormatString(Date date, String dateTimeFormat)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);
		return sdf.format(date);
	}

	public static Date createDateFromString(String dateString, String dateTimeFormat)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);
		Date date = null;

		try
		{
			date = sdf.parse(dateString);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return date;
	}

	//	public static void main(String[] args)
	//	{
	//		Calendar cal = Calendar.getInstance();
	//		cal.set(Calendar.MONTH, 7);
	//		cal.set(Calendar.DAY_OF_MONTH, 31);
	//		System.out.println(cal.get(Calendar.WEEK_OF_MONTH));
	//		System.out.println(cal.getFirstDayOfWeek());
	//		
	//		// 2008�� 8�� 31���� ���° ������, ���� ��������
	//		Date date = DateUtil.createDate(2008, 8, 31);
	//		System.out.println(DateUtil.getDateFormatString(date, "yyyy-MM-dd HH:mm:ss"));
	//		System.out.println(DateUtil.getWeekOfMonth(date));
	//		System.out.println(DateUtil.getDayOfWeek(date));
	//		
	//		System.out.println(DateUtil.createDate().toGMTString());
	//	}
}

