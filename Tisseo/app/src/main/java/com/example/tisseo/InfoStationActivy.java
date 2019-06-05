package com.example.tisseo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InfoStationActivy extends AppCompatActivity {
    private ListView lvi;
    private Stop stop;
    String key ="48e242b6-a196-40ec-8192-74bc0d76eda1";
    ArrayList<Stop> stopList;
    String id_line;
    String url_stops;
    private static InfoStationActivy parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_station_activy);

        stopList = new ArrayList<>();
        lvi = (ListView) findViewById(R.id.list_stop);
        id_line = getIntent().getStringExtra("id");
        new GetStops().execute();


        TextView name = findViewById(R.id.name_info);
        TextView line_number = findViewById(R.id.number_line_info);
        TextView color = findViewById(R.id.color_info);


        name.setText(getIntent().getStringExtra("name"));
        line_number.setText("Ligne: "+getIntent().getStringExtra("line_number"));
        color.setText("Couleur: "+getIntent().getStringExtra("color"));

    }

    private class GetStops extends AsyncTask<Void, Void, ListView> {


        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(InfoStationActivy.this, "Json Data is downloading", Toast.LENGTH_LONG).show();
        }


        @Override
        protected ListView doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            url_stops = new String("https://api.tisseo.fr/v1/stop_areas.json?lineId="+ id_line +"&key="+ key);
            String jsonStr = sh.makeServiceCall(url_stops);

            if (jsonStr != null) {

                try {
                    System.out.println("--------------- recup line stops ---------------");
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONObject stations_json = jsonObj.getJSONObject("stopAreas");
                    JSONArray stations_json_line = stations_json.getJSONArray("stopArea");

                    // looping through All Contacts
                    for (int i = 0; i < stations_json_line.length(); i++) {
                        JSONObject c = stations_json_line.getJSONObject(i);

                        if(c.isNull("id")) {

                            System.out.println("ERRRROR----  !!!!!!!!");

                        }
                        else{

                            String id = c.getString("id");
                            String name = c.getString("name");
                            String city = c.getString("cityName");
                            stop = new Stop(city, id, name);
                            stopList.add(i, stop);
                            System.out.println(name);

                        }

                    }
                } catch (final JSONException e) {
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return lvi;
        }

        @Override
        protected void onPostExecute(ListView result) {
            super.onPostExecute(result);

            System.out.println(stopList);
            ListAdapter adapter = new ListStop(InfoStationActivy.this, R.layout.list_stop, stopList);
            System.out.println(url_stops);
            lvi.setAdapter(adapter);

        }
    }
}
