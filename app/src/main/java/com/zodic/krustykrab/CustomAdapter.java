package com.zodic.krustykrab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zodic.krustykrab.application.database.DAO.OrderDAO;
import com.zodic.krustykrab.application.database.DatabaseHelper;
import com.zodic.krustykrab.application.models.Order;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    public Order cartOrder;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView price;
        private final TextView quantity;
        private final TextView sum_Ofproduct;

        private final TextView plus ;
        private final TextView minus ;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            title = (TextView) view.findViewById(R.id.titleTxt);
            price = (TextView) view.findViewById(R.id.feeEachItem);
            quantity = (TextView) view.findViewById(R.id.numItemtxt);
            sum_Ofproduct = (TextView) view.findViewById(R.id.totalEachItem);
            plus = (TextView) view.findViewById(R.id.plustxt);
            minus = (TextView) view.findViewById(R.id.minustxt);
        }
        public TextView getPriceTV() {
            return price;
        }

        public TextView getTitleTV() {
            return title;
        }

        public TextView getQuantityTV() {
            return quantity;
        }

        public TextView getsumTV() {
            return sum_Ofproduct;
        }

        public TextView getPlusTV() {
            return plus;
        }

        public TextView getMinusTV() {
            return minus;
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param cartOrder Order containing the data to populate views to be used
     * by RecyclerView
     */
    public CustomAdapter(Order cartOrder) {
        this.cartOrder = cartOrder;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.viewholder_cart, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        double priceitem = cartOrder.getOrderProducts().get(position).getProduct().getPrice();
        double quantityitem = cartOrder.getOrderProducts().get(position).getQuantity();
        double sum = priceitem * quantityitem;
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTitleTV().setText(cartOrder.getOrderProducts().get(position).getProduct().getName());
        viewHolder.getPriceTV().setText("" + cartOrder.getOrderProducts().get(position).getProduct().getPrice());
        viewHolder.getQuantityTV().setText("" + cartOrder.getOrderProducts().get(position).getQuantity());
        viewHolder.getsumTV().setText("" + sum);



        viewHolder.getPlusTV().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newQuantity = cartOrder.getOrderProducts().get(position).getQuantity() + 1;

                cartOrder.getOrderProducts().get(position).setQuantity(newQuantity);

                viewHolder.getQuantityTV().setText("" + newQuantity);

                viewHolder.getsumTV().setText("" + newQuantity * priceitem);

            }
        });


        viewHolder.getMinusTV().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newQuantity = cartOrder.getOrderProducts().get(position).getQuantity() - 1;

                cartOrder.getOrderProducts().get(position).setQuantity(newQuantity);

                viewHolder.getQuantityTV().setText("" + newQuantity);

                viewHolder.getsumTV().setText("" + newQuantity * priceitem);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return cartOrder.getOrderProducts().size();
    }

}
