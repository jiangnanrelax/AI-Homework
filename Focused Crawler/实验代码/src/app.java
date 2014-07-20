import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;


public class app {
	
	Label lblNewLabel_1;
	Button startButton;
	ProgressBar progressBar ;
	
	public boolean suspend_mode=true;
	public int threadNum=4;
	public  String torrent;
	public String theme="big data";
	public String config_filepath=null;
	
	protected Shell shlFocusedCrawler;
	private Text torrentText;
	private Text themeText;
	SearchCrawler sc;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			app window = new app();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlFocusedCrawler.open();
		shlFocusedCrawler.layout();
		while (!shlFocusedCrawler.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlFocusedCrawler = new Shell();
		shlFocusedCrawler.setSize(450, 300);
		shlFocusedCrawler.setText("Focused Crawler");
		int width = shlFocusedCrawler.getMonitor().getClientArea().width;
		int height = shlFocusedCrawler.getMonitor().getClientArea().height;
		int x = shlFocusedCrawler.getSize().x;
		int y = shlFocusedCrawler.getSize().y;
		if (x > width) {
			shlFocusedCrawler.getSize().x = width;
		}
		if (y > height) {
			shlFocusedCrawler.getSize().y = height;
		}
		shlFocusedCrawler.setLocation((width - x) / 2, (height - y) / 2);
		
		Label lblNewLabel = new Label(shlFocusedCrawler, SWT.NONE);
		lblNewLabel.setAlignment(SWT.RIGHT);
		lblNewLabel.setFont(SWTResourceManager.getFont("微软雅黑", 13, SWT.NORMAL));
		lblNewLabel.setBounds(48, 26, 90, 22);
		lblNewLabel.setText("初始种子：");
		
		torrentText = new Text(shlFocusedCrawler, SWT.BORDER);
		torrentText.setFont(SWTResourceManager.getFont("Consolas", 11, SWT.NORMAL));
		torrentText.setText("http://www.csdn.net/");
		torrentText.setBounds(204, 28, 178, 23);
		
		Label label = new Label(shlFocusedCrawler, SWT.NONE);
		label.setAlignment(SWT.RIGHT);
		label.setFont(SWTResourceManager.getFont("微软雅黑", 13, SWT.NORMAL));
		label.setBounds(66, 69, 72, 22);
		label.setText("\u4E3B\u9898\uFF1A");
		
		progressBar = new ProgressBar(shlFocusedCrawler, SWT.HORIZONTAL | SWT.SMOOTH);
		progressBar.setBounds(0, 244, 434, 17);
		
		startButton = new Button(shlFocusedCrawler, SWT.NONE);
		startButton.setEnabled(false);
		startButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				suspend_mode=false;
				torrent=torrentText.getText().trim();
				theme=themeText.getText().trim();
				System.out.println(threadNum+" "+torrent+" "+theme);
				sc=new SearchCrawler(torrent,threadNum);
				sc.initalize(torrent);
				sc.init_keyword(config_filepath);
				sc.parallelhandle();
			}
		});
		startButton.setBounds(132, 204, 80, 27);
		startButton.setText("\u5F00\u59CB");
		
		Button suspendButton = new Button(shlFocusedCrawler, SWT.NONE);
		suspendButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				suspend_mode=true;
				sc.close();
			}
		});
		suspendButton.setBounds(238, 204, 80, 27);
		suspendButton.setText("\u6682\u505C");
		
		Button exitButton = new Button(shlFocusedCrawler, SWT.NONE);
		exitButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				sc.close();
				shlFocusedCrawler.close();
			}
		});
		exitButton.setBounds(344, 204, 80, 27);
		exitButton.setText("\u53D6\u6D88");
		
		themeText = new Text(shlFocusedCrawler, SWT.BORDER);
		themeText.setText("大数据");
		themeText.setBounds(204, 63, 178, 23);
		
		Label label_1 = new Label(shlFocusedCrawler, SWT.NONE);
		label_1.setAlignment(SWT.RIGHT);
		label_1.setFont(SWTResourceManager.getFont("微软雅黑", 13, SWT.NORMAL));
		label_1.setBounds(66, 103, 72, 22);
		label_1.setText("线程数：");
		
		final Spinner spinner = new Spinner(shlFocusedCrawler, SWT.BORDER);
		spinner.setMinimum(4);
		spinner.setMaximum(20);
		spinner.setBounds(204, 103, 178, 23);
		
		Button openButton = new Button(shlFocusedCrawler, SWT.NONE);
		openButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(shlFocusedCrawler, SWT.OPEN);
			    fileDialog.setFilterNames(new String[] { "Config Files ","All Files (*.*)" });
			    fileDialog.setFilterExtensions(new String[] { "*.dat ; *.txt", "*.*" });
			    fileDialog.setFilterPath(System.getProperty("user.dir"));
			    fileDialog.setText("open ");
			    config_filepath= fileDialog.open();
			    lblNewLabel_1.setText("关键词加载完成");
			    startButton.setEnabled(true);
			}
		});
		openButton.setBounds(26, 204, 80, 27);
		openButton.setText("打开文件");
		
		Label label_2 = new Label(shlFocusedCrawler, SWT.NONE);
		label_2.setAlignment(SWT.RIGHT);
		label_2.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 13, SWT.NORMAL));
		label_2.setBounds(31, 145, 107, 27);
		label_2.setText("关键词集合：");
		
		lblNewLabel_1 = new Label(shlFocusedCrawler, SWT.NONE);
		lblNewLabel_1.setText("请加载关键词");
		lblNewLabel_1.setToolTipText("请加载关键词");
		lblNewLabel_1.setBounds(204, 145, 178, 27);
		
		spinner.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				String value=String.valueOf(spinner.getSelection());
				threadNum=Integer.parseInt(value);
				System.out.println(value);
				
			}
		});

	}
}
