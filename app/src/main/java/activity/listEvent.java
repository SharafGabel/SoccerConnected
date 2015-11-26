package activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.mathieu.myapplication2.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import services.ConnexionHTTP;

/**
 * Created by Yacine on 26/11/2015.
 */
public class listEvent extends AppCompatActivity {
    private ConnexionHTTP connexionHTTP;
    private static final String	LIST_EVENT_URL	= "https://footapp-sharaf.c9users.io/insert_Evenement.php";
    //private static final String	UPDATE_URL	= "https://footapp-sharaf.c9users.io/login.1.php";
    private static final String TABLE = "Event";
    private HashMap<String,String> map = new HashMap<String,String>();
    private List<String> listeEv;
    ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.listevent);

        //  listeEv = connexionHTTP.selectionData(LIST_EVENT_URL,TABLE,map);
        listeEv = new ArrayList<String>();
        listeEv.add("Event1");
        listeEv.add("Event2");

       /* mListView = (ListView) findViewById(R.id.listViewEvent);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(listEvent.this,
                android.R.layout.simple_list_item_1, listeEv);
    */
        mListView = (ListView) this.findViewById(R.id.listViewEvent);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listeEv);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                String data=(String)arg0.getItemAtPosition(arg2);
               createDialog("test",data);

            }
        });

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

