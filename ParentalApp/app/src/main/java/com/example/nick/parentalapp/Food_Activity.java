package com.example.nick.parentalapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nick.parentalapp.Database_Controllers.Baby_Handler;
import com.example.nick.parentalapp.Database_Controllers.Meal_Handler;
import com.example.nick.parentalapp.Database_Controllers.User_Handler;
import com.example.nick.parentalapp.Database_Models.Meal;
import com.example.nick.parentalapp.Database_Models.User;
import com.example.nick.parentalapp.Dialog_Controllers.Dialog_Controller;

import org.w3c.dom.Text;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class Food_Activity extends AppCompatActivity {

    //Create our local variables
    private TextView BabyName;
    private EditText NewMeal;
    private EditText Time;
    private EditText Eaten;
    private Button Back;
    private Button AddMeal;
    private Button ViewPrevious;
    private String Username;
    private Intent previousIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set the view our user will see to the activity that matches our activity
        setContentView(R.layout.activity_food_);

        //Bind our interface elements to objects in the ID register
        BabyName = (TextView) findViewById(R.id.BabyName_Meal);
        NewMeal = (EditText) findViewById(R.id.New_Meal_Meal);
        Time = (EditText) findViewById(R.id.New_Time_Meal);
        Eaten = (EditText) findViewById(R.id.Percentage_Eaten_Meal);
        Back = (Button) findViewById(R.id.Back_Meal);
        AddMeal = (Button) findViewById(R.id.AddMeal_Meal);
        ViewPrevious = (Button) findViewById(R.id.ViewMeal_Meal);

        //Catch our previous intents extra strings and store them in variables
        previousIntent = getIntent();
        Username = previousIntent.getStringExtra("Username");
        BabyName.setText(previousIntent.getStringExtra("BabyName"));

        //Create our onclick listeners which when recieve a request will activate a function
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Back_Clicked();
            }
        });
        ViewPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreviousMeal();
            }
        });
        AddMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MealCreation();
            }
        });
    }

    private void PreviousMeal() {
        ConnectionDetector Connection = new ConnectionDetector(this);
        if(!Connection.isConnectingToInternet()) {
            Meal_Handler Meal_Class = new Meal_Handler();
            Meal_Class.execute("QueryThree", Username, BabyName.getText().toString());
            Meal[] LatestMeal = null;

            try {
            LatestMeal = Meal_Class.get();
            }    catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            Dialog_Controller DialogBox = new Dialog_Controller();
            if(LatestMeal.length == 0 ){
                DialogBox.CreateNeutralDialog(this, "There was no previous entry found");
            }else{
            DialogBox.CreateNeutralDialog(this, "The last meal was: " + LatestMeal[0].getFoodName()
            + ". The time was: " + LatestMeal[0].getTimeEaten()
            + ". The amount that was consumed: " + LatestMeal[0].getPercentageEaten() + "%");
            }
            }
            else{
                Dialog_Controller DialogBox = new Dialog_Controller();
                DialogBox.CreateNeutralDialog(this, "Unable to detect network connection, please try again");
        }

    }

    private void MealCreation() {
        Dialog_Controller DialogBox = new Dialog_Controller();

        Boolean Validated = false;
        Validated = CheckFields();
        ConnectionDetector Connection = new ConnectionDetector(this);
        if(!Connection.isConnectingToInternet()) {
             if(!Validated){
                AddMeal_ToDatabase();
                DialogBox.CreateNeutralDialog(this, "Meal Added Correctly");
            }
        }
        else{
            DialogBox.CreateNeutralDialog(this, "Unable to detect network connection, please try again");
        }
        }

    private void AddMeal_ToDatabase() {
        //Update old record
        Meal_Handler Meal_Fetch_Class = new Meal_Handler();
        Meal_Fetch_Class.execute("QueryThree", Username, BabyName.getText().toString());
        Meal[] LatestMeal = null;

        try {
            LatestMeal = Meal_Fetch_Class.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        //Push

        Meal_Handler Meal_Update_Class = new Meal_Handler();

        if (LatestMeal.length > 0) {
            Meal_Update_Class.execute("Update", LatestMeal[0].getId());
        }
        Meal_Handler Meal_Add_Class = new Meal_Handler();
        Meal_Add_Class.execute("Add", Username, BabyName.getText().toString(), NewMeal.getText().toString(), Time.getText().toString(), Eaten.getText().toString());
    }



    private Boolean CheckFields() {
        Dialog_Controller AlertBox = new Dialog_Controller();
        String DialogString = "";

        //Validate our user input to ensure they have put the expected values in the field
        DialogString += (Validator(NewMeal.getText().toString(), "Meal Name", 1, 1));
        DialogString += (Validator(Time.getText().toString(), "Time ", 1,2));
        DialogString += (Validator(Eaten.getText().toString(), "Percentage eaten", 1, 1));
        DialogString += (Validator(Time.getText().toString(), "Time ", 1,3));

        //If our string has contents, show it and return false to function, else return true (Meaning all fields are appropriate)
        if(DialogString.length() > 0) {
            AlertBox.CreateNeutralDialog(this, DialogString);
            return true;
        }
        else{
            return false;
        }
    }

    //The validator will take in inputs, check them for certain outcomes. If a hit is made, return a string to be included in the error string above.
    private String Validator(String text, String type, int expectedLength, int ValidationType){

        if(text.length() < expectedLength && ValidationType == 1){
            return "Your " + type + " cannot be empty \n";
        } else if (text.length() < expectedLength && ValidationType == 2) {
            return "Your Time value is not the correct length \n";
        }  else if (text.length() == expectedLength && ValidationType == 3){
            if (!Objects.equals(text.substring(2, 3), ":")){
                return "Time must be in 24 hour format, 17:30 for half five.\n";
            }
        }
        return "";
    }

    //When back is clicked, finish activity
    private void Back_Clicked() {
        finish();
    }
}

