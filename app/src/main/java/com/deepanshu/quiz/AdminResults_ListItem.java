package com.deepanshu.quiz;

/**
 * Created by deepanshu on 29/8/17.
 */
//{"RESULT_ID":"36","name":"ajay","TEST_NAME":"OS-TEST-1","TOPIC":"Deadlock","TOTAL":"5","TOTAL_COR":"3","TOTAL_INCOR":"1","SCORE":"11","PERCENTAGE":"0"},
public class AdminResults_ListItem  {
    private String ResultId;
    private String Name;
    private String Total;
    private String Correct;
    private String Incorect;
    private String Score;
    private String Percentage;

    public AdminResults_ListItem(String resultId, String name, String total, String correct, String incorect, String score, String percentage) {
        ResultId = resultId;
        Name = name;
        Total = total;
        Correct = correct;
        Incorect = incorect;
        Score = score;
        Percentage = percentage;
    }

    public String getResultId() {
        return ResultId;
    }

    public String getName() {
        return Name;
    }

    public String getTotal() {
        return Total;
    }

    public String getCorrect() {
        return Correct;
    }

    public String getIncorect() {
        return Incorect;
    }

    public String getScore() {
        return Score;
    }

    public String getPercentage() {
        return Percentage;
    }
}
