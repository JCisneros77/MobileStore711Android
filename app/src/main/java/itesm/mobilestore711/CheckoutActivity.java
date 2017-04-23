package itesm.mobilestore711;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    private ImageButton ib_back_to_cart;
    private Button btn_place_order;
    private TextView tv_total_price;
    private Spinner sp_payments;
    private EditText et_pickup_time;

    // Payment Methods
    private ArrayList<String> payment_methods;
    private ArrayAdapter<String> payment_methods_adapter;

    // Save User information
    private MobileStoreApp app;
    private UserInformation userInfo;
    private RequestQueue queue;

    // Order
    private String order_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Initialize GUI Elements
        ib_back_to_cart = (ImageButton) findViewById(R.id.ib_back_to_cart);
        btn_place_order = (Button) findViewById(R.id.btn_place_order);
        tv_total_price = (TextView) findViewById(R.id.tv_total_price);
        sp_payments = (Spinner) findViewById(R.id.sp_payment);
        et_pickup_time = (EditText) findViewById(R.id.et_time_order);

        // Initialize Request Queue
        queue = Volley.newRequestQueue(CheckoutActivity.this);

        // Get UserInfo
        app = (MobileStoreApp) getApplicationContext();
        userInfo = app.getUserInformation();

        // Set total price
        tv_total_price.setText("$ ".concat(userInfo.getCartTotal()));

        // Back to Cart button
        ib_back_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cartIntent = new Intent(CheckoutActivity.this, CartActivity.class);
                CheckoutActivity.this.startActivity(cartIntent);
            }
        });

        // Set spinner items
        setupSpinner();

        // Pick Time
        et_pickup_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CheckoutActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if ((hour * 60) + minute + 180 < (selectedHour * 60) + selectedMinute)
                            et_pickup_time.setText(selectedHour + ":" + selectedMinute);
                        else
                            displayMessage("Tiempo de recolección debe ser a partir de 3 horas.");
                    }
                }, hour, minute,false);
                mTimePicker.setTitle("Escoger Hora");
                mTimePicker.show();
            }
        });

        // Place order
        btn_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_pickup_time.getText().toString().equals(""))
                    displayMessage("Favor de escoger una hora de recolección.");
                else{
                    placeOrder();
                }
            }
        });
    }

    private void setupSpinner(){
        payment_methods = new ArrayList<>();
        payment_methods.add("Efectivo");

        payment_methods_adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,payment_methods);
        sp_payments.setAdapter(payment_methods_adapter);
    }

    private void placeOrder(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Order Success!");
                System.out.println(response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean successFlag = jsonResponse.getBoolean("success");
                    if(successFlag){
                        JSONObject order = new JSONObject(jsonResponse.getString("order"));
                        // Get order id
                        order_id = Integer.toString(order.getInt("id"));
                        placeOrderProducts();
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

        String user_id = userInfo.getId();
        String store_id = userInfo.getStore_id();
        String p_method = sp_payments.getSelectedItem().toString();
        if (p_method.equals("Efectivo"))
            p_method = "cash";
        String total_cost = userInfo.getCartTotal();
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String formattedDate = df.format(c.getTime());
        String pickup_date = formattedDate.concat(" ").concat(et_pickup_time.getText().toString());

        CreateOrderRequest createOrderRequest = new CreateOrderRequest(p_method,user_id,total_cost,store_id,pickup_date,responseListener,errorListener);
        queue.add(createOrderRequest);
        System.out.println("Product request done.");
    }

    private void placeOrderProducts(){
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                System.out.println("Product Order Success!");
                System.out.println(response);
                try {
                    boolean successFlag = response.getBoolean("success");
                    if(successFlag) {
                        displayMessage("Productos Agregados a Orden con Éxito.");
                        userInfo.emptyCart();
                        Intent homeIntent = new Intent(CheckoutActivity.this, HomePageActivity.class);
                        CheckoutActivity.this.startActivity(homeIntent);
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
                    displayMessage("Error al agregar productos a la orden.");
                }
                else{
                    displayMessage("No response.");
                }
            }
        };


        ArrayList<Map<String,String>> productParams = new ArrayList<>();

        Map<String,ProductModel> cart = userInfo.getCart();
        for (Map.Entry<String, ProductModel> entry : cart.entrySet()){
            Map<String,String> product = new HashMap<>();
            product.put("amount",Integer.toString(entry.getValue().getAmount()));
            product.put("product_id",entry.getValue().getId());
            productParams.add(product);

        }

        Map<String, String> params = new HashMap<>();
        params.put("order_id", order_id);
        JSONObject orderJSON = new JSONObject(params);
        JSONArray productsJSON = new JSONArray(productParams);

        try {
            orderJSON.put("products",productsJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(orderJSON.toString());
        PlaceOrderRequest placeOrderRequest = new PlaceOrderRequest(orderJSON, responseListener, errorListener);
        placeOrderRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(placeOrderRequest);

    }

    public void displayMessage(String toastString){
        Toast.makeText(CheckoutActivity.this, toastString, Toast.LENGTH_LONG).show();
    }
}
