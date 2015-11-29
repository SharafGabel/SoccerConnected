package activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mathieu.myapplication2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


/**
 * Created by Yacine on 27/11/2015.
 */
public class detailActivity extends AppCompatActivity {

    private static  String	DETAILS_URL;
    private static final String TABLE = "Event";
    private String jsonString;
    public ProgressDialog progressDialog ;

    private String idEvent;
    private String nom;
    private String date;
    private String location;



    @Override
    public void onCreate(Bundle savedInstanceState) {



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("ID");
            idEvent = value;
        }
        super.onCreate(savedInstanceState);


        progressDialog =  new ProgressDialog(detailActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        //progressDialog.show();
        getEventDetails();

        setContentView(R.layout.detailsevent);


    }

    private void getEventDetails() {
        //Création d'un thread


        Thread t = new Thread() {
            public void run() {
                Looper.prepare();
                getJson();
                getParameterDetails();
                Looper.loop();
            }

        };
        t.start();
    }

    private void getJson() {



            try{
                DETAILS_URL = "https://footapp-sharaf.c9users.io/ConnectedSoccerPhp/web/api/events/"+idEvent;
                URL url = new URL(DETAILS_URL);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setDoInput(true);
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK)
                {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                    }
                    jsonString = sb.toString();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


    }

    private void createDialog(String title, String text)
    {
        // Création d'une popup affichant un message
        AlertDialog ad = new AlertDialog.Builder(this)
                .setPositiveButton("Ok", null).setTitle(title).setMessage(text)
                .create();
        ad.show();

    }

    private void getParameterDetails(){

        try{


            JSONObject jsonResponse = new JSONObject(jsonString);
            /*
            this.nom = jsonResponse.getJSONObject("event").getString("nom");
            this.date = jsonResponse.getJSONObject("event").getString("date");
            this.location = jsonResponse.getJSONObject("event").getString("lieu");

          // final TextView nameView = (TextView) findViewById(R.id.name);
            //nameView.setText("test");

            TextView lieuVieuw = (TextView) findViewById(R.id.location);
            lieuVieuw.setText(nom);
            TextView dateView = (TextView) findViewById(R.id.date);
            dateView.setText(nom);
            */

        }
        catch(JSONException e){
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }

    }


}
