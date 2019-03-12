package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView emptyTextView;
    private ListView earthquakeListView;
    private final static String URL_REQ = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.pb_loading);

        emptyTextView = findViewById(R.id.tv_empty);

        earthquakeListView = findViewById(R.id.list);

        earthquakeListView.setEmptyView(emptyTextView);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            EarthquakeAsyncTask earthquakeAsyncTask = new EarthquakeAsyncTask();
            earthquakeAsyncTask.execute(URL_REQ);
        }
        else{
            emptyTextView.setText("No Network Connection! :(");
            progressBar.setVisibility(View.GONE);
        }
    }

    public class EarthquakeAsyncTask extends AsyncTask<String, ArrayList<Earthquake>, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String temp;
            ArrayList earthquakes;

            try {
                URL url = new URL(strings[0]);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.connect();

                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                temp = stream2String(inputStream);

                earthquakes = extractEarthquakesFromJSON(temp);
                publishProgress(earthquakes);

                inputStream.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(ArrayList<Earthquake>... values) {

            if(!values[0].isEmpty()) {
                ArrayList<Earthquake> earthquakes = values[0];

                final EarthquakeAdapter adapter = new EarthquakeAdapter(getApplicationContext(), earthquakes);

                earthquakeListView.setAdapter(adapter);

                earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Earthquake earthquake = adapter.getItem(position);

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(earthquake.getUrl()));
                        startActivity(intent);
                    }
                });
            }else
                emptyTextView.setText("No Earthquakes Found!");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressBar.setVisibility(View.GONE);
        }





    public String stream2String (InputStream inputStream){

        String line;
        String text = "";

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {

            while ((line = reader.readLine()) != null) {
                text += line;
            }
        }catch (IOException e){}

        return text;

    }

    public ArrayList<Earthquake> extractEarthquakesFromJSON(String s){
        ArrayList <Earthquake> earthquakes = new ArrayList<>();

        try {

            JSONObject root = new JSONObject(s);
            JSONArray feature = root.getJSONArray("features");

            for (int i=0; i<feature.length(); i++){
                JSONObject current = feature.getJSONObject(i);

                JSONObject properties = current.getJSONObject("properties");

                Double mag = properties.optDouble("mag");
                String place = properties.getString("place");
                long date = properties.getLong("time");
                String url = properties.getString("url");

                earthquakes.add(new Earthquake(mag, place, date, url));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return earthquakes;

    }
    }
}
