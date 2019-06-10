package com.example.tisseo;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    private Stop click_stop;
    String key ="48e242b6-a196-40ec-8192-74bc0d76eda1";
    ArrayList<Stop> stopList;
    String id_line;
    String id_stop;
    String url_stops;
    private static InfoStationActivy parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_station_activy);

        //init
        stopList = new ArrayList<>();
        lvi = (ListView) findViewById(R.id.list_stop);
        id_line = getIntent().getStringExtra("id");

        TextView name = findViewById(R.id.name_info);
        TextView line_number = findViewById(R.id.number_line_info);
        TextView color = findViewById(R.id.color_info);

        // Set textView
        String color_l = getIntent().getStringExtra("color");
        name.setText(getIntent().getStringExtra("name"));
        line_number.setTextColor(Color.parseColor(color_l));
        line_number.setText("Ligne: "+getIntent().getStringExtra("line_number"));
        color.setText("");

        // call request
        new GetStops().execute();

        // click on physic stop
        lvi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // display message
                String text = lvi.getItemAtPosition(position).toString();
                Toast.makeText(InfoStationActivy.this, "" + text, Toast.LENGTH_SHORT).show();
                // check result
                int numRows = stopList.size();
                if (numRows == 0) {
                    Toast.makeText(InfoStationActivy.this, "No request: try again :(.", Toast.LENGTH_LONG).show();
                }
                // load and start intent
                else{
                    click_stop = stopList.get(position);
                    Intent intent = new Intent(InfoStationActivy.this, PhysicPoint.class);
                    intent.putExtra("id",click_stop.getId());
                    intent.putExtra("id_display", id_line);
                    intent.putExtra("name", click_stop.getName());
                    intent.putExtra("city", click_stop.getCity());
                    intent.putExtra("id_line", getIntent().getStringExtra("line_number"));
                    intent.putExtra("color", getIntent().getStringExtra("color"));
                    startActivity(intent);
                }
            }
        });
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

                            System.out.println(id);
                            System.out.println("LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
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
            ListAdapter adapter = new ListStop(InfoStationActivy.this, R.layout.list_stop, stopList);
            System.out.println(url_stops);
            lvi.setAdapter(adapter);

        }
    }

    // Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.lines:
                Intent aboutIntent_lines = new Intent(InfoStationActivy.this, MainActivity.class);
                startActivity(aboutIntent_lines);
                return true;
            case R.id.itineraire:
                Intent aboutIntent_iti = new Intent(InfoStationActivy.this, journeys.class);
                startActivity(aboutIntent_iti);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
