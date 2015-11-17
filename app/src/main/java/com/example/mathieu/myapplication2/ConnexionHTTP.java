package com.example.mathieu.myapplication2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Yacine on 17/11/2015.
 */
public class ConnexionHTTP {

   // 3 méthode
    // insertion
    // selection
    // connexion

    public HttpURLConnection connect(String liensUrl) throws IOException {

        URL url = new URL(liensUrl);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setDoOutput(true);
        connection.setChunkedStreamingMode(0);
        connection.setRequestMethod("POST");
        return connection;
    }





}
