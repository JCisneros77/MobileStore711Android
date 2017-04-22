package itesm.mobilestore711;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jcisneros77 on 4/21/17.
 */

public class LoginRequest extends StringRequest {

    private static final String login_request_url = "https://mobilestore711.herokuapp.com/api/users/login";
    private Map<String,String> params;

    public LoginRequest(String username,String password, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST,login_request_url,listener,errorListener);
        params = new HashMap<>();
        params.put("username",username);
        params.put("password",password);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}