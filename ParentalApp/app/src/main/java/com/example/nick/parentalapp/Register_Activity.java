package com.example.nick.parentalapp;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.nick.parentalapp.Database_Controllers.User_Handler;
import com.example.nick.parentalapp.Database_Models.User;
import com.example.nick.parentalapp.Dialog_Controllers.Dialog_Controller;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class Register_Activity extends AppCompatActivity {

    //Create our local variables
    private Button Register;
    private EditText Username;
    private EditText FirstName;
    private EditText LastName;
    private EditText Email;
    private EditText Password;
    private EditText Password2;
    private Button Back;
    public static Activity RegisterActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the view our user will see to the activity that matches our activity
        setContentView(R.layout.activity_register_);

        //Bind our interface elements to objects in the ID register
        Back = (Button) findViewById(R.id.Back_Register);
        Register = (Button) findViewById(R.id.Register_Button);
        Username = (EditText) findViewById(R.id.Username_Register);
        FirstName = (EditText) findViewById(R.id.FirstName_Register);
        LastName = (EditText) findViewById(R.id.LastName_Register);
        Email = (EditText) findViewById(R.id.Email_Register);
        Password = (EditText) findViewById(R.id.Password1_Register);
        Password2 = (EditText) findViewById(R.id.Password2_Register);

        //Create our onclick listeners which when recieve a request will activate a function
        Register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Register_Clicked();
            }
        });
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Back_Clicked();
            }
        });

    }

    //When back is clicked finish the activity
    public void Back_Clicked() {
        finish();
    }

    private void Register_Clicked() {

        Dialog_Controller DialogBox = new Dialog_Controller();

        Boolean Validated = false;
        Boolean Exists = false;

        Validated = CheckFields();

        if(!Validated){
            return;
        }
        ConnectionDetector Connection = new ConnectionDetector(this);
        if(!Connection.isConnectingToInternet()) {
            Exists = CheckUser();

            if(!Exists){
                AddUser();
                DialogBox.CreateClosingBox(this, "Username successfully created, please login on the main page.");
            }
            else{
                DialogBox.CreateNeutralDialog(this, "This Username is already taken, try another");
            }
        }
        else{
            DialogBox.CreateNeutralDialog(this, "Unable to detect network connection, please try again");
        }
    }

    private boolean CheckFields() {
        Dialog_Controller AlertBox = new Dialog_Controller();
        String DialogString = "";

        //Validate our user input to ensure they have put the expected values in the field
        DialogString += (Validator(Username.getText().toString(), "Username", 2, 1));
        DialogString += (Validator(FirstName.getText().toString(), "First Name", 2,1));
        DialogString += (Validator(LastName.getText().toString(), "Last Name", 2, 1));
        DialogString += (Validator(Email.getText().toString(), "Email Address", 2, 1 ));
        DialogString += (Validator(Password.getText().toString(), "Password", 2, 1));
        DialogString += (Validator(Password2.getText().toString(), "Password Validation", 2,1 ));
        DialogString += (Validator(Password.getText().toString(), "Password", 2, 2));

        if(DialogString.length() > 0) {
            AlertBox.CreateNeutralDialog(this, DialogString);
            return false;
        }
        else{
            return true;
        }

    }

    private String Validator(String text, String type, int expectedLength, int ValidationType){

        if(text.length() < expectedLength && ValidationType == 1){
            return "Your " + type + " is not long enough \n";
        }
        else if (!Objects.equals(Password.getText().toString(), Password2.getText().toString()) && ValidationType == 2){
            return "Your Passwords do not match \n";
        }
        return "";
    }

    private Boolean CheckUser() {

        User_Handler User_Class = new User_Handler();
        User_Class.execute("Query", Username.getText().toString());
        User[] UsersList = null;

        try {
            UsersList = User_Class.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        if(UsersList.length == 0){
            return false;
        }
        else    {
            return true;
        }


    }


    private void AddUser() {
        User_Handler User_Class = new User_Handler();
        User_Class.execute("Add",
                Username.getText().toString(),
                FirstName.getText().toString(),
                LastName.getText().toString(),
                Email.getText().toString(),
                Password.getText().toString());
    }



}

