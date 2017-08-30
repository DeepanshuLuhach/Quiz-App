package com.deepanshu.quiz;

class AdminResults_ListItem  {
    private String ResultId;
    private String Name;
    private String Total;
    private String Correct;
    private String Incorect;
    private String Score;
    private String Percentage;

    AdminResults_ListItem(String resultId, String name, String total, String correct, String incorect, String score, String percentage) {
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
