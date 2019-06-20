package com.example.nick.parentalapp.Database_Controllers;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.example.nick.parentalapp.Database_Models.Baby;
import com.example.nick.parentalapp.Database_Models.User;
import com.example.nick.parentalapp.Dialog_Controllers.Dialog_Controller;
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
 * Created by Nick on 03/01/2018.
 */

public class Baby_Handler extends AsyncTask<String, String, Baby[]> {

    //Create our OkhttpClient
    private OkHttpClient client = new OkHttpClient();
    //Create our list of baby classes which will be populated by database
    private Baby[] BabyList;
    @Override
    //Run Asynchronously in the background of the app.
    protected Baby[] doInBackground(String... params) {

        //Based on what the first parameter is, run that function
        if(Objects.equals(params[0], "Query")){
            //Populate babylist based on the result of our function
            BabyList = QueryUser(params[1]);
        }
        if(Objects.equals(params[0], "Add")){
            //Populate babylist based on the result of our function
            BabyList = AddBaby(params[1], params[2],params[3],params[4]);
        }
        if(Objects.equals(params[0], "QueryTwice")){
            //Populate babylist based on the result of our function
            BabyList = QueryTwice(params[1], params[2]);
        }
        return BabyList;
    }

    @Override
    protected void onPostExecute(Baby[] s) {
        super.onPostExecute(s);
    }

    private Baby[] QueryUser(String Username){
        //Create our url string
        String url = "https://parentalproject.azurewebsites.net/tables/Babys/?$filter=Username%20eq%20%27" +
                Username +
                "%27&ZUMO-API-VERSION=2.0.0";

        //Build our request using our body, URL and function we want to use
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {

            //Create our clients response so it is ready to be executed
            Response response = client.newCall(request).execute();

            //Create our Gson so we can deserialize our JSON object
            Gson gson = new Gson();

            //Our babylist will be populated by the response body (Seralized Json) by mapping it to our baby class.
            BabyList = gson.fromJson(response.body().string(), Baby[].class);
            //Return List of objects.
            return BabyList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Baby[] QueryTwice(String Username, String Babyname){

        //Create our url string
        String url = "https://parentalproject.azurewebsites.net/tables/Babys?$filter=Username%20eq%20'" +
                Username +
                "'%20and%20BabyName%20eq%20'" + Babyname +
                "'&ZUMO-API-VERSION=2.0.0";

        //Build our request using our body, URL and function we want to use
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            //Create our clients response so it is ready to be executed
            Response response = client.newCall(request).execute();
            //Create our Gson so we can deserialize our JSON object
            Gson gson = new Gson();
            //Our babylist will be populated by the response body (Seralized Json) by mapping it to our baby class.
            BabyList = gson.fromJson(response.body().string(), Baby[].class);
            //Finally return it.
            return BabyList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Baby[] AddBaby(String Username, String BabyName, String Dob, String Sex) {

        //Create our url
        String url = "https://parentalproject.azurewebsites.net/tables/Babys/?ZUMO-API-VERSION=2.0.0";
        //Create our Gson object which we will use to serialize our json object
        Gson gson = new Gson();
        //Create our new baby class and populate it with the parameters passed in.
        Baby baby = new Baby();
        baby.CreateNewChild(Username, BabyName, Dob, Sex);
        //Json Conversion
        String json = gson.toJson(baby);
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
        //Try catch incase of error
        try {
            //Execute our build statement
            Response response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //In order to maintain babylist for our query functions, return null.
        return null;
    }
}
