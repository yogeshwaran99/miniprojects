
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliensocket {

  public static void main(String[] args) throws IOException {
    Socket client = new Socket("localhost",5055);
    System.out.println("Connected to server");
    BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
PrintWriter pw = new PrintWriter(client.getOutputStream(),true);
BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
String l;
 while((l=in.readLine())!=null){
   pw.println(l);
   System.out.println(br.readLine());
 }

    client.close();
  }
}
