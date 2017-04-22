package itesm.mobilestore711;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.List;

public class StoreActivity extends AppCompatActivity {

    private Spinner sp_stores;
    private Button btn_confirm_store;
    private TextView tv_name;
    private TextView tv_address;
    private TextView tv_phone;
    private RequestQueue reqQueue;
    private UserInformation userInfo;
    private MobileStoreApp app;
    private ArrayList<StoreModel> stores;
    private ArrayAdapter<String> stores_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        // Initialize GUI elements
        sp_stores = (Spinner) findViewById(R.id.sp_stores);
        btn_confirm_store = (Button) findViewById(R.id.btn_confirm_store);
        tv_name = (TextView) findViewById(R.id.tv_name_store);
        tv_address = (TextView) findViewById(R.id.tv_address_store);
        tv_phone = (TextView) findViewById(R.id.tv_phone_store);

        // Initialize Request Queue
        reqQueue = Volley.newRequestQueue(StoreActivity.this);

        // Get UserInfo
        app = (MobileStoreApp) getApplicationContext();
        userInfo = app.getUserInformation();

        // Fill spinner with all the stores
        storesRequest();

        // On spinner pick, get store information
        sp_stores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Set store information
                tv_name.setText(stores.get(position).getName());
                tv_address.setText(stores.get(position).getAddress());
                tv_phone.setText(stores.get(position).getPhone());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // On confirm, go to Homepage and set the store on UserInformation

        btn_confirm_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfo.setStore_id(stores.get(sp_stores.getSelectedItemPosition()).getId());

                Intent homePageIntent = new Intent(StoreActivity.this,HomePageActivity.class);
                StoreActivity.this.startActivity(homePageIntent);
            }
        });



    }

    void storesRequest(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Stores Success!");
                System.out.println(response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean successFlag = jsonResponse.getBoolean("success");

                    if(successFlag){
                        stores = new ArrayList<>();
                        List<String> storesSpinner = new ArrayList<String>();

                        JSONArray storesDetails = new JSONArray(jsonResponse.getString("stores"));
                        // Get all stores into the ArrayList stores
                        for(int i = 0; i < storesDetails.length(); ++i){
                            JSONObject jsonObj = storesDetails.getJSONObject(i);
                            // Get all stores
                            stores.add(new StoreModel(jsonObj.getString("id"),jsonObj.getString("name"),
                                    jsonObj.getString("address"),jsonObj.getString("phone")));
                            storesSpinner.add(jsonObj.getString("name"));
                        }

                        // Insert all these into the spinner
                        stores_adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,storesSpinner);
                        sp_stores.setAdapter(stores_adapter);
                    }else{
                        displayMessage("Listado de tiendas Fallido.");
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
                    displayMessage("Error al intentar obtener tiendas.");
                }
                else{
                    displayMessage("No response.");
                }
            }
        };

        StoresRequest storesReq = new StoresRequest(responseListener,errorListener);
        reqQueue.add(storesReq);
        System.out.println("Stores request done.");

    }

    public void displayMessage(String toastString){
        Toast.makeText(StoreActivity.this, toastString, Toast.LENGTH_LONG).show();
    }

}
