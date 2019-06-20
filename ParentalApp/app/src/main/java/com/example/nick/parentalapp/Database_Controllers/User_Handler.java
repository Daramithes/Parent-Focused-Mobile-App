package com.example.nick.parentalapp.Database_Controllers;

import android.os.AsyncTask;
import android.util.Log;

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
 * Created by Nick on 03/01/2018.
 */

public class User_Handler extends AsyncTask<String, String, User[]> {

    //Create our OkhttpClient
    private OkHttpClient client = new OkHttpClient();

    //Create our list of user classes which will be populated by database
    private User[] UserList;
    @Override

    //Run Asynchronously in the background of the app.
    protected User[] doInBackground(String... params) {

        //Based on what the first parameter is, run that function
        if(Objects.equals(params[0], "Query")){
            //Populate userlist based on the result of our function
            UserList = QueryUser(params[1]);
        }
        if(Objects.equals(params[0], "Add")){
            //Populate userlist based on the result of our function
            UserList = AddUser(params[1], params[2],params[3],params[4], params[5]);
        }
        if(Objects.equals(params[0], "QueryLogin")){
            //Populate userlist based on the result of our function
            UserList = QueryLogin(params[1], params[2]);
        }
        return UserList;
    }

    @Override
    protected void onPostExecute(User[] s) {
        super.onPostExecute(s);
    }

    private User[] QueryUser(String Username){
        //Create our url string which when accessed will return json
        String url = "https://parentalproject.azurewebsites.net/tables/Users?$filter=Username%20eq%20'" +
                Username +
                "'&ZUMO-API-VERSION=2.0.0";

        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            Gson gson = new Gson();
            UserList = gson.fromJson(response.body().string(), User[].class);
            return UserList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private User[] QueryLogin(String Username, String Password) {
        //Create our url string which when accessed will return json
        String url = "https://parentalproject.azurewebsites.net/tables/Users?$filter=Username%20eq%20'" +
                Username +
                "'&ZUMO-API-VERSION=2.0.0";

        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            Gson gson = new Gson();
            UserList = gson.fromJson(response.body().string(), User[].class);
            return UserList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private User[] AddUser(String Username, String FirstName, String LastName, String Email, String Password) {
        //Create our url string
        String url = "https://parentalproject.azurewebsites.net/tables/Users/?ZUMO-API-VERSION=2.0.0";
        //Create our Gson object which we will use to serialize our json object
        Gson gson = new Gson();

        //Create our new user class and populate it with the parameters passed in.
        User user = new User();
        user.CreateNewUser(Username, FirstName, LastName, Email, Password);

        //Json Conversion
        String json = gson.toJson(user);

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
}
