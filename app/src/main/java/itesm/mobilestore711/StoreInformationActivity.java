package itesm.mobilestore711;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class StoreInformationActivity extends AppCompatActivity {

    private ImageButton ib_back_to_home;
    private TextView tv_name;
    private TextView tv_address;
    private TextView tv_phone;

    // Save User information
    private MobileStoreApp app;
    private UserInformation userInfo;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_information);

        ib_back_to_home = (ImageButton) findViewById(R.id.ib_back_to_home_storeInfo);
        tv_name = (TextView) findViewById(R.id.tv_name_storeInfo);
        tv_address = (TextView) findViewById(R.id.tv_address_storeInfo);
        tv_phone = (TextView) findViewById(R.id.tv_phone_storeInfo);

        // Get UserInfo
        app = (MobileStoreApp) getApplicationContext();
        userInfo = app.getUserInformation();
        queue = Volley.newRequestQueue(StoreInformationActivity.this);

        // Set store name
        tv_name.setText("7/11 ".concat(userInfo.getStore_name()));

        // Back to home button
        ib_back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(StoreInformationActivity.this, HomePageActivity.class);
                StoreInformationActivity.this.startActivity(homeIntent);
            }
        });

        // Get store info
        getStoreInfo();
    }

    private void getStoreInfo(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Store Success!");
                System.out.println(response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean successFlag = jsonResponse.getBoolean("success");

                    if(successFlag){
                        JSONObject store = new JSONObject(jsonResponse.getString("store"));

                        String address = store.getString("address");
                        String phone = store.getString("phone");

                        tv_address.setText(address);
                        tv_phone.setText(phone);


                    }else{
                        displayMessage("Información de tienda fallido.");
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
                    displayMessage("Error al obtener información de tienda.");
                }
                else{
                    displayMessage("No response.");
                }
            }
        };

        StoreInformationRequest storeReq = new StoreInformationRequest(userInfo.getStore_id(),responseListener,errorListener);
        queue.add(storeReq);
        System.out.println("Store request done.");


    }

    public void displayMessage(String toastString){
        Toast.makeText(StoreInformationActivity.this, toastString, Toast.LENGTH_LONG).show();
    }
}
