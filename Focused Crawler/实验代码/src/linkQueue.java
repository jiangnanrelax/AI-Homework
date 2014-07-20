import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;


public class linkQueue {
	private Set<String> visited;
	private PriorityBlockingQueue<PriorityURL> pageQueue;
	String[] shield_list = { ".zip", ".gz", ".rar", ".exe", ".jpg", ".png",
			".pdf", ".csv", ".iso", ".mp4", ".doc", ".ppt", ".gif", ".avi" };

	
	public linkQueue(){
		visited=new HashSet<String>();
		pageQueue=new PriorityBlockingQueue<PriorityURL>();
		
	}
	public synchronized void setVisited(String contentmd5){
		
		//System.out.println(contentmd5+"  :  "+contentmd5);
		visited.add(contentmd5);
	}
	public boolean isVisited(String url){
		if(visited.contains(url))
			return true;
		return false;
	}

	public synchronized void pushUrl(String url,String contentmd5,double priority) {
		if(isValidate(url)&&(!isVisited(url))){
			pageQueue.put(new PriorityURL("", url, contentmd5, priority));
			//System.out.println("debug mode (push url):"+url);
			setVisited(contentmd5);
		}
		
	}
	public synchronized String popUrl() {
		String url="";
		try {
			url = pageQueue.take().getUrl();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//System.out.println("debug mode (pop url):"+url);
		return url;
	}
	public boolean isEmpty(){
		if(pageQueue.size()==0)
			return true;
		return false;
	}
	public boolean isValidate(String url){
		if(url.length()==0||url==null||url.indexOf("http")==-1){
			return false;
		}
			return accept(url);
	}
	public boolean accept(String url) {
		for (String item : shield_list) {
			if (url.endsWith(item))
				return false;
		}
		return true;
	}
}
