package com.example.mathieu.myapplication2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * Created by Sharaf on 23/11/2015.
 */
public class EventActivity_V2 extends AppCompatActivity implements View.OnClickListener {

    //region Attributs
    private static final String	UPDATE_URL	= "https://footapp-sharaf.c9users.io/insert_Evenement.php";
    private static final String TABLE = "Event";
    private HashMap<String,String> map = new HashMap<String,String>();

    private EditText nomT;
    private EditText lieuT;
    private EditText dateT;
    private Button button;

    private DatePickerDialog fromDate;

    private SimpleDateFormat dateFormat;
    //endregion Attributs

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);
        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.FRANCE);
        findViewsById();
        setDateTimeField();

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

    // Récupération des éléments de la vue définis dans le xml
    private void findViewsById()
    {
        nomT = (EditText) findViewById(R.id.event_name);
        lieuT = (EditText) findViewById(R.id.event_lieu);

        dateT = (EditText) findViewById(R.id.event_date);
        dateT.setInputType(InputType.TYPE_NULL);
        dateT.requestFocus();

        button = (Button) findViewById(R.id.createEvent);
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

    private void setParams(String nom, String lieu, String date) {
        map.put("nom", nom);
        map.put("lieu",lieu);
        map.put("date", date);
    }

    private void setDateTimeField() {
        dateT.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateT.setText(dateFormat.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    @Override
    public void onClick(View view) {
        if(view == dateT) {
            fromDate.show();
        }
    }
    //endregion Utils

    //region UtilsActivity
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
    //endregion UtilsActivity
}
