package com.example.tisseo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView lv;

    ArrayList<Station> stationList;
    Station station;
    Station item;
    URL url = new URL("https://api.tisseo.fr/v1/");
    String key ="48e242b6-a196-40ec-8192-74bc0d76eda1";
    ProgressDialog pd;
    ArrayList<Station> station_click;

    public MainActivity() throws MalformedURLException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stationList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.station_list_view);
        new GetStations().execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = lv.getItemAtPosition(position).toString();
                System.out.println(text);
                Toast.makeText(MainActivity.this, "" + text, Toast.LENGTH_SHORT).show();

                int numRows = stationList.size();
                System.out.println(numRows);

                if (numRows == 0) {
                    Toast.makeText(MainActivity.this, "The Database is empty  :(.", Toast.LENGTH_LONG).show();
                }
                else{

                    System.out.println(stationList.get(position));
                    System.out.println("-------station_click--------");
                    System.out.println(position);
                    System.out.println("-------station_click--------");

                    item = stationList.get(position);
                    Intent intent = new Intent(MainActivity.this, InfoStationActivy.class);
                    intent.putExtra("id",item.getId_station());
                    intent.putExtra("name", item.getName());
                    intent.putExtra("line_number", item.getLine_number());
                    intent.putExtra("color", item.getColor());
                    startActivity(intent);
                }
            }
        });

    }
    private class GetStations extends AsyncTask<Void, Void, ListView> {

        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();
        }


        @Override
        protected ListView doInBackground(Void... arg0) {
            System.out.println("-----0--------");
            HttpHandler sh = new HttpHandler();
            System.out.println("-----1--------");
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("https://api.tisseo.fr/v1/lines.json?&key=48e242b6-a196-40ec-8192-74bc0d76eda1");
            System.out.println("2");
            if (jsonStr != null) {
                System.out.println("3");
                try {
                    System.out.println("--------------- 444 ---------------");


                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONObject stations_json = jsonObj.getJSONObject("lines");

                    JSONArray stations_json_line = stations_json.getJSONArray("line");

                    // looping through All stations
                    for (int i = 0; i < stations_json_line.length(); i++) {
                        JSONObject c = stations_json_line.getJSONObject(i);

                        System.out.println("------------------------------");
                        System.out.println(c);
                        if(c != null && c.getString("name") != null){
                            System.out.println(c.getString("name"));
                            String id = c.getString("id");
                            String name = c.getString("name");
                            String line_number_json = c.getString("shortName");
                            station = new Station(name, line_number_json, id);
                            stationList.add(i, station);
                        }
                        else {
                            System.out.println("-------- NULL ------");

                        }
                    }
                } catch (final JSONException e) {
                    //Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                //Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return lv;
        }

        @Override
        protected void onPostExecute(ListView result) {
            super.onPostExecute(result);
            ListAdapter adapter = new ListStation(MainActivity.this, R.layout.station_list, stationList);
            lv.setAdapter(adapter);
        }
    }
}
