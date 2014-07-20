import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;


public class JDBCManager {
	private static final String DRIVERCLASS = "com.mysql.jdbc.Driver";
	private static final String URL = "jdbc:mysql://127.0.0.1:3306/spidermanstorage";
	private static final String USERNAME = "sa";
	private static final String PASSWORD = "123456";
	protected static Connection conn = null;
	protected static PreparedStatement pstmt ;
	protected static ResultSet re = null;
	public String URL_TABLE="url_table";

	private void open() {
		try {
			Class.forName(DRIVERCLASS);
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean executeQuery(String contentMD5) {
		try {
			if(conn==null)  open();
			String template="select * from "+URL_TABLE+" where contentMD5=?";
			
			pstmt=conn.prepareStatement(template);
			pstmt.setString(1, contentMD5);
			re = pstmt.executeQuery();
			if(re.next()) return true;
			else return false;
			
		} catch (SQLException e) {
			System.out.println("query error!");
			e.printStackTrace();
		}
		return false;
	}
	
	
	public boolean executeUpdate(String[] elements,double pagerank,double cos_Relevancy){
		if(conn==null) open();
		try {
			String template="insert into "+URL_TABLE+" values(null,? ,? ,?,?,?)";
			pstmt=conn.prepareStatement(template);
			int count=0;
			for(;count<elements.length;count++){
				System.out.println(elements[count]);
				pstmt.setString(count+1, elements[count]);
			}
			pstmt.setDouble(4, pagerank);
			pstmt.setDouble(5, cos_Relevancy);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println("update error!");
			e.printStackTrace();
			return false;
		}
	}
	public void close() throws SQLException {
		if (re != null) {
			re.close();
			re = null;
		}
		if (pstmt != null) {
			pstmt.close();
			pstmt = null;
		}
		if (conn != null) {
			conn.close();
			conn = null;
		}

	}

}
