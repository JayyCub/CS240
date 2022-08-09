package Handlers.User;

import DAO_Models.User;
import ReqRes.LoadRequest;
import ReqRes.ResultMessage;
import Service.RegisterService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;

public class RegisterHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        try {
            System.out.println("Registering User...");

            InputStream reqBody = exchange.getRequestBody();
            String inputString = readString(reqBody);

            Gson inputGson = new Gson();
            User user = inputGson.fromJson(inputString, (Type) User.class);

            RegisterService registerService = new RegisterService(user);
            ResultMessage regResult = registerService.register();
            Gson outputGson = new Gson();
            String jsonResult = outputGson.toJson(regResult);

            if (!regResult.getSuccess()){
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            }
            OutputStream respBody = exchange.getResponseBody();
            writeString(jsonResult, respBody);

            respBody.close();
        } catch (IOException e) {
            System.out.println("There was an error.");
        }
    }

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str.replaceAll("\n", "").replaceAll("\t", ""));
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
