package com.channelsoft.ems.report.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 
 * @ClassName: GetKeys
 * @Description: 数据统计的工具类
 * @author chenglitao
 * @date 2015年12月12日 下午3:32:29
 *
 */
public class GetKeys
{

	/**
	 * 
	 * @Description:根据时间段获得时间轴
	 * @param  @param beginTime
	 * @param  @param endTime
	 * @param  @return
	 * @return List<String>  
	 * @author chenglitao
	 * @date 2015年12月12日 下午3:33:01   
	 * @throws
	 */
	public static List<String> getKeys(String beginTime, String endTime)
	{

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<String> resultList = new ArrayList<String>();

		try
		{
			Calendar beginCalendar = Calendar.getInstance();
			beginCalendar.setTime(sdf.parse(beginTime));

			// 计算两个时间的相差天数
			int days = daysBetween(beginTime, endTime);

			if (days == 0)
			{
				resultList.add(
				        beginCalendar.get(Calendar.MONTH) + 1 + "月" + beginCalendar.get(Calendar.DAY_OF_MONTH) + "日");
				String temp = "";
				for (int i = 1; i <= 23; i++)
				{
					if (i < 10)
					{
						temp = "0" + i + ":00";
					} else
					{
						temp = i + ":00";
					}
					resultList.add(temp);
				}
			} else if (days == 1)
			{
				resultList.add(
				        beginCalendar.get(Calendar.MONTH) + 1 + "月" + beginCalendar.get(Calendar.DAY_OF_MONTH) + "日");
				String temp = "";
				for (int i = 3; i <= 23;)
				{
					if (i < 10)
					{
						temp = "0" + i + ":00";
					} else
					{
						temp = i + ":00";
					}
					i += 3;
					resultList.add(temp);
				}

				// 开始日期加一天
				beginCalendar.add(Calendar.DAY_OF_MONTH, 1);

				resultList.add(
				        beginCalendar.get(Calendar.MONTH) + 1 + "月" + beginCalendar.get(Calendar.DAY_OF_MONTH) + "日");
				temp = "";
				for (int i = 3; i <= 23;)
				{
					if (i < 10)
					{
						temp = "0" + i + ":00";
					} else
					{
						temp = i + ":00";
					}
					i += 3;
					resultList.add(temp);
				}
			} else if (days == 2)
			{
				resultList.add(
				        beginCalendar.get(Calendar.MONTH) + 1 + "月" + beginCalendar.get(Calendar.DAY_OF_MONTH) + "日");
				String temp = "";
				for (int i = 4; i <= 23;)
				{
					if (i < 10)
					{
						temp = "0" + i + ":00";
					} else
					{
						temp = i + ":00";
					}
					i += 4;
					resultList.add(temp);
				}
				// 开始日期加一天
				beginCalendar.add(Calendar.DAY_OF_MONTH, 1);

				resultList.add(
				        beginCalendar.get(Calendar.MONTH) + 1 + "月" + beginCalendar.get(Calendar.DAY_OF_MONTH) + "日");
				temp = "";
				for (int i = 4; i <= 23;)
				{
					if (i < 10)
					{
						temp = "0" + i + ":00";
					} else
					{
						temp = i + ":00";
					}
					i += 4;
					resultList.add(temp);
				}
				// 开始日期加一天
				beginCalendar.add(Calendar.DAY_OF_MONTH, 1);

				resultList.add(
				        beginCalendar.get(Calendar.MONTH) + 1 + "月" + beginCalendar.get(Calendar.DAY_OF_MONTH) + "日");
				temp = "";
				for (int i = 4; i <= 23;)
				{
					if (i < 10)
					{
						temp = "0" + i + ":00";
					} else
					{
						temp = i + ":00";
					}
					i += 4;
					resultList.add(temp);
				}

			} else if (days == 3)
			{
				resultList.add(
				        beginCalendar.get(Calendar.MONTH) + 1 + "月" + beginCalendar.get(Calendar.DAY_OF_MONTH) + "日");
				String temp = "";
				for (int i = 6; i <= 23;)
				{
					if (i < 10)
					{
						temp = "0" + i + ":00";
					} else
					{
						temp = i + ":00";
					}
					i += 6;
					resultList.add(temp);
				}
				// 开始日期加一天
				beginCalendar.add(Calendar.DAY_OF_MONTH, 1);

				resultList.add(
				        beginCalendar.get(Calendar.MONTH) + 1 + "月" + beginCalendar.get(Calendar.DAY_OF_MONTH) + "日");
				temp = "";
				for (int i = 6; i <= 23;)
				{
					if (i < 10)
					{
						temp = "0" + i + ":00";
					} else
					{
						temp = i + ":00";
					}
					i += 6;
					resultList.add(temp);
				}
				// 开始日期加一天
				beginCalendar.add(Calendar.DAY_OF_MONTH, 1);

				resultList.add(
				        beginCalendar.get(Calendar.MONTH) + 1 + "月" + beginCalendar.get(Calendar.DAY_OF_MONTH) + "日");
				temp = "";
				for (int i = 6; i <= 23;)
				{
					if (i < 10)
					{
						temp = "0" + i + ":00";
					} else
					{
						temp = i + ":00";
					}
					i += 6;
					resultList.add(temp);
				}
				// 开始日期加一天
				beginCalendar.add(Calendar.DAY_OF_MONTH, 1);

				resultList.add(
				        beginCalendar.get(Calendar.MONTH) + 1 + "月" + beginCalendar.get(Calendar.DAY_OF_MONTH) + "日");
				temp = "";
				for (int i = 6; i <= 23;)
				{
					if (i < 10)
					{
						temp = "0" + i + ":00";
					} else
					{
						temp = i + ":00";
					}
					i += 6;
					resultList.add(temp);
				}

			} else if (days == 4)
			{

				for (int i = 0; i <= 4; i++)
				{
					// 开始日期加一天
					if (i != 0)
					{
						beginCalendar.add(Calendar.DAY_OF_MONTH, 1);
					}
					resultList.add(beginCalendar.get(Calendar.MONTH) + 1 + "月"
					        + beginCalendar.get(Calendar.DAY_OF_MONTH) + "日");
					resultList.add("08:00");
					resultList.add("16:00");
				}
			} else if (days == 5)
			{
				for (int i = 0; i <= 5; i++)
				{
					// 开始日期加一天
					if (i != 0)
					{
						beginCalendar.add(Calendar.DAY_OF_MONTH, 1);
					}
					resultList.add(beginCalendar.get(Calendar.MONTH) + 1 + "月"
					        + beginCalendar.get(Calendar.DAY_OF_MONTH) + "日");
					resultList.add("08:00");
					resultList.add("16:00");
				}
			} else if (days >= 6 && days <= 12)
			{

				for (int i = 0; i <= days; i++)
				{
					// 开始日期加一天
					if (i != 0)
					{
						beginCalendar.add(Calendar.DAY_OF_MONTH, 1);
					}
					resultList.add(beginCalendar.get(Calendar.MONTH) + 1 + "月"
					        + beginCalendar.get(Calendar.DAY_OF_MONTH) + "日");
					resultList.add("12:00");
				}

			} else if (days > 12)
			{

				for (int i = 0; i <= days; i++)
				{
					// 开始日期加一天
					if (i != 0)
					{
						beginCalendar.add(Calendar.DAY_OF_MONTH, 1);
					}
					resultList.add(beginCalendar.get(Calendar.MONTH) + 1 + "月"
					        + beginCalendar.get(Calendar.DAY_OF_MONTH) + "日");

				}

			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return resultList;
	}

	/**
	 * 
	 * @Description: 计算两个字符串类型的日期相差天数
	 * @param  @param beginDateStr
	 * @param  @param endDateStr
	 * @param  @return
	 * @return int  
	 * @author chenglitao
	 * @date 2015年12月14日 上午10:14:58   
	 * @throws
	 */
	public static int daysBetween(String beginDateStr, String endDateStr)
	{

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long between_days = 0;
		Date beginDate = null;
		Date endDate = null;
		try
		{
			beginDate = sdf.parse(beginDateStr);
			endDate = sdf.parse(endDateStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(beginDate);
			long time1 = cal.getTimeInMillis();
			cal.setTime(endDate);
			long time2 = cal.getTimeInMillis();
			between_days = (time2 - time1) / (1000 * 3600 * 24);

		} catch (ParseException e)
		{

			e.printStackTrace();
		}

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 
	 * @Description:比较一个时间是否在一个时间段 内
	 * @param  @param calendar
	 * @param  @param beforeTime
	 * @param  @param endTime
	 * @param  @return
	 * @return boolean  
	 * @author chenglitao
	 * @date 2015年12月14日 上午11:19:25   
	 * @throws
	 */
	public static boolean compareTime(Calendar calendar, Calendar beforCalendar, Calendar endCalendar)
	{
		boolean flag = false;
		if (calendar.after(beforCalendar) && calendar.before(endCalendar))
		{
			flag = true;
		}
		return flag;
	}

	public static List<String> getBeforeAfterCalendar(String beginTime, String endTime)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		List<String> list = new ArrayList<String>();
		try
		{
			Date beginDate = sdf.parse(beginTime);

			Calendar beginCalendar = Calendar.getInstance();

			int days = daysBetween(beginTime, endTime);
			if (days == 0)
			{
				beginCalendar.setTime(beginDate);
				list.add(sdf.format(beginCalendar.getTime()));
				for (int i = 1; i <= 24; i++)
				{
					beginCalendar.add(Calendar.HOUR_OF_DAY, 1);
					
					list.add(sdf.format(beginCalendar.getTime()));
				}

			} else if (days == 1)
			{
				beginCalendar.setTime(beginDate);
				list.add(sdf.format(beginCalendar.getTime()));
				for (int i = 1; i <=16;i++)
				{
					beginCalendar.add(Calendar.HOUR_OF_DAY, 3);
					list.add(sdf.format(beginCalendar.getTime()));
					 
				}

			} else if (days == 2)
			{

				beginCalendar.setTime(beginDate);
				list.add(sdf.format(beginCalendar.getTime()));
				for (int i = 1; i <= 18;i++)
				{
					beginCalendar.add(Calendar.HOUR_OF_DAY, 4);
					list.add(sdf.format(beginCalendar.getTime()));
				 
				}
			} else if (days == 3)
			{
				beginCalendar.setTime(beginDate);
				list.add(sdf.format(beginCalendar.getTime()));
				for (int i = 1; i <= 16;i++)
				{
					beginCalendar.add(Calendar.HOUR_OF_DAY, 6);
					list.add(sdf.format(beginCalendar.getTime()));
					 
				}

			} else if (days == 4)
			{
				beginCalendar.setTime(beginDate);
				list.add(sdf.format(beginCalendar.getTime()));
				for (int i = 1; i <= 15;i++)
				{
					beginCalendar.add(Calendar.HOUR_OF_DAY, 8);
					list.add(sdf.format(beginCalendar.getTime()));
					 
				}

			} else if (days == 5)
			{
				beginCalendar.setTime(beginDate);
				list.add(sdf.format(beginCalendar.getTime()));
				for (int i = 1; i <= 18;i++)
				{
					beginCalendar.add(Calendar.HOUR_OF_DAY, 8);
					list.add(sdf.format(beginCalendar.getTime()));
				 
				}

			} else if (days >= 6 && days <= 12)
			{
				beginCalendar.setTime(beginDate);
				list.add(sdf.format(beginCalendar.getTime()));
				for (int i = 1; i <= (days+1)*2;i++)
				{
					beginCalendar.add(Calendar.HOUR_OF_DAY, 12);				 
					list.add(sdf.format(beginCalendar.getTime()));
					 
				 
				}
			} else if (days > 12)
			{

				beginCalendar.setTime(beginDate);
				list.add(sdf.format(beginCalendar.getTime()));
				for (int i = 1; i <=(days + 1) * 1;i++)
				{
					beginCalendar.add(Calendar.HOUR_OF_DAY, 24);
					list.add(sdf.format(beginCalendar.getTime()));
					 
				}
			}
		} catch (ParseException e)
		{
			 
			e.printStackTrace();
		}
		

 
		return list;
	}

}
