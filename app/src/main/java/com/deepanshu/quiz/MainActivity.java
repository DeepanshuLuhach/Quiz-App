package com.deepanshu.quiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String uid = sharedPreferences.getString(user_id,"0");
        user_display = (TextView) findViewById(R.id.display_user);
        user_display.setText(uid);

        mcreateQB = (Button) findViewById(R.id.btn_createQB);
        mcreateTest = (Button) findViewById(R.id.btn_createTest);

        mcreateQB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent g = new Intent(MainActivity.this,QBDetails.class);
                startActivity(g);
            }
        });

        mcreateTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,TestDetails.class));
            }
        });

    }
}
