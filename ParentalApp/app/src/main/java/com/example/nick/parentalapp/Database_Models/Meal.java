package com.example.nick.parentalapp.Database_Models;

/**
 * Created by Nick on 05/01/2018.
 */

public class Meal
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

    private String FoodName;

    public String getFoodName() { return this.FoodName; }

    public void setFoodName(String FoodName) { this.FoodName = FoodName; }

    private String PercentageEaten;

    public String getPercentageEaten() { return this.PercentageEaten; }

    public void setPercentageEaten(String PercentageEaten) { this.PercentageEaten = PercentageEaten; }

    private String BabyName;

    public String getBabyName() { return this.BabyName; }

    public void setBabyName(String BabyName) { this.BabyName = BabyName; }

    private String Username;

    public String getUsername() { return this.Username; }

    public void setUsername(String Username) { this.Username = Username; }

    private String TimeEaten;

    public String getTimeEaten() { return this.TimeEaten; }

    public void setTimeEaten(String TimeEaten) { this.TimeEaten = TimeEaten; }

    private boolean PreviousMeal;

    public boolean getPreviousMeal() { return this.PreviousMeal; }

    public void setPreviousMeal(boolean PreviousMeal) { this.PreviousMeal = PreviousMeal; }

    public void CreateNewMeal(String username, String babyName, String mealName, String percentage, String time, boolean PreviousMealIdentifier) {
        setUsername(username);
        setBabyName(babyName);
        setFoodName(mealName);
        setPercentageEaten(percentage);
        setTimeEaten(time);
        setPreviousMeal(PreviousMealIdentifier);
    }
}