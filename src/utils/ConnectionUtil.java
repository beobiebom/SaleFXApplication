package utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionUtil {
    Connection conn=null;
    public static Connection conDB(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
         Connection conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/appbanhang?serverTimezone=UTC#", "root", "");
         return conn;
        }catch (Exception ex){
            System.err.println(ex.getMessage());
            return null;
        }
    }
}
