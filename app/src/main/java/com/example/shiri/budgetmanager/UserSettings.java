package com.example.shiri.budgetmanager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class UserSettings extends AppCompatActivity {
    SQLiteDatabase db;
    EditText udtName, udtEmail, udtAddress, udtContact, udtUname, udtPassword;
    String name, email, address, contact, uname, password;
    Button udtBtn;
    LinearLayout layset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("User Settings");
        layset = (LinearLayout) findViewById(R.id.layset);
        layset.setBackground(getResources().getDrawable(R.drawable.imgset));

        //Initialize SQLite
        db = openOrCreateDatabase("andro_db", MODE_PRIVATE, null);

        //Initialize edittext and button
        udtName = (EditText) findViewById(R.id.regName);
        udtEmail = (EditText) findViewById(R.id.regEmail);
        udtAddress = (EditText) findViewById(R.id.regAddress);
        udtContact = (EditText) findViewById(R.id.regContact);
        udtUname = (EditText) findViewById(R.id.regUname);
        udtPassword = (EditText) findViewById(R.id.regPassword);

        udtBtn = (Button) findViewById(R.id.regConfBtn);
        udtBtn.setText("Update");

        //For stml people
        final String temp = getIntent().getStringExtra("name");
        Cursor scv = db.rawQuery("select * from user_detail where u_name='"+ temp +"'",null);
        scv.moveToFirst();
        if(scv.getCount()>0){
            udtName.setText(scv.getString(0));
            udtEmail.setText(scv.getString(1));
            udtAddress.setText(scv.getString(2));
            udtContact.setText(scv.getString(3));
            udtUname.setText(scv.getString(4));
            udtPassword.setText(scv.getString(5));
        }
        scv.close();
        //The Real Game
        udtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = udtName.getText().toString();
                email = udtEmail.getText().toString();
                address = udtAddress.getText().toString();
                contact = udtContact.getText().toString();
                uname = udtUname.getText().toString();
                password = udtPassword.getText().toString();
                if(name.equals("") || email.equals("") || address.equals("") || contact.equals("") ||
                        uname.equals("") || password.equals(""))
                    Toast.makeText(UserSettings.this, "A field cant be empty", Toast.LENGTH_SHORT).show();
                else{
                    ContentValues cv = new ContentValues();
                    cv.put("name",name);
                    cv.put("email",email);
                    cv.put("address",address);
                    cv.put("contact",contact);
                    cv.put("u_name",uname);
                    cv.put("password",password);
                    ContentValues cv2 = new ContentValues();
                    cv2.put("u_name",uname);
                    try{
                        db.update("user_detail",cv,"u_name='"+ temp+"'",null);
                        Cursor cs3 = db.rawQuery("select * from budget where u_name='"+temp+"'",null);
                        ArrayList<String> al= new ArrayList<>();
                        cs3.moveToFirst();
                        if(cs3.getCount()>0){
                            while (!cs3.isAfterLast()) {
                                al.add(cs3.getString(2));
                                cs3.moveToNext();
                            }
                        }
                        cs3.close();
                        for (String x : al){
                            db.update(x,cv2,"u_name='"+temp+"'",null);
                        }
                        db.update("budget",cv2,"u_name='"+temp+"'",null);
                        db.update("feedback",cv2,"u_name='"+temp+"'",null);
                        finish();
                        Toast.makeText(UserSettings.this, "User Settings Updated!! Login Again", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        finish();
                        Toast.makeText(UserSettings.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        Toast.makeText(UserSettings.this, "Please Login Again!", Toast.LENGTH_SHORT).show();
    }
}
