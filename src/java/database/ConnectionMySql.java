/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import mapping.SGBD;

/**
 *
 * @author rango
 */
public class ConnectionMySql {
   
    private static final String nameDatabase = "corn";
    private static final String user = "root";
    private static final String mdp = "root";
    
     public Connection dbConnect() throws Exception{
        Connection temp = null;

        try {      
           
            Class.forName("com.mysql.cj.jdbc.Driver");
            temp = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + nameDatabase, user, mdp);
                
            temp.setAutoCommit(false);
            // System.out.println(temp);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Connection failed");
        }
        return temp;
    }
}
