package Handlers.User;

import ReqRes.ResultMessage;
import Service.EventService;
import Service.PersonService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class EventHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String fullURI = exchange.getRequestURI().toString();
            OutputStream respBody = exchange.getResponseBody();
            String jsonMessage;
            Gson gson = new Gson();

            // This splits the string where it finds the slash, route can be determined based on length.
            String[] items = fullURI.split("/");

            String authToken = exchange.getRequestHeaders().getFirst("Authorization");
            EventService eventService = new EventService(authToken);
            ResultMessage resultMessage = null;

            if (items.length == 2){
                resultMessage = eventService.findAllEvents();

            } else if (items.length == 3){
                resultMessage = eventService.findEvent(items[2]);
            } else {
                resultMessage = new ResultMessage(null, null, null, null,
                        null, null, null, null, null, null,
                        null, null, null, null, null, null, null,
                        null, "Error: Invalid Request", false);
            }
            if (!resultMessage.getSuccess()){
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            }

            jsonMessage = gson.toJson(resultMessage);
            writeString(jsonMessage, respBody);
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
