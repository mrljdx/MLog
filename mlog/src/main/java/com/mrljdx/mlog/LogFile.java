package com.mrljdx.mlog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * Created by Mrljdx on 16/5/26.
 */
public class LogFile {

    private PrintWriter mPrintWriter;

    public File saveLogToFile(String dir, String filename, Object str) {
        File file = new File(dir + File.separator + filename + ".txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            mPrintWriter = new PrintWriter(new FileOutputStream(file, true));
            mPrintWriter.print(str + "\r\n");
            mPrintWriter.flush();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            mPrintWriter.close();
        }
    }

}
