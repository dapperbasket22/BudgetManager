package com.example.shiri.budgetmanager;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateBudgetDetail extends AppCompatActivity implements View.OnClickListener {
    SQLiteDatabase db;
    EditText budgetAmount, budgetReason;
    Button budgetSave;
    Intent intent;
    String uname, bname, amt, reason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_detail_create);
        setTitle("New Budget Entry");

        //Initialize db and other shit
        db = openOrCreateDatabase("andro_db", MODE_PRIVATE, null);
        budgetAmount = (EditText) findViewById(R.id.budgetAmt);
        budgetReason = (EditText) findViewById(R.id.bugReason);
        budgetSave = (Button) findViewById(R.id.budgetSave);

        //Create budget
        intent = getIntent();
        uname = intent.getStringExtra("uname");
        bname = intent.getStringExtra("bname");

        db.execSQL("create table if not exists '"+ bname +"'(u_name varchar(10), amount varchar(10), reason varchar(20) primary key)");

        budgetSave.setOnClickListener(this);
    }

    private boolean getVal() {
        //The Game Begins
        try{
            amt = budgetAmount.getText().toString();
            reason = budgetReason.getText().toString().trim();
            return !(reason.equals("") ||  amt.equals(""));
        }catch (Exception e){
            Toast.makeText(CreateBudgetDetail.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.budgetSave:
                //Creating new budget details
                if(getVal()){
                    ContentValues cbz = new ContentValues();
                    cbz.put("u_name",uname);
                    cbz.put("amount",amt);
                    cbz.put("reason",reason);
                    try{
                        db.insertOrThrow(bname,null,cbz);
                    }catch (Exception e){
                        Toast.makeText(CreateBudgetDetail.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        break;
                    }
                } else{
                    Toast.makeText(CreateBudgetDetail.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                    break;
                }
                finish();
                Intent intentN = new Intent(getApplicationContext(),ShowBudgetDetail.class);
                intentN.putExtra("bname",bname);
                intentN.putExtra("uname",uname);
                startActivity(intentN);
                break;
            default:
                Toast.makeText(CreateBudgetDetail.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
