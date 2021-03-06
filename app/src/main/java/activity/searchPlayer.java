package activity;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.mathieu.myapplication2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yacine on 30/11/2015.
 */
public class searchPlayer extends AppCompatActivity {
    private String idEvent;
    private SearchView search;
    private static String URL_RESEARCH_PLAYER;
    private ListView listeResult;
    private List<Map<String,String>> resultSearch = new ArrayList<>();
    private List<String> resultID = new ArrayList<>();


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
                serachThread(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }


        });
    }

    private void serachThread(final String recherche)
    {

        Thread t = new Thread() {
            public void run() {
                Looper.prepare();
                searchPlayerDb(recherche);
                Looper.loop();
            }
        };
        t.start();

        if(!t.isInterrupted())
        {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, resultSearch, android.R.layout.simple_list_item_multiple_choice, new String[]{"users"}, new int[]{android.R.id.text1});
        listeResult.setAdapter(simpleAdapter);
        listeResult.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    private void searchPlayerDb(String recherche)
    {
        try{
            URL_RESEARCH_PLAYER="https://footapp-sharaf.c9users.io/ConnectedSoccerPhp/web/api/recherches/usernames";
            URL url = new URL(URL_RESEARCH_PLAYER);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");

            HashMap<String,String> map = new HashMap<String,String>();
            map.put("username",recherche);
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
                initDetails(json);
                //createDialog("Test",json);
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




//region JsonStringToListView

    private void initDetails(String jsonString){
        try{
            //
            JSONObject jsonResponse = new JSONObject(jsonString);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("users");
            resultSearch = new ArrayList<>();
            for(int i = 0; i<jsonMainNode.length();i++){
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String id = jsonChildNode.optString("id");
                String username = jsonChildNode.optString("username");
                String outPut = username +" id : " +id;
                resultSearch.add(createPlayer("users", outPut));
                resultID.add(id);
            }
            createDialog("test",jsonString);
        }
        catch(JSONException e){
            Toast.makeText(getApplicationContext(), "Error"+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private HashMap<String, String>createPlayer(String name,String number){
        HashMap<String, String> playersNameNo = new HashMap<String, String>();
        playersNameNo.put(name, number);
        return playersNameNo;
    }
//endregion JsonStringToListView

}
