package io.chelizi.amokhttp.utils.log;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.chelizi.amokhttp.utils.LogUtil;

public class JsonLogger {

    public static void printJson(String tag, String msg, String headString) {

        String message;

        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(AMLogger.JSON_INDENT);
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(AMLogger.JSON_INDENT);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }

        LogUtil.printLine(tag, true);
        message = headString + AMLogger.LINE_SEPARATOR + message;
        String[] lines = message.split(AMLogger.LINE_SEPARATOR);
        for (String line : lines) {
            Log.d(tag, "║ " + line);
        }
        LogUtil.printLine(tag, false);
    }
}
