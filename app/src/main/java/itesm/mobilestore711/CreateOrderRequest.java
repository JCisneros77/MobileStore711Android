package itesm.mobilestore711;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jcisneros77 on 4/23/17.
 */

public class CreateOrderRequest extends StringRequest {

    private static final String request_url = "https://mobilestore711.herokuapp.com/api/orders";
    private Map<String,String> params;

    public CreateOrderRequest(String payment, String user_id, String total_cost,String store_id,String pickup_date, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST,request_url,listener,errorListener);
        params = new HashMap<>();
        params.put("payment_method",payment);
        params.put("user_id",user_id);
        params.put("total_cost",total_cost);
        params.put("store_id",store_id);
        params.put("pickup_date",pickup_date);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}