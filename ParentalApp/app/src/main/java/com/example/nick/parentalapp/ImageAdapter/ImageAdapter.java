package com.example.nick.parentalapp.ImageAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nick.parentalapp.Child_Selection_Activity;
import com.example.nick.parentalapp.Database_Models.Baby;
import com.example.nick.parentalapp.Dialog_Controllers.Dialog_Controller;
import com.example.nick.parentalapp.R;

import java.util.List;

/**
 * Created by Nick on 04/01/2018.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    private Baby[] BabyList;

    public ImageAdapter(Context c, Baby[] Babys) {
        //Pass in our image list and context for creation of the buttons
        mContext = c;
        BabyList = Babys;
    }

    public int getCount() {
        //Discover the length of the babylist passed in
        return BabyList.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        //Create our view object
        View grid;
        //Inflate our view object
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //If context view doesn't exist, we create it, otherwise reuse it.
        if (convertView == null) {
            //Match our grid to our grid model
            grid = inflater.inflate(R.layout.activity_grid, null);
            //Bind the button to an object ID
            Button button = (Button) grid.findViewById(R.id.gridview_button);
            //Set the text of the button to the name of the baby in the current position in array
            button.setText(BabyList[position].getBabyName());
            //When clicked, we want to load a dialog box which will allow the user to select an option
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog_Controller DialogBox = new Dialog_Controller();
                    DialogBox.CreateOptionsBox(mContext, BabyList[position].getUsername(), BabyList[position].getBabyName() );
                }
            });
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}