package activity;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Yacine on 26/11/2015.
 */
public class listEventActivity extends AppCompatActivity {
    //private ConnexionHTTP connexionHTTP;
    //private static final String	LIST_EVENT_URL	= "https://footapp-sharaf.c9users.io/get_Evenement.php";
    private static final String	LIST_EVENT_URL	= "https://footapp-sharaf.c9users.io/ConnectedSoccerPhp/web/api/events";
    //private static final String	UPDATE_URL	= "https://footapp-sharaf.c9users.io/login.1.php";
    private static final String TABLE = "Event";
    private HashMap<String,String> map = new HashMap<String,String>();
    private List<String> listeEv;
    private String jsonString; //= "{\"events\":[{\"id\":1,\"nom\":\"mathieu party\",\"lieu\":\"livry-gargan\",\"date\":\"20-12-2056\"},{\"id\":2,\"nom\":\"test\",\"lieu\":\"test\",\"date\":\"test\"}]}";
    //private ArrayList<HashMap<String,String>> listeEv;
    ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.listevent);
        mListView = (ListView) findViewById(R.id.listViewEvent);
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
                    //connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("GET");
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
                        jsonString = json;
                        initList();

                        try {
                            HashMap<String, String> map = new HashMap<String, String>();
                            JSONObject jObject = new JSONObject(json);
                            Iterator<?> keys = jObject.keys();

                            while( keys.hasNext() ){
                                String key = (String)keys.next();
                                String value = jObject.getString(key);
                                map.put(key, value);

                            }
                            List<HashMap<String, String>> data = new ArrayList<>();
                            data.add(map);
                           createDialog("Evènement",map.toString());
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            createDialog("Evènement","M");
                        }
                    }
                    else
                    {
                        json="Erreur ";
                        createDialog("Erreur Evènement",responseCode+"");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Looper.loop();

            }

        };

        t.start();
        if(t.isAlive()) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SimpleAdapter simpleAdapter = new SimpleAdapter(listEventActivity.this, events, android.R.layout.simple_list_item_1, new String[]{"events"}, new int[]{android.R.id.text1});
            mListView.setAdapter(simpleAdapter);
        }
    }

    //region JsonStringToListView
    List<Map<String,String>> events = new ArrayList<Map<String,String>>();
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
                events.add(createEmployee("events", outPut));
            }
        }
        catch(JSONException e){
            Toast.makeText(getApplicationContext(), "Error"+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private HashMap<String, String>createEmployee(String name,String number){
        HashMap<String, String> eventsNameNo = new HashMap<String, String>();
        eventsNameNo.put(name, number);
        return eventsNameNo;
    }
    //endregion JsonStringToListView

    //region Utils


    private List toList(JSONArray array) throws JSONException {
        List list = new ArrayList();
        int size = array.length();
        for (int i = 0; i < size; i++) {
            list.add(fromJson(array.get(i)));
        }
        return list;
    }

    //Converti json en Objet
    private Object fromJson(Object json) throws JSONException {
        if (json == JSONObject.NULL) {
            return null;
        } else if (json instanceof JSONObject) {
            return jsonToMap((JSONObject) json);
        } else if (json instanceof JSONArray) {
            return toList((JSONArray) json);
        } else {
            return json;
        }
    }

    //Converti Json en Map
    public Map<String, String> jsonToMap(JSONObject object) throws JSONException {
        Map<String, String> map = new HashMap();
        Iterator keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            map.put(key, fromJson(object.get(key)).toString());
        }
        return map;
    }

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

