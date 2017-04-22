package itesm.mobilestore711;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by jcisneros77 on 4/22/17.
 */

public class StoresRequest extends StringRequest {

    private static final String request_url = "https://mobilestore711.herokuapp.com/api/stores";

    public StoresRequest(Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.GET,request_url,listener,errorListener);
    }

}