package com.example.shiri.budgetmanager;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowBudgetDetail extends AppCompatActivity implements View.OnClickListener {
    SQLiteDatabase db;
    Button addBudget, delBudget;
    EditText bugEntry;
    GridView budgetGrid;
    String uname, bname;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_budget_detail);
        setTitle("Budget Details");

        db = openOrCreateDatabase("andro_db",MODE_PRIVATE,null);

        addBudget = (Button) findViewById(R.id.addBudget);
        delBudget = (Button) findViewById(R.id.delBudget);
        budgetGrid = (GridView) findViewById(R.id.budgetGrid);

        Intent intent = getIntent();
        bname = intent.getStringExtra("bname");
        uname = intent.getStringExtra("uname");

        updateList();

        addBudget.setOnClickListener(this);
        delBudget.setOnClickListener(this);
    }

    private void updateList() {
        ArrayList<String> al = new ArrayList<>();
        Cursor sc = db.rawQuery("select * from '"+bname+"' where u_name='"+ uname +"'",null);
        sc.moveToFirst();
        int sum = 0;
        if(sc.getCount()>0){
            while (!sc.isAfterLast()){
                sum += Integer.parseInt(sc.getString(1));
                al.add(sc.getString(2));
                al.add(sc.getString(1));
                sc.moveToNext();
            }
        }
        al.add("Total");
        al.add(""+sum);
        sc.close();
        budgetGrid.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,al));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addBudget:
                Intent intent = new Intent(getApplicationContext(),CreateBudgetDetail.class);
                intent.putExtra("uname",uname);
                intent.putExtra("bname",bname);
                startActivity(intent);
                break;
            case R.id.delBudget:
                dialog = new Dialog(this);
                dialog.getWindow().setTitle("Delete Budget Entry");
                dialog.setContentView(R.layout.newbug);
                dialog.show();

                Button canBtn = (Button) dialog.findViewById(R.id.canBtn);
                Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
                bugEntry = (EditText) dialog.findViewById(R.id.bugName);
                bugEntry.setHint("Enter budget entry to delete");

                canBtn.setOnClickListener(this);
                okBtn.setOnClickListener(this);
                break;
            case R.id.canBtn:
                dialog.dismiss();
                break;
            case R.id.okBtn:
                String entName = bugEntry.getText().toString().trim();
                if(entName.equals("")){
                    Toast.makeText(ShowBudgetDetail.this, "A field needs a valid name", Toast.LENGTH_SHORT).show();
                    break;
                }
                try{
                    db.execSQL("delete from '"+bname+"' where reason='"+entName+"'");
                    updateList();
                    Toast.makeText(ShowBudgetDetail.this, entName+" removed!!", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(ShowBudgetDetail.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                break;
            default:
                Toast.makeText(ShowBudgetDetail.this, "You broke this too!!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent into = new Intent(getApplicationContext(), Home.class);
        into.putExtra("name",uname);
        startActivity(into);
    }
}
