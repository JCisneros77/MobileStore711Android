package itesm.mobilestore711;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jcisneros77 on 4/23/17.
 */

public class CartAdapter extends ArrayAdapter<ProductModel> implements View.OnClickListener {
    private ArrayList<ProductModel> dataSet;
    Context menu_context;

    // View lookup cache
    private static class ViewHolder {
        TextView tv_product_name;
        TextView tv_product_price;
        TextView tv_product_amount;
        ImageView iv_product_image;
    }

    public CartAdapter(ArrayList<ProductModel> data, Context context,int layout_type) {
        super(context, layout_type, data);

        this.dataSet = data;
        this.menu_context=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        ProductModel ProductModel=(ProductModel) object;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ProductModel ProductModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            // Product List
            convertView = inflater.inflate(R.layout.cart_item, parent, false);
            viewHolder.tv_product_name = (TextView) convertView.findViewById(R.id.tv_cart_item_name);
            viewHolder.tv_product_price = (TextView) convertView.findViewById(R.id.tv_cart_item_price);
            viewHolder.iv_product_image = (ImageView) convertView.findViewById(R.id.iv_cart_item_image);
            viewHolder.tv_product_amount = (TextView) convertView.findViewById(R.id.tv_cart_item_amount);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.tv_product_name.setText(ProductModel.getName());
        viewHolder.tv_product_price.setText("$ " + ProductModel.getPrice(ProductModel.getAmount()));
        viewHolder.iv_product_image.setImageResource(R.mipmap.ic_product);
        viewHolder.tv_product_amount.setText(Integer.toString(ProductModel.getAmount()));
        //viewHolder.info.setOnClickListener(this);

        // Return the completed view to render on screen
        return convertView;
    }

}