package com.realizer.FinanceRestServer.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Crawler 
{
	public static Document getCrawlingSite(String url)
	{
		Document document = null;
		try
		{
			document = Jsoup.connect(url).get();
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		return document;
	}
}
