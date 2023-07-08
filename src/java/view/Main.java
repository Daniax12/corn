/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import database.ConnectionBase;
import database.ConnectionMySql;
import database.ConnectionSqlServer;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mapping.BddObject;
import model.Fertilizing;
import model.Harvest;
import model.Harvest_data;
import model.Npk;
import model.Parcel;

/**
 *
 * @author rango
 */
public class Main {
    public static void main(String[] args) throws Exception{
        ConnectionBase cm = new ConnectionBase();
        Connection connection = null;
        try {
            connection = cm.dbConnect();
            System.out.println(connection);
            
            Parcel parcel = new Parcel();
            parcel.setId_parcel("PAR_2");
            parcel = BddObject.findById("v_parcel_responsible", parcel, connection);
            
            Harvest_data hd = new Harvest_data();
            hd.setId_harvest_data("HV_2");
            hd = BddObject.findById("harvest_data", hd, connection);
            
            HashMap<String, Float> test = hd.each_npk_production();
            
            
            for(Map.Entry<String, Float> set: test.entrySet()){
                System.out.println(set.getKey() + " -> " + set.getValue());
            }
            
//            Npk npk_a = new Npk();
//            npk_a.setId_npk("NPK_A");
//            npk_a = BddObject.findById("fertilizer", npk_a, connection);
//            
//            System.out.println("Prod is "+npk_a.my_average_price());
             
             
             
           
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(connection != null) connection.close();
        }
    }
}
