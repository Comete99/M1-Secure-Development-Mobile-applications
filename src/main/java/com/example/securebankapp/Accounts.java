package com.example.securebankapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


public class Accounts extends AppCompatActivity {
    private final String API = BuildConfig.ApiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        TextView textView = findViewById(R.id.textInfo);

        refreshAccounts();

        ImageButton refresh = findViewById(R.id.bRefresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshAccounts();
            }
        });

    }
    private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    private String readFromFile() {

        String ret = "";

        try {
            InputStream inputStream = getApplicationContext().openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    private  void refreshAccounts(){
        //Request
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        //Verify server certificates
        try {
            AsyncTask<String, Void, Exception> e =new RetrieveFeedTask().execute(API);

            ListView listAcc = (ListView)findViewById(R.id.listAccounts);
            listAcc.setAdapter(null);
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, API,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.

                            try {
                                JSONArray jsonArray = new JSONArray(response);

                                //We save the data in the internal storage for offline use
                                writeToFile(jsonArray.toString());

                                ArrayList<String> items = new ArrayList<String>();
                                for(int i=0; i < jsonArray.length() ; i++) {
                                    JSONObject json_data = jsonArray.getJSONObject(i);
                                    String account=json_data.toString();
                                    items.add(account);
                                }


                                ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, items);
                                listAcc.setAdapter(mArrayAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //No connexion so we use local data
                    String dataFile = readFromFile();
                    try {
                        JSONArray jsonArray = new JSONArray(dataFile);

                        ArrayList<String> items = new ArrayList<String>();
                        for(int i=0; i < jsonArray.length() ; i++) {
                            JSONObject json_data = jsonArray.getJSONObject(i);
                            String account=json_data.toString();
                            items.add(account);
                        }

                        ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, items);
                        listAcc.setAdapter(mArrayAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Toast toast = Toast.makeText(getApplicationContext(), "Connexion failed to the API", Toast.LENGTH_LONG);
                    toast.show();
                }
            });
            //Add the request to the RequestQueue.
            queue.add(stringRequest);
        }catch (Exception e){

        }



    }
}