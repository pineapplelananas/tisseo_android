package com.example.tisseo;

import android.os.AsyncTask;
import android.provider.Settings;
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

public class PhysicPoint extends AppCompatActivity {
    private String url_pstops;
    private String url_pstops_hour;
    private StopTrip stop_trip;
    private StopTrip stop_trip_click;
    private ListView lvpp;
    private String key ="48e242b6-a196-40ec-8192-74bc0d76eda1";
    private ArrayList<StopTrip> pstopList;
    private String id_line;
    private String id_stop;
    private String name_area;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physic_point);

        pstopList = new ArrayList<>();
        lvpp = (ListView) findViewById(R.id.pstop_list);
        id_line = getIntent().getStringExtra("id_display");
        id_stop = getIntent().getStringExtra("id");


        TextView name = findViewById(R.id.pstop_name);
        TextView line = findViewById(R.id.pstop_line);

        name_area = getIntent().getStringExtra("name");
        name.setText(name_area);
        line.setText(getIntent().getStringExtra("id_line"));

        new GetPStops().execute();

    }

    private class GetPStops extends AsyncTask<Void, Void, ListView> {

        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(PhysicPoint.this, "Json Data is downloading", Toast.LENGTH_LONG).show();
        }

        @Override
        protected ListView doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            url_pstops = new String("https://api.tisseo.fr/v1/stop_points.json?displayDestinations=1&lineId="+id_line+"&id="+id_stop+"&key="+key);
            String jsonStr = sh.makeServiceCall(url_pstops);

            if (jsonStr != null) {
                String name_destination_a = "";
                String name_destination_b = "";
                int indice_a =0;
                int indice_b =0;
                boolean curent_indice = false;
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONObject stations_json = jsonObj.getJSONObject("physicalStops");
                    JSONArray stations_json_line = stations_json.getJSONArray("physicalStop");

                    // looping through All Contacts
                    for (int i = 0; i < stations_json_line.length(); i++) {
                        JSONObject c = stations_json_line.getJSONObject(i);

                        if(c.isNull("id")) {
                            System.out.println("ERRRROR----  !!!!!!!!");
                        }
                        else{

                            String id = c.getString("id");
                            // destination
                            JSONArray destinations = c.getJSONArray("destinations");
                            String current_dest = new String();
                            String id_dest = new String();
                            String current_city_dest = new String();
                            JSONObject dest_json = destinations.getJSONObject(0);
                            current_dest = dest_json.getString("name");
                            current_city_dest = dest_json.getString("cityName");
                            id_dest = dest_json.getString("id");

                            // lines
                            String string_lines = new String();
                            JSONArray array_lines_json = c.getJSONArray("lines");
                            JSONObject obj_json_lines = array_lines_json.getJSONObject(0);
                            string_lines = obj_json_lines.getString("short_name");

                            //stop name
                            String name = c.getString("name");

                            if(name_destination_a == ""){
                                name_destination_a = current_dest;
                                curent_indice = true;
                                indice_a = 0;
                            }
                            else if(name_destination_a == current_dest ){
                                indice_a+=1;
                                curent_indice = true;
                            }
                            if(name_destination_b == "") {
                                name_destination_b = current_dest;
                                indice_b = 0;
                                curent_indice = false;

                                }
                            else if (name_destination_b == current_dest) {
                                indice_b+=1;
                                curent_indice = false;
                                }

                            // city
                            JSONObject city_array_json = c.getJSONObject("stopArea");
                            String string_city = city_array_json.getString("cityName");

                            // hour*/
                            String string_hour = new String();
                            JSONArray hour_array_json = c.getJSONArray("operatorCodes");
                            JSONObject hour_obj_json = hour_array_json.getJSONObject(0);
                            string_hour = hour_obj_json.getString("operatorCode");
                            String[] parts = string_hour.split(":");
                            string_hour = parts[2];
                            string_hour= string_hour.replace('"','!');
                            String[] parts1 = string_hour.split("!");
                            string_hour = parts1[1];

                            HttpHandler sh_hour = new HttpHandler();
                            // Making a request to url and getting response
                            url_pstops_hour = new String("http://api.tisseo.fr/v1/stops_schedules.json?operatorCode="+ string_hour +"&key="+key);
                            String jsonStr_hour = sh.makeServiceCall(url_pstops_hour);
                            String string_hour_next = "";
                            int indice =0;

                            if (jsonStr_hour != null) {
                                JSONObject jsonObj_hour = new JSONObject(jsonStr_hour);
                                JSONObject hour_json = jsonObj_hour.getJSONObject("departures");
                                JSONArray hour_json_line = hour_json.getJSONArray("departure");

                                if(curent_indice){
                                    indice = indice_a;
                                }
                                else {
                                    indice = indice_b;
                                }

                                JSONObject hour_obj = hour_json_line.getJSONObject(indice);
                                string_hour_next = hour_obj.getString("dateTime");
                                }
                            // build listview
                            stop_trip = new StopTrip(id, string_city, current_dest, string_hour_next, string_lines, name);
                            pstopList.add(i, stop_trip);
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
            }   else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return lvpp;
        }

        @Override
        protected void onPostExecute(ListView result) {
            super.onPostExecute(result);
            // build listView
            ListAdapter adapter = new ListPhysicPoint(PhysicPoint.this, R.layout.list_physic_stop, pstopList);
            lvpp.setAdapter(adapter);
        }
    }
}
