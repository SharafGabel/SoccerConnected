package activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.mathieu.myapplication2.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Yacine on 30/11/2015.
 */
public class searchPlayer extends AppCompatActivity {
    private String idEvent;
    private SearchView search;
    private static String URL_RESEARCH_PLAYER;
    private ListView listeResult;

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
        listeResult= (ListView) findViewById(R.id.resultSearch);
        search=(SearchView) findViewById(R.id.searchPalyerView);

        //*** setOnQueryTextFocusChangeListener ***
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub

                Toast.makeText(getBaseContext(), query,
                        Toast.LENGTH_SHORT).show();
                searchPlayerDb(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }


        });
    }

    private void searchPlayerDb(String recherche)
    {
        try{
            URL_RESEARCH_PLAYER="";
            URL url = new URL(URL_RESEARCH_PLAYER);
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





    private void createDialog(String title, String text)
    {
        // Création d'une popup affichant un message
        AlertDialog ad = new AlertDialog.Builder(this)
                .setPositiveButton("Ok", null).setTitle(title).setMessage(text)
                .create();
        ad.show();
    }



}
