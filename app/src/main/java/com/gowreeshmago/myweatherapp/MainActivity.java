package com.gowreeshmago.myweatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    //ui components
    TextView textView;
    Button button;
    EditText editText;


    //variables;
    double latitude=0;
    double longitude=0;
    double curretTemperature=0;
    String weatherDescription=null;
    String cityName=null;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);
        editText = findViewById(R.id.editText);

        textView.setVisibility(View.GONE);



        button.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if(editText.getText().toString().isEmpty()){


                    Toast.makeText(MainActivity.this, "Enter city", Toast.LENGTH_SHORT).show();




                }
                else{


                    GetDataFromWeb getDataFromWeb = new GetDataFromWeb();
                    getDataFromWeb.execute(editText.getText().toString());
                }

            }
        });


    }


    private class GetDataFromWeb extends AsyncTask<String,Void,JSONObject>{
        @Override
        protected JSONObject doInBackground(String... strings) {


            String city = strings[0];


            try{


                Log.d("fetching data", "doInBackground: ");
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q="
                        +city+"&APPID=0d3ebc136e8ee3da2e6e3a12ad1a79ff");

                HttpURLConnection connection =(HttpURLConnection)url.openConnection();

                if(connection.getResponseCode()==HttpURLConnection.HTTP_OK){

                    StringBuilder buider = new StringBuilder();

                    BufferedReader bufferedReader = new BufferedReader(
                                                        new InputStreamReader(
                                                                connection.getInputStream()
                                                        )

                    );

                    String line;
                    while((line=bufferedReader.readLine())!=null){

                        buider.append(line);
                    }

                    return new JSONObject(buider.toString());



                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;


        }


        @Override
        protected void onPostExecute(JSONObject weather) {


            try{

                cityName = weather.getString("name");
                curretTemperature = weather.getJSONObject("main").getDouble("temp")-273.15;
                weatherDescription = weather.getJSONArray("weather").getJSONObject(0).getString("description");
                latitude = weather.getJSONObject("coord").getDouble("lat");
                longitude = weather.getJSONObject("coord").getDouble("lon");

            } catch (JSONException e) {
                e.printStackTrace();
            }


            result ="City name = "+cityName+"\n"
                    +"Latitude = "+latitude+"\n"
                    +"Longitude = "+longitude+"\n"
                    +"Current Temperature = "+curretTemperature+"\n"
                    +"Weather description:-" +weatherDescription+"\n";



            textView.setText(result);
            textView.setVisibility(View.VISIBLE);


        }
    }
}











