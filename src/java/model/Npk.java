/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import annoted.ColumnField;
import annoted.TableAnnotation;
import database.ConnectionBase;
import java.sql.Connection;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
@TableAnnotation(nameTable = "fertilizer")
public class Npk {
    @ColumnField(column = "id_fertilizer", primary_key = true)
    private String id_npk;
    
    @ColumnField(column = "name_fertilizer")
    private String name_npk;
    
    // GET MY RATIO BY PRICE
    public float ratio_qty_price(Harvest harvest) throws Exception{
        try {
            float my_production = this.my_production_on_harvest(harvest, null);
            
            return my_production / this.my_total_expense() * 100;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on gettinbg the ratio of npk in harvest. Error: "+e.getMessage());
        }
    }
    
    public  float my_total_expense() throws Exception{
        return this.my_qty_bought() * this.my_average_price();
    }
    
    // GET TOTAL QTY BOUGHT
    public float my_qty_bought() throws Exception{
         try {
            List<Expense> expenses = Expense.get_all_expenses();
            int temp_result = 0;
            for(Expense ex : expenses){
                temp_result += ex.getExpense_qty();
            }
            return temp_result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on getting the average price of the npk");
        }
    }
    
    // GET AVERAGE PRICE
    public float my_average_price() throws Exception{
        try {
            List<Expense> expenses = Expense.get_all_expenses();
            int count = 0;
            int temp_result = 0;
            for(Expense ex : expenses){
                if(this.getId_npk().equals(ex.getId_fertilizer()) == true){
                    temp_result += ex.getFertilizer_pu();
                    count += 1;
                }
            }
            return (float) temp_result / count;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on getting the average price of the npk");
        }
    }
    
    
    // GET THE PRODUCTION OF A SPECIFIC FERTILIZXER
    public float my_production_on_harvest(Harvest harvest, Connection connection) throws Exception{
        if(this.getId_npk() == null) return 0.0f;
         boolean isOpen = false;
        ConnectionBase connectionBase = new ConnectionBase();
        if(connection == null){
            connection = connectionBase.dbConnect();     // If it is null, creating connection
        }else{
            isOpen = true;
        }
        try {
            Harvest_data hd = new Harvest_data();
            hd.setId_harvest(harvest.getId_harvest());
            
            List<Harvest_data> in_dab = BddObject.find("harvest_data", hd, null);
            if(in_dab.size() == 0) return 0.0f;
            List<Harvest_data> temporary = harvest.temporary_data(null);
            if(temporary != null){
                for(Harvest_data dh : temporary){
                    in_dab.add(dh);
                }
            }
            HashMap<String, Float> all_repartition = Harvest_data.each_npk_production(in_dab);
            return all_repartition.get(this.getId_npk());
        } catch (Exception e) {
            e.printStackTrace();
            throw  new Exception("Error on getting the production of a fertilizer in an harvest. Error : "+e.getMessage());
        } finally{
            if(isOpen == false) connection.close();
        }
    
    }
    
     // GET RATIO OF EACH FERTILIZER IN A LIST OF HARVEST_DATA
    public static HashMap<String, Float> ratio_each_fertilizer(List<Harvest_data> data) throws Exception{
        try {
            HashMap<String, Float> total_production = Harvest_data.each_npk_production(data);
            float total_weight = Harvest_data.total_weight(data);
            for(Map.Entry<String, Float> set: total_production.entrySet()){
                float ratio = total_weight / set.getValue();
               // System.out.println(repartition);
                total_production.replace(set.getKey(), ratio);
            }
            return total_production;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on getting the ratio of each fertilizer in a list of hd");
        }
    }
    
    //
    public static HashMap<String, Float> sort_by_value(HashMap<String, Float> hashMap) {
        // Convert hashmap entries to a list
        // Convert hashmap entries to a list
        List<Map.Entry<String, Float>> list = new LinkedList<>(hashMap.entrySet());

        // Sort the list based on float values in ascending order
        Collections.sort(list, new Comparator<Map.Entry<String, Float>>() {
            @Override
            public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        // Create a new linked hashmap to store the sorted entries
        HashMap<String, Float> sortedHashMap = new LinkedHashMap<>();

        // Iterate over the sorted list and put entries into the sorted hashmap
        for (Map.Entry<String, Float> entry : list) {
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }

        return sortedHashMap;
    }

    
    // GETTERS AND SETTERS
    public String getId_npk() {
        return id_npk;
    }

    public void setId_npk(String id_npk) {
        this.id_npk = id_npk;
    }

    public String getName_npk() {
        return name_npk;
    }

    public void setName_npk(String name_npk) {
        this.name_npk = name_npk;
    }
    
}
