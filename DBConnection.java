/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LoginPage;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Aida Sophea
 */
public class DBConnection {

    public static Connection getConnection(){
        Connection conn = null;
        try {
            //register jdbc driver
            Class.forName("com.mysql.jdbc.Driver");
            //open a connection
            conn = DriverManager.getConnection("jdbc:mysql://localhost/java_login_register", "root", "");
                    
        }catch(Exception ex){
            System.out.println("There were errors while connectiong to db.");
        }
        return conn;
    }
}