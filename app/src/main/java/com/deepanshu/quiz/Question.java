package com.deepanshu.quiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class Question extends AppCompatActivity {

    TextView mques;
    RadioButton mopA, mopB, mopC, mopD;
    Button mnext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Bundle bundle = getIntent().getExtras();
        String qbid = bundle.getString("QBId");

        //get all questions in an list and set first question

        mques = (TextView) findViewById(R.id.tv_ques);
        mopA = (RadioButton) findViewById(R.id.rb_opA);
        mopB = (RadioButton) findViewById(R.id.rb_opB);
        mopC = (RadioButton) findViewById(R.id.rb_opC);
        mopD = (RadioButton) findViewById(R.id.rb_opD);
        mnext = (Button) findViewById(R.id.btn_next);



    }
}
