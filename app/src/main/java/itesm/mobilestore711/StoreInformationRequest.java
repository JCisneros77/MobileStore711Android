package itesm.mobilestore711;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by jcisneros77 on 4/23/17.
 */

public class StoreInformationRequest extends StringRequest{
    private static final String request_url = "https://mobilestore711.herokuapp.com/api/stores/";

    public StoreInformationRequest(String store_id, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.GET,request_url.concat(store_id),listener,errorListener);
    }

}