package com.deepanshu.quiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;


public class QBDetails extends AppCompatActivity {

    private EditText mname, mposmarks, mnegmarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qbdetails);
        mname = (EditText) findViewById(R.id.et_QBname);
        TextView mhead = (TextView) findViewById(R.id.head1);
        mposmarks = (EditText) findViewById(R.id.et_posMarks);
        mnegmarks = (EditText) findViewById(R.id.et_negMarks);
        Button maddques = (Button) findViewById(R.id.btn_addQuestions);

        String temp = "Enter the details of your Question Bank :";
        mhead.setText(temp);

        maddques.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save data here and go to Add_Question activity

                Check_connectivity check = new Check_connectivity(QBDetails.this);
                if(check.getInternetStatus())
                {
                    Intent i = new Intent(QBDetails.this, Add_Questions.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("Parent","QBDetails");
                    bundle.putString("Name",mname.getText().toString() );
                    bundle.putString("posM",mposmarks.getText().toString() );
                    bundle.putString("negM",mnegmarks.getText().toString() );
                    i.putExtras(bundle);
                    startActivity(i);
                    finish();
                }
                else{
                    Toast.makeText(QBDetails.this,"Internet Connection Problem",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
