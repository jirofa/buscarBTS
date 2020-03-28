
// 17/03/2020

package com.jirofaapp.buscarBTS;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    // Will show the string "data" that holds the results
    TextView results;
    // URL of object to be parsed    
    String JsonURL = "https://us2.unwiredlabs.com/v2/process.php?token=XXXXXX";

    private VolleySingleton singleton;  //Here calling the volley class

           // This string will hold the results
    String data = "";
    // Defining the Volley request queue that handles the URL request concurrently
   // RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Creates the Volley request queue
        singleton=VolleySingleton.getInstance(getApplicationContext());
       // requestQueue = Volley.newRequestQueue(this);

        // Casts results into the TextView found within the main layout XML with id jsonData
        results = (TextView) findViewById(R.id.jsonData);
        JSONObject objectCells = new JSONObject();

        try {
            JSONObject cellTower = new JSONObject();
            cellTower.put("mcc", 732);
            cellTower.put("mnc", 103);
            cellTower.put("lac", 315);
            cellTower.put("cid", 24267);
            JSONArray cellTowers = new JSONArray();
            cellTowers.put(cellTower);
            objectCells.put("cells", cellTowers);
            objectCells.put("address", 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Creating the JsonObjectRequest class called obreq, passing required parameters:
        //GET is used to fetch data from the server, JsonURL is the URL to be fetched from.

        JsonObjectRequest objreq = new JsonObjectRequest (Request.Method.POST, JsonURL, objectCells,
                // The third parameter Listener overrides the method onResponse() and passes
                //JSONObject as a parameter
               new Response.Listener<JSONObject>() {
                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject  response) {
                        try {
                            //the response JSON Object
                            //and converts them into javascript objects
                            String status = response.getString("status");
                            if (status.equals("ok")) {
                                // Adds strings from object to the "data" string
                                String lat = response.getString("lat");
                                String lon = response.getString("lon");
                                String dir = response.getString("address");
                                data += "Latitud : " + lat + (char)10+ (char)13 +
                                        "Longitud : " + lon + (char)10+ (char)13 +
                                        "Direccion : " + dir;
                            }
                            else{
                                data += "NO se encontro";
                            }
                            // Adds the data string to the TextView "results"
                            results.setText(data);
                         }
                        // Try and catch are included to handle any errors due to JSON
                          catch (JSONException e) {
                            // If an error occurs, this prints the error to the log
                               e.printStackTrace();
                        }
                    }
                },
                // The final parameter overrides the method onErrorResponse() and passes VolleyError as a parameter
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                    }
                }
        );
        // Adds the JSON object request "obreq" to the request queue
        //requestQueue.add(obreq);
        singleton.addToRequestQueue(objreq);
    }
}
