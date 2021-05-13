package student.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {


    final String APP_ID = "858c8d05373e0a12d059a64cdaba1ba1";
    final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";

    final long MIN_TIME = 1000;
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 101;

    String Location_Provider = LocationManager.GPS_PROVIDER;
    TextView CITYNAME, WEATHERSTATE, TEMPERATURE, HUMIDITY, WIND, TEMP_MAX, TEMP_MIN;
    ImageView WEATHERICON;
    RelativeLayout CITYFINDER;
    LocationManager LOCATIONMANAGER;
    LocationListener LOCATIONLISTENER;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WEATHERSTATE = findViewById(R.id.condition);
        TEMPERATURE = findViewById(R.id.temperature);
        WEATHERICON = findViewById(R.id.weatherIcon);
        CITYFINDER = findViewById(R.id.relocate);
        CITYNAME = findViewById(R.id.cityName);
        HUMIDITY = findViewById(R.id.humidity);
        WIND = findViewById(R.id.wind);
        TEMP_MAX = findViewById(R.id.temp_max);
        TEMP_MIN = findViewById(R.id.temp_min);

        CITYFINDER.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, city.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent mIntent=getIntent();
        String city= mIntent.getStringExtra("City");
        if(city!=null)
        {
            getWeatherForNewCity(city);
        }
        else
        {
            getWeatherForCurrentLocation();
        }
    }

    private void getWeatherForNewCity(String city)
    {
        RequestParams params=new RequestParams();
        params.put("q",city);
        params.put("appid",APP_ID);
        networkingStuff(params);
    }

    private void getWeatherForCurrentLocation() {

        LOCATIONMANAGER = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LOCATIONLISTENER = location -> {

            String Latitude = String.valueOf(location.getLatitude());
            String Longitude = String.valueOf(location.getLongitude());

            RequestParams params =new RequestParams();
            params.put("lat" ,Latitude);
            params.put("lon",Longitude);
            params.put("appid",APP_ID);
            networkingStuff(params);


        };


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        LOCATIONMANAGER.requestLocationUpdates(Location_Provider, MIN_TIME, MIN_DISTANCE, LOCATIONLISTENER);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if(requestCode==REQUEST_CODE)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(MainActivity.this,"Location recognized",Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation();
            }

        }


    }



    private  void networkingStuff(RequestParams params)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(WEATHER_URL,params,new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Toast.makeText(MainActivity.this,"City Weather Updated",Toast.LENGTH_SHORT).show();

                weatherData weatherZ=weatherData.fromJson(response);
                updateUI(weatherZ);


                // super.onSuccess(statusCode, headers, response);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });



    }

    private  void updateUI(weatherData weather){


        TEMPERATURE.setText(weather.getzTemperature());
        CITYNAME.setText(weather.getzCity());
        WEATHERSTATE.setText(weather.getzWeatherType());
        int resourceID=getResources().getIdentifier(weather.getzIcon(),"drawable",getPackageName());
        WEATHERICON.setImageResource(resourceID);
        HUMIDITY.setText(weather.getzHumidity());
        WIND.setText(weather.getzWind());
        TEMP_MAX.setText(weather.getzTemp_Max());
        TEMP_MIN.setText(weather.getzTemp_Min());


    }

    @Override
    protected void onPause() {
        super.onPause();
        if(LOCATIONMANAGER!=null)
        {
            LOCATIONMANAGER.removeUpdates(LOCATIONLISTENER);
        }
    }
}