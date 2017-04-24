package itesm.mobilestore711;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderActivity extends AppCompatActivity {

    private TextView tv_order_name;
    private TextView tv_order_price;
    private TextView tv_order_store;
    private TextView tv_order_date;
    private ListView lv_products_in_order;
    private ImageButton ib_back_to_orders;

    // Save User information
    private MobileStoreApp app;
    private UserInformation userInfo;
    private RequestQueue queue;

    // Products
    private ArrayList<ProductModel> products;
    private ProductsAdapter product_adapter;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        // Initialize GUI elements
        tv_order_name = (TextView) findViewById(R.id.tv_order_name_order);
        tv_order_price = (TextView) findViewById(R.id.tv_total_price_order);
        tv_order_store = (TextView) findViewById(R.id.tv_store_name_order);
        tv_order_date = (TextView) findViewById(R.id.tv_pickup_date_order);
        ib_back_to_orders = (ImageButton) findViewById(R.id.ib_back_to_past_orders);
        lv_products_in_order = (ListView) findViewById(R.id.lv_products_in_order);

        // Intent
        intent = getIntent();
        String order_date = "";
        // Set order info
        String orderDate = intent.getStringExtra("pickup_date");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.ss'Z'");
        try {
            Date orderDateFormat = format.parse(orderDate);
            System.out.println(orderDateFormat);
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            order_date = df.format(orderDateFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        tv_order_name.setText("Orden #".concat(intent.getStringExtra("order_id")));
        tv_order_price.setText("$ ".concat(intent.getStringExtra("total_price")));
        tv_order_store.setText(intent.getStringExtra("store_name"));
        tv_order_date.setText(order_date);

        // Get User Info
        app = (MobileStoreApp) getApplicationContext();
        userInfo = app.getUserInformation();
        queue = Volley.newRequestQueue(OrderActivity.this);

        // Back to home button
        ib_back_to_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ordersIntent = new Intent(OrderActivity.this, PastOrdersActivity.class);
                OrderActivity.this.startActivity(ordersIntent);
            }
        });

        // Get products from order
        getOrderProducts();
    }

    private void getOrderProducts(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Order Products Success!");
                System.out.println(response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean successFlag = jsonResponse.getBoolean("success");

                    if(successFlag){
                        JSONArray productStores = new JSONArray(jsonResponse.getString("products"));
                        products = new ArrayList<>();

                        for (int i = 0; i < productStores.length(); ++i){
                            JSONObject jsonObj = productStores.getJSONObject(i);
                            // Get all products
                            int amount = jsonObj.getInt("amount");

                            JSONObject currentProduct = jsonObj.getJSONObject("product");
                            String id = currentProduct.getString("id");
                            String name = currentProduct.getString("name");
                            double price = currentProduct.getDouble("price");

                            products.add(new ProductModel(id,name,amount,price));
                        }
                        // Insert into list
                        product_adapter = new ProductsAdapter(products,getApplicationContext(),R.layout.products_item,2);
                        lv_products_in_order.setAdapter(product_adapter);
                    }else{
                        displayMessage("Listado de productos en orden Fallido.");
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
                    displayMessage("Error al obtener productos de orden.");
                }
                else{
                    displayMessage("No response.");
                }
            }
        };

        OrderProductsRequest productsReq = new OrderProductsRequest(intent.getStringExtra("order_id"),responseListener,errorListener);
        queue.add(productsReq);
        System.out.println("Products request done.");
    }

    public void displayMessage(String toastString){
        Toast.makeText(OrderActivity.this, toastString, Toast.LENGTH_LONG).show();
    }
}
