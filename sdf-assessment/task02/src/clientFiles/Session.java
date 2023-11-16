package clientFiles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;



public class Session{

    private final Socket socket;

    public Session(Socket socket) {
        this.socket = socket;
     }

    public void sessionStart() throws IOException{

        InputStream is = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        OutputStream os = socket.getOutputStream();
        OutputStreamWriter ows = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(ows);

        boolean stop = false;
        Console cons = System.console();
        while (!stop) {
        String line = cons.readLine("> ");
        line = line.trim() + "\n";

        bw.write(line);
        bw.flush();

        while (true) {
          String result = br.readLine();
          result = result.trim();
          System.out.println(result);
        }
      }
    }

  }
    