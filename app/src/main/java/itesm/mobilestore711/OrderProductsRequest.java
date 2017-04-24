package itesm.mobilestore711;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jcisneros77 on 4/23/17.
 */

public class OrderProductsRequest extends StringRequest {
    private static final String request_url = "https://mobilestore711.herokuapp.com/api/order_products/get_products_from_order";
    private Map<String,String> params;

    public OrderProductsRequest(String order_id, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST,request_url,listener,errorListener);
        params = new HashMap<>();
        params.put("order_id",order_id);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}