package com.example.nick.parentalapp.Database_Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.example.nick.parentalapp.Database_Models.Baby;
import com.example.nick.parentalapp.Database_Models.Meal;
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
 * Created by Nick on 05/01/2018.
 */

public class Meal_Handler extends AsyncTask<String, String, Meal[]> {
    //Create our OkhttpClient
    private OkHttpClient client = new OkHttpClient();
    //Create our list of meal classes which will be populated by database
    private Meal[] MealList;

    @Override

    //Run Asynchronously in the background of the app.
    protected Meal[] doInBackground(String... params) {

        //Based on what the first parameter is, run that function
        if (Objects.equals(params[0], "Add")) {

            //Populate meallist based on the result of our function
            MealList = AddMeal(params[1], params[2], params[3], params[4], params[5]);
        }
        if (Objects.equals(params[0], "QueryTwice")) {

            //Populate meallist based on the result of our function
            MealList = QueryTwice(params[1], params[2]);
        }
        if (Objects.equals(params[0], "QueryThree")) {

            //Populate meallist based on the result of our function
            MealList = QueryThree(params[1], params[2]);
        }
        if (Objects.equals(params[0] , "Update")) {

            //Populate meallist based on the result of our function
            MealList = UpdateRecord(params[1]);
        }
        return MealList;
    }


    @Override
    protected void onPostExecute(Meal[] s) {
        super.onPostExecute(s);
    }

    private Meal[] QueryTwice(String Username, String Babyname) {

        //Create our url string which when accessed will return json
        String url = "https://parentalproject.azurewebsites.net/tables/Meal?$filter=Username%20eq%20'" +
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

            //Our meallist will be populated by the response body (Seralized Json) by mapping it to our meal class.
            MealList = gson.fromJson(response.body().string(), Meal[].class);

            //Return List of objects.
            return MealList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Meal[] AddMeal(String Username, String BabyName, String MealName, String Time, String Percentage) {
        //Create our url string
        String url = "https://parentalproject.azurewebsites.net/tables/Meal/?ZUMO-API-VERSION=2.0.0";

        //Create our Gson object which we will use to serialize our json object
        Gson gson = new Gson();

        //Create our new meal class and populate it with the parameters passed in.
        Meal meal = new Meal();
        meal.CreateNewMeal(Username, BabyName, MealName, Percentage, Time, true);

        //Json Conversion
        String json = gson.toJson(meal);

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

            //Execute our build statement
            Response response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Meal[] QueryThree(String Username, String Babyname) {

        //Create our url string
        String url = "https://parentalproject.azurewebsites.net/tables/Meal?$filter=Username%20eq%20'" +
                Username +
                "'%20and%20BabyName%20eq%20'" +
                Babyname +
                "'%20and%20PreviousMeal%20eq%20'true'&ZUMO-API-VERSION=2.0.0";


        //Build our request using our body, URL and function we want to use
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {

            //Create our clients response so it is ready to be executed
            Response response = client.newCall(request).execute();

            //Create our Gson so we can deserialize our JSON object
            Gson gson = new Gson();

            //Our meallist will be populated by the response body (Seralized Json) by mapping it to our meal class.
            MealList = gson.fromJson(response.body().string(), Meal[].class);

            //Return List of objects.
            return MealList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Meal[] UpdateRecord(String record) {
        //Create our url string
        String url = "https://parentalproject.azurewebsites.net/tables/Meal/" + record + "?ZUMO-API-VERSION=2.0.0";

        //Create our Gson object which we will use to serialize our json object
        Gson gson = new Gson();


        //Create our new meal class and populate it with the parameters passed in.
        Meal meal = new Meal();
        meal.setPreviousMeal(false);

        //Json Conversion
        String json = gson.toJson(meal);

        //Create our request body which we will patch to the database
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), json);

        //Build our request using our body, URL and function we want to use
        Request request = new Request.Builder()
                .url(url)
                .patch(body)
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
        return null;
    }
}