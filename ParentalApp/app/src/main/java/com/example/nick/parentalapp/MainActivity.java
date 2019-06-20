package com.example.nick.parentalapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.nick.parentalapp.Database_Controllers.Baby_Handler;
import com.example.nick.parentalapp.Database_Controllers.User_Handler;
import com.example.nick.parentalapp.Database_Models.Baby;
import com.example.nick.parentalapp.Database_Models.User;
import com.example.nick.parentalapp.Dialog_Controllers.Dialog_Controller;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    //Create our local variables
    private Button Login;
    private EditText Username;
    private EditText Password;
    private TextView RegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the view our user will see to the activity that matches our activity
        setContentView(R.layout.activity_main);

        //Bind our interface elements to objects in the ID register
        Login = (Button) findViewById(R.id.Login_Main);
        Username = (EditText) findViewById(R.id.Username_Main);
        Password = (EditText) findViewById(R.id.Password_Main);
        RegisterButton = (TextView) findViewById(R.id.RegisterHere_Main);

        //Create our onclick listeners which when recieve a request will activate a function
        Login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Login_Clicked();
            }
        });
        RegisterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Register_Clicked();
            }
        });

    }

    private void Login_Clicked(){
        //Run Check Fields, if it returns false, Run Check Exists
        if(!CheckFields()){
            ConnectionDetector Connection = new ConnectionDetector(this);
            if(!Connection.isConnectingToInternet()) {
                //Run CheckExists, if it returns false, Load next page
                if (!CheckUser()) {
                    LoadPage();
                }

            }
            else{
                Dialog_Controller DialogBox = new Dialog_Controller();
                DialogBox.CreateNeutralDialog(this, "Unable to detect network connection, please try again");
            }
        };
        

    }

    //Check the fields of the username and password. If there is a hit return true and inform user of what the issue is.
    private boolean CheckFields() {
        Dialog_Controller DialogBox = new Dialog_Controller();
        if(Username.length() == 0 & Password.length() == 0){
            DialogBox.CreateNeutralDialog(this, "The Username field and Password Field cannot be left blank");
            return true;
        }
        else if(Username.length() == 0){
            DialogBox.CreateNeutralDialog(this, "The Username field cannot be left blank");
            return true;
        }
        else if(Password.length() == 0){
            DialogBox.CreateNeutralDialog(this, "The Password Field cannot be left blank");
            return true;
        }
        else{
            return false;
        }
    }

    //Run a check user to see if user exists, if not inform them. If a hit is found bit wrong password then inform them too.
    private boolean CheckUser() {
        //Create our Database handler, Execute it and pass it parameters
        User[] UserList = null;
        User_Handler User_Controller = new User_Handler();
        User_Controller.execute("Query", Username.getText().toString());
        Dialog_Controller DialogBox = new Dialog_Controller();

        try {
            //Get the result of our query
            UserList = User_Controller.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //If we have a match, return true, else false.
        if (UserList.length == 0){
            DialogBox.CreateNeutralDialog(this, "Your Username does not exist, you can register by clicking the 'Register Here' Button");
            return true;
        }
        else if (!Objects.equals(UserList[0].getPassword(), Password.getText().toString())){
            DialogBox.CreateNeutralDialog(this, "Your password is not correct!");
            return true;
        }
        return false;
    }


    private void LoadPage() {
        //Load our child selection page and pass in username
        Intent intent = new Intent(this, Child_Selection_Activity.class);
        intent.putExtra("Username", Username.getText().toString());
        startActivity(intent);
    }

    private void Register_Clicked(){
        //Register button is clicked load register page.
       Intent intent = new Intent(this, Register_Activity.class);
       startActivity(intent);

    }
}