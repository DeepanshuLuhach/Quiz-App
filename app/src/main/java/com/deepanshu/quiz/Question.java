package com.deepanshu.quiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.CountDownTimer;
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
    RadioButton mopA, mopB, mopC, mopD, ans, mnoOption;
    RadioGroup grp;
    Button mnext;
    List <QuestionDetails> quesList;
    int index = 0;
    int quescount = 0;
    int right = 0, wrong = 0, score = 0, skipped = 0, maxMarks;
    QuestionDetails currentQuestion;
    SharedPreferences sharedPreferences;
    private TextView timed;
    public double percent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Bundle bundle = getIntent().getExtras();
        String qbid = bundle.getString("QBId");
        final String test_id = bundle.getString("TestId");
        final int positive = Integer.parseInt(bundle.getString("posMarks"));
        final int negative = Integer.parseInt(bundle.getString("negMarks"));
        final int time = (Integer.parseInt(bundle.getString("mDuration"))*60);
        percent = 0.0;
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String uid = sharedPreferences.getString(user_id,"0");

        DateFormat df = new SimpleDateFormat("dd MM yyyy, HH:mm:ss");
        final String startTime = df.format(Calendar.getInstance().getTime());
        Toast.makeText(Question.this,startTime,Toast.LENGTH_SHORT).show();

        FetchTask f = new FetchTask();        //get all questions in an list and set first question
        f.execute(qbid);


        //timer

        timed = (TextView) findViewById(R.id.tx_timed);
        final long seconds =60;
        CountDownTimer countDownTimer= new CountDownTimer(time*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;

                long elapsedHours = millisUntilFinished / hoursInMilli;
                millisUntilFinished = millisUntilFinished % hoursInMilli;

                long elapsedMinutes = millisUntilFinished / minutesInMilli;
                millisUntilFinished = millisUntilFinished % minutesInMilli;

                long elapsedSeconds = millisUntilFinished / secondsInMilli;

                String yy = String.format("%02d:%02d:%02d", elapsedHours, elapsedMinutes,elapsedSeconds);
                timed.setText("Time Left: " + yy);
            }

            public void onFinish() {

                timed.setText("Test Finished");
                resultTask r = new resultTask();
                r.execute(uid,test_id, String.valueOf(score), String.valueOf(right), String.valueOf(wrong), String.valueOf(quescount), String.valueOf(time));
            }
        }.start();

        mques = (TextView) findViewById(R.id.tv_ques);
        mopA = (RadioButton) findViewById(R.id.rb_opA);
        mopB = (RadioButton) findViewById(R.id.rb_opB);
        mopC = (RadioButton) findViewById(R.id.rb_opC);
        mopD = (RadioButton) findViewById(R.id.rb_opD);
        mnoOption = (RadioButton) findViewById(R.id.rb_no);
        mnext = (Button) findViewById(R.id.btn_next);
        mnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grp=(RadioGroup)findViewById(R.id.rg_options);
                ans=(RadioButton)findViewById(grp.getCheckedRadioButtonId());
                //Toast.makeText(Question.this,ans.getText(),Toast.LENGTH_SHORT).show();

                if(ans == null || ans.getText().equals("noooo"))
                {
                    Toast.makeText(Question.this,"Skipped",Toast.LENGTH_SHORT).show();
                    skipped++;

                }
                else if(currentQuestion.getAnswer().equals(ans.getText()) && index <= quescount){
                    right++;
                }
                else if(!currentQuestion.getAnswer().equals(ans.getText()) && index <= quescount){
                    wrong++;
                    System.out.println("Right Answer = "+currentQuestion.getAnswer()+" Attempted Answer = "+ans.getText());
                }
//                System.out.println("Right Answer = "+currentQuestion.getAnswer()+" Attempted Answer = "+ans.getText());
                System.out.println("Right = "+right+" Wrong = "+wrong+" Skipped = "+(quescount-right-wrong));

                if(index < quescount){
                    currentQuestion=quesList.get(index);
                    setQuestionView();
//                    System.out.println(mopA.isChecked()+" "+mopB.isChecked()+" "+mopC.isChecked()+" "+mopD.isChecked()+" "+ans.isChecked());

                }
                else{
                    maxMarks = quescount*positive;
                    score = positive*right - negative*wrong;
                    if(score > 0)
                        percent= ((double)score/maxMarks)*100;

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
                    r.execute(uid,test_id, String.valueOf(score), String.valueOf(right), String.valueOf(wrong), String.valueOf(quescount), String.valueOf(min), String.valueOf(percent));
                }
            }
        });


    }

    private void setQuestionView() {
        //reset
        if(mopA.isChecked())
            mopA.setChecked(false);
        else if(mopB.isChecked())
            mopB.setChecked(false);
        else if(mopC.isChecked())
            mopC.setChecked(false);
        else if(mopD.isChecked())
            mopD.setChecked(false);
        if (ans != null){
            ans.setChecked(false);
            ans = null;
        }
        mnoOption.setChecked(true);
        grp=(RadioGroup)findViewById(R.id.rg_options);
        ans=(RadioButton)findViewById(grp.getCheckedRadioButtonId());
        //setting question
        String question = String.valueOf(index + 1) + ". " + currentQuestion.getQuestion();
        mques.setText(question);
        mopA.setText(currentQuestion.getA());
        mopB.setText(currentQuestion.getB());
        mopC.setText(currentQuestion.getC());
        mopD.setText(currentQuestion.getD());
        if(index == quescount-1)
        {
            mnext.setText("Submit");
        }
        index++;

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
            pd1.show();
        }

        @Override
        protected void onPostExecute(String s) {
            pd1.dismiss();
            super.onPostExecute(s);
            if ("valid".equals(s.trim())){
                //Toast.makeText(Question.this,s,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Question.this, TestResult.class);
                Bundle bundle = new Bundle();
                bundle.putString("Total", String.valueOf(quescount));
                bundle.putString("Correct", String.valueOf(right));
                bundle.putString("Incorrect", String.valueOf(wrong));
                bundle.putString("Score", String.valueOf(score));
                bundle.putString("Percentage", String.valueOf(percent));
                intent.putExtras(bundle);
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
                String right = strings[3];
                String wrong = strings[4];
                String total = strings[5];
                String duration = strings[6];//total time taken by the user
                String percentage = strings[7];
                String data=  URLEncoder.encode("user_id","UTF-8") + "=" +
                        URLEncoder.encode(uid,"UTF-8") + "&" +
                        URLEncoder.encode("test_id","UTF-8") + "=" +
                        URLEncoder.encode(test_id,"UTF-8") + "&" +
                        URLEncoder.encode("score","UTF-8") + "=" +
                        URLEncoder.encode(score,"UTF-8")+ "&" +
                        URLEncoder.encode("percentage","UTF-8") + "=" +
                        URLEncoder.encode(percentage,"UTF-8")+ "&" +
                        URLEncoder.encode("right","UTF-8") + "=" +
                        URLEncoder.encode(right,"UTF-8")+ "&" +
                        URLEncoder.encode("wrong","UTF-8") + "=" +
                        URLEncoder.encode(wrong,"UTF-8")+ "&" +
                        URLEncoder.encode("total","UTF-8") + "=" +
                        URLEncoder.encode(total,"UTF-8")+ "&" +
                        URLEncoder.encode("duration","UTF-8") + "=" +
                        URLEncoder.encode(duration,"UTF-8");

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
