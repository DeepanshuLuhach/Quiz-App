package com.deepanshu.quiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static com.deepanshu.quiz.Login.MyPREFERENCES;
import static com.deepanshu.quiz.Login.user_id;
import static java.lang.Boolean.TRUE;

public class TestDetails extends AppCompatActivity {

    EditText mname, mtopic, mduration;
    Button msave, mcancel;
    SharedPreferences sharedPreferences;
    CheckBox monlineStatus;
    int startNow = 0;
    Spinner mspinner;
    String uid;
    public static String qbid; //select from the spinner

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_details);

        mname = (EditText) findViewById(R.id.et_testName);
        mtopic = (EditText) findViewById(R.id.et_topic);
        mduration = (EditText) findViewById(R.id.et_duration);
        monlineStatus = (CheckBox) findViewById(R.id.chk_OnlineStatus);
        msave = (Button) findViewById(R.id.btn_saveTest);
        mcancel = (Button) findViewById(R.id.btn_cancelTest);
        mspinner = (Spinner) findViewById(R.id.spinner_select_qb);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        uid = sharedPreferences.getString(user_id,"0");

        JsonParser_testDetails jsonParser = new JsonParser_testDetails(TestDetails.this,mspinner);
        jsonParser.execute(uid);



        msave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Check_connectivity check = new Check_connectivity(TestDetails.this);
                if(check.getInternetStatus())
                {
                    SaveTestTask s = new SaveTestTask();
                    s.execute(mname.getText().toString(),mtopic.getText().toString(),mduration.getText().toString());

                    Intent intent = new Intent(TestDetails.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(TestDetails.this,"Internet Connection Problem",Toast.LENGTH_LONG).show();
                }


            }
        });

        monlineStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b == TRUE)
                {
                    startNow = 1;
                }
                else {
                    startNow = 0;
                }
            }
        });

        mcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(TestDetails.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public class SaveTestTask extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            if(s.trim() == null){
                Toast.makeText(getBaseContext(),"Check your internet connectivity!!!",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getBaseContext(),s,Toast.LENGTH_LONG).show();
                if (!("not valid".equals(s.trim()))){

                }
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            try{

                String name = strings[0];
                String topic = strings[1];
                String duration = strings[2];
                String id = qbid;
                String data=  URLEncoder.encode("user_id","UTF-8") + "=" +
                        URLEncoder.encode(uid,"UTF-8") + "&" +
                        URLEncoder.encode("qb_id","UTF-8") + "=" +
                        URLEncoder.encode(id,"UTF-8") + "&" +
                        URLEncoder.encode("name","UTF-8") + "=" +
                        URLEncoder.encode(name,"UTF-8")+ "&" +
                        URLEncoder.encode("topic","UTF-8") + "=" +
                        URLEncoder.encode(topic,"UTF-8")+ "&" +
                        URLEncoder.encode("duration","UTF-8") + "=" +
                        URLEncoder.encode(duration,"UTF-8")+ "&" +
                        URLEncoder.encode("online","UTF-8") + "=" +
                        URLEncoder.encode(String.valueOf(startNow),"UTF-8");

                URL url = new URL("https://contests.000webhostapp.com/php/save_Test.php?"+data);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String result;

                result = bufferedReader.readLine();

                return result;

            }catch(Exception e){
                e.printStackTrace();
            }

            return null;
        }
    }
}
