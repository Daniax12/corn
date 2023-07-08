/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import database.ConnectionMySql;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rango
 */
public class Fertilizing {
    private String id_fertilizing;
    private java.sql.Date date_fertilizing;
    private String id_fertilizer;
    private String id_parcel;
    private Float weight_fertilizer;
    
    
    // GET ALL FERTILIZING IN DB
    public static List<Fertilizing> get_all_fertilizing() throws Exception{
        java.sql.Statement stm = null;
        ResultSet resultset = null;
        String query = "SELECT * FROM fertilizing";
        Connection connection = null;
        
        ConnectionMySql cm = new ConnectionMySql();
        List<Fertilizing> result = new ArrayList<>();
        try {
            connection = cm.dbConnect();
            stm = connection.createStatement();
            resultset = stm.executeQuery(query);
            
            while(resultset.next()){
                String my_id = resultset.getString("id_fertilizing");
                java.sql.Date date = resultset.getDate("date_fertilizing");
                String fertilizer_id = resultset.getString("id_fertilizer");
                String parcel_id = resultset.getString("id_parcel");
                Float weight = resultset.getFloat("qty_fertilizer");
                Fertilizing fertilizing = new Fertilizing(my_id, date, fertilizer_id, parcel_id, weight);
                result.add(fertilizing);
            }
            return result;
        } catch (Exception e) {
            throw new Exception("Error on getting all fertilizing. Error : "+e.getMessage());
        } finally{
            resultset.close();
            stm.close();
            connection.close();
        }
    }

    
    // CONSTRUCTORS
    public Fertilizing(String id_fertilizing, Date date_fertilizing, String id_fertilizer, String id_parcel, Float weight_fertilizer) {
        this.id_fertilizing = id_fertilizing;
        this.date_fertilizing = date_fertilizing;
        this.id_fertilizer = id_fertilizer;
        this.id_parcel = id_parcel;
        this.weight_fertilizer = weight_fertilizer;
    }
    
    public Fertilizing(){}
    
    // GETTERS AND SETTERS
    public String getId_fertilizing() {
        return id_fertilizing;
    }

    public void setId_fertilizing(String id_fertilizing) {
        this.id_fertilizing = id_fertilizing;
    }

    public Date getDate_fertilizing() {
        return date_fertilizing;
    }

    public void setDate_fertilizing(Date date_fertilizing) {
        this.date_fertilizing = date_fertilizing;
    }

    public String getId_fertilizer() {
        return id_fertilizer;
    }

    public void setId_fertilizer(String id_fertilizer) {
        this.id_fertilizer = id_fertilizer;
    }

    public String getId_parcel() {
        return id_parcel;
    }

    public void setId_parcel(String id_parcel) {
        this.id_parcel = id_parcel;
    }

    public Float getWeight_fertilizer() {
        return weight_fertilizer;
    }

    public void setWeight_fertilizer(Float weight_fertilizer) {
        this.weight_fertilizer = weight_fertilizer;
    }
    
    
    
}
