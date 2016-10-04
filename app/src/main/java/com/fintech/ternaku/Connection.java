package com.fintech.ternaku;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Pandhu on 9/19/16.
 */
public class Connection {
    public String GetJSONfromURL(String link, String params)
    {
        URL url;
        HttpURLConnection urlConnection = null;
        String data="kosong";
        try {
            url = new URL(link);

            urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            urlConnection.setRequestProperty("Content-Length", "" +
                    Integer.toString(params.getBytes().length));
            urlConnection.setRequestProperty("Content-Language", "en-US");

            urlConnection.setUseCaches (false);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream ());
            wr.writeBytes (params);
            wr.flush ();
            wr.close ();

            BufferedReader rd  = null;
            StringBuilder sb = null;
            String line = null;

            rd  = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            sb = new StringBuilder();

            while ((line = rd.readLine()) != null)
            {
                sb.append(line + '\n');
            }
            data = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return data;
    }
}
