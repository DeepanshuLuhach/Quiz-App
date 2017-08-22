package com.deepanshu.quiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.deepanshu.quiz.Login.MyPREFERENCES;
import static com.deepanshu.quiz.Login.user_id;

public class Question extends AppCompatActivity {

    TextView mques;
    RadioButton mopA, mopB, mopC, mopD;
    Button mnext;
    List <QuestionDetails> quesList;
    int index = 0;
    int quescount = 0;
    int right = 0, wrong = 0, score = 0;
    QuestionDetails currentQuestion;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Bundle bundle = getIntent().getExtras();
        String qbid = bundle.getString("QBId");
        final String test_id = bundle.getString("TestId");
        final int positive = Integer.parseInt(bundle.getString("posMarks"));
        final int negative = Integer.parseInt(bundle.getString("negMarks"));

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String uid = sharedPreferences.getString(user_id,"0");

        DateFormat df = new SimpleDateFormat("dd MM yyyy, HH:mm:ss");
        final String startTime = df.format(Calendar.getInstance().getTime());
        Toast.makeText(Question.this,startTime,Toast.LENGTH_SHORT).show();

        FetchTask f = new FetchTask();
        f.execute(qbid);

        //get all questions in an list and set first question

        mques = (TextView) findViewById(R.id.tv_ques);
        mopA = (RadioButton) findViewById(R.id.rb_opA);
        mopB = (RadioButton) findViewById(R.id.rb_opB);
        mopC = (RadioButton) findViewById(R.id.rb_opC);
        mopD = (RadioButton) findViewById(R.id.rb_opD);
        mnext = (Button) findViewById(R.id.btn_next);
        mnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioGroup grp=(RadioGroup)findViewById(R.id.rg_options);
                RadioButton ans=(RadioButton)findViewById(grp.getCheckedRadioButtonId());
                //Toast.makeText(Question.this,ans.getText(),Toast.LENGTH_SHORT).show();

                if(ans == null)
                {
                    Toast.makeText(Question.this,"Skipped",Toast.LENGTH_SHORT).show();
                }
                else if(currentQuestion.getAnswer().equals(ans.getText())){
                    right++;
                }
                else{
                    wrong++;
                }
                assert ans != null;
                Toast.makeText(Question.this,currentQuestion.getAnswer()+" "+ans.getText(),Toast.LENGTH_SHORT).show();

