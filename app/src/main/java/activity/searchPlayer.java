package activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.example.mathieu.myapplication2.R;

/**
 * Created by Yacine on 30/11/2015.
 */
public class searchPlayer extends AppCompatActivity {
    private String idEvent;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("ID_EVENT");
            this.idEvent = value;
        }
        else {
            createDialog("Erreur", "Erreur lors de la recupérations des informations. ");
            searchPlayer.this.stopService(this.getIntent());
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchplayer);
    }
    private void createDialog(String title, String text)
    {
        // Création d'une popup affichant un message
        AlertDialog ad = new AlertDialog.Builder(this)
                .setPositiveButton("Ok", null).setTitle(title).setMessage(text)
                .create();
        ad.show();
    }



}
