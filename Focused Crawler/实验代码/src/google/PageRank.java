package google;

import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;


public class PageRank {
	

	/**
	 * List of available google datacenter IPs and addresses
	 */
	static final public String GOOGLE_PR_DATACENTER_IP = "toolbarqueries.google.com";

	/**
	 * Must receive a domain in form of: "http://www.domain.com"
	 * 
	 * @param domain 
	 * @return PR rating (int) or -1 if unavailable or internal error happened.
	 */
	public int getPageRank(String domain) {
		
		JenkinsHash jHash = new JenkinsHash();
		long hash = jHash.hash(("info:" + domain).getBytes());

		String url = "http://" + GOOGLE_PR_DATACENTER_IP
				+ "/tbr?client=navclient-auto&hl=en&" + "ch=6" + hash
				+ "&ie=UTF-8&oe=UTF-8&features=Rank&q=info:" + domain;

		try {
			URLConnection conn = new URL(url).openConnection();
			conn.setConnectTimeout(10000);
			//conn.set
			String pageRankResponse = IOUtils.toString(conn.getInputStream());
			
			if (StringUtils.isNotBlank(pageRankResponse)) {
				return NumberUtils.toInt(pageRankResponse.split(":")[2].trim());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return -1;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PageRank prService = new PageRank();
		System.out.println("PageRank: " + prService.getPageRank("http://www.narutom.com/"));
	}
}
