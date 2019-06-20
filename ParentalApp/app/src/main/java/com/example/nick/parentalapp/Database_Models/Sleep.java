package com.example.nick.parentalapp.Database_Models;

/**
 * Created by Nick on 06/01/2018.
 */

public class Sleep {
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

    private String Time;

    public String getTime() { return this.Time; }

    public void setTime(String Time) { this.Time = Time; }

    private int RecordNumber;

    public int getRecordNumber() { return this.RecordNumber; }

    public void setRecordNumber(int RecordNumber) { this.RecordNumber = RecordNumber; }

    private String BabyName;

    public String getBabyName() { return this.BabyName; }

    public void setBabyName(String BabyName) { this.BabyName = BabyName; }

    private String Username;

    public String getUsername() { return this.Username; }

    public void setUsername(String Username) { this.Username = Username; }

    private int SleepLength;

    public int getSleepLength() { return this.SleepLength; }

    public void setSleepLength(int SleepLength) { this.SleepLength = SleepLength; }

    public void CreateNewSleep(String Username, String BabyName, int SleepLength, int RecordNumber){
        setUsername(Username);
        setBabyName(BabyName);
        setSleepLength(SleepLength);
        setRecordNumber(RecordNumber);
    }
}
