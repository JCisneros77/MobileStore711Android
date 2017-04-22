package itesm.mobilestore711;

import android.app.Application;

/**
 * Created by jcisneros77 on 4/21/17.
 */

public class MobileStoreApp extends Application{
    private UserInformation obj;

    public UserInformation getUserInformation(){
        return obj;
    }

    public void setUserInformation(UserInformation o){
        obj = o;
    }
}
