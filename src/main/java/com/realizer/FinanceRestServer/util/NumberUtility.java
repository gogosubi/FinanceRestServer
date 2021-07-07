package com.realizer.FinanceRestServer.util;

public class NumberUtility 
{
	// 숫자와 부호만 남겨놓고 제거하는 필터
	private static final String filter = "[^0-9]*.";

	public static Object convertNumber(String str)
	{		
		System.out.println("======>" + str);
		System.out.println("======>" + str.replaceAll(filter, ""));
		// 실수형이면
		if ( str.contains(".") )
		{
			return Double.parseDouble(str.replaceAll(filter, ""));
		}
		else
		{
			return Integer.parseInt(str.replaceAll(filter, ""));
		}
	}
}
