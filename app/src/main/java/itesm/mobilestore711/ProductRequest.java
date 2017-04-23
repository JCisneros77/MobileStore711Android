package itesm.mobilestore711;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jcisneros77 on 4/22/17.
 */

public class ProductRequest extends StringRequest{
    private static final String request_url = "https://mobilestore711.herokuapp.com/api/product_stores/get_product_from_store";
    private Map<String,String> params;

    public ProductRequest(String store_id, String product_id, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST,request_url,listener,errorListener);
        params = new HashMap<>();
        params.put("store_id",store_id);
        params.put("product_id",product_id);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}
