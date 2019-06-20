package com.example.nick.parentalapp.Database_Models;

import java.util.Date;

/**
 * Created by Nick on 04/01/2018.
 */

public class Baby
{
    private String id;

    public String getId() { return this.id; }

    public void setId(String id) { this.id = id; }

    private String createdAt;

    public String getCreatedAt() { return this.createdAt; }

    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    private String updatedAt;

    public String getUpdatedAt() { return this.updatedAt; }

    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    private String version;

    public String getVersion() { return this.version; }

    public void setVersion(String version) { this.version = version; }

    private String Username;

    public String getUsername() { return this.Username; }

    public void setUsername(String Username) { this.Username = Username; }

    private String BabyName;

    public String getBabyName() { return this.BabyName; }

    public void setBabyName(String BabyName) { this.BabyName = BabyName; }

    private String DoB;

    public String getDoB() { return this.DoB; }

    public void setDoB(String DoB) { this.DoB = DoB; }

    private String Sex;

    public String getSex() { return this.Sex; }

    public void setSex(String Sex) { this.Sex = Sex; }

    public void CreateNewChild(String username, String babyname, String DoB, String Sex){
        setUsername(username);
        setBabyName(babyname);
        setDoB(DoB);
        setSex(Sex);
    }
}