/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import annoted.ColumnField;
import annoted.TableAnnotation;
import database.ConnectionBase;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import mapping.BddObject;

/**
 *
 * @author rango
 */
@TableAnnotation(nameTable = "harvest")
public class Harvest {
    @ColumnField(column = "id_harvest", primary_key = true)
    public String id_harvest;
    
    @ColumnField(column = "name_harvest")
    public String name_harvest;
    
    @ColumnField(column = "date_harvest")
    public java.sql.Date date_harvest;
    
    @ColumnField(column = "minimum_growth")
    public Integer minimum_growth;
    
    // RATIO OF ALL NPK_PRICE
    public List<Npk> npk_rank_qty_price() throws Exception{
        try {
            List<Npk> all = BddObject.find("fertilizer", new Npk(), null);
            HashMap<String, Float> temp = new LinkedHashMap<>();
            
            for(Npk npk : all){
                temp.put(npk.getId_npk(), npk.ratio_qty_price(this));
            }
            temp = Npk.sort_by_value(temp);
            List<Npk> result = new LinkedList<>();
            for(Map.Entry<String, Float> set: temp.entrySet()){
                Npk the_npk = new Npk();
                the_npk.setId_npk(set.getKey());
                result.add(BddObject.findById("fertilizer", the_npk, null));
            }
            
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on getting the rank of npk by its qty/price");
        }
    }
    
