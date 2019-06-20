package com.example.nick.parentalapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.nick.parentalapp.Database_Controllers.Baby_Handler;
import com.example.nick.parentalapp.Database_Controllers.User_Handler;
import com.example.nick.parentalapp.Database_Models.Baby;
import com.example.nick.parentalapp.Database_Models.User;
import com.example.nick.parentalapp.Dialog_Controllers.Dialog_Controller;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class Child_Create_Activity extends AppCompatActivity {

    //Create our local variables
    private String Username;
    private Intent previousIntent;
    private EditText BabyName;
    private EditText DateOfBirth;
    private RadioGroup Sex;
    private Button AddChild;
    private Button Back;
    public static Activity CreateActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the view our user will see to the activity that matches our activity
        setContentView(R.layout.activity_child__create_);

        //Bind our interface elements to objects in the ID register
        BabyName = (EditText) findViewById(R.id.BabyName_Field_Create);
        DateOfBirth = (EditText) findViewById(R.id.Dob_Field_Create);
        Sex = (RadioGroup) findViewById(R.id.Sex_RadioGroup_Create);
        AddChild = (Button) findViewById(R.id.AddChild_Create);
        Back = (Button) findViewById(R.id.Back_ChildAdd);

        //Catch our previous intents extra strings and store them in variables
        previousIntent = getIntent();
        Username = previousIntent.getStringExtra("Username");

        //Create our onclick listeners which when recieve a request will activate a function
        AddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddChild_Clicked();
            }
        });
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Back_Clicked();
            }
        });
        
    }

    //When back button is clicked, finish activity
    private void Back_Clicked() {
        finish();
    }

    private void AddChild_Clicked() {
        //Create our dialog box controller
        Dialog_Controller DialogBox = new Dialog_Controller();

        //Create our local variables
        Boolean Validated = false;
        Boolean Exists = false;

        //Check our fields, if they fail the checks, end the function
        Validated = CheckFields();
        if(!Validated){
            return;
        }

        //Create our connection object for checking conenction on phone
        ConnectionDetector Connection = new ConnectionDetector(this);
        //If there isn't a connection on phone, throw dialog box to show this
        if(!Connection.isConnectingToInternet()) {
            //Check our Child entry, if a match is found, inform user. If not add the child to database using function.
            Exists = CheckChild();
            if(!Exists){
                AddChild_ToDatabase();
                DialogBox.CreateClosingBox(this, "Child successfully created");
            }
            else{
                DialogBox.CreateNeutralDialog(this, "You already have a child with this name!");

            }
        }
        else{
            DialogBox.CreateNeutralDialog(this, "Unable to detect network connection, please try again");
        }
    }

    private boolean CheckFields(){
        //Create our alert box object
        Dialog_Controller AlertBox = new Dialog_Controller();
        //Create our error string as blank
        String DialogString = "";

        //Run our Validator function to check if there is an error, if no error is found string will be blank.
        DialogString += (Validator(BabyName.getText().toString(), "Baby Name", 2, 1));
        DialogString += (Validator(DateOfBirth.getText().toString(), "Date of Birth", 8,2));
        DialogString += (Validator(DateOfBirth.getText().toString(), "Date of Birth", 8,3));

        //If our string has contents, show it and return false to function, else return true (Meaning all fields are appropriate)
        if(DialogString.length() > 0) {
            AlertBox.CreateNeutralDialog(this, DialogString);
            return false;
        }
        else{
            return true;
        }

    }

    //The validator will take in inputs, check them for certain outcomes. If a hit is made, return a string to be included in the error string above.
    private String Validator(String text, String type, int expectedLength, int ValidationType) {

        if (text.length() < expectedLength && ValidationType == 1) {
            return "Your " + type + " is not long enough \n";
        } else if (text.length() < expectedLength && ValidationType == 2) {
            return "Your Date of Birth is not the correct length! DD/MM/YY \n";

        } else if (text.length() == expectedLength && ValidationType == 3){
            if (!Objects.equals(text.substring(2, 3), "/") || !Objects.equals(text.substring(5, 6), "/")){
                Log.i("e", text.substring(2,3)+text.substring(5,6));
                return "Invalid Date type, it must be DD/MM/YY \n";
            }
        }
    return "";
    }

    //Run a function to check if the child exists in the database.
    private Boolean CheckChild() {
        //Create our Database handler, Execute it and pass it parameters
        Baby_Handler Baby_Class = new Baby_Handler();
        Baby_Class.execute("QueryTwice", Username, BabyName.getText().toString());
        Baby[] BabyList = null;

        try {
            //Get the result of our query
            BabyList = Baby_Class.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //If we have a match, return true, else false.
        if(BabyList.length == 0){
            return false;
        }
        else    {
            return true;
        }
    }

    //Add our child to the database
    private void AddChild_ToDatabase() {
        //Create our Database Handler
        Baby_Handler Baby_Class = new Baby_Handler();

        //Check what sex of the baby
        int SelectedRadioButtonID = Sex.getCheckedRadioButtonId();
        RadioButton SelectedRadioButton = (RadioButton) findViewById(SelectedRadioButtonID);
        //Execute our post command.
        Baby_Class.execute("Add", Username, BabyName.getText().toString(), DateOfBirth.getText().toString(), SelectedRadioButton.getText().toString());
    }
}
