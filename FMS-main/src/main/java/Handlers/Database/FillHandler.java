package Handlers.Database;

import DAOs.DatabaseUtil;
import ReqRes.ResultMessage;
import Service.FillService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import static java.lang.Integer.parseInt;

/**
 * Class that handles Fill command
 */
public class FillHandler implements HttpHandler {
    /**
     * Method to handle Fill command, passes to service and returns message to user
     * @param exchange the exchange containing the request from the
     *      client and used to send the response
     */
    @Override
    public void handle(HttpExchange exchange) {
        try {
            String fullURI = exchange.getRequestURI().toString();
            OutputStream respBody = exchange.getResponseBody();
            String jsonMessage;
            Gson gson = new Gson();

            // This splits the string where it finds the slash, route can be determined based on length.
            String[] items = fullURI.split("/");
            String username = items[2];

            if (items.length == 3) {
                System.out.println("Filling 4 generations for " + username);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                // CHECK for username and fill 4, generations
                DatabaseUtil DB = new DatabaseUtil();
                DB.open();
                FillService fillService = new FillService(4, username, DB);
                ResultMessage resultMessage = fillService.FillGenerations();
                jsonMessage = gson.toJson(resultMessage);

            } else if (items.length == 4 && items[3].matches("[0-5]")) {
                String numGens = items[3];
                System.out.println("Loading " + numGens + " generations for " + username);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                DatabaseUtil DB = new DatabaseUtil();
                DB.open();

                // Check for username and fill for numGens
                FillService fillService = new FillService(parseInt(numGens), username, DB);
                ResultMessage resultMessage = fillService.FillGenerations();
                Gson outputGson = new Gson();
                jsonMessage = outputGson.toJson(resultMessage);

            } else {
                System.out.println("Invalid Generations Number");
                ResultMessage resultMessage = new ResultMessage(null, null, null,
                        null, null, null, null, null,
                        null, null, null, null, null, null,
                        null, null, null, null,
                        "Error: Invalid number of generations", false);
                Gson outputGson = new Gson();
                jsonMessage = outputGson.toJson(resultMessage);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }

            writeString(jsonMessage, respBody);
            System.out.println("Done");

            respBody.close();
        } catch (IOException e) {
            System.out.println("There was an error.");
        }
    }

    /**
     * Outputs messages as server response
     * @param str input string
     * @param os  OutputStream
     * @throws IOException Thrown if error with write or flush
     */

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

}
