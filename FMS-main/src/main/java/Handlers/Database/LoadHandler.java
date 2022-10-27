package Handlers.Database;

import ReqRes.LoadRequest;
import ReqRes.ResultMessage;
import Service.LoadService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.google.gson.*;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;

/**
 * Class to handle Load command
 */
public class LoadHandler implements HttpHandler {
    /**
     * Main method to handle Load command, interprets user input data and passes on to load services
     * @param exchange the exchange containing the request from the
     *      client and used to send the response
     */
    @Override
    public void handle(HttpExchange exchange) {
        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                System.out.println("Loading Database");

                InputStream reqBody = exchange.getRequestBody();
                String inputString = readString(reqBody);

                Gson inputGson = new Gson();
                LoadRequest loadRequest = inputGson.fromJson(inputString, (Type) LoadRequest.class);
                LoadService LoadData = new LoadService();
                ResultMessage loadResult = LoadData.loadDataService(loadRequest);
                Gson outputGson = new Gson();
                String jsonResult = outputGson.toJson(loadResult);

                if (!loadResult.getSuccess()){
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }

                OutputStream respBody = exchange.getResponseBody();
                writeString(jsonResult, respBody);


                respBody.close();
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                System.out.println("Bad Response Header: " + exchange.getRequestMethod() + ", expected Post");
            }
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
        sw.write(str.replaceAll("\n", "").replaceAll("\t", ""));
        sw.flush();
    }

    /**
     * Read the input message from the user
     * @param is InputStream
     * @return String that can be used by outer funciton
     * @throws IOException If there is an error with input
     */
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
