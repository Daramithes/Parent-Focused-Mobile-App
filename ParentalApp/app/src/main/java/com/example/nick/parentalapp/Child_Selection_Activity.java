package com.example.nick.parentalapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nick.parentalapp.Database_Controllers.Baby_Handler;
import com.example.nick.parentalapp.Database_Controllers.User_Handler;
import com.example.nick.parentalapp.Database_Models.Baby;
import com.example.nick.parentalapp.Database_Models.User;
import com.example.nick.parentalapp.Dialog_Controllers.Dialog_Controller;
import com.example.nick.parentalapp.ImageAdapter.ImageAdapter;

import java.util.concurrent.ExecutionException;

public class Child_Selection_Activity extends AppCompatActivity {

    //Create our local variables
    private Button Logout;
    private Button AddChild;
    private GridView ChildView;
    private static Baby[] BabyListStatic;
    private String Username;
    private Intent previousIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the view our user will see to the activity that matches our activity
        setContentView(R.layout.activity_child__selection_);

        //Bind our interface elements to objects in the ID register
        Logout = (Button) findViewById(R.id.Logout_Selection);
        AddChild = (Button) findViewById(R.id.AddBaby_Selection);
        ChildView = (GridView) findViewById(R.id.ChildView_Selection);

        //Catch our previous intents extra strings and store them in variables
        previousIntent = getIntent();
        Username = previousIntent.getStringExtra("Username");

        //Create our onclick listeners which when recieve a request will activate a function
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout_Clicked();
            }
        });

        AddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddChild_Clicked();
            }
        });

    }

    //When the activity resumes focus, check the internet connection, and if it has one, repopulate interface
    protected void onResume(){
        super.onResume();
        //Create our connection detector class
        ConnectionDetector Connection = new ConnectionDetector(this);
        //If there isn't a connection on phone, throw dialog box to show this
        if(!Connection.isConnectingToInternet()) {

            BabyListStatic  = ChildrenFetch();
        ChildView.setAdapter(new ImageAdapter(this,BabyListStatic));
        }
        else{
            Dialog_Controller DialogBox = new Dialog_Controller();
            DialogBox.CreateNeutralDialog(this, "Unable to detect network connection, please try again");
        }
    }

    //Fetch any children belonging to currently logged in user
    private Baby[] ChildrenFetch() {
        //Create our Database handler, Execute it and pass it parameters
        Baby_Handler Baby_Class = new Baby_Handler();
        Baby_Class.execute("Query", Username);
        Baby[] BabyList = null;

        try {
            //Get the result of our query
            return Baby_Class.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    //When logout is clicked, end activity.
    private void Logout_Clicked() {
        finish();
    }
    //When add child is clicked, load add child page.
    private void AddChild_Clicked() {
        //Load our create child function.
        Intent intent = new Intent(this, Child_Create_Activity.class);
        intent.putExtra("Username", Username);
        startActivity(intent);
    }
}

