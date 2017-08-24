package com.deepanshu.quiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserActivity extends AppCompatActivity {
    Button mswtich_admin, mstartTest, mshowResult;
    EditText mtestid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        mswtich_admin = (Button) findViewById(R.id.switch_to_admin);
        mstartTest = (Button) findViewById(R.id.btn_start);
        mshowResult = (Button) findViewById(R.id.btn_viewMyResult);
        mtestid = (EditText) findViewById(R.id.et_testid);

        mswtich_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent g = new Intent(UserActivity.this,MainActivity.class);
                g.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(g);
                finish();
            }
        });

        mstartTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check_connectivity check = new Check_connectivity(UserActivity.this);
                String id = mtestid.getText().toString();
                if(check.getInternetStatus() && id != "")
                {
                    Intent i = new Intent(UserActivity.this, StartTest.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("TestId", id);
                    i.putExtras(bundle);
                    startActivity(i);
                    finish();
                }
                else{
                    Toast.makeText(UserActivity.this,"Internet Connection Problem",Toast.LENGTH_LONG).show();
                }
            }
        });
        
    }
}
