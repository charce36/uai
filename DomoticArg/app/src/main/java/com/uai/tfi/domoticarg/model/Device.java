//package com.androidhive.androidsqlite;
package com.uai.tfi.domoticarg.model;
 
public class Device {
     
    //private variables
    int _id;
    String _name;
    String _onlineID;
     
    // Empty constructor
    public Device(){
         
    }
	
    // constructor
    public Device(int id, String name, String onlineID){
        this._id = id;
        this._name = name;
        this._onlineID = onlineID;
    }
     
    // constructor
    public Device(String name, String onlineID){
        this._name = name;
        this._onlineID = onlineID;
    }
    // getting ID
    public int getID(){
        return this._id;
    }
     
    // setting id
    public void setID(int id){
        this._id = id;
    }
     
    // getting name
    public String getName(){
        return this._name;
    }
     
    // setting name
    public void setName(String name){
        this._name = name;
    }
     
    // getting id firebase
    public String getOnlineID(){
        return this._onlineID;
    }
     
    // setting id firebase
    public void setOnlineID(String onlineID){
        this._onlineID = onlineID;
    }
}