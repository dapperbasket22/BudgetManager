package com.example.shiri.budgetmanager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AppActivity extends AppCompatActivity implements View.OnClickListener {
    SQLiteDatabase db;
    EditText logName, logPassword;
    Button logBtn, regBtn;
    Spinner userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        //Initialize and create tables
        db = openOrCreateDatabase("andro_db", MODE_PRIVATE, null);
        db.execSQL("create table if not exists user_detail(name varchar(20), email varchar(30) primary key, contact varchar(10), address varchar(50), u_name varchar(15) unique, password varchar(20))");
        db.execSQL("create table if not exists budget(budgetId integer primary key autoincrement not null, u_name varchar(15), budgetName varchar(20) unique)");
        db.execSQL("create table if not exists feedback(fid integer primary key autoincrement not null, u_name varchar(15),title varchar(10),review varchar(50))");

        userType = (Spinner) findViewById(R.id.userType);
        logName = (EditText) findViewById(R.id.logName);
        logPassword = (EditText) findViewById(R.id.logPassword);
        logBtn = (Button) findViewById(R.id.logBtn);
        regBtn = (Button) findViewById(R.id.regBtn);

        //Spinner code
        String[] type = {"Member", "Admin"};
        userType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                type));

        //onClick
        logBtn.setOnClickListener(this);
        regBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        logName.setText("");
        logPassword.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logBtn:
                //Get Values
                String userSelected = userType.getSelectedItem().toString();
                String uname = logName.getText().toString().trim();
                String password = logPassword.getText().toString();
                // For extraordinary people
                if(uname.equals("") || password.equals("")){
                    Toast.makeText(getApplicationContext(),"Field can't be empty",Toast.LENGTH_SHORT).show();
                    break;
                }
                //Login logic
                if(userSelected.equals("Admin")){
                    //For admin
                    if(uname.equals("admin") && password.equals("admin")){
                        Intent intent = new Intent(getApplicationContext(), AdminHome.class);
                        intent.putExtra("name","Admin");
                        startActivity(intent);
                    } else{
                        Toast.makeText(AppActivity.this, "Wrong UserName or Password", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    //For member
                    Cursor cr = db.rawQuery("select * from user_detail where u_name='"+ uname +"' and password = '"+ password +"'",null);
                    if (cr.getCount() > 0){
                        cr.moveToFirst();
                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        intent.putExtra("name", cr.getString(4));
                        startActivity(intent);
                    } else{
                        Toast.makeText(AppActivity.this, "Wrong UserName or Password", Toast.LENGTH_SHORT).show();
                    }
                    cr.close();
                }
                break;
            case R.id.regBtn:
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                break;
            default:
                Toast.makeText(AppActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
