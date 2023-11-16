package clientFiles;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    static int PORT;
    static String serverName = "localhost";

    public Client() { }

    public static void start(String[] _args) throws UnknownHostException, IOException{
        
        // ternary operator to set the PORT based on arguments
        // PORT = (_args.length == 1) ? Integer.parseInt(_args[0]) : 3000;
        // PORT = (_args.length > 1) ? Integer.parseInt(_args[1]) : 3000;

        switch (_args.length) {
            case (0):
                PORT = 3000;;
                break;
            case (1):
                PORT = Integer.parseInt(_args[0]);
                break;
            case (2):
                PORT = Integer.parseInt(_args[1]);
                serverName = _args[2];
                break;
        }
        
        try (Socket socket = new Socket(serverName, PORT)) {

            System.out.printf("Connected to server running on %s listening port %d", serverName, PORT);
            Session sess = new Session(socket);
            sess.sessionStart();
        }

    }

}