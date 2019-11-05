package com.klakier.proRobIntranet.Old;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

public class MySQLScriptTask extends AsyncTask<Void, Void, String> {
    private Context context;
    private OnMySQLScriptTaskComplete listener;
    private List<PostTableItem> postTableItemList;
    private URL scriptURL;

    public MySQLScriptTask(Context context, OnMySQLScriptTaskComplete listener, List<PostTableItem> postTableItemList, URL scriptURL) {
        this.context = context;
        this.listener = listener;
        this.postTableItemList = postTableItemList;
        this.scriptURL = scriptURL;
    }


    @Override
    protected String doInBackground(Void... voids) {

        try {

            String data = "";
            for(PostTableItem item : postTableItemList)
            {
                data  += "&" + URLEncoder.encode(item.getKey(), "UTF-8") + "=" +
                        URLEncoder.encode(item.getValue(), "UTF-8");
            }

            URLConnection conn = scriptURL.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

            return sb.toString();
        } catch (Exception e) {
            return "Exception: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if(listener != null)
        {
            listener.onMySQLScriptTaskComplete(result);
        }
    }

    public interface OnMySQLScriptTaskComplete {
        void onMySQLScriptTaskComplete(String s);
    }

}

