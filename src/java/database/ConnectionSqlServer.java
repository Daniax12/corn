/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author rango
 */
public class ConnectionSqlServer {
    private static final String user = "corn";
    private static final String mdp = "corn";
    
     public Connection dbConnect() throws Exception{
        Connection temp = null;
        try {      
           
            Class.forName("oracle.jdbc.driver.OracleDriver");
            temp = DriverManager.getConnection("jdbc:oracle:thin:@ETU1757-DANIA:1521/XE", user, mdp);
            temp.setAutoCommit(false);
            // System.out.println(temp);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Connection failed");
        }
        return temp;
    }
}
