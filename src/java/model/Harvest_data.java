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
@TableAnnotation(nameTable = "harvest_data", sequence = "harvest_data_seq", prefix = "HD_")
public class Harvest_data {
    @ColumnField(column = "id_harvest_data", primary_key = true, is_increment = true)
    private String id_harvest_data;
    
    @ColumnField(column = "id_harvest")
    private String id_harvest;
     
    @ColumnField(column = "id_parcel")
    private String id_parcel;
      
    @ColumnField(column = "corncob_received")
    private Integer corncob_received;
       
    @ColumnField(column = "weight_received")
    private Float weight_received;
    
    // GET THE TOTAL PRODUCTION OF EACH NPK IN SOME HARVEST_DATA
    public static HashMap<String, Float> each_npk_production(List<Harvest_data> data) throws Exception{
        try {
            if(data.size() == 0) return null;
            List<Npk> npks = BddObject.find("fertilizer", new Npk(), null);
            HashMap<String, Float> result = new HashMap<>();
            
            for(Npk npk : npks){
                result.put(npk.getId_npk(), 0.0f);
            }
            
            for(Harvest_data hd : data){
                HashMap<String, Float> temp = hd.each_npk_production();
                for(Map.Entry<String, Float> set: temp.entrySet()){
                    float old_value = result.get(set.getKey());
                    float new_value = old_value + set.getValue();
                    result.replace(set.getKey(), new_value);
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on getting the total production of each fertilizer in all harvest_data");
        }
    }
    
    
    // GET THE REPARTITION PRODUCTION OF EACH FERTILIIZER BASED ON WEIGHT ODF HARVEST DATA
    public HashMap<String, Float> each_npk_production() throws Exception{
        try {
            Parcel my_parcel = this.hd_parcel(null);
            HashMap<String, Float> npk_percentage = my_parcel.my_npk_percentage();
             for(Map.Entry<String, Float> set: npk_percentage.entrySet()){
                float percentage = set.getValue();
                float new_value = percentage * this.getWeight_received() / 100;
                npk_percentage.replace(set.getKey(), new_value);
                 
            }
            return npk_percentage;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on getting the npk_production of an harvest data. Error: "+e.getMessage());
        }
    }
    
    // GET THE PROPORTIONALITY OF A LIST OF HARVEST DATA
    public static float get_proportionality(List<Harvest_data> data, Connection connection) throws Exception{
        boolean isOpen = false;
        ConnectionBase connectionBase = new ConnectionBase();
        if(connection == null){
            connection = connectionBase.dbConnect();     // If it is null, creating connection
        }else{
            isOpen = true;
        }
        try {
            float result = 0;
            for(Harvest_data hd : data){
                result += hd.get_proportionality(connection);
            }
            return result / data.size();
        } catch (Exception e) {
            e.printStackTrace();
            throw  new Exception("Error on getting the proportionality of a list of harvest_data. Error : "+e.getMessage());
        } finally{
            if(isOpen == false) connection.close();
        }
    }
    
    
    // GET THE PROPORTIONALITY OF AN HARDEST_DATA, GETTING THE LAST GROWTH OF THE PARCEL, GETTING THE WEIGHT PER CORN
    public float get_proportionality(Connection connection) throws Exception{
        boolean isOpen = false;
        ConnectionBase connectionBase = new ConnectionBase();
        if(connection == null){
            connection = connectionBase.dbConnect();     // If it is null, creating connection
        }else{
            isOpen = true;
        }
        try {
            Parcel my_parcel = this.hd_parcel(connection);
            Check_parcel last_check = my_parcel.get_last_check(connection);
            float weight_per_corncob = this.get_weight_per_corncob();
          //  System.out.println("Weight per corn "+weight_per_corncob);
            return  weight_per_corncob / last_check.getGrowth();
        } catch (Exception e) {
            e.printStackTrace();
            throw  new Exception("Error on getting the proportionality of the harvest_data. Error : "+e.getMessage());
        } finally{
            if(isOpen == false) connection.close();
        }
    }

    // GET THE AVERAGE WEIGHT OF AN HARDEST DATA KNOWING THE CORNCOB RECEIVED AND THE WEIGHT RECEIVED
    public float get_weight_per_corncob(){
        return this.getWeight_received() / this.getCorncob_received();
    }
    
   public static float total_weight(List<Harvest_data> data){
       float result = 0;
       
       for(Harvest_data hd : data){
           result += hd.getWeight_received();
       }
       return result;
   }
    
    // NOTICE OF HARDEST_DATA
    public String get_harvest_data_notice(Connection connection) throws Exception{
        boolean isOpen = false;
        ConnectionBase connectionBase = new ConnectionBase();
        if(connection == null){
            connection = connectionBase.dbConnect();     // If it is null, creating connection
        }else{
            isOpen = true;
        }
        try {
            Parcel my_parcel = this.hd_parcel(connection);
            Integer expected = my_parcel.corncob_expected(connection);
            if(expected > this.getCorncob_received()){
                Integer diff = expected - this.getCorncob_received();
                return "Lack of at least "+ diff + " corncobs";
            }
            return "-";
        } catch (Exception e) {
            e.printStackTrace();
            throw  new Exception("Error on getting the notice for the harvest_data. Error : "+e.getMessage());
        } finally{
            if(isOpen == false) connection.close();
        }
    }
    // Constructors
    public Harvest_data(){}

    public Harvest_data(String id_harvest, String id_parcel, String corncob_received, String weight_received) throws Exception{
        try {
            this.setId_harvest(id_harvest);
            this.setId_parcel(id_parcel);
            this.setCorncob_received(corncob_received);
            this.setWeight_received(weight_received);
        } catch (Exception e) {
            throw new Exception("Error on constructing the harvest_data. Error: "+e.getMessage());
        }
    }
    
    
    
    // Getters and setters
    public String getId_harvest_data() {
        return id_harvest_data;
    }

    public void setId_harvest_data(String id_harvest_data) {
        this.id_harvest_data = id_harvest_data;
    }

    public String getId_harvest() {
        return id_harvest;
    }

    public void setId_harvest(String id_harvest) {
        this.id_harvest = id_harvest;
    }

    public String getId_parcel() {
        return id_parcel;
    }

    public void setId_parcel(String id_parcel) {
        this.id_parcel = id_parcel;
    }

    public Integer getCorncob_received() {
        return corncob_received;
    }

    public void setCorncob_received(Integer corncob_received) {
        this.corncob_received = corncob_received;
    }
    
    public void setCorncob_received(String corncob_received_html) throws Exception{
        try {
            Integer value = Integer.valueOf(corncob_received_html);
            this.setCorncob_received(value);
        } catch (Exception e) {
            throw new Exception("Error on setting the corncob received of Harvest data. Error : "+e.getMessage());
        }
    }

    public Float getWeight_received() {
        return weight_received;
    }

    public void setWeight_received(Float weight_received) {
        this.weight_received = weight_received;
    }
    
    public void setWeight_received(String weight_received_html) throws Exception{
        try {
            Float value = Float.valueOf(weight_received_html);
            this.setWeight_received(value);
        } catch (Exception e) {
            throw new Exception("Error on setting the weight received of Harvest data. Error : "+e.getMessage());
        }
    }
    
    public Parcel hd_parcel(Connection connection) throws Exception{
        if(this.getId_parcel() == null) return null;
        boolean isOpen = false;
        ConnectionBase connectionBase = new ConnectionBase();
        if(connection == null){
            connection = connectionBase.dbConnect();     // If it is null, creating connection
        }else{
            isOpen = true;
        }
        try {
            Parcel parcel = new Parcel();
            parcel.setId_parcel(this.getId_parcel());
            
            parcel = BddObject.findById("v_parcel_responsible", parcel, connection);
            return parcel;
        } catch (Exception e) {
            e.printStackTrace();
            throw  new Exception("Error on getting the parcel of the harvest data. Error : "+e.getMessage());
        } finally{
            if(isOpen == false) connection.close();
        }
    }
    
    public String status_prevision(){
        if(this.getId_harvest_data() == null) return "Forecast";
        else return "Got";
    }
    
}
