package com.example.tyson.transguardserver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TransGuardServer extends Activity {

    String apiKey = "AIzaSyBWfKLPBvX8P4tm2sI4bKiT4LA2XUyejp4";
    //String apiKey = "AIzaSyAnQRrzN8JVNqCSG6NKNJPLcLEPCSqLdyw";
    public static String clientRegID;
    String regID;
    String PROJECT_NUMBER = "492813484993";;
    GoogleCloudMessaging gcm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_guard_server);
        getRegId();
    }

    public static void post(final String apiKey, final Content content) {

        new AsyncTask<Void, Void, String>() {
        @Override
        protected String doInBackground(Void... params) {
            try {

                // 1. URL
                URL url = new URL("https://android.googleapis.com/gcm/send");

                // 2. Open connection
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // 3. Specify POST method
                conn.setRequestMethod("POST");

                // 4. Set the headers
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", "key=" + apiKey);

                conn.setDoOutput(true);

                // 5. Add JSON data into POST request body


                //`5.1 Use Jackson object mapper to convert Contnet object into JSON
                ObjectMapper mapper = new ObjectMapper();

                // 5.2 Get connection output stream
                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());

                // 5.3 Copy Content "JSON" into
                mapper.writeValue(wr, content);


                // 5.4 Send the request
                wr.flush();

                // 5.5 close
                wr.close();
                // 6. Get the response
                int responseCode = conn.getResponseCode();
                Log.i("CONNECTION:", "\nSending 'POST' request to URL : " + url);
                Log.i("CONNECTION:", "Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // 7. Print result
                Log.i("RESPONSE:", response.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        }.execute(null, null, null);
    }

    public void onButtonClick(View view) {

        switch(view.getId()) {
            case R.id.postButton:
                Content content = createContent();

                this.post(apiKey, content);
                break;
        }
    }

    public Content createContent(){

        Content c = new Content();

        c.addRegId(clientRegID);
        c.createData(this.getBaseContext());

        return c;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.trans_guard_server, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getRegId(){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regID = gcm.register(PROJECT_NUMBER);
                    msg = "Device registered, registration ID=" + regID;
                    Log.i("GCM", msg);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.i("Post execute: ", msg);
            }
        }.execute(null, null, null);
    }
}
