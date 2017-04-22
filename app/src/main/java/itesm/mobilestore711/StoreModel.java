package itesm.mobilestore711;

/**
 * Created by jcisneros77 on 4/22/17.
 */

public class StoreModel {
    private String id;
    private String name;
    private String address;
    private String phone;
    private String opening_hour;
    private String closing_hour;

    public StoreModel(String Id, String Name, String Address, String Phone){
        id = Id;
        name = Name;
        address = Address;
        phone = Phone;
        opening_hour = "0";
        closing_hour = "0";
    }

    public StoreModel(String Id, String Name, String Address, String Phone, String Oh, String Ch){
        id = Id;
        name = Name;
        address = Address;
        phone = Phone;
        opening_hour = Oh;
        closing_hour = Ch;
    }

    public String getId() {return id;}

    public String getName() {return name;}

    public String getAddress() {return address;}

    public String getPhone() {return phone;}

    public String getOpening_hour() {return opening_hour;}

    public String getClosing_hour() {return closing_hour;}

}
