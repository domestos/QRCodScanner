package es.hol.valera.qrcodscanner;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by valera.pelenskyi on 13.10.17.
 */

public class Connect extends AsyncTask<String, Void, String> {

    private Context context;
    private String number;
    private String url_select_php;
    private String singleJO;
    private String arrayJO;


    public Connect(String url_select_php, Context context) {
        this.url_select_php = url_select_php;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {

        URL url = null;


        try {
            url = new URL(url_select_php);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "ERROR " + e.getMessage();
        }

        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(2500);
            httpURLConnection.setReadTimeout(25000);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();

            StringBuilder sb = new StringBuilder();

            if (HttpURLConnection.HTTP_OK == httpURLConnection.getResponseCode()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));

                String line;
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }

                //  return sb.toString();
                return sb.toString();
            } else {
                return "fail " + httpURLConnection.getResponseCode() + " " + httpURLConnection.getResponseMessage();

            }


        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR 2r" + e.getMessage();
        }


    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();

        dataParsed(s);

    }


    private String dataParsed(String sb) {


        try {
            JSONObject JO = new JSONObject(sb);

            if (JO.get("success").toString().equals("0")){

                MainQRScanner.item.setText( JO.get("message").toString());
                return JO.get("message").toString();

            }

            JSONArray JA = new JSONArray(JO.get("product").toString());


            if (JA.length() == 1) {
                JSONObject singItem = (JSONObject) JA.get(0);

                singleJO = "number:  " + singItem.getString("number") + "\n" +
                        "item:   " + singItem.getString("item") + "\n" +
                        "owner:  " + singItem.getString("owner") + "\n" +
                        "location:  " + singItem.getString("location") + "\n" +
                        "description:  " + singItem.getString("description");
                MainQRScanner.item.setText(singleJO);


            }


        } catch (JSONException e) {
            e.printStackTrace();
            //  MainQRScanner.item.setText("ERROR");
        }


        /*
        try {
            JSONArray JA =  new JSONArray(sb);
            Log.d("JA  ",JA.toString());
            for (int i = 0; i < JA.length(); i++) {
                JSONObject JO = (JSONObject) JA.get(i);
                *//*singleJO = "number: " + JO.get("number") + "\n" +
                           "item: " + JO.get("item") + "\n" +
                           "owner: " + JO.get("owner") + "\n" +
                           "location: " + JO.get("location") + "\n";*//*
                singleJO = "success: " + JO.get("success") + "\n";
              Log.d("JA1  ",singleJO.toString());
                arrayJO = arrayJO + singleJO+"\n";
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        return arrayJO;
    }

}