package itesm.mobilestore711;

/**
 * Created by jcisneros77 on 4/21/17.
 */

public class UserInformation {
    private  String name;
    private  String username;
    private  String id;

    public UserInformation(String Name, String Username, String Id){
        name = Name;
        username = Username;
        id = Id;
    }

    // Get User Information
    public String getName(){
        return name;
    }

    public  String getUsername(){
        return username;
    }

    public String getId(){
        return id;
    }
}