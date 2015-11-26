package activity;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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

/**
 * Created by Yacine on 26/11/2015.
 */
public class listEventActivity extends AppCompatActivity {
    //private ConnexionHTTP connexionHTTP;
    //private static final String	LIST_EVENT_URL	= "https://footapp-sharaf.c9users.io/get_Evenement.php";
    private static final String	LIST_EVENT_URL	= "https://footapp-sharaf.c9users.io/ConnectedSoccerPhp/web/api/events/1";
    //private static final String	UPDATE_URL	= "https://footapp-sharaf.c9users.io/login.1.php";
    private static final String TABLE = "Event";
    private HashMap<String,String> map = new HashMap<String,String>();
    private List<String> listeEv;
    //private ArrayList<HashMap<String,String>> listeEv;
    ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.listevent);
        getEvents();
        //  listeEv = connexionHTTP.selectionData(LIST_EVENT_URL,TABLE,map);
        /*listeEv = new ArrayList<String>();
        listeEv.add("Event1");
        listeEv.add("Event2");*/

       /* mListView = (ListView) findViewById(R.id.listViewEvent);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(listEventActivity.this,
                android.R.layout.simple_list_item_1, listeEv);
    */
        /*mListView = (ListView) this.findViewById(R.id.listViewEvent);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listeEv);
        mListView.setAdapter(adapter);*/

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
                            /*JSONObject jObject = new JSONObject(json);
                            Iterator<?> keys = jObject.keys();

                            while( keys.hasNext() ){
                                String key = (String)keys.next();
                                String value = jObject.getString(key);
                                listeEv.add(key);
                                listeEv.add(value);

                            }*/
                            //listeEv.add(list);
                            /*ArrayList<String> listdata = new ArrayList<String>();
                            //JSONObject jObject = new JSONObject(json);
                            JSONArray jArray = new JSONArray(json);
                            if (jArray != null) {
                                for (int i=0;i<jArray.length();i++){
                                    listdata.add(jArray.get(i).toString());
                                }
                            }*/

                            /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(listEventActivity.this, android.R.layout.simple_list_item_1, listeEv);
                            mListView.setAdapter(adapter);*/


                            /*mListView = (ListView) listEventActivity.this.findViewById(R.id.listViewEvent);
                            SimpleAdapter simpleAdapter = new SimpleAdapter(listEventActivity.this, (List<? extends Map<String, ?>>) map, android.R.layout.simple_list_item_1, new String[] {"country"}, new int[] {android.R.id.text1});
                            */
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
    }
/*
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
*/
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

