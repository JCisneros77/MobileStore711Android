package itesm.mobilestore711;

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
    private Map<Integer,Integer> cart_ids; // Product_id,Amount
    private Map<String,Integer> cart;

    public UserInformation(String Name, String Username, String Id){
        name = Name;
        username = Username;
        id = Id;
        cart_ids = new HashMap<>();
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

    public Map<Integer,Integer> getCartIds() {return cart_ids;}

    public Map<String,Integer> getCart() {return cart;}

    public int getProductAmountInCart(int product_id) {
        return cart_ids.get(product_id);
    }

    public void addProduct(String product_name, int product_id, int amount){
        if (cart_ids.containsKey(product_id)){
            // Product's already in cart
            cart_ids.put(product_id,cart_ids.get(product_id) + amount);
            cart.put(product_name,cart_ids.get(product_name) + amount);

        }
        else{
            //Product doesn't exist in cart
            cart_ids.put(product_id,amount);
            cart.put(product_name,amount);
        }
    }
}