package activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.mathieu.myapplication2.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import services.ConnexionHTTP;

/**
 * Created by Yacine on 26/11/2015.
 */
public class listEventActivity extends AppCompatActivity {
    //private ConnexionHTTP connexionHTTP;
    private static final String	LIST_EVENT_URL	= "https://footapp-sharaf.c9users.io/get_Evenement.php";
    //private static final String	UPDATE_URL	= "https://footapp-sharaf.c9users.io/login.1.php";
    private static final String TABLE = "Event";
    private HashMap<String,String> map = new HashMap<String,String>();
    private List<String> listeEv;
    ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.listevent);
        getEvents();
        //  listeEv = connexionHTTP.selectionData(LIST_EVENT_URL,TABLE,map);
        listeEv = new ArrayList<String>();
        listeEv.add("Event1");
        listeEv.add("Event2");

       /* mListView = (ListView) findViewById(R.id.listViewEvent);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(listEventActivity.this,
                android.R.layout.simple_list_item_1, listeEv);
    */
        mListView = (ListView) this.findViewById(R.id.listViewEvent);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listeEv);
        mListView.setAdapter(adapter);
        getEvents();
    }

    private void getEvents() {
        // Création d'un thread
        Thread t = new Thread()
        {

            public void run()
            {

                Looper.prepare();
                try {
                    URL url = new URL(LIST_EVENT_URL);
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);

                    /*A ne jamais mettre lorsque l'on récupère des données json*/
                    //connection.setChunkedStreamingMode(0);

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
                        createDialog("Evènement",json);
                    }
                    else
                    {
                        json="Erreur ";
                        createDialog("Ereur Evènement",json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Looper.loop();

            }

        };

        t.start();
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
    //endregion UtilsActivity

}

