package com.jusu.hangout;

/**
 * Created by ayoko001 on 2/9/16.
 */
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by ayoko001 on 2/9/16.
 */
public class httpClient {

    public httpClient() {

    }

    public static void sendGet(String url) throws Exception {

        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String Post(String u, String json) {
        String result = "";
        try {
            URL url = new URL(u);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            //        List<NameValuePair> params = new ArrayList<NameValuePair>();
            //        params.add(new BasicNameValuePair("firstParam", paramValue1));
            //        params.add(new BasicNameValuePair("secondParam", paramValue2));
            //        params.add(new BasicNameValuePair("thirdParam", paramValue3));
//            Map<String, String> params = new HashMap<String, String>();
//            params.put("username", "test");
//            params.put("password", "pass123");

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));


            //String json = new Gson().toJson(json);
            System.out.println(json);
            writer.write(json);
            writer.flush();
            writer.close();
            os.close();

            conn.connect();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            //System.out.println(response.toString());
            result = response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main (String [] args) {
        try {
            Post("http://ec2-54-201-118-78.us-west-2.compute.amazonaws.com:8080/main_server/userAuthentication", "i");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
