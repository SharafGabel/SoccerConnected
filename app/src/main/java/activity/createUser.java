package activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mathieu.myapplication2.R;

import java.io.IOException;
import java.util.HashMap;

import services.ConnexionHTTP;

/**
 * Created by Narkan on 01/12/2015.
 */
public class createUser extends AppCompatActivity
{
    // URL pour creer un nouvel utilisateur en utilisant l'API Symfony 2 :
    private static final String	UPDATE_URL = "https://footapp-sharaf.c9users.io/ConnectedSoccerPhp/web/api/users";
    private ConnexionHTTP connexionHTTP;

    private static final String TABLE = "Event";
    private HashMap<String,String> map = new HashMap<String,String>();

    private EditText usernameT;
    private EditText emailT;
    private EditText passwordT;
    //endregion Attributs

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user);

        // Recuperation des elements de la vue definis dans le xml
        usernameT = (EditText) findViewById(R.id.create_user_username);
        emailT    = (EditText) findViewById(R.id.create_user_email);
        passwordT = (EditText) findViewById(R.id.create_user_password);

        Button button = (Button) findViewById(R.id.create_user_button_1);

        // Definition du listener du bouton
        button.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                int nomsize = usernameT.getText().length();
                int lieusize = emailT.getText().length();
                int datesize = passwordT.getText().length();

                if (nomsize > 0 && lieusize > 0 && datesize > 0)
                {

                    String nom = usernameT.getText().toString();
                    String lieu = emailT.getText().toString();
                    String dateTemp = passwordT.getText().toString();

                    setParams(nom,lieu,dateTemp);
                    try {
                        if (connexionHTTP.insertData(UPDATE_URL, TABLE, map)) {
                            //createDialog("Creation Evenement", "Evenement cree avec succes !");

                            //Retour au menu principal
                                    /*Intent secondeActivite = new Intent(EventActivity.this, MenuApp.class);
                                    startActivity(secondeActivite);*/
                        } else {
                            createDialog("Cr�ation Evenement", "Cr�ation de l'�v�nement �chou�e");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
                else
                    createDialog("Cr�ation Evenement", "Cr�ation de l'�v�nement �chou�e");

            }
        });

        button = (Button) findViewById(R.id.create_user_button_2);
        // Creation du listener du bouton cancel (on sort de l'appli)
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
        // On envoie un resultat qui va permettre de quitter l'appli
        setResult((success) ? Activity.RESULT_OK : Activity.RESULT_CANCELED, i);
        finish();

    }

    public void createDialog(String title, String text)
    {
        // Creation d'une popup affichant un message
        AlertDialog ad = new AlertDialog.Builder(this)
                .setPositiveButton("Ok", null).setTitle(title).setMessage(text)
                .create();
        ad.show();

    }
    //endregion Utils


} // public class createUser
