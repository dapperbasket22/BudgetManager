package com.example.shiri.budgetmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread timer = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    startActivity(new Intent(getApplicationContext(), AppActivity.class));
                }
            }
        });
        timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
