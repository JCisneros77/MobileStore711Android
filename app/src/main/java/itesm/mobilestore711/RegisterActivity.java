package itesm.mobilestore711;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    private RequestQueue reqQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Declare GUI elements
        final EditText tf_name = (EditText) findViewById(R.id.tf_name_register);
        final EditText tf_username = (EditText) findViewById(R.id.tf_username_register);
        final EditText tf_password = (EditText) findViewById(R.id.tf_password_register);
        final EditText tf_confirm_password = (EditText) findViewById(R.id.tf_confirm_password_register);
        final Button btn_register = (Button) findViewById(R.id.btn_register);
        final ImageButton ib_back_to_login = (ImageButton) findViewById(R.id.ib_back_to_login);

        // Initialize Request Queue
        reqQueue = Volley.newRequestQueue(RegisterActivity.this);

        // Back to Login Button
        ib_back_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(loginIntent);
            }
        });

        // Register Button

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = tf_name.getText().toString();
                final String username = tf_username.getText().toString();
                final String password = tf_password.getText().toString();
                final String confirm_password = tf_confirm_password.getText().toString();

                if (password.equals(confirm_password)){
                    // Make request
                    registerRequest(name,username,password);
                } else{
                    // Display error Message
                    displayMessage("Contraseña y confirmar contraseña deben ser iguales.");
                }
            }
        });

    }

    public void registerRequest(String name, String username, String password){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Register Success!");
                System.out.println(response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean successFlag = jsonResponse.getBoolean("success");

                    if(successFlag){
                        displayMessage("Usuario Registrado con Éxito.");

                        Intent loginIntent = new Intent(RegisterActivity.this,LoginActivity.class);
                        RegisterActivity.this.startActivity(loginIntent);

                    }else{
                        displayMessage("Registro de Usuario Fallido.");
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
                    displayMessage("Error al intentar registrar usuario.");
                }
                else{
                    displayMessage("No response.");
                }
            }
        };

        RegisterRequest registerReq = new RegisterRequest(name,username,password,responseListener,errorListener);
        reqQueue.add(registerReq);
        System.out.println("Register request done.");
    }

    public void displayMessage(String toastString){
        Toast.makeText(RegisterActivity.this, toastString, Toast.LENGTH_LONG).show();
    }
}
