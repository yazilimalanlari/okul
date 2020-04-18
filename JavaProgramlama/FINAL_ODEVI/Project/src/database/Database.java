package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Database{
    String URL = "jdbc:mysql://localhost?characterEncoding=UTF-8";
    
    Connection conn = null;
    Statement stmt = null;
    
    String dbName, sql, tableName, whereStatement = "";
    JSONObject tables = new JSONObject();

    public Database(String DB_NAME, String DB_USER, String DB_PASS){
        dbName = DB_NAME;
        try{
            conn = DriverManager.getConnection(URL, DB_USER, DB_PASS);
            stmt = conn.createStatement();
            
            sql = String.format("SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '%s'", DB_NAME);
            
            if(!stmt.executeQuery(sql).next()){
                sql = String.format("CREATE DATABASE %s CHARACTER SET utf8 COLLATE utf8_turkish_ci", DB_NAME);
                if(stmt.executeUpdate(sql) == 1)
                    System.out.println("Veritabanı oluşturuldu.");
                else
                    System.err.println("Veritabanı oluşturulamadı!");
            }
            
            // USE DATABASE
            stmt.executeUpdate(String.format("USE %s", DB_NAME));
            
            sql = "";
        }catch(SQLException e){
            e.printStackTrace();
//            if(e.getErrorCode() == 1049){
//                System.err.println("Veritabanı bulunamadı.");
//            }
        }
    }
    
    public void model(String... args) throws JSONException{
        tables.put(tableName, new ArrayList<String>());
       
        
        String data = "";
        int count = 0;
        for(String arg: args){
            int i = 0;
            for(String item: arg.split("\\|")){
                if(i == 0)
                    tables.getJSONArray(tableName).put(item.trim());
                switch(item.trim()){
                    case "AUTO_INCREMENT":
                        data += " UNSIGNED AUTO_INCREMENT PRIMARY KEY";
                        break;
                    case "CURRENT_TIMESTAMP":
                        data += " DEFAULT CURRENT_TIMESTAMP";
                        break;
                    default:
                        data += " " + item.trim();
                }
                i++;
            }
            
            if(++count < args.length)
                data += ",";
        }
        
        
        try{
            sql = String.format("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '%s' AND TABLE_NAME = '%s'", dbName, tableName);
            if(!stmt.executeQuery(sql).next()){
                sql = String.format("CREATE TABLE %s (%s)", tableName, data);
                stmt.executeUpdate(sql);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public Database table(String table){
        tableName = table;
        return this;
    }
    
    public Database select(String table){
        sql = String.format("SELECT * FROM %s", table);
        return this;
    }
    
    public boolean insert(String tableName, JSONObject data){
        Iterator<String> JSONKeys = data.keys();
        
        String keys = "", values = "";
        
        try{
            while(JSONKeys.hasNext()){
                String key = JSONKeys.next();
                keys += ", " + key;
                values += ", '" + data.get(key) + "'";
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        
        sql = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, keys.substring(2), values.substring(2));
        
        try{
            if(stmt.executeUpdate(sql) == 1){
                System.out.println("Veri başarıyla eklendi.");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return true;
    }
    
    public boolean delete(String key, String value) throws SQLException{
        sql = String.format("DELETE FROM %s WHERE %s='%s'", tableName, key, value);
        if(stmt.executeUpdate(sql) == 1)
            return true;
        return false;
    }
    
    public boolean update(JSONObject data) throws SQLException{
        Iterator<String> JSONKeys = data.keys();
        String updateData = "";
        
        try{
            int i = 0;
            while(JSONKeys.hasNext()){
                String key = JSONKeys.next();
                if(i++ != 0)
                    updateData += ",";
                
                updateData += String.format("%s=%s", key, data.get(key).equals("@CURRENT_TIMESTAMP") ? "CURRENT_TIMESTAMP" : "'" + data.get(key) + "'");
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        
        sql = String.format("UPDATE %s SET %s WHERE%s", tableName, updateData, whereStatement);
        if(stmt.executeUpdate(sql) == 1)
            return true;
        return false;
    }
    
    public Database where(String key, String value){
        if(whereStatement.length() > 0)
            whereStatement += " AND";
        whereStatement += String.format(" %s='%s'", key, value);
        return this;
    }
    
    public JSONObject get() throws JSONException, SQLException{
        JSONObject data = new JSONObject();
        sql += whereStatement.length() > 0 ? " WHERE " + whereStatement : "" + " ORDER BY id DESC LIMIT 1";
        
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next()){
            JSONArray tableSchema = tables.getJSONArray(tableName);
            for(int i = 0; i < tableSchema.length(); i++){
                String key = tableSchema.getString(i);
                data.put(key, rs.getString(key));
            }
        }        
        return data;
    }
    
    public List getAll() throws JSONException, SQLException{
        List<JSONObject> data = new ArrayList();
        
        ResultSet rs = stmt.executeQuery(sql);
        JSONArray tableSchema = tables.getJSONArray(tableName);

        int count = 0;
        while(rs.next()){
            data.add(new JSONObject());
            for(int i = 0; i < tableSchema.length(); i++){
                String key = tableSchema.getString(i);
                data.get(count).put(key, rs.getString(key));
            }
            count++;
        }

        rs.close();
        stmt.close();
        conn.close();
        
        return data;
    }
}