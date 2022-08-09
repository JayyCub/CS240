package Handlers.User;

import DAO_Models.User;
import ReqRes.ResultMessage;
import Service.LoginService;
import Service.RegisterService;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;

public class LoginHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        try {
            System.out.println("Logging in...");

            InputStream reqBody = exchange.getRequestBody();
            String inputString = readString(reqBody);

            Gson inputGson = new Gson();
            User user = inputGson.fromJson(inputString, (Type) User.class);


            LoginService loginService = new LoginService(user);
            ResultMessage loginResult = loginService.login();
            Gson outputGson = new Gson();
            String jsonResult = outputGson.toJson(loginResult);
            if (!loginResult.getSuccess()){
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            }
            OutputStream respBody = exchange.getResponseBody();
            writeString(jsonResult, respBody);

            respBody.close();
        } catch (IOException e) {
            System.out.println("There was an error.");
        } catch (JsonSyntaxException jsonSyntaxException){
            System.out.println("There was an error with the Json input");
        }
    }

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
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

}
