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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;



public class Session{

    private final Socket socket;
    boolean connected = true;
    ArrayList<String> itemListObject = new ArrayList<String>();
    // Map<String, String> itemMap = new HashMap<String, String>();

    public Session(Socket _socket) {
        socket = _socket;
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
      
      while (connected) {
      
        String result = br.readLine();

        List<Optional<Item>> resultList = br.lines()
                                                .stream()
                                                .flatMap(map -> map.values().stream())
                                                .collect(Collectors.toList());

        String[] lines = result.split(":");

        itemListObject.addAll(Arrays.asList(lines));
        // if only line
        itemListObject.stream().sorted(Comparator.comparingDouble(lines::getPrice))
        .collect(Collectors.toList());

        // Store the result in a map, key : value pair
        // if(lines.length < 2){
        //   continue;
        // }
        // itemMap.put(lines[0], lines[1]);

        // after sorting, send back to clien using a writer
        
        }
      }
    }

}
    