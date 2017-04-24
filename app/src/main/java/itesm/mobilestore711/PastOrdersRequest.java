package itesm.mobilestore711;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jcisneros77 on 4/23/17.
 */

public class PastOrdersRequest extends StringRequest {

    private static final String login_request_url = "https://mobilestore711.herokuapp.com/api/orders/get_user_orders";
    private Map<String,String> params;

    public PastOrdersRequest(String user_id, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST,login_request_url,listener,errorListener);
        params = new HashMap<>();
        params.put("user_id",user_id);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}
