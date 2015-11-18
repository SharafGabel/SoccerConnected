package activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.mathieu.myapplication2.R;

/**
 * Created by Yacine on 17/11/2015.
 */
public class MenuApp extends Activity  implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton b = (ImageButton) this.findViewById(R.id.menu_event);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent secondeActivite = new Intent(MenuApp.this, EventActivity.class);
                startActivity(secondeActivite);
            }
        });

    }

    public void onClick(View v) {
        if (v == this.findViewById(R.id.createEvent)) {
            Intent intent = new Intent(MenuApp.this, EventActivity.class);
            startActivity(intent);

        }
    }
}
