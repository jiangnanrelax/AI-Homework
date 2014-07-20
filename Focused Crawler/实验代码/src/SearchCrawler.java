import google.PageRank;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Timer;
import java.util.Map.Entry;

import HTTP.MyTimerTask;

public class SearchCrawler {
	public static final double threadhold = 0.91;
	public int threadNum = 0;
	private Hashtable<String, Integer> keyWords = new Hashtable<String, Integer>();
	public Downloader downloader;
	public linkQueue queue;
	public AsynStorage asynstorage;
	public PageRank prService;

	public static final String filepath = "D:\\data.txt";

	public SearchCrawler(String torrent, int threadNum) {
		downloader = new Downloader();
		this.threadNum = threadNum;
		queue = new linkQueue();
		queue.pushUrl(torrent, "", 1);
		asynstorage = new AsynStorage();
		prService = new PageRank();
		Timer timer = new Timer();

		long delay = 1 * 1000;
		long period = 1000;

		timer.schedule(asynstorage, delay, period);

	}
	
	

	public void init_keyword(String config_filepath) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					config_filepath));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] items = line.split(" ");
				for (String item : items) {
					addKeyWord(item, 1);
				}
			}
		} catch (Exception ex) {

		}
	}

	public void addKeyWord(String word, int count) {
		keyWords.put(word, count);
	}

	public void removeKeyWord(String word) {
		if (word != null) {
			if (keyWords.containsKey(word)) {
				keyWords.remove(word);
			}
		}
	}

	public void removeAllKeyWords() {
		keyWords.clear();
	}

	public synchronized void push(HashMap<String, String> map,
			String contentmd5, double priority) {
		Iterator<Entry<String, String>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> et = it.next();
			String key = et.getKey();
			String value = et.getValue();
			PrintWriter pw;
			try {
				pw = new PrintWriter(new BufferedWriter(new FileWriter("D://debug.txt")));
				pw.print(value);
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (!queue.isVisited(key))
				queue.pushUrl(key, contentmd5, priority);
		}
	}
	
	

	public void close() {
		if(downloader!=null)	downloader = null;
		if(queue!=null)        queue = null;
		if(asynstorage!=null)  asynstorage = null;
		if(prService!=null)    prService = null;
	}

	/**
	 * 计算相关度
	 * 
	 * @param destination
	 *            ：目标网页与初始的主题网页计算相关度
	 */
	public double calRelavancy(String content) {
		Hashtable<String, Integer> destination = new Hashtable<String, Integer>();

		int count=0;
		for (String key : keyWords.keySet()) {
			count = content.split(key).length - 1;
			destination.put(key, count);
		}

		long sum1 = 0, sum2 = 0, sum3 = 0;
		for (String key : keyWords.keySet()) {
			sum1 += keyWords.get(key).intValue()
					* destination.get(key).intValue();
			sum2 += keyWords.get(key).intValue() * keyWords.get(key).intValue();
			sum3 += destination.get(key).intValue()
					* destination.get(key).intValue();
		}
		if (sum3 == 0)      return 0;
		return sum1 * 1.0 / (Math.sqrt(sum2) * Math.sqrt(sum3));
	}

	public void parallelhandle() {
		for (int i = 0; i < threadNum; i++) {
			new Task(i).start();
		}
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private class Task extends Thread {
		public int number;

		Task(int num) {
			this.number = num;
		}

		public void run() {
			try {
				while (!queue.isEmpty()) {
					String urlpattern = queue.popUrl();
					HashMap<String, String> map = downloader.getUrl(urlpattern);
					String contentMD5 = downloader.getContentMD5();
					String title = downloader.getTitle();
					String content = downloader.getContent();
					double cos_relevant = calRelavancy(content);
					//double priority=prService.getPageRank(urlpattern);
					double priority=0.1;
					PriorityURL purl=new PriorityURL(title, urlpattern,
							contentMD5, priority,cos_relevant);
					//purl.setCos_relevant(cos_relevant);
					System.out.println("cos_relevancy: "+cos_relevant+"  pagerank :"+priority);
					asynstorage.push(purl);
					if (cos_relevant >= threadhold && map != null) {
						push(map, contentMD5, cos_relevant);

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				close();
			}
		}
	}

	public void initalize(String urlPattern) {
		try {
			HashMap<String, String> map = downloader.getUrl(urlPattern);
			String contentMD5 = downloader.getContentMD5();
			String title = downloader.getTitle();
			String content = downloader.getContent();
			double cos_relevant = calRelavancy(content);
			asynstorage.push(new PriorityURL(title, urlPattern,contentMD5, cos_relevant));
			if (map != null) {
				push(map, contentMD5, cos_relevant);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
