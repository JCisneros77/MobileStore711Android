package itesm.mobilestore711;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Created by jcisneros77 on 4/23/17.
 */

public class PlaceOrderRequest extends JsonObjectRequest {

    private static final String request_url = "https://mobilestore711.herokuapp.com/api/order_products";

    public PlaceOrderRequest(JSONObject params, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
        super(Method.POST,request_url,params,listener,errorListener);
    }
}
