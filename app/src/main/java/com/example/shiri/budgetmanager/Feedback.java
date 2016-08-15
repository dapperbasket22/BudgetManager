package com.example.shiri.budgetmanager;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Feedback extends AppCompatActivity {
    SQLiteDatabase db;
    EditText feedBackTitle, feedBackText;
    Button fsave;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        setTitle("FeedBack");

        db = openOrCreateDatabase("andro_db",MODE_PRIVATE,null);

        feedBackTitle = (EditText) findViewById(R.id.feedbackTitle);
        feedBackText = (EditText) findViewById(R.id.feedbackText);
        fsave = (Button) findViewById(R.id.feedbackSave);
        final String name = getIntent().getStringExtra("name");

        fsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = feedBackTitle.getText().toString();
                String b = feedBackText.getText().toString();
                if(a.equals("") || b.equals(""))
                    Toast.makeText(Feedback.this, "A field needs a review", Toast.LENGTH_SHORT).show();
                else{
                    ContentValues cv = new ContentValues();
                    cv.put("u_name",name);
                    cv.put("title",a);
                    cv.put("review",b);
                    try{
                        db.insertOrThrow("feedback",null,cv);
                        Toast.makeText(Feedback.this, "Thank you for the feedback!!", Toast.LENGTH_SHORT).show();
                        finish();
                    }catch (Exception e){
                        Toast.makeText(Feedback.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
