package com.mrljdx.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mrljdx.mlog.MLog;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Sample";
    private static String XML = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><!--  Copyright w3school.com.cn --><note><to>George</to><from>John</from><heading>Reminder</heading><body>Don't forget the meeting!</body></note>";
    private static String JSON;
    private static String JSON_LONG;
    private static String STRING_LONG;

    private String SIMPLE_LOG = "This 一个简单的 Log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MLog.initLog(TAG)
                .saveLog(false)
                .showLog(true);
        init();
    }

    private void init() {
        JSON_LONG = getResources().getString(R.string.json_long);
        JSON = getResources().getString(R.string.json);
        STRING_LONG = getString(R.string.string_long);
    }

    public void logAll(View v) {
        MLog.d(SIMPLE_LOG);
        MLog.i(SIMPLE_LOG);

    }

    public void logWithLong(View v) {
        MLog.d(STRING_LONG);
    }

    public void logWithTag(View v) {
        MLog.tag("NEW_TAG").d(SIMPLE_LOG);
    }

    public void logWithParams(View v) {
        MLog.d("%s是一个%s调试工具%d分", "MLog", "Android", 100);
    }

    public void logWithJson(View v) {
        MLog.json(JSON);
    }

    public void logWithLongJson(View v) {
        MLog.json(JSON_LONG);
    }

    public void logWithXml(View v) {
        MLog.xml(XML);
    }

    public void logWithError(View v) {
        Throwable t = new Throwable("Test Error ");
        MLog.e(t, "error :%s", t.getMessage());
        try {
            int i = 0;
            int b = 1;
            int c = b / i;
        } catch (Exception e) {
            MLog.e(e.getMessage());
            MLog.e(e);
        }
    }

    @Override
    protected void onDestroy() {
        MLog.clear();
        super.onDestroy();
    }
}
