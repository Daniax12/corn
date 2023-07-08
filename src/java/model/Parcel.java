/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import annoted.ColumnField;
import annoted.TableAnnotation;
import database.ConnectionBase;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mapping.BddObject;
import utilities.Ordering;

/**
 *
 * @author rango
 */
@TableAnnotation(nameTable = "parcel")
public class Parcel {
    @ColumnField(column = "id_parcel", primary_key = true)
    private String id_parcel;
    
    @ColumnField(column = "name_parcel")
    private String name_parcel;
    
    @ColumnField(column = "ares")
    private Float ares;
    
    @ColumnField(column = "id_responsible")
    private String id_responsible;
    
    @ColumnField(column = "name_responsible")
    private String name_responsible;

    // Get the ration of parcel
    public float get_corncob_weight_ratio(Connection connection) throws Exception{
        if(this.getId_parcel() == null) return 0.0f;
        boolean isOpen = false;
        ConnectionBase connectionBase = new ConnectionBase();
        if(connection == null){
            connection = connectionBase.dbConnect();     // If it is null, creating connection
        }else{
            isOpen = true;
        }
        try {
            Harvest_data hd = new Harvest_data();
            hd.setId_parcel(this.getId_parcel());
            List<Harvest_data> hds = BddObject.find("harvest_data", hd, connection);
            
            if(hds.size() == 0) return 0.0f;
            return hds.get(0).getCorncob_received() / hds.get(0).getWeight_received();          
        } catch (Exception e) {
            e.printStackTrace();
            throw  new Exception("Error on geting parcel ratio by corncob and weight. Error : "+e.getMessage());
        } finally{
            if(isOpen == false) connection.close();
        }
    }
    
    // GET THE REPARTITION % OF ALL NPKS IN PARCEL
    public HashMap<String, Float> my_npk_percentage() throws Exception{
        try {
            HashMap<String, Float> npk_repartition = this.my_npk_amount();
            float total_npk = this.my_npk_used();
            for(Map.Entry<String, Float> set: npk_repartition.entrySet()){
                float temp = set.getValue();
                if(total_npk == 0){
                
                } else {
                    float repartition = temp/total_npk * 100;
                    npk_repartition.replace(set.getKey(), repartition);
                }
                
            }
            return npk_repartition;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error of repartition of the npks in %. Error: "+e.getMessage());
        }
    }
    
    // GET THE AMOUNT TOTAL OF MIXED NPK
    public float my_npk_used()throws Exception{
        float result = 0;
        try {
            HashMap<String, Float> npk_repartition = this.my_npk_amount();
            for(Map.Entry<String, Float> set: npk_repartition.entrySet()){
                result += set.getValue();
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on getting the amount total of npk used. Error: "+e.getLocalizedMessage());
        }
    }
    
    // GET THE AMOUNT OF EACH FERTILIZER (KG) IN A SPECIFIC PARCEL
    public HashMap<String, Float> my_npk_amount() throws Exception{
        try {
            List<Npk> npks = BddObject.find("fertilizer", new Npk(), null);
           // System.out.println("");
            List<Fertilizing> fertilizing = Fertilizing.get_all_fertilizing();
            //System.out.println("hehe "+fertilizing.size());
            HashMap<String, Float> result = new HashMap<>();
            
            for(Npk npk : npks){
                result.put(npk.getId_npk(), 0.0f);
            }
            for(Fertilizing f : fertilizing){
                if(f.getId_parcel().equals(this.getId_parcel()) == true){
                    Float temp = result.get(f.getId_fertilizer());
                    result.replace(f.getId_fertilizer(), temp + f.getWeight_fertilizer());
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on getting the amoung of parcel npks (kg). Error: "+e.getLocalizedMessage());
        }
    }
        
     
    // GET THE AMOUNT OF FORECAST CORNCOB
    public int corncob_expected(Connection connection) throws Exception{
        boolean isOpen = false;
        ConnectionBase connectionBase = new ConnectionBase();
        if(connection == null){
            connection = connectionBase.dbConnect();     // If it is null, creating connection
        }else{
            isOpen = true;
        }
        try {
            Check_parcel last = this.get_last_check(connection);
            int result = last.getAverage_corncob() * last.getFeet_amount();
            
           // System.out.println("THis " + this.getName_parcel() +" should have "+ result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw  new Exception("Error on getting the expected corncob amount. Error : "+e.getMessage());
        } finally{
            if(isOpen == false) connection.close();
        }
    }
    
    
    // Get last check of the parcel
    public Check_parcel get_last_check(Connection connection) throws Exception{
        boolean isOpen = false;
        ConnectionBase connectionBase = new ConnectionBase();
        if(connection == null){
            connection = connectionBase.dbConnect();     // If it is null, creating connection
        }else{
            isOpen = true;
        }
        try {
            Check_parcel temp = new Check_parcel();
            temp.setId_parcel(this.getId_parcel());
            List<Check_parcel> all = BddObject.findByOrder("check_parcel", temp, "date_check", Ordering.DESC, connection);
            
            if(all.size() > 0) return all.get(0);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw  new Exception("Error on getting the last check of the parcel. Error : "+e.getMessage());
        } finally{
            if(isOpen == false) connection.close();
        }
    }
    
    // Getters and setters
    public String getId_parcel() {
        return id_parcel;
    }

    public void setId_parcel(String id_parcel) {
        this.id_parcel = id_parcel;
    }

    public String getName_parcel() {
        return name_parcel;
    }

    public void setName_parcel(String name_parcel) {
        
        this.name_parcel = name_parcel;
    }

    public Float getAres() {
        return ares;
    }

    public void setAres(Float ares) {
        this.ares = ares;
    }

    public String getId_responsible() {
        return id_responsible;
    }

    public void setId_responsible(String id_responsible) {
        this.id_responsible = id_responsible;
    }

    public String getName_responsible() {
        return name_responsible;
    }

    public void setName_responsible(String name_responsible) {
        this.name_responsible = name_responsible;
    }
    
    
}
