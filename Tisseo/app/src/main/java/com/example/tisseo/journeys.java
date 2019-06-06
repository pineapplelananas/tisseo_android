package com.example.tisseo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class journeys extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journeys);
        //http://api.tisseo.fr/v1/journeys.json?departurePlace=basso%20cambo%20&arrivalPlace=fran%C3%A7ois%20verdier%20toulouse&number=2&displayWording=1&key=48e242b6-a196-40ec-8192-74bc0d76eda1
    }
}
