package com.example.nick.parentalapp.Database_Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by Nick on 02/01/2018.
 */
public class User
{
    private String id;

    public String getId() { return this.id; }

    public void setId(String id) { this.id = id; }

    private String Username;

    public String getUsername() { return this.Username; }

    public void setUsername(String Username) { this.Username = Username; }

    private String FirstName;

    public String getFirstName() { return this.FirstName; }

    public void setFirstName(String FirstName) { this.FirstName = FirstName; }

    private String LastName;

    public String getLastName() { return this.LastName; }

    public void setLastName(String LastName) { this.LastName = LastName; }

    private String Password;

    public String getPassword() { return this.Password; }

    public void setPassword(String Password) { this.Password = Password; }

    private String Email;

    public String getEmail() { return this.Email; }

    public void setEmail(String Email) { this.Email = Email; }

    private String version;

    public String getVersion() { return this.version; }

    public void setVersion(String version) { this.version = version; }

    private String createdAt;

    public String getCreatedAt() { return this.createdAt; }

    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    private String updatedAt;

    public String getUpdatedAt() { return this.updatedAt; }

    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public void CreateNewUser(String Username, String FirstName, String Lastname, String Email, String Password){
        setUsername(Username);
        setFirstName(FirstName);
        setLastName(Lastname);
        setEmail(Email);
        setPassword(Password);
    }

}