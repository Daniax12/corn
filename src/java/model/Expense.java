/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import database.ConnectionMySql;
import database.ConnectionSqlServer;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rango
 */
public class Expense {
    private String id_expense;
    private String id_fertilizer;
    private java.sql.Date date_expense;
    private Integer fertilizer_pu;
    private Integer expense_qty;

    // GET ALL EXPENSES
    public static List<Expense> get_all_expenses() throws Exception{
        java.sql.Statement stm = null;
        ResultSet resultset = null;
        String query = "SELECT * FROM expense";
        Connection connection = null;
        
        ConnectionSqlServer cm = new ConnectionSqlServer();
        List<Expense> result = new ArrayList<>();
        try {
            connection = cm.dbConnect();
            stm = connection.createStatement();
            resultset = stm.executeQuery(query);
            
            while(resultset.next()){
                String my_id = resultset.getString("id_expense");
                java.sql.Date date = resultset.getDate("date_expense");
                String fertilizer_id = resultset.getString("id_fertilizer");
                int pu = resultset.getInt("fertilizer_pu");
                int qty = resultset.getInt("expense_qty"); 
                Expense expense = new Expense(my_id, fertilizer_id, date, pu, qty);
                result.add(expense);
            }
            return result;
        } catch (Exception e) {
            throw new Exception("Error on getting all expenses. Error : "+e.getMessage());
        } finally{
            resultset.close();
            stm.close();
            connection.close();
        }
    }
    
    public Expense(){}
    
    // CONSTRUCTORS
    public Expense(String id_expense, String id_fertilizer, Date date_expense, Integer fertilizer_pu, Integer expense_qty) {
        this.setId_expense(id_expense);
        this.setId_fertilizer(id_fertilizer);
        this.setDate_expense(date_expense);
        this.setFertilizer_pu(fertilizer_pu);
        this.setExpense_qty(expense_qty);
    }
    
    
    
    // GETTERS AND SETTERS

    public String getId_expense() {
        return id_expense;
    }

    public void setId_expense(String id_expense) {
        this.id_expense = id_expense;
    }

    public String getId_fertilizer() {
        return id_fertilizer;
    }

    public void setId_fertilizer(String id_fertilizer) {
        this.id_fertilizer = id_fertilizer;
    }

    public Date getDate_expense() {
        return date_expense;
    }

    public void setDate_expense(Date date_expense) {
        this.date_expense = date_expense;
    }

    public Integer getFertilizer_pu() {
        return fertilizer_pu;
    }

    public void setFertilizer_pu(Integer fertilizer_pu) {
        this.fertilizer_pu = fertilizer_pu;
    }

    public Integer getExpense_qty() {
        return expense_qty;
    }

    public void setExpense_qty(Integer expense_qty) {
        this.expense_qty = expense_qty;
    }
    
    
}
