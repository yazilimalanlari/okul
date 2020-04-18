package models;

import app.Config;
import database.Database;
import java.sql.SQLException;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;


interface CustomersInterface{
    public boolean add(JSONObject customer) throws JSONException, SQLException;
    public boolean update(JSONObject customer, int id) throws JSONException, SQLException;
    public List getAll() throws JSONException, SQLException;
}

public class Customers implements CustomersInterface{
    Database db;
    public Customers() throws JSONException, SQLException{
        db = new Database("bank", Config.DB_USER, Config.DB_PASS);
        db.table("customers").model(
                "id | INT(11) | AUTO_INCREMENT",
                "firstname | VARCHAR(50) NOT NULL",
                "lastname | VARCHAR(50) NOT NULL",
                "date_of_birth | DATE NOT NULL",
                "no | VARCHAR(11) NOT NULL",
                "date_enter | TIMESTAMP | CURRENT_TIMESTAMP",
                "date_leave | TIMESTAMP | NULL",
                "phone1 | VARCHAR(11) NOT NULL",
                "phone2 | VARCHAR(11)"
        );
    }
    
    public boolean add(JSONObject customer) throws JSONException, SQLException{
        return this.db.insert("customers", customer);
    }
    
    public boolean update(JSONObject customer, int id) throws JSONException, SQLException{
        return this.db.table("customers").where("id", Integer.toString(id)).update(customer);
    }
    
    public List getAll() throws JSONException, SQLException{
        return this.db.select("customers").getAll();
    }
}
