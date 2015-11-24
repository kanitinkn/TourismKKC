package com.tourismkkc;

import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class APIConnect {

    private String URL_MAIN = "http://192.168.0.132/TourismKKC/main/";
    private String TAG = "CBNUKE_Dev";
    private OkHttpClient okHttpClient = new OkHttpClient();
    private APIStatus apiStatus = new APIStatus();

    public APIConnect() {

    }

    public APIStatus register(String user_email, String user_password, String user_fname, String user_lname) {
        RequestBody formBody = new FormEncodingBuilder()
                .add("user_email", user_email)
                .add("user_password", user_password)
                .add("user_fname", user_fname)
                .add("user_lname", user_lname)
                .build();

        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(URL_MAIN + "register")
                .post(formBody)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return checkAPIStatus(response.body().string());
            } else {
                Log.d(TAG, "Not Success - code in register : " + response.code());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "ERROR in register : " + e.getMessage());
            return null;
        }
    }

    public APIStatus login(String user_email, String user_password) {
        RequestBody formBody = new FormEncodingBuilder()
                .add("user_email", user_email)
                .add("user_password", user_password)
                .build();

        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(URL_MAIN + "login")
                .post(formBody)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return checkAPIStatus(response.body().string());
            } else {
                Log.d(TAG, "Not Success - code in login : " + response.code());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "ERROR in login : " + e.getMessage());
            return null;
        }
    }

    private APIStatus checkAPIStatus(String json_string) {
        APIStatus apiStatus = new APIStatus();
        try {
            JSONObject jStatus = new JSONObject(json_string);
            String status = jStatus.getString("status");
            apiStatus.setStatus(status);
            Log.d(TAG, "STATUS:" + status);

            JSONObject objData = jStatus.getJSONObject("data");
            String action = objData.getString("action");
            apiStatus.setAction(action);
            Log.d(TAG, "ACTION:" + action);

            String reason = objData.getString("reason");
            apiStatus.setReason(reason);
            Log.d(TAG, "REASON:" + reason);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "ERROR in checkAPIStatus : " + e.toString());
        }

        return apiStatus;
    }

//    @Override
//      protected String doInBackground(Void... voids) {
//        OkHttpClient okHttpClient = new OkHttpClient();
//
//        RequestBody formBody = new FormEncodingBuilder()
//                .add("user_email", "test@test.com")
//                .add("user_password", "1234")
//                .add("user_fname", "Test")
//                .add("user_lname", "Com")
//                .build();
//
//        /*RequestBody requestBody = new MultipartBuilder()
//                .type(MultipartBuilder.FORM)
//                .addPart(
//                        Headers.of("Content-Disposition", "form-data; name=\"user_email\""),
//                        RequestBody.create(null, "test@test.com"))
//                .addPart(
//                        Headers.of("Content-Disposition", "form-data; name=\"user_password\""),
//                        RequestBody.create(null, "1234"))
//                .addPart(
//                        Headers.of("Content-Disposition", "form-data; name=\"user_fname\""),
//                        RequestBody.create(null, "Test"))
//                .addPart(
//                        Headers.of("Content-Disposition", "form-data; name=\"user_lname\""),
//                        RequestBody.create(null, "Com"))
//                .addPart(
//                        Headers.of("Content-Disposition", "form-data; name=\"image\""),
//                        RequestBody.create(MEDIA_TYPE_PNG, new File("website/static/logo-square.png")))
//                .build();*/
//
//        Request.Builder builder = new Request.Builder();
//        String URL = "http://192.168.0.132/TourismKKC/main/register";
//        Request request = builder.url(URL).post(formBody).build();
//
//        Log.d(TAG, "Start API");
//
//        try {
//            Response response = okHttpClient.newCall(request).execute();
//
//            if (response.isSuccessful()) {
//                return response.body().string();
//            } else {
//                Log.d(TAG, "Not Success - code : " + response.code());
//                return "Not Success - code : " + response.code();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.d(TAG, "Error - " + e.getMessage());
//            return "Error - " + e.getMessage();
//        }
//    }
//
//    @Override
//    protected void onPostExecute(String string) {
//
//        super.onPostExecute(string);
//        Log.d(TAG, "End API");
//        Log.d(TAG, string);
//
//        try {
//            JSONObject jStatus = new JSONObject(string);
//            String status = jStatus.getString("status");
//            Log.d(TAG, "STATUS:" + status);
//
//            JSONObject objData = jStatus.getJSONObject("data");
//            String action = objData.getString("action");
//            Log.d(TAG, "ACTION:" + action);
//
//            String reason = objData.getString("reason");
//            Log.d(TAG, "REASON:" + reason);
//
///*            JSONArray jArray = jObject.getJSONArray("data");
//            for (int i = 0; i < jArray.length(); i++) {
//                try {
//                    Log.d(TAG, "Loop " + i);
//                    JSONObject oneObject = jArray.getJSONObject(i);
//                    // Pulling items from the array
//                    String oneObjectsItem = oneObject.getString("action");
//                    String oneObjectsItem2 = oneObject.getString("reason");
//                    Log.d(TAG, oneObjectsItem);
//                    Log.d(TAG, oneObjectsItem2);
//                } catch (JSONException e) {
//                    // Oops
//                }
//            }*/
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Log.d(TAG, "ERROR " + e.toString());
//        }
//
//    }
}

