package com.mrljdx.mlog;

import android.os.Environment;

import java.io.File;

/**
 * Created by Mrljdx on 16/5/25.
 */
public class Settings {
    private boolean showLog = true;
    private boolean saveLog = true;
    private String ANDROID_ROOT_DIR = Environment
            .getExternalStorageDirectory()
            + File.separator;
    private String logDir = ANDROID_ROOT_DIR + "MLog";

    public Settings showLog(boolean flag) {
        this.showLog = flag;
        return this;
    }

    public Settings saveLog(boolean flag) {
        this.saveLog = flag;
        return this;
    }

    public boolean isShowLog() {
        return showLog;
    }

    public boolean isSaveLog() {
        return saveLog;
    }

    public void setLogDir(String logDir) {
        this.logDir = ANDROID_ROOT_DIR + logDir;
    }

    public String getLogDir() {
        return logDir;
    }

    public boolean isLogDirExists() {
        return new File(logDir).exists();
    }

}
