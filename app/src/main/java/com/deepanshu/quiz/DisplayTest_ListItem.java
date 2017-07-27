package com.deepanshu.quiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DisplayTest_ListItem  {
    private String TestId;
    private String TestName;
    private String TestTopic;
    private String TestDuration;
    private String TestQBId;
    private String TestStatus;

    public DisplayTest_ListItem(String testId, String testName, String testTopic, String testDuration, String testQBId, String testStatus) {
        this.TestId = testId;
        this.TestName = testName;
        this.TestTopic = testTopic;
        this.TestDuration = testDuration;
        this.TestQBId = testQBId;
        this.TestStatus = testStatus;
    }

    public String getTestId() {
        return TestId;
    }

    public String getTestName() {
        return TestName;
    }

    public String getTestTopic() {
        return TestTopic;
    }

    public String getTestDuration() {
        return TestDuration;
    }

    public String getTestQBId() {
        return TestQBId;
    }

    public String getTestStatus() {
        return TestStatus;
    }
}
