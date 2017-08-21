package com.deepanshu.quiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.deepanshu.quiz.Login.MyPREFERENCES;
import static com.deepanshu.quiz.Login.user_id;

public class MainActivity extends AppCompatActivity {

    private TextView user_display;
    SharedPreferences sharedPreferences;
    Button mcreateQB, meditQB, mcreateTest;
    Button mviewTest;
    Button mswtich_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String uid = sharedPreferences.getString(user_id,"0");
        if(uid == "0")
        {
            logout();
        }
       /* user_display = (TextView) findViewById(R.id.display_user);
        user_display.setText(uid); */

        mcreateQB = (Button) findViewById(R.id.btn_createQB);
        mcreateTest = (Button) findViewById(R.id.btn_createTest);
        meditQB = (Button) findViewById(R.id.btn_editQB);
        mviewTest = (Button) findViewById(R.id.btn_viewTest);
        mswtich_user = (Button) findViewById(R.id.switch_to_user);

        mswtich_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent g = new Intent(MainActivity.this,UserActivity.class);
                startActivity(g);
                finish();
            }
        });

        mcreateQB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent g = new Intent(MainActivity.this,QBDetails.class);
                startActivity(g);
            }
        });

        meditQB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gb = new Intent(MainActivity.this,DisplayQB.class);
                startActivity(gb);
            }
        });

        mcreateTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,TestDetails.class));
            }
        });

        mviewTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gb = new Intent(MainActivity.this,DisplayTest.class);
                startActivity(gb);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
       if(item.getItemId() == R.id.action_logout){
            logout();
           return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout(){
        SharedPreferences myPrefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(MainActivity.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

        }


}
