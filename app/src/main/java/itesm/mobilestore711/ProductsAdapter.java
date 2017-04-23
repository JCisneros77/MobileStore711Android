package itesm.mobilestore711;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jcisneros77 on 4/22/17.
 */

public class ProductsAdapter extends ArrayAdapter<ProductModel> implements View.OnClickListener,Filterable{
    private ArrayList<ProductModel> dataSet;
    public ArrayList<ProductModel> orig;
    Context menu_context;

    // View lookup cache
    private static class ViewHolder {
        TextView tv_product_name;
        TextView tv_product_price;
        ImageView iv_product_image;
    }

    public ProductsAdapter(ArrayList<ProductModel> data, Context context,int layout_type) {
        super(context, layout_type, data);

        this.dataSet = data;
        this.menu_context=context;

    }

    //@NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<ProductModel> results = new ArrayList<>();
                if (orig == null)
                    orig = dataSet;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final ProductModel p : orig) {
                            if (p.getName().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(p);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                dataSet = (ArrayList<ProductModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public ProductModel getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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
            convertView = inflater.inflate(R.layout.products_item, parent, false);
            viewHolder.tv_product_name = (TextView) convertView.findViewById(R.id.tv_product_item_name);
            viewHolder.tv_product_price = (TextView) convertView.findViewById(R.id.tv_product_item_price);
            viewHolder.iv_product_image = (ImageView) convertView.findViewById(R.id.iv_product_image);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.tv_product_name.setText(ProductModel.getName());
        viewHolder.tv_product_price.setText("$ " + ProductModel.getPrice());
        viewHolder.iv_product_image.setImageResource(R.mipmap.ic_product);
        //viewHolder.info.setOnClickListener(this);

        // Return the completed view to render on screen
        return convertView;
    }

}
