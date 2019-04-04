import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server implements InterfaceServer{

    static {
        Scanner sc=new Scanner(System.in);
        System.out.println("Server PORT :");
        PORT=sc.nextInt();
    }

    private final ArrayList<NetServer>clients=new ArrayList<>();
    static final int PORT;
    static Scanner sc;

    public Server(){
        try(ServerSocket serverSocket=new ServerSocket(PORT)){
        while (true){
            new NetServer(serverSocket.accept(),this);
        }
        }catch (IOException e){
            System.out.println("ошибка на серве");
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void sendMsg(NetServer netServer, String msg) {
        int len=clients.size();
        for (int i = 0; i < len ; i++) {
            clients.get(i).writeMessage(netServer+"<->"+msg);
        }
    }

    @Override
    public synchronized void newConnection(NetServer netServer) {
     clients.add(netServer);
    }

    @Override
    public synchronized void closeConnection(NetServer netServer) {
       clients.remove(netServer);
    }

    UserRepozitories repozitories=UserRepozitories.repozitories;
    @Override
    public synchronized boolean openDoor(NetServer netServer, String msg,String password) {
            if( repozitories.checkUserInFile(msg.getBytes()) && repozitories.checkUserInFile(password.getBytes()) ) {return true;} else { return false;}
    }

    @Override
    public synchronized void createNewLogin(String login,String pass){
        try {
            repozitories.addUserInFile(login,pass);
        } catch (IOException e) {
            System.out.println("Ошибка при создании нового пользователя");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}