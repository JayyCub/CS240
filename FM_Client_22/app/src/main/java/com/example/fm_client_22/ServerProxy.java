package com.example.fm_client_22;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServerProxy {
    String serverHost = "localhost";
    String serverPort = "8080";
    /*
    login
    register
    getPeople
    getEvents

    clear
    fill
    getPerson
    getEvent
    load
     */

    //LoginResult login(LoginRequest request){}

    String connect(){
        try {
            URL url = new URL("http://10.0.2.2:8080/clear");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);

            http.connect();
            // For some reason the server doesnt actually process the request until some
            // sort of getResponse... funciton is called. Works regardless, but look into this
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK){
                return "Connected, done";
            }

        } catch (MalformedURLException e) {
            System.out.println("Errroorrr111");
            e.printStackTrace();
            return "Error 1";
        } catch (IOException e) {
            System.out.println("errRRRorrr22");
            e.printStackTrace();
            return e.toString();
        } catch (Error error){
            return "Dif error";
        }
        return "Connected";
    }

}
