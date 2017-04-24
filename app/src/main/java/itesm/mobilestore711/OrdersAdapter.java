package itesm.mobilestore711;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jcisneros77 on 4/23/17.
 */

public class OrdersAdapter extends ArrayAdapter<OrderModel> implements View.OnClickListener{
    private ArrayList<OrderModel> dataSet;
    Context menu_context;

    // View lookup cache
    private static class ViewHolder {
        TextView tv_order_id;
        TextView tv_order_total_price;
    }

    public OrdersAdapter(ArrayList<OrderModel> data, Context context,int layout_type) {
        super(context, layout_type, data);

        this.dataSet = data;
        this.menu_context=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        OrderModel OrderModel =(OrderModel) object;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        OrderModel OrderModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            // Side Menu
            convertView = inflater.inflate(R.layout.orders_item, parent, false);
            viewHolder.tv_order_id = (TextView) convertView.findViewById(R.id.tv_order_id);
            viewHolder.tv_order_total_price = (TextView) convertView.findViewById(R.id.tv_order_total);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.tv_order_id.setText("Orden #".concat(OrderModel.getId()));
        viewHolder.tv_order_total_price.setText("Total $".concat(OrderModel.getTotal_cost()));
        //viewHolder.info.setOnClickListener(this);

        // Return the completed view to render on screen
        return convertView;
    }
}
