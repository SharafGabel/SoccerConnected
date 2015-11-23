package com.example.mathieu.myapplication2;

import android.app.Activity;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Sharaf on 23/11/2015.
 */
public class EventActivity_V2 extends AppCompatActivity {

    //region Attributs
    private static final String	UPDATE_URL	= "https://footapp-sharaf.c9users.io/insert_Evenement.php";
    private static final String TABLE = "Event";
    private HashMap<String,String> map = new HashMap<String,String>();
    private EditText nomT;
    private EditText lieuT;
    private EditText dateT;
    //endregion Attributs

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        // Récupération des éléments de la vue définis dans le xml
        nomT = (EditText) findViewById(R.id.event_name);
        lieuT = (EditText) findViewById(R.id.event_lieu);
        dateT = (EditText) findViewById(R.id.event_date);
        Button button = (Button) findViewById(R.id.createEvent);

        button.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                int nomsize = nomT.getText().length();
                int lieusize = lieuT.getText().length();
                int datesize = dateT.getText().length();

                if (nomsize > 0 && lieusize > 0 && datesize > 0)
                {

                    String nom = nomT.getText().toString();
                    String lieu = lieuT.getText().toString();
                    String dateTemp = dateT.getText().toString();

                    /*
                    //Convertir String date en Date date
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    try {
                        date = formatter.parse(dateTemp);
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }*/
                    // On appelle la fonction createEvent qui va communiquer avec le PHP
                    createEvent(nom, lieu,dateTemp);
                }
                else
                    createDialog("Error", "Error during creation of event");

            }

        });
/*
        button = (Button) findViewById(R.id.cancelbutton);
        // Création du listener du bouton cancel (on sort de l'appli)
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                quit(false, null);
            }

        });
*/
    }

    private void quit(boolean success, Intent i)
    {
        // On envoie un résultat qui va permettre de quitter l'appli
        setResult((success) ? Activity.RESULT_OK : Activity.RESULT_CANCELED, i);
        finish();

    }

    private void createDialog(String title, String text)
    {
        // Création d'une popup affichant un message
        AlertDialog ad = new AlertDialog.Builder(this)
                .setPositiveButton("Ok", null).setTitle(title).setMessage(text)
                .create();
        ad.show();

    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
    private void createEvent(final String nom, final String lieu, final String date)
    {
        // Création d'un thread
        Thread t = new Thread()
        {

            public void run()
            {

                Looper.prepare();
                try {
                    URL url = new URL(UPDATE_URL);
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    /*A ne jamais mettre lorsque l'on récupère des données json*/
                    //connection.setChunkedStreamingMode(0);
                    setParams(nom,lieu,date);
                    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                    writer.write(getPostDataString(map));
                    writer.flush();
                    writer.close();
                    String json;
                    int responseCode = connection.getResponseCode();
                    if(responseCode == HttpURLConnection.HTTP_OK)
                    {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder sb=new StringBuilder();
                        String line=null;
                        while( (line=in.readLine())!=null)
                        {
                            sb.append(line);
                        }
                        json = sb.toString();
                        createDialog("toto",json);
                    }
                    else
                    {
                        json="Erreur ";
                        createDialog("toto","erreur");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Looper.loop();

            }

        };

        t.start();
    }

    //region Utils
    private void setParams(String nom, String lieu, String date) {
        map.put("nom", nom);
        map.put("lieu",lieu);
        map.put("date", date);
    }
    //endregion Utils

}
