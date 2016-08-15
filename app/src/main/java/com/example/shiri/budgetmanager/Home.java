package com.example.shiri.budgetmanager;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Home extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AbsListView.MultiChoiceModeListener {
    SQLiteDatabase db;
    ListView budgetList;
    Dialog dialog;
    EditText bugName = null;
    Intent intent;
    ArrayAdapter<String> bugAdapt;
    String uname;

    @Override
    protected void onResume() {
        super.onResume();
        viewBudgetList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = openOrCreateDatabase("andro_db",MODE_PRIVATE,null);
        intent = getIntent();
        uname = intent.getStringExtra("name");
        budgetList = (ListView) findViewById(R.id.budgetList);
        setTitle("Home (" + uname+")");
        viewBudgetList();

        budgetList.setOnItemClickListener(this);
        budgetList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        budgetList.setMultiChoiceModeListener(this);
    }

    private void viewBudgetList() {
        //get budget list
        Cursor csr;
        ArrayList<String> al = new ArrayList<>();

        if(uname.equals("Admin")){
            csr = db.rawQuery("select * from budget",null);

        } else{
            csr = db.rawQuery("select * from budget where u_name='"+ uname +"'",null);
        }

        csr.moveToFirst();
        if(csr.getCount() > 0){
            while(!csr.isAfterLast()) {
                al.add(csr.getString(2));
                csr.moveToNext();
            }
        }
        csr.close();
        //set budget
        bugAdapt = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, al);
        budgetList.setAdapter(bugAdapt);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.createBud:
                createBugDialog();
                break;
            case R.id.userLogout:
                finish();
                break;
            case R.id.userSetting:
                Intent setIntent = new Intent(getApplicationContext(),UserSettings.class);
                finish();
                setIntent.putExtra("name",uname);
                startActivity(setIntent);
                break;
            case R.id.userFeedback:
                Intent feedIntent = new Intent(getApplicationContext(),Feedback.class);
                feedIntent.putExtra("name",uname);
                startActivity(feedIntent);
                break;
            default:
                Toast.makeText(Home.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createBugDialog() {
        dialog = new Dialog(this);
        dialog.getWindow().setTitle("Create Budget");
        dialog.setContentView(R.layout.newbug);
        dialog.show();

        Button canBtn = (Button) dialog.findViewById(R.id.canBtn);
        Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
        bugName = (EditText) dialog.findViewById(R.id.bugName);

        canBtn.setOnClickListener(this);
        okBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.canBtn:
                dialog.dismiss();
                break;
            case R.id.okBtn:
                // Organize required values
                ContentValues cs = new ContentValues();
                String bname = bugName.getText().toString().replace(' ','_');
                if(bname.equals("")){
                    Toast.makeText(Home.this, "A budget needs a name", Toast.LENGTH_SHORT).show();
                    break;
                }

                // Main stuff
                cs.put("u_name",uname);
                cs.put("budgetName",bname);
                try{
                    db.insertOrThrow("budget",null,cs);
                    Intent bugin = new Intent(getApplicationContext(), CreateBudgetDetail.class);
                    // Reducing work for other activity
                    bugin.putExtra("uname",uname);
                    bugin.putExtra("bname",bname);
                    startActivity(bugin);
                    finish();
                    Toast.makeText(Home.this, "Budget Created", Toast.LENGTH_SHORT).show();
                }catch(Exception e){
                    Toast.makeText(Home.this, "Failed to Create Budget", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                break;
            default:
                Toast.makeText(Home.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String temp = adapterView.getItemAtPosition(i).toString();
        finish();
        Intent intent = new Intent(getApplicationContext(),ShowBudgetDetail.class);
        intent.putExtra("uname",uname);
        intent.putExtra("bname",temp);
        startActivity(intent);
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater inflater = actionMode.getMenuInflater();
        inflater.inflate(R.menu.longpre,menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.remItem:
                SparseBooleanArray selected = budgetList.getCheckedItemPositions();
                for (int i=0; i<selected.size();i++){
                    if(selected.valueAt(i)){
                        String temp = bugAdapt.getItem(selected.keyAt(i)).toString();
                        try{
                            db.execSQL("drop table '"+temp+"'");
                            db.delete("budget","budgetName='"+temp+"'",null);
                            Toast.makeText(Home.this, "Removed budget", Toast.LENGTH_SHORT).show();
                        }catch (Exception e){
                            Toast.makeText(Home.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                actionMode.finish();
                viewBudgetList();
                return true;
            default:
                Toast.makeText(Home.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {

    }
}
