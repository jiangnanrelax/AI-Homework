
public class PriorityURL implements Comparable<PriorityURL>{
	public String title;
	public String contentMD5;
	public String url_link;
	public double priority;
	public double cos_relevant;
	public PriorityURL(String title,String url,String contentmd5,double priority){
		super();
		this.title=title;
		this.url_link=url;
		this.contentMD5=contentmd5;
		this.priority=priority;
	}
	public PriorityURL(String title,String url,String contentmd5,double priority,double cos_relevant){
		super();
		this.title=title;
		this.url_link=url;
		this.contentMD5=contentmd5;
		this.priority=priority;
		this.cos_relevant=cos_relevant;
	}
	public String getUrl(){
		return url_link;
	}
	
	public void setUrl(String url){
		this.url_link=url;
	}
	public void setContentMD5(String md5){
		this.contentMD5=md5;
	}
	public void getTitle(String title){
		this.title=title;
	}
	public void setCos_relevant(double cos_relevant){
		this.cos_relevant=cos_relevant;
	}

	@Override
	public int compareTo(PriorityURL pu) {
		if(priority>pu.priority)  return 1;
		else if(priority<pu.priority) return -1;
		else                        return 0;
	}

}
