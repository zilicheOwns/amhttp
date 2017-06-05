package io.chelizi.amokhttp.utils;

import android.text.TextUtils;
import android.util.Log;

import io.chelizi.amokhttp.utils.log.AMLogger;

public class LogUtil {

    public static boolean isEmpty(String line) {
        return TextUtils.isEmpty(line) || line.equals("\n") || line.equals("\t") || TextUtils.isEmpty(line.trim());
    }

    public static void printLine(String tag, boolean isTop) {
        if (isTop) {
            Log.d(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
        } else {
            Log.d(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
        }
    }


    public static void openDebug(boolean debug){
        AMLogger.DEBUGGABLE = debug;
    }

}
