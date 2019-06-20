package com.example.nick.parentalapp.Dialog_Controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.example.nick.parentalapp.Food_Activity;
import com.example.nick.parentalapp.Sleep_Activity;

/**
 * Created by Nick on 03/01/2018.
 */

public class Dialog_Controller {

    public void CreateNeutralDialog(Context context, String text){
        //Create our dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //Set our message to the parameter
        builder.setMessage(text);
        //Set our neautral button
        builder.setNeutralButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        //Create our object
        AlertDialog Alert = builder.create();
        //Finally Display to user
        Alert.show();
    }

    public void CreateOptionsBox(final Context context, final String Username, final String BabyName) {
        //Create our dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //Set our Message
        builder.setMessage("What would you like to do?");
        //Set our neautral button so that once the button is clicked, it loads activity.
        builder.setNeutralButton("Add a meal",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(context, Food_Activity.class);
                        intent.putExtra("Username", Username);
                        intent.putExtra("BabyName", BabyName);
                        context.startActivity(intent);
                    }
                });
        //Set our negative button so that once the button is clicked, it loads activity.
        builder.setNegativeButton("Add a sleep",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(context, Sleep_Activity.class);
                        intent.putExtra("Username", Username);
                        intent.putExtra("BabyName", BabyName);
                        context.startActivity(intent);
                    }
                });
        //Create our object
        AlertDialog Alert = builder.create();
        //Display to user
        Alert.show();
    }

    public void CreateClosingBox(final Context context, String text){
        //Create our dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //Set our message to the parameter
        builder.setMessage(text);
        //Set our neautral button so that once the ok button is clicked, it closes activity
        builder.setNeutralButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            ((Activity)context).finish();
                    }
                });
        //Create our object
        AlertDialog Alert = builder.create();
        //Display to user
        Alert.show();
    }
}
