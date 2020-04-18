package models;

import database.Database;
import java.sql.SQLException;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import app.Config;

interface PersonsInterface{
    public boolean add(JSONObject person) throws JSONException, SQLException;
    public boolean update(JSONObject person, int id) throws JSONException, SQLException;
    public List getAll() throws JSONException, SQLException;
    public JSONObject login(String username, String password) throws JSONException, SQLException;
}

public class Persons implements PersonsInterface{
    Database db;
    public Persons() throws JSONException, SQLException{
        db = new Database("bank", Config.DB_USER, Config.DB_PASS);
        db.table("persons").model(
                "id | INT(11) | AUTO_INCREMENT",
                "username | VARCHAR(50) NOT NULL",
                "password | VARCHAR(50) NOT NULL",
                "firstname | VARCHAR(50) NOT NULL",
                "lastname | VARCHAR(50) NOT NULL",
                "date_of_birth | DATE NOT NULL",
                "no | VARCHAR(11) NOT NULL",
                "date | TIMESTAMP | CURRENT_TIMESTAMP",
                "accesses | INT",
                "phone | VARCHAR(11) NOT NULL"

        );
    }
    
    public boolean add(JSONObject person) throws JSONException, SQLException{
        return this.db.insert("persons", person);
    }
    
    public JSONObject login(String username, String password) throws JSONException, SQLException{
        return this.db.select("persons")
                .where("username", username)
                .where("password", password)
                .get();
    }
    
    public boolean update(JSONObject person, int id) throws JSONException, SQLException{
        return this.db.table("persons").where("id", Integer.toString(id)).update(person);
    }
    
    public List getAll() throws JSONException, SQLException{
        return this.db.select("persons").getAll();
    }
}
