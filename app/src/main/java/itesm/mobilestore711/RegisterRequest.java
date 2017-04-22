package itesm.mobilestore711;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jcisneros77 on 4/22/17.
 */

public class RegisterRequest extends StringRequest {

    private static final String register_request_url = "https://mobilestore711.herokuapp.com/api/users";
    private Map<String,String> params;

    public RegisterRequest(String name,String username, String password, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST,register_request_url,listener,errorListener);
        params = new HashMap<>();
        params.put("name",name);
        params.put("username",username);
        params.put("password",password);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}
