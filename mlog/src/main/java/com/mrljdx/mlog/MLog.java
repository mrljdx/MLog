package com.mrljdx.mlog;

import java.io.File;

/**
 * Created by Mrljdx on 16/5/25.
 */
public final class MLog {

    private static Printer printer = new LogPrinter();

    private static LogFile logFile = new LogFile();

    private MLog() {
        //不能被实例化
    }

    /**
     * 初始化配置TAG
     *
     * @param tag
     * @return
     */
    public static Settings initLog(String tag) {
        printer.init(tag);
        return printer.init(tag);
    }

    public static Settings getSettings() {
        return printer.getSettings();
    }

    /**
     * 设置Log临时TAG
     *
     * @param tag
     * @return
     */
    public static Printer tag(String tag) {
        return printer.tag(tag);
    }

    public static void d(String message, Object... args) {
        printer.d(message, args);
    }

    public static void e(String message, Object... args) {
        printer.e(null, message, args);
    }

    public static void e(Throwable throwable) {
        printer.e(throwable, null);
    }

    public static void e(Throwable throwable, String message, Object... args) {
        printer.e(throwable, message, args);
    }

    public static void i(String message, Object... args) {
        printer.i(message, args);
    }

    public static void v(String message, Object... args) {
        printer.v(message, args);
    }

    public static void w(String message, Object... args) {
        printer.w(message, args);
    }

    public static void wtf(String message, Object... args) {
        printer.wtf(message, args);
    }

    /**
     * Formats the json content and print it
     *
     * @param json the json content
     */
    public static void json(String json) {
        printer.json(json);
    }

    /**
     * Formats the json content and print it
     *
     * @param xml the xml content
     */
    public static void xml(String xml) {
        printer.xml(xml);
    }

    /**
     * 应用程序退出时清除printer
     */
    public static void clear() {
        printer.clear();
        printer = null;
    }

    /**
     * 将特定的日志信息存储为.txt文件并返回File
     *
     * @param fileDir
     * @param fileName
     * @param message
     */
    public static File file(String fileDir, String fileName, String message) {
        return logFile.saveLogToFile(fileDir, fileName, message);
    }

}
