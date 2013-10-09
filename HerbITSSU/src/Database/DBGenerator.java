package Database;

import java.sql.ResultSet;



public class DBGenerator {
	// HerbMenuTable 클래스: 테이블 내용을 가지는 클래스
		 		
	    private static String jdbcUrl = "jdbc:mysql://203.253.20.207:3306/herb?characterEncoding=utf8";// 사용하는 데이터베이스명과 인코딩을 포함한 url
	   // private static String jdbcUrl = "jdbc:mysql://192.168.32.148:3306/herb?characterEncoding=utf8";// 사용하는 데이터베이스명과 인코딩을 포함한 url
	    private static String userId = "itssu";// 사용자계정
	    private static String userPass = "ssuherb";// 사용자 패스워드
        private java.sql.Connection conn = null;
        private java.sql.Statement stmt = null;
        private java.sql.ResultSet rs = null;
        
        public boolean closeDB() {
        	boolean res = false;
        	try {
        		if (rs != null) rs.close();
        		if (stmt != null) stmt.close();
        		if (conn != null) conn.close();
        		res = true;
	           	} catch (java.sql.SQLException e) {
	           		e.printStackTrace();
	        }
	        return res;
        }
        
        public ResultSet exec(String query) {
        	ResultSet res = null;
        	query = query.trim();
        	boolean isSelectQuery = false;
        	if( query.toUpperCase().substring(0, 6).compareTo("SELECT") == 0 ){
        		isSelectQuery = true;
        	}
        	
    		try{
    			if( isSelectQuery ){
    				rs = stmt.executeQuery(query);
    			}
    			else {
    				stmt.executeUpdate(query);
    			}
    			res = rs;
    		}catch( java.sql.SQLException e ){
    			System.out.println("SQLException: " + e.getMessage());
	            e.printStackTrace();
    		}
    		return res;
        	
        }
        
	    public boolean connectDB() {
	    	boolean res = false;
	    	 try {
		            Class.forName("com.mysql.jdbc.Driver");// 드라이버 로딩: DriverManager에 등록
		        } catch (ClassNotFoundException e) {
		            System.err.print("ClassNotFoundException: ");
		        }
		        try {
		 
		            conn = java.sql.DriverManager.getConnection(jdbcUrl, userId, userPass);// Connection 객체를 얻어냄
		 
		            stmt = conn.createStatement();// Statement 객체를 얻어냄
		            res = true;

		        	
		        } catch (java.sql.SQLException e) {
		            System.out.println("SQLException: " + e.getMessage());
		            e.printStackTrace();
		        } catch (Exception e) {
		        	e.printStackTrace();
		        }
		        
	    	return res;
	    }
	  
}
