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
import java.util.Comparator;

public class Session {

  private final Socket socket;
  boolean connected = true;
  ArrayList<Item> itemListObject = new ArrayList<Item>();
  ArrayList<Item> selectedItems = new ArrayList<Item>();
  float budget = 0;
  String requestID = "";

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

    Console cons = System.console();

    String line = cons.readLine("> ");
    line = line.trim() + "\n";
    bw.write(line);
    bw.flush();

    
    int id = 0;
    String title = "";
    float price = 0;
    float rating = 0;

    int itemCount = -1;
    
    line = "";
    
    while (true) {
      if (itemCount == 0)
        break;
      
      line = br.readLine().trim();
      if (line.startsWith("request_id")) {
        requestID = splitStr(line);
      }
      
      if (line.startsWith("budget")) {
        budget = Float.parseFloat(splitStr(line));
      }

      if (line.startsWith("item_count")) {
        itemCount = Integer.parseInt(splitStr(line));
      }

      if (line.equals("prod_list"))
        continue;
      else if (line.equals("prod_start")) {
        while (true) {
          line = br.readLine().trim();
          if (line.startsWith("prod_end")) {
            System.out.println("Processed a Product");
            --itemCount;
            itemListObject.add(new Item(id, title, price, rating));
            break;
          }
          else {
            id = line.startsWith("prod_id") ? Integer.parseInt(splitStr(line)) : id;
            title = line.startsWith("title") ? splitStr(line) : title;
            price = line.startsWith("price") ? Float.parseFloat(splitStr(line)) : price;
            rating = line.startsWith("rating") ? Float.parseFloat(splitStr(line)) : rating;
          }
        }
      }
      
      // Section the items via product_start and end by checking what the string starts with
      // Read the line, pass information to the class
      // Create list object and sort it using comparator via price and rating
      // check if item is within budget and add it to a select list
      // write list back to server using output stream
      
    }
    
    sortItemList();
    selectItems();
    bw.write(constructReponse());
    bw.flush();

    is.close();
    os.close();
    socket.close();
  }

  public String constructReponse() {
    String name = "name: chewychew\n";
    String email = "email: chewychew@gmail.com\n";
    String itemString = "items: ";
    float spent = 0;
    for (Item item : selectedItems) {
      itemString += Integer.toString(item.m_prod_Id) + ",";
      spent += item.m_price;
    }
    StringBuilder response = new StringBuilder();
    response.append("request_id: " + requestID + "\n" + name + email + itemString + "\nspent: " + spent + "\nremaining: " + budget + "\nclient_end\n");
    return response.toString();
  }

  public void sortItemList() {
    itemListObject.sort(Comparator.comparing(Item::getPrice).reversed());
    itemListObject.sort(Comparator.comparing(Item::getRating).reversed());
  }

  public void selectItems() {
    for (Item item : itemListObject) {
      float newBudget = budget - item.m_price;
      if (newBudget > 0) {
        selectedItems.add(item);
        budget = newBudget;
      } else {
        break;
      }
    }
  }

  private static String splitStr(String line){
    return line.split(":")[1].trim();
  }
}
