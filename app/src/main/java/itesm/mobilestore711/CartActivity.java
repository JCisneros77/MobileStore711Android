package itesm.mobilestore711;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private ImageButton ib_back_to_home;
    private Button btn_checkout;
    private ListView lv_cart;

    // Save User information
    private MobileStoreApp app;
    private UserInformation userInfo;
    private RequestQueue queue;

    // Products
    private ArrayList<ProductModel> products;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize GUI elements
        ib_back_to_home = (ImageButton) findViewById(R.id.ib_back_to_home_cart);
        btn_checkout = (Button) findViewById(R.id.btn_checkout);
        lv_cart = (ListView) findViewById(R.id.lv_cart);

        // Get User Info
        app = (MobileStoreApp) getApplicationContext();
        userInfo = app.getUserInformation();
        queue = Volley.newRequestQueue(CartActivity.this);

        // Back to home button
        ib_back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(CartActivity.this, HomePageActivity.class);
                CartActivity.this.startActivity(homeIntent);
            }
        });

        // Set cart items
        setupCart();

        // Go to checkout
        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent checkoutIntent = new Intent(CartActivity.this, CheckoutActivity.class);
                CartActivity.this.startActivity(checkoutIntent);
            }
        });
    }

    void setupCart(){
        Map<String,ProductModel> productsInCart = userInfo.getCart();
        products = new ArrayList<>();

        for (Map.Entry<String, ProductModel> entry : productsInCart.entrySet())
            products.add(entry.getValue());

        cartAdapter = new CartAdapter(products,getApplicationContext(),R.layout.cart_item);
        lv_cart.setAdapter(cartAdapter);
    }

    public void displayMessage(String toastString){
        Toast.makeText(CartActivity.this, toastString, Toast.LENGTH_LONG).show();
    }
}
