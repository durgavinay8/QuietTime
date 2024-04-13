package com.example.quiettimeapp.GeneralLocations;

import static java.lang.System.exit;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.quiettimeapp.BuildConfig;
import com.example.quiettimeapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;
import com.google.api.client.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class LocationUpdatesReceiver extends BroadcastReceiver {
    double latitude, longitude;
    PlacesClient placesClient;
    LocationManager locationManager;
    @SuppressLint("NewApi")
    @Override
    public void onReceive(Context context, Intent intent) {

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if(!locationManager.isLocationEnabled()){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Location is disabled");
            builder.setTitle("Enable the locations to avail the feature");
            builder.setNeutralButton("OK", (DialogInterface.OnClickListener)(dialog, which) ->{
                dialog.cancel();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return;
        }

        Location location = intent.getExtras().getParcelable(LocationManager.KEY_LOCATION_CHANGED);
        Log.d("ekkada","in LocationUpdateReciever");
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.d("ekkada", "latitude : " + String.valueOf(latitude )+"longitude" + String.valueOf(longitude));
        }

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + String.valueOf(latitude ) + "," + String.valueOf(longitude));
        googlePlacesUrl.append("&radius=" + 500);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + BuildConfig.MAPS_API_KEY);
//        new PlaceTask().execute(String.valueOf(googlePlacesUrl));
        Log.d("ekkada", googlePlacesUrl.toString());

        // TODO: 17-05-2023 code from here





        Places.initialize(context, BuildConfig.MAPS_API_KEY);
        placesClient = Places.createClient(context);

        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.TYPES );
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

        @SuppressWarnings("MissingPermission") final Task<FindCurrentPlaceResponse> placeResult = placesClient.findCurrentPlace(request);
        placeResult.addOnCompleteListener (new OnCompleteListener<FindCurrentPlaceResponse>() {
            @Override
            public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    FindCurrentPlaceResponse likelyPlaces = task.getResult();
                    for (PlaceLikelihood placeLikelihood : likelyPlaces.getPlaceLikelihoods()) {
                        Log.d("ekkada",placeLikelihood.getPlace().getName() +"-"+placeLikelihood.getLikelihood()+"-"+placeLikelihood.getPlace().getTypes());
                    }
                }
                else {
                    Log.e("ekkada", "Exception: %s", task.getException());
                }
            }
        });

    }
    /*
    private static final String PLACES_SEARCH_URL =  "https://maps.googleapis.com/maps/api/place/search/json?";

    private static final boolean PRINT_AS_STRING = false;


    public void performSearch() throws Exception {
        try {
            System.out.println("Perform Search ....");
            System.out.println("-------------------");
            HttpRequestFactory httpRequestFactory = createRequestFactory(transport);
            HttpRequest request = httpRequestFactory.buildGetRequest(new GenericUrl(PLACES_SEARCH_URL));
            request.getUrl().put("key", BuildConfig.MAPS_API_KEY);
            request.getUrl().put("location", latitude + "," + longitude);
            request.getUrl().put("radius", 500);
            request.getUrl().put("sensor", "false");

            if (PRINT_AS_STRING) {
                System.out.println(request.execute().parseAsString());
            } else {

                PlacesList places = request.execute().parseAs(PlacesList.class);
                System.out.println("STATUS = " + places.status);
                for (Place place : places.results) {
                    System.out.println(place);
                }
            }

        } catch (HttpResponseException e) {
            System.err.println(e.response.parseAsString());
            throw e;
        }
    }

    private static final HttpTransport transport = new ApacheHttpTransport();

    public static <GoogleHeaders> HttpRequestFactory createRequestFactory(final HttpTransport transport) {

        return transport.createRequestFactory(new HttpRequestInitializer() {
            public void initialize(HttpRequest request) {
                GoogleHeaders headers = new GoogleHeaders();
                headers.setApplicationName("Google-Places-DemoApp");
                request.setHeaders((HttpHeaders) headers);
                JsonHttpParser parser = new JsonHttpParser();
                parser.jsonFactory = new JacksonFactory();
                request.addParser(parser);
            }
        });
    }
/*
    private class PlaceTask extends AsyncTask<String,Integer,String> {
        @Override
        protected String doInBackground(String... strings)  {
            String data =null;
            try
            {
                data= downloadUrl(strings[0]);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            return data;
        }
        @Override
        protected void onPostExecute(String s){
            //execute ParseTask
            new ParseTask().execute(s);
            super.onPostExecute(s);
        }


        private String downloadUrl(String string) throws IOException {
            //initialize url
            URL googlePlacesUrl=new URL(string);
            //initialize connection
            HttpURLConnection connection=(HttpURLConnection) googlePlacesUrl.openConnection();
            //connect connection
            connection.connect();
            //iniitalize input stream
            InputStream stream =connection.getInputStream();
            //initialize buffer reader
            BufferedReader reader= new BufferedReader(new InputStreamReader(stream));
            //Initialize string builder
            StringBuilder builder=new StringBuilder();
            //initialize string variable
            String line ="";
            //Use whiel loop
            while((line=reader.readLine()) !=null)
            {
                builder.append(line);
            }
            String data = builder.toString();
            reader.close();
            return data;




        }
    }

    private class ParseTask extends AsyncTask<String,Integer,List<HashMap<String,String>>> {
        //Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            //create json parser class
            JsonParser parser = new JsonParser();
            List<HashMap<String, String>> mapList = null;
            JSONObject object = null;

            if(strings[0]==null){
                Log.d("ekkada","strings is null");
                Log.d("ekkada","strings is null");
                Log.d("ekkada","strings is null");
                Log.d("ekkada","strings is null");
                Log.d("ekkada","strings is null");
                Log.d("ekkada","strings is null");

                exit(1);
            }
            else {
                Log.d("ekkada","strings is NOT null");
                Log.d("ekkada","strings is NOT null");
                Log.d("ekkada","strings is NOT null");
                Log.d("ekkada","strings is NOT null");
                Log.d("ekkada","strings is NOT null");

            }
            try {
                //intialize json object
                object = new JSONObject(strings[0]);

                //parse json object
                mapList = JsonParser.parseResult(object);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            //clear map
            GoogleMap map = null;
            map.clear();
            for (int i = 0; i < hashMaps.size(); i++) {
                HashMap<String, String> hashMapList = hashMaps.get(i);
                //get latitude
                double lat = Double.parseDouble(hashMapList.get("lat"));
                //get long
                double lng = Double.parseDouble(hashMapList.get("lng"));
                //get name
                String name = hashMapList.get("name");
                //concat latitude and longitude
                LatLng latlng = new LatLng(lat, lng);
                MarkerOptions options = new MarkerOptions();

                //setposition
                options.position(latlng);
                //set title
                options.title(name);
                //add marker on map
                map.addMarker(options);

            }
        }
    }
 */
}
