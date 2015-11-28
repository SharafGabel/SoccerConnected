package activity;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yacine on 26/11/2015.
 */
public class listEventActivity extends AppCompatActivity {

    private static final String	LIST_EVENT_URL	= "https://footapp-sharaf.c9users.io/ConnectedSoccerPhp/web/api/events";
    private String jsonString;
    List<Map<String,String>> events = new ArrayList<Map<String,String>>();
    ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listevent);
        mListView = (ListView) findViewById(R.id.listViewEvent);
        getEvents();
    }

    //region getEvents
    private void getEvents() {
        //Création d'un thread
        Thread t = new Thread()
        {
            public void run()
            {
                Looper.prepare();
                getJsonString();
                initList();
                Looper.loop();
            }

        };
        t.start();

        if(!t.isInterrupted() ) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(listEventActivity.this, events, android.R.layout.simple_list_item_1, new String[]{"events"}, new int[]{android.R.id.text1});
        mListView.setAdapter(simpleAdapter);
    }
    //endregion getEvents

    //region Utils

    private void getJsonString()
    {
        try{
            URL url = new URL(LIST_EVENT_URL);
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

    //region JsonStringToListView

    private void initList(){

        try{
            JSONObject jsonResponse = new JSONObject(jsonString);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("events");

            for(int i = 0; i<jsonMainNode.length();i++){
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String id = jsonChildNode.optString("id");
                String nom = jsonChildNode.optString("nom");
                String date = jsonChildNode.optString("date");
                String lieu = jsonChildNode.optString("lieu");
                String outPut = nom + "-" +id +"-" +date +"-" +lieu;
                events.add(createEvents("events", outPut));
            }
        }
        catch(JSONException e){
            Toast.makeText(getApplicationContext(), "Error"+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private HashMap<String, String>createEvents(String name,String number){
        HashMap<String, String> eventsNameNo = new HashMap<String, String>();
        eventsNameNo.put(name, number);
        return eventsNameNo;
    }

    //endregion JsonStringToListView

    //endregion Utils

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

