package itesm.mobilestore711;

import java.text.DecimalFormat;

/**
 * Created by jcisneros77 on 4/22/17.
 */

public class ProductModel {
    private String name;
    private String id;
    private int amount;
    private double price;

    public ProductModel(String Id, String Name, int Amount, double Price){
        id = Id;
        name = Name;
        amount = Amount;
        price = Price;
    }

    public String getName(){return name;}

    public String getId(){return id;}

    public int getAmount(){return amount;}

    public String getPrice(){
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(price);
    }
}
