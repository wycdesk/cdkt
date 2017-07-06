package com.channelsoft.cri.util;

import java.sql.Types;

import com.channelsoft.cri.constant.BaseErrCode;
import com.channelsoft.cri.exception.BaseException;

public class SqlParamCheckUtil
{
	public static String check(String sqls, Object[] args, int[] argTypes) throws BaseException
	{
		String [] sqlSplit = sqls.split("\\?", -1);
		if ((sqlSplit.length -1) != args.length || args.length!=argTypes.length)
		{
			throw new BaseException(BaseErrCode.DB_ERROR.code, "参数个数不正确：问号=" + (sqlSplit.length -1) + "，params="+args.length+"，types=" + argTypes.length);
		}
		StringBuffer sql = new StringBuffer();
		sql.append(sqlSplit[0]);
		for (int i=0;i<args.length;i++)
		{
			if (args[i]==null)
			{
				sql.append("NULL");
			}
			else
			{
				switch(argTypes[i])
				{
				
					case Types.INTEGER:
						sql.append(args[i]);
						break;
					case Types.NUMERIC:
						sql.append(args[i]);
						break;
					case Types.VARCHAR:
						sql.append("'").append(args[i]).append("'");
						break;
					default:
						sql.append("?");
				}
			}
			sql.append(sqlSplit[i+1]);
		}
		return sql.toString();
	}
}
