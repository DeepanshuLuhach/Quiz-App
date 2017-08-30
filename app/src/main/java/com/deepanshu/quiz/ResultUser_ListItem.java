package com.deepanshu.quiz;

class ResultUser_ListItem  {
    //result_id,auth_name,test_name,topic,total,total_cor,total_incor,score
    private String ResultId;
    private String Name;
    private String Testname;
    private String Topic;
    private String Total;
    private String TotalCor;
    private String TotalIncor;
    private String Score;

    ResultUser_ListItem(String result_id, String auth_name, String test_name, String topic, String total, String total_cor, String total_incor, String score) {
        this.ResultId = result_id;
        this.Name = auth_name;
        this.Testname = test_name;
        this.Topic = topic;
        this.Total = total;
        this.TotalCor = total_cor;
        this.TotalIncor = total_incor;
        this.Score = score;
    }

    public String getResultId() {return ResultId;}

    public String getName() {
        return Name;
    }

    public String getTestname() {
        return Testname;
    }

    public String getTopic() {
        return Topic;
    }

    public String getTotal() {
        return Total;
    }

    public String getTotalCor() {
        return TotalCor;
    }

    public String getTotalIncor() {
        return TotalIncor;
    }

    public String getScore() {
        return Score;
    }
}
