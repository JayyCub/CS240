package Handlers.Database;

import ReqRes.ResultMessage;
import Service.ClearService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;


public class ClearHandler implements HttpHandler{
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            System.out.println("Clearing Database...");

            ClearService clearService = new ClearService();
            ResultMessage resultMessage = clearService.clear();
            Gson outputGson = new Gson();
            String jsonResult = outputGson.toJson(resultMessage);


            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            OutputStream respBody = exchange.getResponseBody();

            writeString(jsonResult, respBody);

            respBody.close();
        } catch (IOException e) {
            System.out.println("There was an error.");
        }
    }

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
