package edu.kvcc.cis298.cis298assignment4;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeffrey Martin.
 */
public class BeverageFetcher {
    // String constant for logging
    private static final String TAG ="Beverage";

    //Method to get the raw bytes from the web source
    private byte[] getURLBytes (String urlSpec) throws IOException {

        URL url = new URL(urlSpec);//  Get a new URL object from the url string that was passed in.


        HttpURLConnection connection = (HttpURLConnection)url.openConnection();   //Create a new HTTP connection to the specified url.
        Log.d(TAG, "Inside BeverageFetcher.java inside private byte, after creating new HTTP connection to the URL =" + url);

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(); //Create an output stream to hold the data that is read from the url source
            Log.d(TAG, "Inside BeverageFetcher.java inside private byte, after creating new output stream fails right after here here!!!!!!!");

            InputStream in = connection.getInputStream();  //Create an input stream from the HTTP connection.
            Log.d(TAG, "Inside BeverageFetcher.java inside private byte, after creating new input stream HTTP connection");
            Log.d(TAG, "Inside BeverageFetcher.java inside private byte, response code from HTTP= " + connection.getResponseCode());

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK){  //Check to see that the response code from the HTTP is not in the 200's
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }


            int bytesRead = 0; //Hold how many bytes were read in

            byte[] buffer = new byte[1024]; //Array to act as a buffer that will read in up to 1024 bytes at a time
            Log.d(TAG, "Inside BeverageFetcher.java inside private byte, after creating new byte buffer");

            while ((bytesRead = in.read(buffer))>0) {//Loop while we can read bytes from the input stream

                out.write(buffer, 0, bytesRead); //Write the bytes out to the output stram
            }
            Log.d(TAG, "Inside BeverageFetcher.java inside private byte, after read loop");


            out.close(); // Close the output stream now that everything has been read and written.
            Log.d(TAG, "Inside BeverageFetcher.java inside private byte, after closeing the output stream");

            return out.toByteArray(); //Convert the output stream  to a byte array and return

        }finally {

            Log.d(TAG, "Inside BeverageFetcher.java inside private byte, about to close connection");
            connection.disconnect(); //Make sure the connection to the web is closed.
        }
    }


    //Method to get the string result from the http web address.  The url bytes representing the data get returned from the getURLBytes method, and are then transformed int a string.
    private String getUrlString (String urlSpec) throws IOException {

        Log.d(TAG, "returned value from getUrlString =" + urlSpec);
        return new String(getURLBytes(urlSpec));
    }

    public List<Beverage> fetchBeverages(){
        List<Beverage> beverages = new ArrayList<>();
        Log.d(TAG, "Inside public List<Beverage> fetchBeverages() and created List<Beverages>");
        try {
            String url = Uri.parse("http://barnesbrothers.homeserver.com/beverageapi/").buildUpon().build().toString();// Take the URL and add parameters that are needed
            Log.d(TAG, "Inside public List<Beverage> fetchBeverages() and created String url");

            String jsonString = getUrlString(url); //Get the JSON from the above method using the new url just created.
            Log.d(TAG, "Inside public List<Beverage> fetchBeverages() and getting the JSON from getUrlString(url");

            JSONArray jsonArray = new JSONArray(jsonString);//Since the JSON starts out with an array we put the jsonString we just go into a JSONArray object.
            Log.d(TAG, "Inside public List<Beverage> fetchBeverages() and get the JSONArray object");

            parseBeverages(beverages, jsonArray); //Call the method below to parse out the jason into a usable form.

            Log.d(TAG, "Received JSON: " + jsonString);
        }catch (JSONException je){
            Log.e(TAG, "Received JSON: " + je);
        }catch (IOException ioe){
            Log.e(TAG, "Failed to fetch items ", ioe);
        }
        return beverages;
    }

    //Method to parse the beverage JSON into the Beverage Array
    private void parseBeverages(List<Beverage> beverages, JSONArray jsonArray) throws IOException, JSONException{

        Log.d(TAG, "inside of parseBeverages");
        //Loop through all the elements in the JSON array
        for (int i = 0; i<jsonArray.length(); i++){

            JSONObject beverageJsonObject = jsonArray.getJSONObject(i);//Get current index single JSONObject out of the JSONArray

            // Get each item out of the JSONObject and place in appropriate data type.
            String idString = beverageJsonObject.getString("id");
            String nameString = beverageJsonObject.getString("name");
            String packString = beverageJsonObject.getString("pack");
            double priceDouble = Double.parseDouble(beverageJsonObject.getString("price"));
            boolean isActiveBoolean = beverageJsonObject.getString("isActive").equals("1");

            //Create a new Beverage from the data just gathered
            Beverage beverage = new Beverage(idString,nameString,packString,priceDouble,isActiveBoolean);

            beverages.add(beverage);//Add the Beverage to the list.

        }


    }
}
