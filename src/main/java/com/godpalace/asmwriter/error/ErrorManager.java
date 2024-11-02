package com.godpalace.asmwriter.error;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

public class ErrorManager {
    private String lastError;
    private final Vector<ErrorListener> listeners;

    public void addErrorListener(ErrorListener listener) {
        listeners.add(listener);
    }

    public ErrorManager() {
        lastError = "";
        listeners = new Vector<>();
    }

    public void setLastError(String error) {
        lastError = error;

        for (ErrorListener listener : listeners) {
            listener.error(this);
        }
    }

    public void setLastError(Exception e) {
        lastError = e.getMessage();

        for (ErrorListener listener : listeners) {
            listener.error(this);
        }
    }

    public boolean hasLastError() {
        return !lastError.isEmpty();
    }

    public void clearLastError() {
        lastError = "";
    }

    public void printLastError() {
        // 输出信息: [HH:mm:ss] 错误信息

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = dateFormat.format(now);

        System.err.println("[" + time + "]\n" + lastError + "\n");
    }

    public String getLastError() {
        // 输出信息: [HH:mm:ss] 错误信息

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = dateFormat.format(now);

        return "[" + time + "]\n" + lastError + "\n";
    }

    @Override
    public String toString() {
        return lastError;
    }
}
