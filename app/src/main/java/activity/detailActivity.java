package activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

import com.example.mathieu.myapplication2.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


/**
 * Created by Yacine on 27/11/2015.
 */
public class detailActivity extends AppCompatActivity {

    private static  String	LIST_EVENT_URL;
    private static final String TABLE = "Event";
    private int idEvent=1;
    public ProgressDialog progressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        LIST_EVENT_URL = "https://footapp-sharaf.c9users.io/ConnectedSoccerPhp/web/api/events/"+idEvent;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);

        progressDialog.show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailsevent);

    }


    private void getDetailsEvents(int id) {


        Thread t = new Thread() {

            public void run() {

                Looper.prepare();

                URL url = null;
                try {

                    url = new URL(LIST_EVENT_URL);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("GET");
                String json;

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {

                }

            }
                 catch (IOException e) {
                e.printStackTrace();
                }
                Looper.loop();
            }

    };
        t.start();

    }

}
