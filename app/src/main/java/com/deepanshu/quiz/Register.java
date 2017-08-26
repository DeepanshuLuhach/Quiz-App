package com.deepanshu.quiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Register extends AppCompatActivity {
    EditText mname,memail,mpassword;
    Button mregister;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mname = (EditText) findViewById(R.id.etname);
        memail = (EditText) findViewById(R.id.etemail);
        mpassword = (EditText) findViewById(R.id.etpass);

        mregister = (Button) findViewById(R.id.btnRegister);

        mregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Check_connectivity check = new Check_connectivity(Register.this);
                if(fieldsvalidation())
                {
                    if(check.getInternetStatus())
                    {
                        RegisterTask r = new RegisterTask();
                        r.execute(mname.getText().toString(),mpassword.getText().toString(),memail.getText().toString());
                    }
                    else{
                        Toast.makeText(Register.this,"Internet Connection Problem",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(Register.this,"Field cannot be left blank",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private boolean fieldsvalidation() {
        return !mname.getText().toString().equals("") && !mpassword.getText().toString().equals("") && !memail.getText().toString().equals("");
    }

    public class RegisterTask extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pd=new ProgressDialog(Register.this);
            pd.setTitle("Register");
            pd.setMessage("Registering...Please wait");
            pd.show();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            pd.dismiss();
            Toast.makeText(getBaseContext(),s,Toast.LENGTH_LONG).show();
            if ("valid".equals(s.trim())){
                Intent i = new Intent(Register.this, Login.class);
                startActivity(i);
                finish();
            }


        }

        @Override
        protected String doInBackground(String... strings) {

            try{
                String username = strings[0];
                String password = strings[1];
                String email = strings[2];
                String data= URLEncoder.encode("username","UTF-8") + "=" +
                        URLEncoder.encode(username,"UTF-8") + "&" +
                        URLEncoder.encode("password","UTF-8") + "=" +
                        URLEncoder.encode(password,"UTF-8") + "&" +
                        URLEncoder.encode("email","UTF-8") + "=" +
                        URLEncoder.encode(email,"UTF-8");

                URL url = new URL("https://contests.000webhostapp.com/php/register.php?"+data);
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
