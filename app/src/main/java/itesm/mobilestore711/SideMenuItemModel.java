package itesm.mobilestore711;

/**
 * Created by jcisneros77 on 4/22/17.
 */

public class SideMenuItemModel {
    String title;
    int image_path;

    public SideMenuItemModel(String Title, int ip){
        this.title = Title;
        this.image_path = ip;
    }

    public String getTitle(){
        return this.title;
    }

    public int getImage_path(){
        return this.image_path;
    }
}
