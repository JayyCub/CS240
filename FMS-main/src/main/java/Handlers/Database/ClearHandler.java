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

/**
 * Class to manage the Server's clearing function
 */
public class ClearHandler implements HttpHandler{
    /**
     * Primary method called for clearing database, passes work on to Service
     * @param exchange the exchange containing the request from the
     *      client and used to send the response
     */
    @Override
    public void handle(HttpExchange exchange) {
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

    /**
     * Outputs messages as server response
     * @param str input string
     * @param os OutputStream
     * @throws IOException Thrown if error with write or flush
     */
    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
