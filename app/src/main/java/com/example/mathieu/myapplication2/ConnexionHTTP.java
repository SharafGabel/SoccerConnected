package com.example.mathieu.myapplication2;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

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

    public int insertData(String url,String table,HashMap data) throws IOException {

        HttpURLConnection connection =  this.connect(url);
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        data.put("table",table);
        writer.write(getPostDataString(data));
        writer.flush();
        writer.close();

        return 0;
    }

    public String selectionData(String url,String table,HashMap data) throws IOException {

        HttpURLConnection connection =  this.connect(url);
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        data.put("table",table);
        writer.write(getPostDataString(data));
        writer.flush();
        writer.close();

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
            return sb.toString();
        }
        else
            return "erreur";
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




}
