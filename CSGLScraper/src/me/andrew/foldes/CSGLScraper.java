package me.andrew.foldes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CSGLScraper
{

	//ArrayList<ArrayList<String>> tradeDescs;
	
	public CSGLScraper()
	{
		
	}
	
	public ArrayList<ArrayList<String>> Scrape(String url, ArrayList<String> keyList, boolean shouldContain)
	{
		ArrayList<ArrayList<String>> tradeDescs = new ArrayList<ArrayList<String>>();
		try
		{
			
			
			ArrayList<String> tLinks = new ArrayList<String>();
			ArrayList<String> tDescs = new ArrayList<String>();
			boolean isOnePage = false;
			String generalURL = null;
			int index = 1;
			if (url.charAt(url.length() - 3) != 'p' )
			{
				index = 20;
				isOnePage = true;
			}
			else 
			{
				generalURL = url.substring(0, url.length() - 1);
			}
			boolean isSiteEmpty = false;
			while (!isSiteEmpty && index < 21)
			{
				
				Document site = null;
				if (isOnePage)
				{
					site = Jsoup.connect(url).get();
				} else
				{
					site = Jsoup.connect(generalURL + "" + index).get();
				}
				Elements tradeheaders = site.getElementsByClass("tradeheader");
				if (tradeheaders.isEmpty())
				{
					isSiteEmpty = true;
					continue;
				}
				for (Element e : tradeheaders)
				{
					String tradeLink = "csgolounge.com/" + e.select("a[href]").first().attr("href").toString();
					String tradeDesc = " " + e.attr("title").toString();
					if (!keyList.isEmpty())
					{
						for (String s : keyList)
						{
							if (Pattern.compile(Pattern.quote(s), Pattern.CASE_INSENSITIVE).matcher(tradeDesc).find())
							{
								if (shouldContain)
								{
									int matchIndex = tradeDesc.indexOf(s);
									tLinks.add(tradeLink);
									int beginIndex = matchIndex - 40;
									int endIndex = matchIndex + 40;
									if (beginIndex < 0)
									{
										beginIndex = 0;
									}
									if (endIndex > tradeDesc.length() - 1)
									{
										endIndex = tradeDesc.length() - 1;
									}
									tDescs.add(tradeDesc.substring(beginIndex, endIndex));
								}
							} else
							{
								if (!shouldContain)
								{
									tLinks.add(tradeLink);
									tDescs.add(tradeDesc);
								}
							}
						}
					} else
					{
						tLinks.add(tradeLink);
						tDescs.add(tradeDesc);
					}
					
					
				}
				index++;
			}

			
			tradeDescs.add(tLinks);
			tradeDescs.add(tDescs);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			//JOptionPane.showInternalMessageDialog(null, e.toString());
			JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		return tradeDescs;
	}
	
	
	

}
