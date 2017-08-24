package com.deepanshu.quiz;

import android.app.ProgressDialog;
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
import static com.deepanshu.quiz.Login.user_id;

public class Add_Questions extends AppCompatActivity {

    private String selectedOption;
    private EditText mques,mopA,mopB,mopC,mopD;
    public String quesB_id;
    int flag, submitflag;
    String calling;
    SharedPreferences sharedPreferences;
    String answer = "";
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__questions);

        //Saving question Bank details
        Bundle bundle = getIntent().getExtras();
        calling = bundle.getString("Parent");
        Toast.makeText(Add_Questions.this,calling,Toast.LENGTH_SHORT).show();
        if(calling == "QBDetails"){
            String qbName = bundle.getString("Name");
            String posMarks = bundle.getString("posM");
            String negMarks = bundle.getString("negM");
            flag = 1;
            SaveTask s = new SaveTask();
            s.execute(qbName, posMarks, negMarks);
            if(flag == 0)
            {
                Toast.makeText(Add_Questions.this,"Could not save the Quesion Bank details",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Add_Questions.this,QBDetails.class));
                finish();
            }
        }
        else {
            quesB_id = bundle.getString("qbid");
            Toast.makeText(Add_Questions.this,quesB_id,Toast.LENGTH_SHORT).show();
        }

        submitflag = 0;


        mques = (EditText) findViewById(R.id.et_enterQuestion);
        mopA = (EditText) findViewById(R.id.et_optionA);
        mopB = (EditText) findViewById(R.id.et_optionB);
        mopC = (EditText) findViewById(R.id.et_optionC);
        mopD = (EditText) findViewById(R.id.et_optionD);
        Button madd = (Button) findViewById(R.id.btn_add_question);
        ///madd.setEnabled(false);
        Button mcancel = (Button) findViewById(R.id.btn_cancel_question);
        Button msubmit = (Button) findViewById(R.id.btn_submitquestionbnk);

        //Populating Spinner with options
        spinner = (Spinner) findViewById(R.id.spinner_selectOption);
        adapter = ArrayAdapter.createFromResource(this, R.array.select_correct_option, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //addListenertoSpinner();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> options, View view, int pos, long l) {
                selectedOption = options.getItemAtPosition(pos).toString();
                switch (selectedOption) {
                    case "A":
                        answer = mopA.getText().toString();
                        break;
                    case "B":
                        answer = mopB.getText().toString();
                        break;
                    case "C":
                        answer = mopC.getText().toString();
                        break;
                    case "D":
                        answer = mopD.getText().toString();
                        break;
                }
                Toast.makeText(getBaseContext(),answer,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> options) {
                selectedOption = options.getItemAtPosition(0).toString();
                switch (selectedOption) {
                    case "A":
                        answer = mopA.getText().toString();
                        break;
                    case "B":
                        answer = mopB.getText().toString();
                        break;
                    case "C":
                        answer = mopC.getText().toString();
                        break;
                    case "D":
                        answer = mopD.getText().toString();
                        break;
                }
                Toast.makeText(getBaseContext(),answer,Toast.LENGTH_SHORT).show();
            }
        });

        /*if(fieldsvalidation())
            madd.setEnabled(true);*/

        madd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Check_connectivity check = new Check_connectivity(Add_Questions.this);
                if(fieldsvalidation())
                {
                    if(check.getInternetStatus())
                    {
                        insertTask i = new insertTask();
                        i.execute(mques.getText().toString(),mopA.getText().toString(),mopB.getText().toString(),mopC.getText().toString(),mopD.getText().toString());
                    }
                    else{
                        Toast.makeText(Add_Questions.this,"Internet Connection Problem",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(Add_Questions.this,"Field cannot be left blank",Toast.LENGTH_LONG).show();
                }

            }

        });
        mcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(calling == "QBDetails"){
                    startActivity(new Intent(Add_Questions.this,MainActivity.class));
                    finish();
                }
                else {
                    startActivity(new Intent(Add_Questions.this,DisplayQB.class));
                    finish();
                }
            }
        });
        msubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Check_connectivity check = new Check_connectivity(Add_Questions.this);
                if(fieldsvalidation())
                {
                    if(check.getInternetStatus())
                    {
                        insertTask i = new insertTask();
                        i.execute(mques.getText().toString(),mopA.getText().toString(),mopB.getText().toString(),mopC.getText().toString(),mopD.getText().toString());
                        submitflag = 1;
                    }
                    else{
                        Toast.makeText(Add_Questions.this,"Internet Connection Problem",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(Add_Questions.this,"Field cannot be left blank",Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    private boolean fieldsvalidation() {
        return mques.getText().toString() != "" && mopA.getText().toString() != "" && mopB.getText().toString() != "" && mopC.getText().toString() != "" && mopD.getText().toString() != "";
    }
/*
    private void addListenertoSpinner() {
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
    }*/

    private class insertTask extends AsyncTask<String,String,String>
    {
        ProgressDialog pd1;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pd1=new ProgressDialog(Add_Questions.this);
            pd1.setTitle("Saving Question");
            pd1.setMessage("Please wait...");
            pd1.setCancelable(false);
            pd1.setCanceledOnTouchOutside(false);
            System.out.println("Inside Pre-execute");
            pd1.show();
        }

        @Override
        protected void onPostExecute(String s) {
            pd1.dismiss();
            System.out.println("Inside Post-execute");
            super.onPostExecute(s);
            if ("valid".equals(s.trim())){
                //Toast.makeText(Add_Questions.this,s,Toast.LENGTH_SHORT).show();
                mques.getText().clear();
                mopA.getText().clear();
                mopB.getText().clear();
                mopC.getText().clear();
                mopD.getText().clear();
                spinner.setAdapter(null);
                spinner.setAdapter(adapter);
                spinner.setSelection(0);
                if(submitflag == 1)
                {
                    if(calling == "QBDetails"){
                        startActivity(new Intent(Add_Questions.this,MainActivity.class));
                        finish();
                    }
                    else {
                        startActivity(new Intent(Add_Questions.this,DisplayQB.class));
                        finish();
                    }

                }
            }
            else {
                Toast.makeText(Add_Questions.this,"Question could not be saved.",Toast.LENGTH_SHORT).show();
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
                        URLEncoder.encode(answer,"UTF-8");

                URL url = new URL("https://contests.000webhostapp.com/php/add_question.php?"+data);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String result;

                result = bufferedReader.readLine();
                System.out.println("Inside Do-in-Background");
                return result;


            }catch(Exception e){
                e.printStackTrace();
            }

            return "not valid";
        }
    }


    private class SaveTask extends AsyncTask<String,String,String>
    {

        ProgressDialog pd;
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pd=new ProgressDialog(Add_Questions.this);
            pd.setTitle("Saving Question Bank Details");
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            pd.dismiss();

            if (!("error".equals(s.trim()))){

                quesB_id = s.trim();
                //Toast.makeText(getBaseContext(),quesB_id,Toast.LENGTH_SHORT).show();
                mques.getText().clear();
                mopA.getText().clear();
                mopB.getText().clear();
                mopC.getText().clear();
                mopD.getText().clear();
            }
            else
            {
                Toast.makeText(getBaseContext(),"Check your internet connectivity!!!",Toast.LENGTH_LONG).show();
                flag = 0;
            }
            if(flag == 0)
            {
                Toast.makeText(Add_Questions.this,"Could not save the Question Bank details",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Add_Questions.this,QBDetails.class));
                finish();
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            try{
                String qbname = strings[0];
                String pmarks = strings[1];
                String nmarks = strings[2];
                sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                String uid = sharedPreferences.getString(user_id,"0");
                String data= URLEncoder.encode("qbname","UTF-8") + "=" +
                        URLEncoder.encode(qbname,"UTF-8") + "&" +
                        URLEncoder.encode("user_id","UTF-8") + "=" +
                        URLEncoder.encode(uid,"UTF-8") + "&" +
                        URLEncoder.encode("pmarks","UTF-8") + "=" +
                        URLEncoder.encode(pmarks,"UTF-8") + "&" +
                        URLEncoder.encode("nmarks","UTF-8") + "=" +
                        URLEncoder.encode(nmarks,"UTF-8");

                URL url = new URL("https://contests.000webhostapp.com/php/addtoqb.php?"+data);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String result;

                result = bufferedReader.readLine();
                System.out.println("result "+result);
                if(result == null)
                    return "error";
                return result;



            }catch(Exception e){
                e.printStackTrace();
            }

            return "error";
        }
    }

}
