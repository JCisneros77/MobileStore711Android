package itesm.mobilestore711;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private RequestQueue reqQueue;
    private UserInformation userInfo;
    private MobileStoreApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Declare GUI elements
        final EditText tf_username = (EditText) findViewById(R.id.tf_username_register);
        final EditText tf_password = (EditText) findViewById(R.id.tf_password_register);
        final Button btn_login = (Button) findViewById(R.id.btn_login);
        final TextView tv_link_register =(TextView) findViewById(R.id.tv_linkRegister);

        // Initialize Request Queue
        reqQueue = Volley.newRequestQueue(LoginActivity.this);

        // Register Link
        tv_link_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        // Login Button

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = tf_username.getText().toString();
                final String password = tf_password.getText().toString();

                // Call Request
                loginRequest(username,password);
            }
        });

    }

    void loginRequest(String username, String password){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Login Success!");
                System.out.println(response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean successFlag = jsonResponse.getBoolean("success");

                    if(successFlag){
                        JSONObject userDetails = new JSONObject(jsonResponse.getString("user"));
                        String nameResponse = userDetails.getString("name");
                        String usernameResponse = userDetails.getString("username");
                        String idResponse = userDetails.getString("id");

                        userInfo = new UserInformation(nameResponse,usernameResponse,idResponse);
                        app = (MobileStoreApp) getApplicationContext();
                        app.setUserInformation(userInfo);

                        Intent homePageIntent = new Intent(LoginActivity.this,HomePageActivity.class);
                        LoginActivity.this.startActivity(homePageIntent);

                    }else{
                        displayMessage("Inicio de Sesión Fallido.");
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
                    displayMessage("Error al intentar iniciar sesión.");
                }
                else{
                    displayMessage("No response.");
                }
            }
        };

        LoginRequest loginReq = new LoginRequest(username,password,responseListener,errorListener);
        reqQueue.add(loginReq);
        System.out.println("Login request done.");
    }

    public void displayMessage(String toastString){
        Toast.makeText(LoginActivity.this, toastString, Toast.LENGTH_LONG).show();
    }
}

