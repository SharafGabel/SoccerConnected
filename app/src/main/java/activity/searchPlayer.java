package activity;

import android.os.Bundle;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailsevent);
    }



}
