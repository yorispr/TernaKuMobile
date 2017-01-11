package com.fintech.ternaku.TernakPerah.Handler;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pandu on 10/20/16.
 */

public class LoggingExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final static String TAG = LoggingExceptionHandler.class.getSimpleName();
    private final static String ERROR_FILE = MyAuthException.class.getSimpleName() + ".error";

    private final Context context;
    private final Thread.UncaughtExceptionHandler rootHandler;

    public LoggingExceptionHandler(Context context) {
        this.context = context;
        // we should store the current exception handler -- to invoke it for all not handled exceptions ...
        rootHandler = Thread.getDefaultUncaughtExceptionHandler();
        // we replace the exception handler now with us -- we will properly dispatch the exceptions ...
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(final Thread thread, final Throwable ex) {
        try {
            Log.d(TAG, "called for " + ex.getClass());
            // assume we would write each error in one file ...
            File f = new File(context.getFilesDir(), ERROR_FILE);
            // log this exception ...
            FileUtils.writeStringToFile(f, ex.getClass().getSimpleName() + " " + System.currentTimeMillis() + "\n", true);
        } catch (Exception e) {
            Log.e(TAG, "Exception Logger failed!", e);
        }

        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                // we cant start a dialog here, as the context is maybe just a background activity ...
                FirebaseCrash.report(ex);
                Toast.makeText(context, ex.getMessage() + " Application will close!", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();

        try {
            Thread.sleep(4000); // Let the Toast display before app will get shutdown
        } catch (InterruptedException e) {
            // Ignored.
        }

        rootHandler.uncaughtException(thread, ex);
    }

    public static final List<String> readExceptions(Context context) {
        List<String> exceptions = new ArrayList<>();
        File f = new File(context.getFilesDir(), ERROR_FILE);
        if (f.exists()) {
            try {
                exceptions = FileUtils.readLines(f);
            } catch (IOException e) {
                Log.e(TAG, "readExceptions failed!", e);
            }
        }
        return exceptions;
    }
}