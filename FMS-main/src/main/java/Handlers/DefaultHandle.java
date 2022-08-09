package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.nio.file.Files;

/**
 * Default Server connection handler
 */
public class DefaultHandle implements HttpHandler {

    /**
     * Handles no-command and gives affirmative message about server connection
     * @param givenExchange the exchange containing the request from the
     *      client and used to send the response
     * @throws IOException If there is an error with input
     */
    @Override
    public void handle(HttpExchange givenExchange) throws IOException {
        try {
            if (givenExchange.getRequestMethod().equalsIgnoreCase("get")) {
                givenExchange.getRequestHeaders();
                String urlPathString = givenExchange.getRequestURI().toString();
                if (urlPathString.equals("/")) {
                    urlPathString = "/index.html";
                }

                String pathString = "web" + urlPathString;
                File pathFile = new File(pathString);
                if (!pathFile.exists()) {
                    givenExchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                    OutputStream responseStream = givenExchange.getResponseBody();
                    File notFoundError = new File("web/HTML/404.html");
                    Files.copy(notFoundError.toPath(), responseStream);
                    responseStream.close();
                } else {
                    givenExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream responseStream = givenExchange.getResponseBody();
                    Files.copy(pathFile.toPath(), responseStream);
                    responseStream.close();
                }

            }
            System.out.println("Loading Web...");
            String respData = "{ \"Message\": \"Connected to Server\" }";

            givenExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            OutputStream respBody = givenExchange.getResponseBody();
            writeString(respData, respBody);
            respBody.close();

        } catch(IOException error) {
            givenExchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            givenExchange.getResponseBody().close();
            error.printStackTrace();
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