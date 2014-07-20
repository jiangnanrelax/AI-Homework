import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Downloader {
	protected Document doc;
	
	
	private static long serialNum = 1;
	public static final String DEFAULT_PATH = "D://data";
	public static final String agent = "Mozilla";


	public HashMap<String, String> getUrl(String url) throws IOException {
		System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "8182");
		doc = Jsoup.connect(url.toString()).userAgent(agent)
				.cookie("auth", "token").
				timeout(10000)
				.ignoreContentType(true)
				.ignoreHttpErrors(true)
				.get();
		saveTolocal();
		Elements links = doc.getElementsByTag("a");
		HashMap<String, String> map = new HashMap<String, String>();
		for (Element item : links) {
			String href = item.attr("abs:href");
			String anchor = item.text();
			map.put(href, anchor);
		}
		return map;
	}

	public String getTitle() {
		String title = "";
		if (doc != null)
			title = doc.title();
		System.out.println("title is " + title);
		return title;
	}

	public String getContent() {
		if (doc != null)
			return doc.body().toString();
		else
			return "";
	}

	public String getContentMD5() {
		String mymd5 = MD5.GetMD5Code(getContent());
		//System.out.println("MD5 code :" + mymd5);
		return mymd5;
	}

	public synchronized void saveTolocal() {
		String content = getContent();
		DecimalFormat format = new DecimalFormat("00000000");
		String name = format.format(serialNum++);
		String filepath = DEFAULT_PATH + "/" + name+".html";
		try {
			File outfile = new File(filepath);
			if (!outfile.isFile()) {
				outfile.createNewFile();
			}
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter (new FileOutputStream(outfile),"gbk"));
			bw.write(content);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
