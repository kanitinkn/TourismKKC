package com.tourismkkc;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created by cbnuke on 11/23/15.
 */
public class APIconnect extends AsyncTask<String, String, JSONArray> {
    private String URL = "http://tourismkkc.thaihubhosting.com/main/register";
    private String TAG = "CBNUKE_Dev";

    public APIconnect() {

    }

    @Override
    protected JSONArray doInBackground(String... params) {
        URL url;
        HttpURLConnection urlConnection = null;
        JSONArray response = new JSONArray();

        try {
            url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("user_email", "test@test.com")
                    .appendQueryParameter("user_fname", "test")
                    .appendQueryParameter("user_lname", "1234");
            String query = builder.build().getEncodedQuery();

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();

            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String responseString = readStream(urlConnection.getInputStream());
                Log.d(TAG, responseString);
                response = new JSONArray(responseString);
            } else {
                Log.d(TAG, "Response code:" + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        return response;
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    public void test() {
        Log.d(TAG, "1");
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL("http://www.android.com/");
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.d(TAG, "2");
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            Log.d(TAG, "3");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.d(TAG, "Error");
        } finally {
            urlConnection.disconnect();
            Log.d(TAG, "4");
        }
        Log.d(TAG, "5");
    }

    public InputStream getMain() {
        InputStream in = null;
        int resCode = -1;

        try {
            URL url = new URL(URL);
            URLConnection urlConn = url.openConnection();

            if (!(urlConn instanceof HttpURLConnection)) {
                throw new IOException("URL is not an Http URL");
            }
            Log.d(TAG, "1");
            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            resCode = httpConn.getResponseCode();
            Log.d(TAG, "2");

            if (resCode == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
                Log.d(TAG, "3");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }
}

