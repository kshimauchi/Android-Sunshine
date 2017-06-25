/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mWeatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        /*
         * Using findViewById, we get a reference to our TextView from xml. This allows us to
         * do things like set the text of the TextView.
         */
        mWeatherTextView = (TextView) findViewById(R.id.tv_weather_data);

        // TODO (4) Delete the dummy weather data. You will be getting REAL data from the Internet in this lesson.
        /*
         * This String array contains dummy weather data. Later in the course, we're going to get
         * real weather data. For now, we want to get something on the screen as quickly as
         * possible, so we'll display this dummy data.
         */
        /**
         String[] dummyWeatherData = {
         "Today, May 17 - Clear - 17&deg;C / 15&deg;C",
         "Tomorrow - Cloudy - 19&deg;C / 15&deg;C",
         "Thursday - Rainy- 30&deg;C / 11&deg;C",
         "Friday - Thunderstorms - 21&deg;C / 9&deg;C",
         "Saturday - Thunderstorms - 16&deg;C / 7&deg;C",
         "Sunday - Rainy - 16&deg;C / 8&deg;C",
         "Monday - Partly Cloudy - 15&deg;C / 10&deg;C",
         "Tue, May 24 - Meatballs - 16&deg;C / 18&deg;C",
         "Wed, May 25 - Cloudy - 19&deg;C / 15&deg;C",
         "Thu, May 26 - Stormy - 30&deg;C / 11&deg;C",
         "Fri, May 27 - Hurricane - 21&deg;C / 9&deg;C",
         "Sat, May 28 - Meteors - 16&deg;C / 7&deg;C",
         "Sun, May 29 - Apocalypse - 16&deg;C / 8&deg;C",
         "Mon, May 30 - Post Apocalypse - 15&deg;C / 10&deg;C",
         };
         */
        // TODO (3) Delete the for loop that populates the TextView with dummy data
        /**
         * Iterate through the array and append the Strings to the TextView. The reason why we add
         * the "\n\n\n" after the String is to give visual separation between each String in the
         * TextView. Later, we'll learn about a better way to display lists of data.
         */

        /*
        for (String dummyWeatherDay : dummyWeatherData) {
            mWeatherTextView.append(dummyWeatherDay + "\n\n\n");
        }
        */
        // TODO (9) Call loadWeatherData to perform the network request to get the weather
        //loads the weather Data
        callWeatherData();
    }

    // TODO (8) Create a method that will get the user's preferred location and execute your new AsyncTask and call it loadWeatherData
    private void callWeatherData() {
        String location = SunshinePreferences.getPreferredWeatherLocation(this);
        //FetchWeatherTask()...
        new getWeatherTask().execute(location);
    }
    // TODO (5) Create a class that extends AsyncTask to perform network requests
    //	android.os.AsyncTask<Params, Progress, Result>
    public class getWeatherTask extends AsyncTask<String, Void, String[]> {
    /*AsyncTask must be subclassed to be used. The subclass will override
    at least one method (doInBackground(Params...)),
    and most often will override a second one (onPostExecute(Result).
    *AsyncTask is designed to be a helper class around Thread and Handler and does not constitute a generic threading framework.
    * AsyncTasks should ideally be used for short operations (a few seconds at the most.) If you need to keep threads running for
    * long periods of time,
    * it is highly recommended you use the various APIs provided by the java.util.concurrent package such as Executor,
    * ThreadPoolExecutor and FutureTask.
    **/
        @Override
        protected String[] doInBackground(String... params) {
            // TODO (6) Override the doInBackground method to perform your network requests

            //NO ZIP CODE, NO DATA!
            if (params.length == 0) {
                return null;
            }
            String location = params[0];
            //URL
            URL requestURL = NetworkUtils.buildUrl(location);

            try {

                //jsonWeatherResponse: returns entire result to string then
                String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(requestURL);
                //class and method parses json response
                String[] simpleJsonWeatherData = OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(
                        MainActivity.this
                        , jsonWeatherResponse);

                return simpleJsonWeatherData;

            } catch (Exception e) {

                e.printStackTrace();

                return null;
            }
        }

        // TODO (7) Override the onPostExecute method to display the results of the network request
        @Override
        protected void onPostExecute(String[] weather_data) {
            if (weather_data != null) {
                /**
                 * Iterate through the array and append the Strings to the TextView. The reason why we add
                 * the "\n\n\n" after the String is to give visual separation between each String in the
                 * TextView. Later, we'll learn about a better way to display lists of data.
                 */
                for (String weatherString : weather_data) {

                    mWeatherTextView.append((weatherString) + "\n\n\n");
                }
            }
        }
    }
}
