package clientFiles;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    static int PORT = 3000;
    static String serverName = "localhost";

    public Client() { }

    public static void start(String[] _args) throws UnknownHostException, IOException{

        switch (_args.length) {
            case (1):
                PORT = Integer.parseInt(_args[0]);
                break;
            case (2):
                serverName = _args[0];
                PORT = Integer.parseInt(_args[1]);
                break;
        }
        

        try (Socket socket = new Socket(serverName, PORT)) {
            System.out.printf("Connected to server running on %s listening port %d", serverName, PORT);
            Session sess = new Session(socket);
            sess.sessionStart();
        }

    }

}