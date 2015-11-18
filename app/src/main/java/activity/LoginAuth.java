package activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.mathieu.myapplication2.R;

/**
 * Created by Yacine on 17/11/2015.
 */
public class LoginAuth extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginauth);
        Button b = (Button) this.findViewById(R.id.connectB);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent secondeActivite = new Intent(LoginAuth.this, MenuApp.class);
                startActivity(secondeActivite);
            }
        });

    }

    public void onClick(View v) {
        if (v == this.findViewById(R.id.connectB)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        }

    }
}
