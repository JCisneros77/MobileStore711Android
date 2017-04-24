package itesm.mobilestore711;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PastOrdersActivity extends AppCompatActivity {

    private ListView lv_past_orders;
    private ImageButton ib_back_to_home;

    // Save User information
    private MobileStoreApp app;
    private UserInformation userInfo;
    private RequestQueue queue;

    // Orders
    // Products
    private ArrayList<OrderModel> orders;
    private OrdersAdapter order_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_orders);

        // Initialize GUI elements
        lv_past_orders = (ListView) findViewById(R.id.lv_past_orders);
        ib_back_to_home = (ImageButton) findViewById(R.id.ib_back_to_home_past_orders);

        // Get User Info
        app = (MobileStoreApp) getApplicationContext();
        userInfo = app.getUserInformation();
        queue = Volley.newRequestQueue(PastOrdersActivity.this);

        // Back to home button
        ib_back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(PastOrdersActivity.this, HomePageActivity.class);
                PastOrdersActivity.this.startActivity(homeIntent);
            }
        });

        // Fill list with orders
        getUserOrders();

        // // Product List Listener
        lv_past_orders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent orderIntent = new Intent(PastOrdersActivity.this,OrderActivity.class);
                orderIntent.putExtra("order_id",orders.get(position).getId());
                orderIntent.putExtra("store_name",orders.get(position).getStore_name());
                orderIntent.putExtra("total_price",orders.get(position).getTotal_cost());
                orderIntent.putExtra("pickup_date",orders.get(position).getPickup_date());
                PastOrdersActivity.this.startActivity(orderIntent);
            }
        });
    }

    private void getUserOrders(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Past Orders Success!");
                System.out.println(response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean successFlag = jsonResponse.getBoolean("success");

                    if(successFlag){
                        JSONArray productStores = new JSONArray(jsonResponse.getString("orders"));
                        orders = new ArrayList<>();

                        for (int i = 0; i < productStores.length(); ++i){
                            JSONObject jsonObj = productStores.getJSONObject(i);
                            // Get all orders
                            String order_id = jsonObj.getString("id");
                            String payment_method = jsonObj.getString("payment_method");
                            String total_cost = jsonObj.getString("total_cost");
                            String pickup_date = jsonObj.getString("pickup_date");
                            String store_id = jsonObj.getString("store_id");

                            JSONObject currentStore = jsonObj.getJSONObject("store");
                            String store_name = currentStore.getString("name");

                            orders.add(new OrderModel(order_id,payment_method,total_cost,pickup_date,store_id,store_name));
                        }
                        // Insert into list
                        order_adapter = new OrdersAdapter(orders,getApplicationContext(),R.layout.orders_item);
                        lv_past_orders.setAdapter(order_adapter);
                    }else{
                        displayMessage("Listado de productos Fallido.");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null){
                    String jsonResponse = new String(response.data);
                    System.out.println(jsonResponse);
                    displayMessage("Error al obtener ordenes pasadas.");
                }
                else{
                    displayMessage("No response.");
                }
            }
        };

        PastOrdersRequest pastOrdersReq = new PastOrdersRequest(userInfo.getId(),responseListener,errorListener);
        queue.add(pastOrdersReq);
        System.out.println("Product request done.");


    }

    public void displayMessage(String toastString){
        Toast.makeText(PastOrdersActivity.this, toastString, Toast.LENGTH_LONG).show();
    }
}
