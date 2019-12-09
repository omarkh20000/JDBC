package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.net.ssl.SSLException;

public class Main {
	static private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

	// update USER, PASS and DB URL according to credentials provided by the website:
	// https://remotemysql.com/
	// in future move these hard coded strings into separated config file or even better env variables
	static private final String DB = "UrzHpVpLYN";
	static private final String DB_URL = "jdbc:mysql://remotemysql.com/"+ DB + "?useSSL=false";
	static private final String USER = "UrzHpVpLYN";
	static private final String PASS = "1H9J1q0ZDI";

	public static void main(String[] args) throws SSLException {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			//conn.setReadOnly(false);

			stmt = conn.createStatement();

			System.out.println("\t============");

			String sql = "SELECT * FROM flights";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int num = rs.getInt("num");
				String origin = rs.getString("origin");
				String destination = rs.getString("destination");
				int distance = rs.getInt("distance");
				int price = rs.getInt("price");

				System.out.format("Number %5s Origin %15s destinations %18s Distance %5d Price %5d\n", num, origin, destination, distance, price);
			}

			System.out.println("\t============");

			sql = "SELECT origin, destination, distance, num FROM flights";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String origin = rs.getString("origin");
				String destination = rs.getString("destination");
				int distance = rs.getInt("distance");

				System.out.print("From: " + origin);
				System.out.print(",\tTo: " + destination);
				System.out.println(",\t\tDistance: " + distance);
			}

			System.out.println("\t============");
			
			sql = "SELECT origin, destination FROM flights WHERE distance > ?";
			PreparedStatement prep_stmt = conn.prepareStatement(sql);
			prep_stmt.setInt(1, 200);
			rs = prep_stmt.executeQuery();
			while (rs.next()) {
				String origin = rs.getString("origin");
				System.out.println("From: " + origin);
			}
			
			//sql= "Update flights Set price = 2019 where num = 387 ";			
			//stmt.executeUpdate(sql);
			//////////// aaaaaaa
			Statement stm=conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			sql="SELECT * FROM flights WHERE num=387 ";
			rs= stm.executeQuery(sql);	     
		    rs.last();
			rs.updateFloat("price",(float) 2019);
			rs.updateRow();
			
			System.out.println("Flight num 387 new price is:"+ rs.getFloat("price"));
			
			
			//////////////// bbbbbbbbbbbb
			sql="SELECT * FROM flights WHERE distance> 1000";
			rs= stm.executeQuery(sql);
			while (rs.next()) {
				int price = rs.getInt("price");
				rs.updateInt("price", price+100);
				rs.updateRow();		
				System.out.println("The price of flight num:" + rs.getInt("num") + " is " + (rs.getFloat("price") ));

			}
			
			
			////////////////       cccccccccccccc
			sql="SELECT * FROM flights WHERE price <300";
			rs= stm.executeQuery(sql);
			while (rs.next()) {
				int price = rs.getInt("price");
				rs.updateInt("price", price-25);
				rs.updateRow();			
				System.out.println("The price of flight num:" + rs.getInt("num") + " is " + (rs.getFloat("price") ));

			}
			
			
			
			////////////////     dddddddddddddd
			//sql="UPDATE flights SET price=price+1000 WHERE distance>?";
			//PreparedStatement update=conn.prepareStatement(sql);4
			//update.setFloat(1,);
			//Float price=update.getF(1);
			
			
			
			////////ddddddddddd     cccccccccccccc1111111
			PreparedStatement update=conn.prepareStatement(sql);
			sql=" SELECT price, num FROM flights WHERE distance > 1000";
			rs=stm.executeQuery(sql);
			while(rs.next())
			{

				sql="UPDATE flights SET price=? WHERE num=?";
				 update = conn.prepareStatement(sql);
				update.setFloat(1,(rs.getFloat("price") +100));
				update.setInt(2, rs.getInt("num"));
				update.executeUpdate();
				System.out.println("The price of flight num:" + rs.getInt("num") + " is " +( rs.getFloat("price")+100));

			}
			
			sql=" SELECT price, num FROM flights WHERE price < 300 AND price>25";
			rs=stmt.executeQuery(sql);
			
			while(rs.next()) {

		     
		     
		     sql="UPDATE flights SET price=? WHERE num=?";
		  	PreparedStatement updatePrice = conn.prepareStatement(sql);
			updatePrice.setFloat(1,rs.getFloat("price")-25);
			updatePrice.setInt(2, rs.getInt("num"));
			updatePrice.executeUpdate();
			System.out.println("The price of flight num:" + rs.getInt("num") + " is " + (rs.getFloat("price")-25 ));


			}
			
			
			
			
			
			
			
			rs.close();
			stmt.close();
			conn.close();

		} catch (SQLException se) {
			se.printStackTrace();
			System.out.println("SQLException: " + se.getMessage());
            System.out.println("SQLState: " + se.getSQLState());
            System.out.println("VendorError: " + se.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se)  {
				se.printStackTrace();
			}
		}
	}

	private static statment C(int typeScrollSensitive, int concurUpdatable) {
		// TODO Auto-generated method stub
		return null;
	}
}
