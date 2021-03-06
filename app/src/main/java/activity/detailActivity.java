package activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mathieu.myapplication2.R;

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
    private String jsonString;
    public ProgressDialog progressDialog ;

    private String idEvent;
    private String nom;
    private String date;
    private String location;

    private TextView nomTV;
    private TextView dateTV;
    private TextView locationTV;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("ID");
            idEvent = value;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailsevent);
        findViewsByID();

        /*progressDialog =  new ProgressDialog(detailActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.show();*/
        getEventDetails();


        Button b = (Button) this.findViewById(R.id.addPlayer);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddPlayer = new Intent(detailActivity.this, searchPlayer.class);
                String message = idEvent;
                intentAddPlayer.putExtra("ID_EVENT", message);
                startActivity(intentAddPlayer);
            }
        });

    }

    private void getEventDetails() {
        //Création d'un thread
        Thread t = new Thread() {
            public void run() {
                Looper.prepare();
                getJson();
                initDetails();
                Looper.loop();
            }
        };
        t.start();

        if(!t.isInterrupted())
        {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        setViewsByID();
    }

    private void getJson() {
            try{
                DETAILS_URL="https://footapp-sharaf.c9users.io/ConnectedSoccerPhp/web/api/events/"+idEvent;
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
                //connection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    //region UtilsActivity
    private void createDialog(String title, String text)
    {
        // Création d'une popup affichant un message
        AlertDialog ad = new AlertDialog.Builder(this)
                .setPositiveButton("Ok", null).setTitle(title).setMessage(text)
                .create();
        ad.show();
    }

    private void findViewsByID()
    {
        nomTV = (TextView) findViewById(R.id.name);
        locationTV = (TextView) findViewById(R.id.location);
        dateTV = (TextView) findViewById(R.id.date);
    }

    private void setViewsByID()
    {
        nomTV.setText(nom);
        dateTV.setText(date);
        locationTV.setText(location);
    }

    //endregion UtilsActivity

    //region JsonStringToTextView
    private void initDetails(){
        try{
            JSONObject jsonResponse = new JSONObject(jsonString);
            JSONObject jsonMainNode = jsonResponse.optJSONObject("event");
            String id = jsonMainNode.optString("id");
            nom = jsonMainNode.optString("nom");
            date = jsonMainNode.optString("date");
            location = jsonMainNode.optString("lieu");
            String output=  id+"-"+nom+"-"+date+"-"+location;

        }
        catch(JSONException e){
            Toast.makeText(getApplicationContext(), "Error"+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    //endregion JsonStringToTextView

}
