package com.example.nick.parentalapp.Database_Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.example.nick.parentalapp.Database_Models.Meal;
import com.example.nick.parentalapp.Database_Models.Sleep;
import com.example.nick.parentalapp.Database_Models.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Nick on 06/01/2018.
 */

public class Sleep_Handler extends AsyncTask<String, String, Sleep[]> {

    //Create our OkHttpClient
    private OkHttpClient client = new OkHttpClient();

    //Create our list of sleep classes which will be populated by database
    private Sleep[] SleepList;
    @Override

    //Run Asynchronously in the background of the app.
    protected Sleep[] doInBackground(String... params) {

        //Based on what the first parameter is, run that function
        if(Objects.equals(params[0], "Query")){
            //Populate sleeplist based on the result of our function
            SleepList = QuerySleep(params[1], params[2]);
        }
        if(Objects.equals(params[0], "Add")){
            //Populate sleeplist based on the result of our function
            SleepList = AddSleep(params[1], params[2], params[3],params[4]);
        }
        return SleepList;
    }

    private Sleep[] QuerySleep(String Username, String Babyname) {
        //Create our url string which when accessed will return json
        String url = "https://parentalproject.azurewebsites.net/tables/Sleep?$filter=Username%20eq%20'" +
            Username +
            "'%20and%20BabyName%20eq%20'" +
            Babyname +
            "'&ZUMO-API-VERSION=2.0.0";

        //Build Request
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            //Execute our call to fetch response
            Response response = client.newCall(request).execute();
            //Json creation
            Gson gson = new Gson();
            //Convert Json to sleep object
            SleepList = gson.fromJson(response.body().string(), Sleep[].class);
            //Return object list.
            return SleepList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Sleep[] s) {
        super.onPostExecute(s);
    }

    private Sleep[] AddSleep(String Username, String BabyName, String SleepLength, String RecordNumber) {
        //Create our url string
        String url = "https://parentalproject.azurewebsites.net/tables/Sleep/?ZUMO-API-VERSION=2.0.0";
        //Create our Gson object which we will use to serialize our json object
        Gson gson = new Gson();

        //Create our new sleep class and populate it with the parameters passed in.
        //We have to convert the parameters passed in to match our class first.
        int SleepLengthConverted;
        int RecordNumberConverted;

        SleepLengthConverted = Integer.parseInt(SleepLength);
        RecordNumberConverted = Integer.parseInt(RecordNumber);

        Sleep sleep = new Sleep();
        sleep.CreateNewSleep(Username, BabyName, SleepLengthConverted, RecordNumberConverted);

        //Json Conversion
        String json = gson.toJson(sleep);

        //Create our request body which we will send to the database
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), json);

        //Build our request using our body, URL and function we want to use
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        //Create our clients call so it is ready to be executed
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
