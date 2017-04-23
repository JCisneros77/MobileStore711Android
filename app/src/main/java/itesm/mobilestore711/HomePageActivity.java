package itesm.mobilestore711;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static itesm.mobilestore711.R.id.lv_side_menu;

public class HomePageActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private ArrayList<SideMenuItemModel> sidemenu_dataset;
    private static SideMenuAdapter sidemenu_Adapter;
    private ListView lv_sidemenu;
    private DrawerLayout sidemenu_drawer_layout;
    private RelativeLayout rl_side_bar;
    private ListView lv_products;
    private android.widget.SearchView sv_products;

    // Save User information
    private MobileStoreApp app;
    private UserInformation userInfo;
    private RequestQueue queue;

    // Products
    private ArrayList<ProductModel> products;
    private ProductsAdapter product_adapter;
    private android.widget.Filter product_filter;

    @Override
    public boolean onQueryTextChange(String newText)
    {
        if (TextUtils.isEmpty(newText)) {
            lv_products.clearTextFilter();
        } else {
            product_filter.filter(newText);
            //lv_products.setFilterText(newText);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Side Menu GUI items
        final TextView tv_name = (TextView) findViewById(R.id.tv_name_sidemenu);
        final TextView tv_username = (TextView) findViewById(R.id.tv_username_sidemenu);
        final ImageView img_user_avatar = (ImageView) findViewById(R.id.img_user_avatar);

        // Main GUI items
        lv_products = (ListView) findViewById(R.id.lv_products);
        lv_products.setTextFilterEnabled(false);

        sv_products = (android.widget.SearchView) findViewById(R.id.sv_products);


        /* Side Menu Setup */
        sidemenu_drawer_layout = (DrawerLayout)findViewById(R.id.drawer_layout);
        lv_sidemenu = (ListView) findViewById(lv_side_menu);
        rl_side_bar = (RelativeLayout) findViewById(R.id.activity_side_bar);

        // Get User Info
        app = (MobileStoreApp) getApplicationContext();
        userInfo = app.getUserInformation();
        queue = Volley.newRequestQueue(HomePageActivity.this);

        // Setup Search
        setupSearchView();

        // Add side menu user information
        tv_name.setText(userInfo.getName());
        tv_username.setText(userInfo.getUsername());
        img_user_avatar.setImageResource(R.drawable.ic_user_image);

        // Add Side menu items
        addDrawerItems();

        // Add Products
        productsRequest();

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

        // Product List Listener
        lv_products.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent productIntent = new Intent(HomePageActivity.this,ProductActivity.class);
                productIntent.putExtra("id",products.get(position).getId());
                HomePageActivity.this.startActivity(productIntent);
            }
        });

        // SearchView On close
        sv_products.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                product_filter.filter("");
                return false;
            }
        });

        int searchCloseButtonId = sv_products.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        ImageView closeButton = (ImageView) this.sv_products.findViewById(searchCloseButtonId);
        // Set on click listener
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sv_products.getQuery().toString().equals("")) {
                    sv_products.clearFocus();
                    sv_products.setIconified(true);
                }
                else {
                    sv_products.setQuery("", false);
                    product_filter.filter("");
                }
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

    private void productsRequest(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Products Success!");
                System.out.println(response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean successFlag = jsonResponse.getBoolean("success");

                    if(successFlag){
                        JSONArray productStores = new JSONArray(jsonResponse.getString("product_stores"));
                        products = new ArrayList<>();

                        for (int i = 0; i < productStores.length(); ++i){
                            JSONObject jsonObj = productStores.getJSONObject(i);
                            // Get all products
                            int amount = jsonObj.getInt("amount");

                            JSONObject currentProduct = jsonObj.getJSONObject("product");
                            String id = currentProduct.getString("id");
                            String name = currentProduct.getString("name");
                            double price = currentProduct.getDouble("price");

                            products.add(new ProductModel(id,name,amount,price));
                        }
                        // Insert into list
                        product_adapter = new ProductsAdapter(products,getApplicationContext(),R.layout.products_item);
                        lv_products.setAdapter(product_adapter);
                        product_filter = product_adapter.getFilter();
                    }else{
                        displayMessage("Listado de productos Fallido.");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null){
                    String jsonResponse = new String(response.data);
                    System.out.println(jsonResponse);
                    displayMessage("Error al intentar iniciar sesión.");
                }
                else{
                    displayMessage("No response.");
                }
            }
        };

        ProductsRequest productsReq = new ProductsRequest(userInfo.getStore_id(),responseListener,errorListener);
        queue.add(productsReq);
        System.out.println("Products request done.");

    }

    public void displayMessage(String toastString){
        Toast.makeText(HomePageActivity.this, toastString, Toast.LENGTH_LONG).show();
    }

    private void setupSearchView()
    {
        sv_products.setIconifiedByDefault(true);
        sv_products.setOnQueryTextListener(this);
        sv_products.setSubmitButtonEnabled(false);
        sv_products.setQueryHint("Buscar Producto");
    }

}
