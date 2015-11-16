package com.example.mathieu.myapplication2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    public static final int RESULT_Main = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), RESULT_Main);

        tv = new TextView(this);
        setContentView(tv);
        setContentView(R.layout.activity_main);
    }

    private void startup(Intent i)
    {
        // Récupère l'identifiant
        int user = i.getIntExtra("userid",-1);

        //Affiche les identifiants de l'utilisateur
        tv.setText("UserID: "+String.valueOf(user)+" logged in");
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == RESULT_Main && resultCode == RESULT_CANCELED)
            finish();
        else
            startup(data);
    }
}