                mopA.setChecked(false);
                mopB.setChecked(false);
                mopC.setChecked(false);
                mopD.setChecked(false);
                if(index < quescount){
                    currentQuestion=quesList.get(index);
                    setQuestionView();
                    if(index == quescount-1)
                    {
                        mnext.setText("Submit");
                    }
                    index++;
                }
                else{
                    score = positive*right - negative*wrong;
                    Toast.makeText(Question.this,right+" "+wrong+" = "+score,Toast.LENGTH_SHORT).show();

                    DateFormat df = new SimpleDateFormat("dd MM yyyy, HH:mm:ss");
                    String endTime = df.format(Calendar.getInstance().getTime());

                    DateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy, HH:mm:ss");
                    Date date1,date2;
                    long min = 0;
                    try {
                        date1 = simpleDateFormat.parse(startTime);
                        date2 = simpleDateFormat.parse(endTime);
                        long difference = date2.getTime() - date1.getTime();
                        long days = (int) (difference / (1000*60*60*24));
                        long hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
                        min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
                        hours = (hours < 0 ? -hours : hours);
                        Toast.makeText(Question.this,days+" "+hours+" "+min,Toast.LENGTH_SHORT).show();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    resultTask r = new resultTask();
                    r.execute(uid,test_id, String.valueOf(score), String.valueOf(min),startTime,endTime);
                }
            }
        });


    }

    private void setQuestionView() {
        mques.setText(currentQuestion.getQuestion());
        mopA.setText(currentQuestion.getA());
        mopB.setText(currentQuestion.getB());
        mopC.setText(currentQuestion.getC());
        mopD.setText(currentQuestion.getD());

    }

    private class FetchTask extends AsyncTask<String,String,String>
    {

        ProgressDialog pd;
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pd=new ProgressDialog(Question.this);
            pd.setTitle("Fetching Questions");
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            pd.dismiss();
            int flag = 1;
            if(s.trim().equals("")){
                Toast.makeText(Question.this,"No questions added to test.",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Question.this,StartTest.class));
                finish();
            }
            else if (!("error".equals(s.trim()))){

                //set the data list of questions
                //{"QUESTION_ID":"42","QUESTION":"first","A":"a","B":"b","C":"c","D":"d","CORRECT":"A"}
                try {
                    quesList = new ArrayList<>();
                    quesList.clear();
                    JSONArray ja = new JSONArray(s);
                    JSONObject jo;
                    quescount = ja.length();
                    if(quescount == 0){
                        Toast.makeText(Question.this,"No questions added in the test",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Question.this,StartTest.class));
                        finish();
                    }
                    for (int i = 0; i < ja.length(); i++) {
                        jo = ja.getJSONObject(i);
                        QuestionDetails qd = new QuestionDetails();
                        String temp = jo.getString("QUESTION_ID");
                        qd.setQid(temp);
                        temp = jo.getString("QUESTION");
                        qd.setQuestion(temp);
                        temp = jo.getString("A");
                        qd.setA(temp);
                        temp = jo.getString("B");
                        qd.setB(temp);
                        temp = jo.getString("C");
                        qd.setC(temp);
                        temp = jo.getString("D");
                        qd.setD(temp);
                        temp = jo.getString("CORRECT");
                        qd.setAnswer(temp);
                        quesList.add(qd);

                    }
                    currentQuestion = quesList.get(index);
                    setQuestionView();
                    index++;

                } catch (JSONException e) {
                    e.printStackTrace();
                    flag = 0;
                }
            }
            else
            {
                flag = 0;
                Toast.makeText(getBaseContext(),"Error : "+s,Toast.LENGTH_LONG).show();
            }
            if(flag == 0)
            {
                Toast.makeText(Question.this,"Error while fetching questions",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Question.this,StartTest.class));
                finish();
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            try{
                String qbid = strings[0];
                String data= URLEncoder.encode("qb_id","UTF-8") + "=" +
                        URLEncoder.encode(qbid,"UTF-8");

                URL url = new URL("https://contests.000webhostapp.com/php/get_questions.php?"+data);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                StringBuilder jsonData = new StringBuilder();

                //READ
                while ((line = bufferedReader.readLine()) != null) {
                    jsonData.append(line).append("\n");
                }

                System.out.println(jsonData.toString());
                return jsonData.toString();


            }catch(Exception e){
                e.printStackTrace();
            }

            return "error";
        }
    }

    private class resultTask extends AsyncTask<String,String,String>
    {
        ProgressDialog pd1;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pd1=new ProgressDialog(Question.this);
            pd1.setTitle("Saving Result");
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
                //Toast.makeText(Question.this,s,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Question.this, UserActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(Question.this,"Could not reach server.Try Again!!!",Toast.LENGTH_SHORT).show();
            }


        }

        @Override
        protected String doInBackground(String... strings) {

            try{
                String uid = strings[0];
                String test_id = strings[1];
                String score = strings[2];
                String duration = strings[3];//total time taken by the user
                String start_time = strings[4];
                String end_time = strings[5];
                String data=  URLEncoder.encode("user_id","UTF-8") + "=" +
                        URLEncoder.encode(uid,"UTF-8") + "&" +
                        URLEncoder.encode("test_id","UTF-8") + "=" +
                        URLEncoder.encode(test_id,"UTF-8") + "&" +
                        URLEncoder.encode("score","UTF-8") + "=" +
                        URLEncoder.encode(score,"UTF-8")+ "&" +
                        URLEncoder.encode("duration","UTF-8") + "=" +
                        URLEncoder.encode(duration,"UTF-8")+ "&" +
                        URLEncoder.encode("start_time","UTF-8") + "=" +
                        URLEncoder.encode(start_time,"UTF-8")+ "&" +
                        URLEncoder.encode("end_time","UTF-8") + "=" +
                        URLEncoder.encode(end_time,"UTF-8");

                URL url = new URL("https://contests.000webhostapp.com/php/submit_result.php?"+data);
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
    @Override
    public void onBackPressed() {
    }
}
