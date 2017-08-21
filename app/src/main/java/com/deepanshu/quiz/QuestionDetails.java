package com.deepanshu.quiz;

/**
 * Created by deepanshu on 21/8/17.
 */

public class QuestionDetails {
    private String qid,question, answer,a,b,c,d;
    public QuestionDetails() {
        this.qid = "";
        this.question = "";
        this.answer = "";
        this.a = "";
        this.b = "";
        this.c = "";
        this.d = "";
    }

    public QuestionDetails(String qid, String question, String answer, String a, String b, String c, String d) {
        this.qid = qid;
        this.question = question;
        this.answer = answer;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }
}
