package com.example.shiri.budgetmanager;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    SQLiteDatabase db;
    EditText regName, regEmail, regAddress, regContact, regUname, regPassword;
    Button regConfBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Initialize SQLite
        db = openOrCreateDatabase("andro_db", MODE_PRIVATE, null);

        //Initialize edittext and button
        regName = (EditText) findViewById(R.id.regName);
        regEmail = (EditText) findViewById(R.id.regEmail);
        regAddress = (EditText) findViewById(R.id.regAddress);
        regContact = (EditText) findViewById(R.id.regContact);
        regUname = (EditText) findViewById(R.id.regUname);
        regPassword = (EditText) findViewById(R.id.regPassword);

        regConfBtn = (Button) findViewById(R.id.regConfBtn);

        //OnClick
        regConfBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.regConfBtn:
                String name = regName.getText().toString().trim(),
                        email = regEmail.getText().toString().trim(),
                        contact = regContact.getText().toString(),
                        address = regAddress.getText().toString(),
                        uname = regUname.getText().toString().trim(),
                        password = regPassword.getText().toString();

                // For extraordinary people
                if(name.equals("") || email.equals("") || contact.equals("") || address.equals("")
                        || uname.equals("") || password.equals("")){
                    Toast.makeText(getApplicationContext(),"Field can't be empty",Toast.LENGTH_SHORT).show();
                    break;
                }

                //Values to be inserted
                ContentValues regVal = new ContentValues();
                regVal.put("name",name);
                regVal.put("email",email);
                regVal.put("contact",contact);
                regVal.put("address",address);
                regVal.put("u_name",uname);
                regVal.put("password",password);

                //Inserting values
                try{
                    db.insertOrThrow("user_detail",null,regVal);
                    finish();
                    Intent intent = new Intent(getApplicationContext(),Home.class);
                    intent.putExtra("name",regUname.getText().toString());
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(RegisterActivity.this, "Sorry!! Can't Register", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
