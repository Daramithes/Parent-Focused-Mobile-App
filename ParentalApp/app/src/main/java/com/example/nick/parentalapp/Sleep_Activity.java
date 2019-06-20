package com.example.nick.parentalapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nick.parentalapp.Database_Controllers.Sleep_Handler;
import com.example.nick.parentalapp.Database_Models.Sleep;
import com.example.nick.parentalapp.Dialog_Controllers.Dialog_Controller;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.concurrent.ExecutionException;

public class Sleep_Activity extends AppCompatActivity {

    //Create our local variables
    private Button Back;
    private Button AddSleep;
    private GraphView Graph;
    private Intent previousIntent;
    private String Username;
    private TextView BabyName;
    private EditText TimeSlept;
    private Sleep[] SleepList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        //Set the view our user will see to the activity that matches our activity
        setContentView(R.layout.activity_sleep_);

        //Bind our interface elements to objects in the ID register
        Graph = (GraphView) findViewById(R.id.GraphView);
        BabyName = (TextView) findViewById(R.id.BabyName_Sleep);
        Back = (Button) findViewById(R.id.Back_Sleep);
        AddSleep = (Button) findViewById(R.id.AddSleep_Sleep);
        TimeSlept = (EditText) findViewById(R.id.SleepLength_Sleep);

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
        AddSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddSleep_Clicked();
            }
        });

        //Create our label for our graph
        GridLabelRenderer gridLabel = Graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Record Entry");
        gridLabel.setVerticalAxisTitle("Hours of sleep");

        //Run our graph generation function
        RenderGraph();


    }

    //When back is clicked finish the activity
    private void Back_Clicked() {
        finish();
    }

    private void AddSleep_Clicked() {

        if (Integer.parseInt(String.valueOf(TimeSlept.getText())) < 25){
            ConnectionDetector Connection = new ConnectionDetector(this);
            if(!Connection.isConnectingToInternet()) {
                Sleep_Handler Sleep_Controller = new Sleep_Handler();
                Sleep_Controller.execute("Add", Username, BabyName.getText().toString(), TimeSlept.getText().toString(), String.valueOf(SleepList.length));
                RenderGraph();
            }
            else{
                Dialog_Controller DialogBox = new Dialog_Controller();
                DialogBox.CreateNeutralDialog(this, "Unable to detect network connection, please try again");
            }
        }
        else{
            Dialog_Controller DialogBox = new Dialog_Controller();
            DialogBox.CreateNeutralDialog(this, "Your Hours has be less than 24!");
        }


    }


    private void RenderGraph() {

        //Fetch our sleeping list from database
        Sleep[] SleepList = FetchSleep();
        if (SleepList.length == 0){
            return;
        }

        //Create our datapoint list
        LineGraphSeries<DataPoint> series;
        series =  new LineGraphSeries();

        //Create a for loop to create our list.
        int index = 0;
        for (Sleep SleepRecord : SleepList) {
            DataPoint NewData = new DataPoint(SleepRecord.getRecordNumber(), SleepRecord.getSleepLength());
            NewData.getX();
            series.appendData(NewData, true, SleepList.length);

        }

        //Configure our graph settings.
        Graph.getViewport().setXAxisBoundsManual(true);
        Graph.getViewport().setYAxisBoundsManual(true);
        Graph.getViewport().setMinY(0);
        Graph.getViewport().setMaxY(24);
        Graph.getViewport().setMinX(0);
        Graph.getViewport().setMaxX(series.getHighestValueX());
        Graph.getViewport().setScrollable(true);
        Graph.getViewport().setScalable(true);
        Graph.getViewport().setScalableY(false);
        Graph.getViewport().setScrollableY(true);

        //Assign our datapoints to our graph
        Graph.addSeries(series);
    }

    private Sleep[] FetchSleep() {

        Sleep_Handler Sleep_Controller = new Sleep_Handler();
        Sleep_Controller.execute("Query", Username, BabyName.getText().toString());

        try {
            SleepList = Sleep_Controller.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return SleepList;

    }
}
