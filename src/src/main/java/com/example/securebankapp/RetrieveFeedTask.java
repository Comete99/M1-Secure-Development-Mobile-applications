package com.example.securebankapp;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class RetrieveFeedTask extends AsyncTask<String, Void, Exception> {
    public Exception exception;

    protected Exception doInBackground(String... urls) {
        try {
            URL url2 = new URL(urls[0]);

            URLConnection urlConnection = url2.openConnection();
            InputStream in = urlConnection.getInputStream();
            IOUtils.copy(in, System.out);

        } catch (Exception e) {
            this.exception = e;
            Log.e("error", e.toString());
            return e;
        }
        return null;
    }
}
