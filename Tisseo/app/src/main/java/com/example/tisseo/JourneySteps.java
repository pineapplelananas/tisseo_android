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

public class JourneySteps extends AppCompatActivity {
    String start;
    String end;
    String duration_a;
    String departureDateTime_a;
    ArrayList<String> stepsList;
    private ListView lv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_steps);
        //http://api.tisseo.fr/v1/journeys.json?departurePlace=basso%20cambo%20&arrivalPlace=fran%C3%A7ois%20verdier%20toulouse&number=2&displayWording=1&key=48e242b6-a196-40ec-8192-74bc0d76eda1

        start = getIntent().getStringExtra("start");
        end = getIntent().getStringExtra("end");

        TextView start_name = findViewById(R.id.start_name);
        TextView end_name = findViewById(R.id.end_name);

        start_name.setText(start);
        end_name.setText(end);

        stepsList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.station_list_view);

        new GetStations().execute();


    }
    private class GetStations extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(JourneySteps.this, "Json Data is downloading", Toast.LENGTH_LONG).show();
        }


        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("http://api.tisseo.fr/v1/journeys.json?departurePlace="+ start +"&arrivalPlace="+ end +"&number=1&displayWording=1&key=48e242b6-a196-40ec-8192-74bc0d76eda1");
            System.out.println(jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONObject stations_json = jsonObj.getJSONObject("routePlannerResult");
                    JSONArray stations_json_line = stations_json.getJSONArray("journeys");

                    // looping through All stations
                    for (int i = 0; i < stations_json_line.length(); i++) {
                        JSONObject c = stations_json_line.getJSONObject(i);
                        JSONObject d = c.getJSONObject("journey");

                        if(d != null && d.getString("duration") != null){
                            //System.out.println(c.getString("name"));
                            duration_a = d.getString("duration");
                            departureDateTime_a = d.getString("departureDateTime");

                            JSONArray chunks = d.getJSONArray("chunks");
                            for (int j = 0; i < chunks.length(); j++) {
                                JSONObject stop_json = chunks.getJSONObject(i);
                                JSONArray stop_json_stop = stop_json.getJSONArray("stop");
                                String name_stop = stop_json_stop.getString(7);
                                System.out.println(name_stop);
                                stepsList.add(j, name_stop);
                                break;
                            }
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
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            TextView date_name = findViewById(R.id.start_date);
            TextView duration_name = findViewById(R.id.duration);

            date_name.setText("durée : "+duration_a);
            duration_name.setText("Départe le : "+departureDateTime_a);
            //System.out.println("----------- Steps prepare------------");
            //ListAdapter adapter = new ListSteps(JourneySteps.this, R.layout.list_steps, stepsList);
            //lv.setAdapter(adapter);

        }
    }
}
