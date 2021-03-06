package com.realizer.FinanceRestServer.util;

public class TypeUtility 
{
	// 부호, 정수, 실수를 확인하는 Filter
	private static final String filter = "^[+-]?\\d*(\\.?\\d*)$";

	public static Object convertType(String str)
	{	
		System.out.println("입력 문자열 : " + str);
		// 지정된 문자열 제거
		String convertStr = str.replaceAll(",", "")
				.replaceAll("%", "")
				.replaceAll(" ", "")
				.replaceAll("하락", "-")
				.replaceAll("상승", "+")
				.replaceAll("∞", "9999.99");
		
		// 실수형이면
		if ( convertStr.contains(".") )
		{
			return Double.parseDouble(convertStr);
		}
		// 정수형
		else if ( convertStr.matches(filter) )
		{
			return Long.parseLong(convertStr);
		}
		// 문자형
		else
		{
			System.out.println("========>" + convertStr);
			System.out.println(str);
			return str;
		}
	}
}
