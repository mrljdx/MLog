package com.mrljdx.mlog;

/**
 * Created by Mrljdx on 16/5/25.
 */
public interface Printer {

    int VERBOSE = 2;
    int DEBUG = 3;
    int INFO = 4;
    int WARN = 5;
    int ERROR = 6;
    int ASSERT = 7;

    /**
     * Android 系统Log能打印的最大长度
     */
    int CHUNK_SIZE = 4000;

    /**
     * json 格式化单位
     */
    int JSON_INDENT = 4;

    /**
     * stack offset
     */
    int MIN_STACK_OFFSET = 3;

    /**
     * min method count
     */
    int MIN_METHODE_COUNT = 1;

    /**
     * 局部TAG
     * @param tag 日志TAG
     * @return Printer
     */
    Printer tag(String tag);

    /**
     * 全局默认TAG
     * @param tag 日志TAG
     * @return Settings
     */
    Settings init(String tag);

    Settings getSettings();

    void d(String message, Object... args);

    void e(String message, Object... args);

    void e(Throwable throwable, String message, Object... args);

    void w(String message, Object... args);

    void i(String message, Object... args);

    void v(String message, Object... args);

    void wtf(String message, Object... args);

    void json(String json);

    void xml(String xml);

    void clear();

}
