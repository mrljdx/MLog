package com.mrljdx.mlog;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Created by Mrljdx on 16/5/25.
 */
public class LogPrinter implements Printer {

    private Settings mSettings = new Settings();

    private PrintWriter mPrintWriter = null;

    private String mDefaultTag = "TAG";
    /**
     * Localize single tag and method count for each thread
     */
    private final ThreadLocal<String> mLocalTag = new ThreadLocal<>();

    @Override
    public Printer tag(String tag) {
        if (tag != null) {
            mLocalTag.set(tag);
        }
        return this;
    }

    @Override
    public Settings init(String tag) {
        if (mSettings == null) {
            mSettings = new Settings();
        }
        this.mDefaultTag = tag;
        return mSettings;
    }

    @Override
    public Settings getSettings() {
        return mSettings;
    }

    @Override
    public void d(String message, Object... args) {
        printLog(DEBUG, message, args);
    }

    @Override
    public void e(String message, Object... args) {
        e(null, message, args);
    }

    @Override
    public void e(Throwable throwable, String message, Object... args) {
        if (throwable != null && message != null) {
            message += " : " + Log.getStackTraceString(throwable);
        }
        if (throwable != null && message == null) {
            message = throwable.toString();
        }
        if (message == null) {
            message = "No message/exception is set";
        }
        printLog(ERROR, message, args);
    }

    @Override
    public void w(String message, Object... args) {
        printLog(WARN, message, args);
    }

    @Override
    public void i(String message, Object... args) {
        printLog(INFO,message,args);
    }

    @Override
    public void v(String message, Object... args) {
        printLog(VERBOSE,message,args);
    }

    @Override
    public void wtf(String message, Object... args) {
        printLog(ASSERT,message,args);
    }

    @Override
    public void json(String json) {
        if (TextUtils.isEmpty(json)) {
            d("Empty/Null json content");
            return;
        }
        try {
            json = json.trim();
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                String message = jsonObject.toString(JSON_INDENT);
                d(message);
                return;
            }
            if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                String message = jsonArray.toString(JSON_INDENT);
                d(message);
            }
        } catch (JSONException e) {
            e(e.getCause().getMessage() + "\n" + json);
        }
    }

    @Override
    public void xml(String xml) {
        if (TextUtils.isEmpty(xml)) {
            d("Empty/Null xml content");
            return;
        }
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);
            d(xmlOutput.getWriter().toString().replaceFirst(">", ">\n"));
        } catch (TransformerException e) {
            e(e.getCause().getMessage() + "\n" + xml);
        }
    }

    @Override
    public void clear() {
        mSettings = null;
    }

    /**
     * @return the appropriate tag based on local or global
     */
    private String getTag() {
        String tag = mLocalTag.get();
        if (tag != null) {
            mLocalTag.remove();
            return tag;
        }
        return this.mDefaultTag;
    }

    private String createMessage(String message, Object... args) {
        return args.length == 0 ? message : String.format(message, args);
    }

    private synchronized void printLog(int logType, String msg, Object... args) {
        if(!mSettings.isShowLog()){
            return;
        }
        String tag = getTag();
        String message = createMessage(msg,args);
        String headString = getStackStace();
        if(headString!=null&&headString.length()>0){
            message = headString+message;
        }

        if(mSettings.isSaveLog()){
            writeLogToFile(message);
        }

        int index = 0;
        int countOfSub = msg.length() / CHUNK_SIZE;

        if (countOfSub > 0) {
            for (int i = 0; i < countOfSub; i++) {
                String sub = message.substring(index, index + CHUNK_SIZE);
                printSub(logType, tag, sub);
                index += CHUNK_SIZE;
            }
            printSub(logType, tag, message.substring(index, message.length()));
        } else {
            printSub(logType, tag, message);
        }
    }


    private static void printSub(int type, String tag, String sub) {
        switch (type) {
            case VERBOSE:
                Log.v(tag, sub);
                break;
            case DEBUG:
                Log.d(tag, sub);
                break;
            case INFO:
                Log.i(tag, sub);
                break;
            case WARN:
                Log.w(tag, sub);
                break;
            case ERROR:
                Log.e(tag, sub);
                break;
            case ASSERT:
                Log.wtf(tag, sub);
                break;
        }
    }

    private void initDirInThread() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    File file = new File(mSettings.getLogDir());
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                }
            }
        }).start();
    }

    private void writeLogToFile(Object str) {
        if(!mSettings.isLogDirExists()){
            initDirInThread();
        }
        File fil = new File(getSettings().getLogDir() + File.separator + "log" + getFormatDate("yyyy-MM-dd") + ".txt");
        if (!fil.exists()) {
            try {
                fil.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            mPrintWriter = new PrintWriter(new FileOutputStream(fil, true));
            mPrintWriter.print(str + "\r\n");
            mPrintWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mPrintWriter.close();
        }
    }

    private String getFormatDate(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINESE);
        return sdf.format(new Date());
    }


    private String getStackStace(){

        StackTraceElement[] trace = Thread.currentThread().getStackTrace();

        int stackOffset = getStackOffset(trace);
        int methodCount = MIN_METHODE_COUNT;
        //corresponding method count with the current stack may exceeds the stack trace. Trims the count
        if (stackOffset > trace.length) {
            methodCount = trace.length - stackOffset - 1;
        }

        for (int i = methodCount; i > 0; i--) {
            int stackIndex = i + stackOffset;
            if (stackIndex >= trace.length) {
                continue;
            }
            StringBuilder builder = new StringBuilder();
            builder.append("[")
                    .append(Thread.currentThread().getName())
                    .append(":")
                    .append(getSimpleClassName(trace[stackIndex].getClassName()))
                    .append(".")
                    .append(trace[stackIndex].getMethodName())
                    .append("->")
                    .append("(")
                    .append(trace[stackIndex].getFileName())
                    .append(":")
                    .append(trace[stackIndex].getLineNumber())
                    .append(")]:\n");
            return builder.toString();
        }
        return null;
    }

    private String getSimpleClassName(String name) {
        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }

    /**
     * Determines the starting index of the stack trace, after method calls made by this class.
     *
     * @param trace the stack trace
     * @return the stack offset
     */
    private int getStackOffset(StackTraceElement[] trace) {
        for (int i = MIN_STACK_OFFSET; i < trace.length; i++) {
            StackTraceElement e = trace[i];
            String name = e.getClassName();
            if (!name.equals(LogPrinter.class.getName()) && !name.equals(MLog.class.getName())) {
                return --i;
            }
        }
        return -1;
    }
}
