package com.deepanshu.quiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static com.deepanshu.quiz.Login.MyPREFERENCES;
import static com.deepanshu.quiz.Login.qb_id;

public class Add_Questions extends AppCompatActivity {

    private Spinner spinner;
    String selectedOption;
    private EditText mques,mopA,mopB,mopC,mopD;
    Button madd,mcancel,msubmit;
    String quesB_id;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__questions);

        mques = (EditText) findViewById(R.id.et_enterQuestion);
        mopA = (EditText) findViewById(R.id.et_optionA);
        mopB = (EditText) findViewById(R.id.et_optionB);
        mopC = (EditText) findViewById(R.id.et_optionC);
        mopD = (EditText) findViewById(R.id.et_optionD);
        madd = (Button) findViewById(R.id.btn_add_question);
        mcancel = (Button) findViewById(R.id.btn_cancel_question);
        msubmit = (Button) findViewById(R.id.btn_submitquestionbnk);

        spinner = (Spinner) findViewById(R.id.spinner_selectOption);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.select_correct_option, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        addListenertoSpinner();

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        quesB_id = sharedPreferences.getString(qb_id,"0");
        if(quesB_id == "0")
        {
            Toast.makeText(Add_Questions.this,"Invalid reference to question bank",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Add_Questions.this,MainActivity.class));
            finish();
        }

        madd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Check_connectivity check = new Check_connectivity(Add_Questions.this);
                if(check.getInternetStatus())
                {
                    insertTask i = new insertTask();
                    i.execute(mques.getText().toString(),mopA.getText().toString(),mopB.getText().toString(),mopC.getText().toString(),mopD.getText().toString());
                    mques.setText("");
                    mopA.setText("");
                    mopB.setText("");
                    mopC.setText("");
                    mopD.setText("");
                    startActivity(new Intent(Add_Questions.this,Add_Questions.class));
                    finish();
                }
                else{
                    Toast.makeText(Add_Questions.this,"Internet Connection Problem",Toast.LENGTH_LONG).show();
                }

            }
        });
        mcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Add_Questions.this,MainActivity.class));
                finish();
            }
        });
        msubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Check_connectivity check = new Check_connectivity(Add_Questions.this);
                if(check.getInternetStatus())
                {
                    insertTask i = new insertTask();
                    i.execute(mques.getText().toString(),mopA.getText().toString(),mopB.getText().toString(),mopC.getText().toString(),mopD.getText().toString());

                    sharedPreferences = getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(qb_id,"0");
                    editor.apply();
                    startActivity(new Intent(Add_Questions.this,MainActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(Add_Questions.this,"Internet Connection Problem",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void addListenertoSpinner() {
        spinner = (Spinner) findViewById(R.id.spinner_selectOption);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> options, View view, int pos, long l) {
                selectedOption = options.getItemAtPosition(pos).toString();
                Toast.makeText(getBaseContext(),selectedOption,Toast.LENGTH_SHORT);
            }

            @Override
            public void onNothingSelected(AdapterView<?> options) {
                selectedOption = options.getItemAtPosition(0).toString();
                Toast.makeText(getBaseContext(),selectedOption,Toast.LENGTH_SHORT);
            }
        });
    }

    public class insertTask extends AsyncTask<String,String,String>
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
                //Toast.makeText(getBaseContext(),s,Toast.LENGTH_LONG).show();
                if (!("not valid".equals(s.trim()))){
                    Toast.makeText(Add_Questions.this,s,Toast.LENGTH_SHORT).show();
                }
            }



        }

        @Override
        protected String doInBackground(String... strings) {

            try{
                String qname = strings[0];
                String opa = strings[1];
                String opb = strings[2];
                String opc = strings[3];
                String opd = strings[4];
                String data=  URLEncoder.encode("qbid","UTF-8") + "=" +
                        URLEncoder.encode(quesB_id,"UTF-8") + "&" +
                        URLEncoder.encode("question","UTF-8") + "=" +
                        URLEncoder.encode(qname,"UTF-8") + "&" +
                        URLEncoder.encode("optionA","UTF-8") + "=" +
                        URLEncoder.encode(opa,"UTF-8")+ "&" +
                        URLEncoder.encode("optionB","UTF-8") + "=" +
                        URLEncoder.encode(opb,"UTF-8")+ "&" +
                        URLEncoder.encode("optionC","UTF-8") + "=" +
                        URLEncoder.encode(opc,"UTF-8")+ "&" +
                        URLEncoder.encode("optionD","UTF-8") + "=" +
                        URLEncoder.encode(opd,"UTF-8")+ "&" +
                        URLEncoder.encode("correct","UTF-8") + "=" +
                        URLEncoder.encode(selectedOption,"UTF-8");

                URL url = new URL("https://contests.000webhostapp.com/php/add_question.php?"+data);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String result;

                result = bufferedReader.readLine();
                //Toast.makeText(Add_Questions.this,url.toString(),Toast.LENGTH_SHORT);
                return result;

            }catch(Exception e){
                e.printStackTrace();
            }

            return null;
        }
    }
}
