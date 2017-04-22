package itesm.mobilestore711;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import static itesm.mobilestore711.R.id.lv_side_menu;

public class HomePageActivity extends AppCompatActivity {
    private ArrayList<SideMenuItemModel> sidemenu_dataset;
    private static SideMenuAdapter sidemenu_Adapter;
    private ListView lv_sidemenu;
    private DrawerLayout sidemenu_drawer_layout;
    private RelativeLayout rl_side_bar;

    // Save User information
    private MobileStoreApp app;
    private UserInformation userInfo;
    private RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Side Menu GUI items
        final TextView tv_name = (TextView) findViewById(R.id.tv_name_sidemenu);
        final TextView tv_username = (TextView) findViewById(R.id.tv_username_sidemenu);
        final ImageView img_user_avatar = (ImageView) findViewById(R.id.img_user_avatar);


        /* Side Menu Setup */
        sidemenu_drawer_layout = (DrawerLayout)findViewById(R.id.drawer_layout);
        lv_sidemenu = (ListView) findViewById(lv_side_menu);
        rl_side_bar = (RelativeLayout) findViewById(R.id.activity_side_bar);

        // Get User Info
        app = (MobileStoreApp) getApplicationContext();
        userInfo = app.getUserInformation();
        queue = Volley.newRequestQueue(HomePageActivity.this);

        // Add side menu user information
        tv_name.setText(userInfo.getName());
        tv_username.setText(userInfo.getUsername());
        img_user_avatar.setImageResource(R.drawable.ic_user_image);

        // Add Side menu items
        addDrawerItems();

        // Side menu listener
        final ImageButton btn_side_menu = (ImageButton) findViewById(R.id.btn_sidemenu);
        btn_side_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sidemenu_drawer_layout.openDrawer(Gravity.LEFT);
            }
        });

        // Side menu Item click Listener
        lv_sidemenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayMessage(sidemenu_dataset.get(position).getTitle());
                switch (sidemenu_dataset.get(position).getTitle()){
                    case "Mi Información":
                    {
                        Intent userInfoIntent = new Intent(HomePageActivity.this,UserInformationActivity.class);
                        HomePageActivity.this.startActivity(userInfoIntent);

                        break;
                    }

                    case "Mis Pedidos":
                    {
                        Intent ordersIntent = new Intent(HomePageActivity.this,OrderActivity.class);
                        HomePageActivity.this.startActivity(ordersIntent);
                        break;
                    }

                    case "Carrito":
                    {
                        Intent cartIntent = new Intent(HomePageActivity.this,CartActivity.class);
                        HomePageActivity.this.startActivity(cartIntent);
                        break;
                    }


                }

                sidemenu_drawer_layout.addDrawerListener( new DrawerLayout.SimpleDrawerListener(){
                    @Override
                    public void onDrawerClosed(View drawerView){
                        super.onDrawerClosed(drawerView);
                    }
                });
                sidemenu_drawer_layout.closeDrawer(rl_side_bar);
            }
        });
    }

    private void addDrawerItems() {
        sidemenu_dataset = new ArrayList<>();
        sidemenu_dataset.add(new SideMenuItemModel("Mi Información",R.mipmap.ic_info));
        sidemenu_dataset.add(new SideMenuItemModel("Mis Pedidos",R.mipmap.ic_orders));
        sidemenu_dataset.add(new SideMenuItemModel("Carrito",R.mipmap.ic_cart));

        sidemenu_Adapter = new SideMenuAdapter(sidemenu_dataset,getApplicationContext(),R.layout.side_menu_item);

        lv_sidemenu.setAdapter(sidemenu_Adapter);
    }

    public void displayMessage(String toastString){
        Toast.makeText(HomePageActivity.this, toastString, Toast.LENGTH_LONG).show();
    }
}