    // RANK ALL PARCEL BY RATIO CORNCOB / WEIGHT
    public List<Parcel> rank_parcel_ratio(Connection connection)throws Exception{ 
        boolean isOpen = false;
        ConnectionBase connectionBase = new ConnectionBase();
        if(connection == null){
            connection = connectionBase.dbConnect();     // If it is null, creating connection
        }else{
            isOpen = true;
        }
        try {
            if(this.is_harvest_done(connection) == false) return null;
            List<Parcel> parcels = BddObject.find("v_parcel_responsible", new Parcel(), connection);
            HashMap<String, Float> temp_result = new HashMap<>();
            
            for(Parcel parcel : parcels){
                temp_result.put(parcel.getId_parcel(), parcel.get_corncob_weight_ratio(connection));
            }
            temp_result = Npk.sort_by_value(temp_result);
            
            List<Parcel> result = new LinkedList<>();
             for(Map.Entry<String, Float> set: temp_result.entrySet()){
                Parcel temp = new Parcel();
                temp.setId_parcel(set.getKey());
                result.add(BddObject.findById("v_parcel_responsible", temp, connection));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw  new Exception("Error on geting parcel rank by ration corncob and weight. Error : "+e.getMessage());
        } finally{
            if(isOpen == false) connection.close();
        }
    }
    
    // GET THE RANK OF COMPOSITION
    public boolean is_harvest_done(Connection connection) throws Exception{
        boolean isOpen = false;
        ConnectionBase connectionBase = new ConnectionBase();
        if(connection == null){
            connection = connectionBase.dbConnect();     // If it is null, creating connection
        }else{
            isOpen = true;
        }
        try {
            List<Parcel> not_in_db = this.parcels_not_in_harvest(connection);
            if(not_in_db.size() == 0) return true;
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw  new Exception("Error on knowing if harvest is already done or not. Error : "+e.getMessage());
        } finally{
            if(isOpen == false) connection.close();
        }
    }
    
    // GET THE SUMMARY OF THE HARVEST
    public HashMap<String, Float> fertilizer_rank() throws Exception{
        try {
            Harvest_data hd = new Harvest_data();
            hd.setId_harvest(this.getId_harvest());
            
            List<Harvest_data> in_dab = BddObject.find("harvest_data", hd, null);
            if(in_dab.size() == 0) return null;
            List<Harvest_data> temporary = this.temporary_data(null);
            if(temporary != null){
                for(Harvest_data dh : temporary){
                    in_dab.add(dh);
                }
            }
            HashMap<String, Float> result = Npk.ratio_each_fertilizer(in_dab);      // TRIAGE
            HashMap<String, Float> main_result = Npk.sort_by_value(result);
  
            return main_result;
        } catch (Exception e) {
            throw new Exception("Error on calculing the ranking of the harvest. Error: "+e.getMessage());
        }
    }
    
    public List<Npk> npk_rank() throws Exception{
        try {
            
            HashMap<String, Float> temp_value = this.fertilizer_rank();
            List<Npk> list_result = new LinkedList<>();
            
            for(Map.Entry<String, Float> set: temp_value.entrySet()){
               Npk temp = new Npk();
               temp.setId_npk(set.getKey());
               temp = BddObject.findById("fertilizer", temp, null);
               list_result.add(temp);
            }
            return list_result;
        } catch (Exception e) {
            throw new Exception("Error on calculing the ranking of the harvest. Error: "+e.getMessage());
        }
    }
    
    // GET THE FORECAST
    public float get_forecast(Connection connection) throws Exception{
        boolean isOpen = false;
        ConnectionBase connectionBase = new ConnectionBase();
        if(connection == null){
            connection = connectionBase.dbConnect();     // If it is null, creating connection
        }else{
            isOpen = true;
        }
        try {
            float already_get = this.get_sum_harvest(connection);
            List<Harvest_data> temporary = this.temporary_data(connection);
            for(Harvest_data hv : temporary){
                already_get += hv.getWeight_received();
            }
            
            return already_get;
        } catch (Exception e) {
            e.printStackTrace();
            throw  new Exception("Error on getting the forecast of harvest. Error : "+e.getMessage());
        } finally{
            if(isOpen == false) connection.close();
        }
    }
    
    // Get all previsions of other parcels
    public List<Harvest_data> temporary_data(Connection connection) throws Exception{
        boolean isOpen = false;
        ConnectionBase connectionBase = new ConnectionBase();
        if(connection == null){
            connection = connectionBase.dbConnect();     // If it is null, creating connection
        }else{
            isOpen = true;
        }
        try {
           List<Harvest_data> result = new ArrayList<>();
           
            Harvest_data hd = new Harvest_data();
            hd.setId_harvest(this.getId_harvest());
            
            float already_get = this.get_sum_harvest(connection);
            List<Harvest_data> data = BddObject.find("harvest_data", hd, connection);
            if(data.size() == 0) return result;
            List<Parcel> not_in_database = this.parcels_not_in_harvest(connection);
            if(not_in_database.size() == 0) return result;

            float proportionality = Harvest_data.get_proportionality(data, connection);
            for(Parcel parcel : not_in_database){
                 int excpected = parcel.corncob_expected(connection);
                 Check_parcel cp = parcel.get_last_check(connection);

                 float per_corn = proportionality * cp.getGrowth();
                 
                 Harvest_data to_insert = new Harvest_data();
                 to_insert.setCorncob_received(excpected);
                 to_insert.setId_parcel(parcel.getId_parcel());
                 to_insert.setWeight_received(per_corn * excpected);
                 
                 result.add(to_insert);
             }
           return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw  new Exception("Error on getting all other parcels forecast of harvest. Error : "+e.getMessage());
        } finally{
            if(isOpen == false) connection.close();
        }
    }
    
    // GET TOTAL OF HARVEST WEIGHT RECEIVED
    public float get_sum_harvest(Connection connection) throws Exception{
        boolean isOpen = false;
        ConnectionBase connectionBase = new ConnectionBase();
        if(connection == null){
            connection = connectionBase.dbConnect();     // If it is null, creating connection
        }else{
            isOpen = true;
        }
        try {
            Harvest_data hd = new Harvest_data();
            hd.setId_harvest(this.getId_harvest());
            
            List<Harvest_data> data = BddObject.find("harvest_data", hd, connection);
            if(data.size() == 0) return 0;
            float result = 0;
            for(Harvest_data hda : data) result += hda.getWeight_received();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw  new Exception("Error on getting the total weight of harvest_data got. Error : "+e.getMessage());
        } finally{
            if(isOpen == false) connection.close();
        }
    }

    // GET PARCEL THAT HAS NOT YET HARVEST_DATA
    public List<Parcel> parcels_not_in_harvest(Connection connection) throws Exception{
        boolean isOpen = false;
        ConnectionBase connectionBase = new ConnectionBase();
        if(connection == null){
            connection = connectionBase.dbConnect();     // If it is null, creating connection
        }else{
            isOpen = true;
        }
        try {
            Harvest_data hd = new Harvest_data();
            hd.setId_harvest(this.getId_harvest());
            List<Harvest_data> data = BddObject.find("harvest_data", hd, connection);
            
            List<Parcel> all_parcels = BddObject.find("v_parcel_responsible", new Parcel(), connection);
            
            for(Harvest_data hda : data){
                all_parcels.removeIf(parcel -> parcel.getId_parcel().equals(hda.getId_parcel()));
            }
            return all_parcels;
        } catch (Exception e) {
            e.printStackTrace();
            throw  new Exception("Error on getting parcels that have not yet giving its data. Error : "+e.getMessage());
        } finally{
            if(isOpen == false) connection.close();
        }
    }
    
    // Getters and setters
    public String getId_harvest() {
        return id_harvest;
    }

    public void setId_harvest(String id_harvest) {
        this.id_harvest = id_harvest;
    }

    public String getName_harvest() {
        return name_harvest;
    }

    public void setName_harvest(String name_harvest) {
        this.name_harvest = name_harvest;
    }

    public Date getDate_harvest() {
        return date_harvest;
    }

    public void setDate_harvest(Date date_harvest) {
        this.date_harvest = date_harvest;
    }

    public Integer getMinimum_growth() {
        return minimum_growth;
    }

    public void setMinimum_growth(Integer minimum_growth) {
        this.minimum_growth = minimum_growth;
    }
    
    
    
}
