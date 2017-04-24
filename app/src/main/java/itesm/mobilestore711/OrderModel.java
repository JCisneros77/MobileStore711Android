package itesm.mobilestore711;

/**
 * Created by jcisneros77 on 4/23/17.
 */

public class OrderModel {
    private String id;
    private String payment_method;
    private String total_cost;
    private String pickup_date;
    private String store_id;
    private String store_name;

    public OrderModel(String Id, String PayMethod, String TotalCost, String PickupDate, String Store_Id, String Store_Name){
        id = Id;
        payment_method = PayMethod;
        total_cost = TotalCost;
        pickup_date = PickupDate;
        store_id = Store_Id;
        store_name = Store_Name;
    }

    public String getId(){return id;}

    public String getPayment_method(){ return payment_method;}

    public String getTotal_cost(){return total_cost;}

    public String getPickup_date(){ return pickup_date;}

    public String getStore_id(){return store_id;}

    public String getStore_name(){ return store_name;}
}
