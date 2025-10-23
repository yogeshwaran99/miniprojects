import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Sockets {

  static  int count =0;
  public static void main(String[] args)  throws IOException{

    ServerSocket socket = new ServerSocket(5055);
    ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();    
    while (true) {
      Socket client = socket.accept();
      int id = ++count;
      System.out.println("Client "+id+" Connected");
      executor.submit(()->socketserver(client,id));
    }
  }
  private static void socketserver(Socket client, int id){
  
    try( BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter pw  = new PrintWriter(client.getOutputStream(),true)){
String l;
while((l=br.readLine())!=null){
  System.out.println("client "+id+" : "+l);
  pw.println("echo: "+l);
}
System.out.println("client "+id+" left");
    }catch (Exception e){
      e.printStackTrace();
    }
  }
}
