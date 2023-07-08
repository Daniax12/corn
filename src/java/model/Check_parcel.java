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
import java.util.List;
import mapping.BddObject;
import utilities.DatePattern;
import utilities.DateUtil;
import utilities.Ordering;

/**
 *
 * @author rango
 */
@TableAnnotation(nameTable = "check_parcel", sequence = "check_seq", prefix = "CH_")
public class Check_parcel {
    @ColumnField(column = "id_check_parcel", primary_key = true, is_increment = true)
    private String id_check_parcel;
    
    @ColumnField(column = "id_parcel")
    private String id_parcel;
    
    @ColumnField(column = "date_check")
    private java.sql.Date date_check;
    
    @ColumnField(column = "feet_amount")
    private Integer feet_amount;
    
    @ColumnField(column = "average_corncob")
    private Integer average_corncob;
    
    @ColumnField(column = "color")
    private Integer color;
    
    @ColumnField(column = "growth")
    private Float growth;
    
    // Get the anomalies of a current checking -> A REFAIRE
    public HashMap<String, String[]> get_anomalies(Connection connection) throws Exception{
        boolean isOpen = false;
        ConnectionBase connectionBase = new ConnectionBase();
        if(connection == null){
            connection = connectionBase.dbConnect();     // If it is null, creating connection
        }else{
            isOpen = true;
        }
        try {
            HashMap<String, String[]> result = new HashMap<>();
           Check_parcel previous = this.get_previous_check(connection);
           
           if(previous == null){
                String[] temp = {"-", "-"};
                result.put("corncob", temp);
                result.put("color", temp);
                result.put("growth", temp);
                result.put("feet", temp);
                result.put("date", new String[]{"-"});
           } else {
               result.put("date", new String[]{previous.getDate_check().toString()});
               if(this.getFeet_amount() < previous.getFeet_amount()) result.put("feet", new String[]{previous.getFeet_amount().toString(), "Lack of corn feet"});
               else result.put("feet", new String[]{previous.getFeet_amount().toString(), "-"});
               
               if(this.getAverage_corncob() < previous.getAverage_corncob()) result.put("corncob", new String[]{previous.getAverage_corncob().toString(), "Lack of corncob"});
               else result.put("corncob", new String[]{previous.getAverage_corncob().toString(), "-"});
               
               if(this.getColor() < previous.getColor()) result.put("color", new String[]{previous.getColor().toString(), "Decreasing color"});
               else result.put("color", new String[]{previous.getColor().toString(), "-"});
               
               if(this.getGrowth() < 15 && this.getGrowth().equals(previous.getGrowth()) == true) result.put("growth", new String[]{previous.getGrowth().toString(), "Growth static"});
               else result.put("growth", new String[]{previous.getGrowth().toString(), "-"});
           }
           return result;
           
        } catch (Exception e) {
            e.printStackTrace();
            throw  new Exception("Error on getting the anomalies of the current parcel. Error : "+e.getMessage());
        } finally{
            if(isOpen == false) connection.close();
        }
    }

    // Get the previous check before THIS
    public Check_parcel get_previous_check(Connection connection) throws Exception{
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
         
            
            for(Check_parcel cp : all){
                if(cp.getDate_check().before(this.getDate_check()) == true){
                    return cp;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw  new Exception("Error on getting the last check of the parcel. Error : "+e.getMessage());
        } finally{
            if(isOpen == false) connection.close();
        }
    }
    
    // Constructors
    public Check_parcel(String id_parcel, String date_check, String feet_amount, String average_corncob, String color, String growth) throws Exception{
        try {
            this.setId_parcel(id_parcel);
            this.setDate_check(date_check);
            this.setFeet_amount(feet_amount);
            this.setAverage_corncob(average_corncob);
            this.setColor(color);
            this.setGrowth(growth);
        } catch (Exception e) {
            throw new Exception("Error on construction the check_panel. Error : "+e.getMessage());
        }   
    }
    
    public Check_parcel(){}
    
    
    // Getters and setters
    public String getId_check_parcel() {
        return id_check_parcel;
    }

    public void setId_check_parcel(String id_check_parcel) {
        this.id_check_parcel = id_check_parcel;
    }

    public String getId_parcel() {
        return id_parcel;
    }

    public void setId_parcel(String id_parcel) {
        this.id_parcel = id_parcel;
    }

    public Date getDate_check() {
        return date_check;
    }

    public void setDate_check(Date date_check) {
        this.date_check = date_check;
    }
    
    public void setDate_check(String dateHtml) throws Exception{
        try {
            java.sql.Date the_date = DateUtil.stringToSqlDate(java.sql.Date.class, dateHtml, DatePattern.YYYY_MM_DD);
            this.setDate_check(the_date);
        } catch (Exception e) {
            throw new Exception("Error on casting the date of checking");
        }
    }

    public Integer getFeet_amount() {
        return feet_amount;
    }

    public void setFeet_amount(Integer feet_amount) {
        this.feet_amount = feet_amount;
    }
    
    
    public void setFeet_amount(String feet_html) throws Exception{
        try {
            Integer value = Integer.valueOf(feet_html);
            this.setFeet_amount(value);
        } catch (Exception e) {
            throw new Exception("Error on setting the amount of feet");
        }
    }

    public Integer getAverage_corncob() {
        return average_corncob;
    }

    public void setAverage_corncob(Integer average_corncob) {
        this.average_corncob = average_corncob;
    }
    
    public void setAverage_corncob(String corncob_html) throws  Exception{
        try {
            Integer value = Integer.valueOf(corncob_html);
            this.setAverage_corncob(value);
        } catch (Exception e) {
            throw new Exception("Error on setting the amount of corncob");
        }
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }
    
    public void setColor(String color_html) throws Exception{
        try {
            Integer value = Integer.valueOf(color_html);
            this.setColor(value);
        } catch (Exception e) {
            throw new Exception("Error on setting the color of checking");
        }
    }

    public Float getGrowth() {
        return growth;
    }

    public void setGrowth(Float growth) {
        this.growth = growth;
    }
    
    public void setGrowth(String growth_html) throws Exception{
        try {
            Float value = Float.valueOf(growth_html);
            this.setGrowth(value);
        } catch (Exception e) {
            throw new Exception("Error on setting the growth of checking");
        }
    }
}
