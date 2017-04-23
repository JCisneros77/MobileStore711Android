package itesm.mobilestore711;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
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

import java.text.DecimalFormat;

public class ProductActivity extends AppCompatActivity {
    private TextView tv_name;
    private TextView tv_amount_in_store;
    private TextView tv_price;
    private NumberPicker np_amount_to_cart;
    private Button btn_add_to_cart;
    private ImageButton ib_back_to_home;
    // Save User information
    private MobileStoreApp app;
    private UserInformation userInfo;
    private RequestQueue queue;

    //Product Info
    private String id;
    private String name;
    private double price;
    private int amount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // Get product id from intent
        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        // Initialize GUI elements
        tv_name = (TextView) findViewById(R.id.tv_product_name);
        tv_amount_in_store = (TextView) findViewById(R.id.tv_amount_in_store);
        tv_price = (TextView) findViewById(R.id.tv_price_product);
        np_amount_to_cart = (NumberPicker) findViewById(R.id.np_product_amount);
        btn_add_to_cart = (Button) findViewById(R.id.btn_add_to_cart);
        ib_back_to_home = (ImageButton) findViewById(R.id.ib_back_to_home_product);

        // Get User Info
        app = (MobileStoreApp) getApplicationContext();
        userInfo = app.getUserInformation();
        queue = Volley.newRequestQueue(ProductActivity.this);

        // Back to home button
        ib_back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(ProductActivity.this, HomePageActivity.class);
                ProductActivity.this.startActivity(homeIntent);
            }
        });

        // Load product info
        productRequest();

        // Add to Cart button
        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if user already has product in his cart
                if(userInfo.getProductAmountInCart(id) + np_amount_to_cart.getValue() > amount){
                    // User is trying to order more products than there are in stock
                    displayMessage("No puedes ordenar más producto del que hay disponible.");
                }
                else{
                    displayMessage("Se agregaron ".concat(Integer.toString(np_amount_to_cart.getValue()).concat(" ").concat(name).concat(" al carrito.")));
                    userInfo.addProduct(name,id,np_amount_to_cart.getValue(),price);
                    Intent homeIntent = new Intent(ProductActivity.this, HomePageActivity.class);
                    ProductActivity.this.startActivity(homeIntent);
                }
            }
        });

    }

    void setupNumberPicker(){
        np_amount_to_cart.setMaxValue(amount);
        np_amount_to_cart.setMinValue(1);
        np_amount_to_cart.setWrapSelectorWheel(true);
    }


    void productRequest(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Product Success!");
                System.out.println(response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean successFlag = jsonResponse.getBoolean("success");

                    if(successFlag){
                        JSONArray productStore = new JSONArray(jsonResponse.getString("product_store"));

                        for (int i = 0; i < productStore.length(); ++i){
                            JSONObject jsonObj = productStore.getJSONObject(i);
                            // Get product info
                            amount = jsonObj.getInt("amount");
                            JSONObject currentProduct = jsonObj.getJSONObject("product");
                            id = currentProduct.getString("id");
                            name = currentProduct.getString("name");
                            price = currentProduct.getDouble("price");

                            tv_name.setText(name);
                            tv_amount_in_store.setText(Integer.toString(amount));
                            DecimalFormat df = new DecimalFormat("#.00");
                            tv_price.setText(df.format(price));
                        }

                        // Setup number picker
                        setupNumberPicker();
                    }else{
                        displayMessage("Información de producto fallido.");
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
                    displayMessage("Error al obtener información de producto.");
                }
                else{
                    displayMessage("No response.");
                }
            }
        };

        ProductRequest productReq = new ProductRequest(userInfo.getStore_id(),id,responseListener,errorListener);
        queue.add(productReq);
        System.out.println("Product request done.");


    }

    public void displayMessage(String toastString){
        Toast.makeText(ProductActivity.this, toastString, Toast.LENGTH_LONG).show();
    }
}
