public interface InterfaceServer {
    void sendMsg(NetServer netServer,String msg);
    void newConnection(NetServer netServer);
    void closeConnection(NetServer netServer);
    boolean openDoor(NetServer netServer,String msg,String password);
    void createNewLogin(String login,String pass);
}
