
package activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mathieu.myapplication2.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import services.ConnexionHTTP;

/**
 * Created by Sharaf on 18/11/2015.
 */

public class EventActivity extends AppCompatActivity{

    //region Attributs
    private ConnexionHTTP connexionHTTP;
    private static final String	UPDATE_URL	= "https://footapp-sharaf.c9users.io/insert_Evenement.php";
    //private static final String	UPDATE_URL	= "https://footapp-sharaf.c9users.io/login.1.php";
    private static final String TABLE = "Event";
    private HashMap<String,String> map = new HashMap<String,String>();
    //endregion Attributs

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        // Récupération des éléments de la vue définis dans le xml
        final EditText nomT = (EditText) findViewById(R.id.event_name);
        final EditText lieuT = (EditText) findViewById(R.id.event_lieu);
        final EditText dateT = (EditText) findViewById(R.id.event_date);

        Button button = (Button) findViewById(R.id.createEvent);

        // Définition du listener du bouton
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
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }*/

                    setParams(nom,lieu,dateTemp);
                            try {
                                if (connexionHTTP.insertData(UPDATE_URL, TABLE, map)) {
                                    //createDialog("Création Evenement", "Evenement créé avec succès !");

                                    //Retour au menu principal
                                    /*Intent secondeActivite = new Intent(EventActivity.this, MenuApp.class);
                                    startActivity(secondeActivite);*/
                                } else {
                                    createDialog("Création Evenement", "Création de l'évènement échouée");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                }
                else
                    createDialog("Création Evenement", "Création de l'évènement échouée");

            }
        });

        button = (Button) findViewById(R.id.cancelEvent);
        // Création du listener du bouton cancel (on sort de l'appli)
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                quit(false, null);
            }

        });

    }

    //region Utils
    private void setParams(String nom, String lieu, String date) {
        map.put("nom", nom);
        map.put("lieu",lieu);
        map.put("date", date);
    }

    private void quit(boolean success, Intent i)
    {
        // On envoie un résultat qui va permettre de quitter l'appli
        setResult((success) ? Activity.RESULT_OK : Activity.RESULT_CANCELED, i);
        finish();

    }

    public void createDialog(String title, String text)
    {
        // Création d'une popup affichant un message
        AlertDialog ad = new AlertDialog.Builder(this)
                .setPositiveButton("Ok", null).setTitle(title).setMessage(text)
                .create();
        ad.show();

    }
    //endregion Utils
}
