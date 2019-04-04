import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class NetServer {

    private final Socket socket;
    private Thread thread;
    private final BufferedReader reader;
    private final BufferedWriter writer;
    private final InterfaceServer interfaceServer;
    public NetServer(Socket socket,InterfaceServer interfaceServer) throws IOException {
        this.socket=socket;
        this.interfaceServer=interfaceServer;
        writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));
        reader=new BufferedReader(new InputStreamReader(socket.getInputStream(),Charset.forName("UTF-8")));
        thread=new Thread(()->{
           interfaceServer.newConnection(NetServer.this);
            while (!openChat){
                try {
                    if(reader.readLine().equals("haveLogin")){
                        while (true) {
                            writeMessage("Send yours login");
                            String login = reader.readLine();
                            writeMessage("Send yours Password");
                            String password = reader.readLine();
                            if (interfaceServer.openDoor(NetServer.this, login , password)) {
                                openChat = true;
                                break;
                            }
                        }
                    } else{
                        writeMessage("Send new login");
                        String login=reader.readLine();
                        writeMessage("Send new Password");
                        String password=reader.readLine();
                        interfaceServer.createNewLogin(login,password);
                        openChat=true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
           while (!thread.isInterrupted()) {
               try {
                   interfaceServer.sendMsg(NetServer.this,reader.readLine());
               } catch (IOException e) {
                   closeAll();
                   e.printStackTrace();
               }
           }
        });
        thread.start();
    }

    public synchronized void writeMessage(String msg){
        try {
            writer.write(msg+"\n\r");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void closeAll(){
        try {
            writer.close();
            reader.close();
            socket.close();
            thread.interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean openChat=false;

    @Override
    public String toString() {
        return socket.getInetAddress()+":"+socket.getPort();
    }
}
