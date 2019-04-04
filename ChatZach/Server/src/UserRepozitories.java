import javax.jws.soap.SOAPBinding;
import java.io.*;

public class UserRepozitories {
    public static final UserRepozitories repozitories=new UserRepozitories();
    private UserRepozitories(){
        file=new File("Repozitory.txt");
        try {
            fileWriter=new BufferedWriter(new FileWriter(file,true));
            fileReader=new BufferedReader(new FileReader(file));
            } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static File file;
    protected BufferedWriter fileWriter;
    protected static BufferedReader fileReader;

    protected void addUserInFile(String login,String pass) throws IOException{
        UserInfo userInfo=new UserInfo(login,pass);
        fileWriter.write(userInfo+"\n\r");
        fileWriter.newLine();
        fileWriter.flush();
    }


    public boolean checkUserInFile(byte []arrPassword){
        String line;
        int hits=0;
        boolean allHits=true;
        byte arrPass [] = arrPassword;
        int passLength=arrPass.length;
        try {
            while (((line=fileReader.readLine()) != null) && allHits){
                byte arrLine[]=line.getBytes();
                int arrLineLength=arrLine.length;
                for (int i = 0; i < arrLineLength-passLength+1 && allHits ; i++) {
                    if(arrLine[i]==arrPass[0]){
                        hits=1;
                        for (int j = 0; j < passLength && allHits; j++) {
                            if(arrLine[j+i]==arrPass[j]){
                                hits++;
                                if(hits==passLength){
                                    allHits=false;
                                }
                                if ((arrLine[arrLineLength-1]==arrLine[j+i]) && (arrPass[j]==arrPass[passLength-1]) ){
                                    allHits=false;
                                }
                            } else {hits=1;}
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(passLength-hits>2) {
            return true;
        }else            {
            return false;}
    }
}
class UserInfo{
    private String login;
    private int password;
    public UserInfo(String login,String pass){
     this.login=login;
    setPasswordHash(pass);
    }

    void setPasswordHash(String pass){
        int len=pass.length();
        int res=0;
        for (int i = 0; i < len ; i++) {
            res+=Math.sqrt(Math.exp(Math.tan((pass.charAt(i)>>1))));
        }
    this.password=res;}

    public int getPasswordHash(){
        return password;
    }

    @Override
    public String toString() {
        return login+":"+password;
    }
}