package com.example.fm_client_22;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;

import DAO_Models.Event;
import DAO_Models.Person;
import ReqRes.LoginRequest;
import ReqRes.RegisterRequest;
import ReqRes.ResultMessage;

public class ServerProxy {
    String serverHost;
    String serverPort;
    DataCache dataCache = DataCache.getInstance();

    public ServerProxy(String serverHost, String serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    boolean register(RegisterRequest registerRequest) throws IOException {
        URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.addRequestProperty("Accept", "application/json");
        http.setConnectTimeout(1000);
        http.connect();

        OutputStream reqBody = http.getOutputStream();
        Gson gson = new Gson();
        writeString(gson.toJson(registerRequest), reqBody);
        reqBody.close();

        if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
            dataCache.serverHost = serverHost;
            dataCache.port = serverPort;
            InputStream respBody = http.getInputStream();
            String respData = readString(respBody);
            dataCache.recentResult = gson.fromJson(respData, ResultMessage.class);
            dataCache.authToken = dataCache.recentResult.getAuthtoken();
            return true;
        } else {
            InputStream respBody = http.getErrorStream();
            String respData = readString(respBody);
            dataCache.recentResult = gson.fromJson(respData, ResultMessage.class);
            return false;
        }
    }

    boolean login(LoginRequest loginRequest) throws IOException {
        URL url = new URL("http://" + serverHost + ":" + serverPort +"/user/login");
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.addRequestProperty("Accept", "application/json");
        http.setConnectTimeout(1000);
        http.connect();

        OutputStream reqBody = http.getOutputStream();
        Gson gson = new Gson();
        writeString(gson.toJson(loginRequest), reqBody);
        reqBody.close();

        if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
            dataCache.serverHost = serverHost;
            dataCache.port = serverPort;
            InputStream respBody = http.getInputStream();
            String respData = readString(respBody);
            dataCache.recentResult = gson.fromJson(respData, ResultMessage.class);
            dataCache.authToken = dataCache.recentResult.getAuthtoken();
            return true;
        } else {
            InputStream respBody = http.getErrorStream();
            String respData = readString(respBody);
            dataCache.recentResult = gson.fromJson(respData, ResultMessage.class);
            return false;
        }
    }

    public void getPersonName(){
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person/" +
                    dataCache.recentResult.getPersonID());
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", dataCache.recentResult.getAuthtoken());
            http.setConnectTimeout(1000);
            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                Gson gson = new Gson();
                ResultMessage resultMessage = gson.fromJson(respData, ResultMessage.class);
                dataCache.currentPerson = new Person(resultMessage.getPersonID(),
                        resultMessage.getAssociatedUsername(), resultMessage.getFirstName(),
                        resultMessage.getLastName(), resultMessage.getGender(), resultMessage.getFatherID(),
                        resultMessage.getMotherID(), resultMessage.getSpouseID());
            } else {
                System.out.println("Server returned not 200 status");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getAllPersonData(){
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", dataCache.authToken);
            http.setConnectTimeout(1000);
            http.connect();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                Gson gson = new Gson();
                ResultMessage resultMessage = gson.fromJson(respData, ResultMessage.class);
                for (int i = 0; i < resultMessage.getData().length; i++){
                    String personString = gson.toJson(resultMessage.getData()[i]);
                    Person person = gson.fromJson(personString, Person.class);
                    dataCache.people.put(person.getPersonID(), person);
                }
            }

            URL url2 = new URL("http://" + serverHost + ":" + serverPort + "/event");
            HttpURLConnection http2 = (HttpURLConnection) url2.openConnection();
            http2.setRequestMethod("GET");
            http2.setDoOutput(false);
            http2.addRequestProperty("Authorization", dataCache.authToken);
            http2.setConnectTimeout(1000);
            http2.connect();
            if (http2.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream respBody = http2.getInputStream();
                String respData = readString(respBody);
                Gson gson = new Gson();
                ResultMessage resultMessage = gson.fromJson(respData, ResultMessage.class);
                for (int i = 0; i < resultMessage.getData().length; i++){
                    String personString = gson.toJson(resultMessage.getData()[i]);
                    Event event = gson.fromJson(personString, Event.class);
                    dataCache.events.put(event.getEventID(), event);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }
    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
