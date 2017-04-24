package itesm.mobilestore711;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jcisneros77 on 4/21/17.
 */

public class UserInformation {
    private  String name;
    private  String username;
    private  String id;
    private String store_id;
    private Map<String,ProductModel> cart;

    public UserInformation(String Name, String Username, String Id){
        name = Name;
        username = Username;
        id = Id;
        cart = new HashMap<>();
    }

    // Get User Information
    public String getName(){
        return name;
    }

    public String getUsername(){
        return username;
    }

    public String getId(){
        return id;
    }

    public String getStore_id() {return store_id;}

    public void setStore_id(String id) { store_id = id; }

    public Map<String,ProductModel> getCart() {return cart;}

    public void emptyCart(){
        cart = new HashMap<>();
    }

    public void deleteFromCart(String product_id){
        cart.remove(product_id);
    }

    public void printCart(){
        for (Map.Entry<String, ProductModel> entry : cart.entrySet())
            System.out.println(entry.getValue().getName().concat(Integer.toString(entry.getValue().getAmount())));
    }

    public int getProductAmountInCart(String product_id) {
        if (cart.containsKey(product_id))
            return cart.get(product_id).getAmount();
        else
            return 0;
    }

    public void addProduct(String product_name, String product_id, int amount, double price){
        if (cart.containsKey(product_id)){
            // Product's already in cart
            cart.put(product_id,new ProductModel(product_id,product_name,amount + cart.get(product_id).getAmount(),price));
        }
        else{
            //Product doesn't exist in cart

            cart.put(product_id,new ProductModel(product_id,product_name,amount,price));
        }
    }

    public String getCartTotal(){
        double cartTotal = 0.0;
        for (Map.Entry<String, ProductModel> entry : cart.entrySet())
            cartTotal+= entry.getValue().getAmount() * entry.getValue().getPriceDouble();

        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(cartTotal);
    }
}