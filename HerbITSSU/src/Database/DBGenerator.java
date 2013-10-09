package Database;

import java.sql.ResultSet;



public class DBGenerator {
	// HerbMenuTable Ŭ����: ���̺� ������ ������ Ŭ����
		 		
	    private static String jdbcUrl = "jdbc:mysql://203.253.20.207:3306/herb?characterEncoding=utf8";// ����ϴ� �����ͺ��̽���� ���ڵ��� ������ url
	   // private static String jdbcUrl = "jdbc:mysql://192.168.32.148:3306/herb?characterEncoding=utf8";// ����ϴ� �����ͺ��̽���� ���ڵ��� ������ url
	    private static String userId = "itssu";// ����ڰ���
	    private static String userPass = "ssuherb";// ����� �н�����
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
		            Class.forName("com.mysql.jdbc.Driver");// ����̹� �ε�: DriverManager�� ���
		        } catch (ClassNotFoundException e) {
		            System.err.print("ClassNotFoundException: ");
		        }
		        try {
		 
		            conn = java.sql.DriverManager.getConnection(jdbcUrl, userId, userPass);// Connection ��ü�� ��
		 
		            stmt = conn.createStatement();// Statement ��ü�� ��
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
