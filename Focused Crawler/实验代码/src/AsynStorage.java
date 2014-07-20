import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TimerTask;


public class AsynStorage extends TimerTask{
	private Queue<PriorityURL> focusedQueue;
	private JDBCManager jm;
	public AsynStorage(){
		super();
		focusedQueue=new PriorityQueue<PriorityURL>();
		jm=new JDBCManager();
	}
	@Override
	public void run() {
		while(!isEmpty()){
			PriorityURL pUrl=pop();
			boolean ret=jm.executeQuery(pUrl.contentMD5);
			if(ret==true)
				System.out.println("Duplicate entry");
			else{
				jm.executeUpdate(new String[]{pUrl.title,pUrl.url_link,pUrl.contentMD5},
						pUrl.priority,pUrl.cos_relevant);
			}
				
		}
	}
	public synchronized void push(PriorityURL pUrl){
		focusedQueue.add(pUrl);
	}
	
	public synchronized PriorityURL pop(){
		PriorityURL pUrl=focusedQueue.poll();
		return pUrl;
	}
	
	public boolean isEmpty(){
		return focusedQueue.isEmpty();
	}
}
