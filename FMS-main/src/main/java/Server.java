import Handlers.Database.ClearHandler;
import Handlers.Database.FillHandler;
import Handlers.Database.LoadHandler;
import Handlers.DefaultHandle;
import Handlers.User.EventHandler;
import Handlers.User.LoginHandler;
import Handlers.User.PersonHandler;
import Handlers.User.RegisterHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * THE Server
 */
public class Server {

    private static final int MAX_WAITING_CONNECTIONS = 12;

    /**
     * An identifier for the server
     */
    private HttpServer server;

    /**
     * Takes the port number and starts the server running on that local port
     * @param portNumber String, but expected to be numerical value (i.e. "8080")
     */
    private void run(String portNumber) {
        System.out.println("Initializing HTTP Server on " + portNumber);

        try {
            server = HttpServer.create(
                    new InetSocketAddress(Integer.parseInt(portNumber)),
                    MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        server.setExecutor(null);

        System.out.println("Creating contexts");


        server.createContext("/", new DefaultHandle());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/fill", new FillHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/event", new EventHandler());
        server.createContext("/person", new PersonHandler());

        System.out.println("Starting server");

        server.start();

        System.out.println("Server started");
    }

    /**
     *
     * @param args , List of program command-line arguments, expects [0] to be a port number (i.e. "8080")
     */
    public static void main(String[] args) {
        String portNumber = args[0];
        new Server().run(portNumber);
    }
}

