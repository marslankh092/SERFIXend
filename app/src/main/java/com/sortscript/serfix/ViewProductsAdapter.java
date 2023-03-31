package com.sortscript.serfix;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewProductsAdapter extends RecyclerView.Adapter<ViewProductsAdapter.Holder> {
    ArrayList<ProductModel> arrayList;
    Context context;
    ItemClickListener itemClickListener;

    public ViewProductsAdapter(ArrayList<ProductModel> arrayList, Context context, ViewProductsAdapter.ItemClickListener itemClickListener) {
        this.arrayList = arrayList;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewProductsAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_product_item, parent, false);
        return new ViewProductsAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewProductsAdapter.Holder holder, int position) {


        Picasso.get().load(arrayList.get(position).getProductImage()).into(holder.product_image);
        holder.name.setText(arrayList.get(position).getProductName());
        holder.description.setText(arrayList.get(position).getProductDescription());
        holder.price.setText(arrayList.get(position).getPrice());

        holder.delete_item.setOnClickListener(view -> {
            itemClickListener.delete(arrayList.get(position));
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        ImageView product_image, delete_item;
        TextView name, description, price;

        public Holder(@NonNull View itemView) {
            super(itemView);
            delete_item = itemView.findViewById(R.id.delete_item_view);
            product_image = itemView.findViewById(R.id.product_image_view);
            name = itemView.findViewById(R.id.name_view);
            description = itemView.findViewById(R.id.description_view);
            price = itemView.findViewById(R.id.price_view);

        }
    }

    interface ItemClickListener {
        public void delete(ProductModel model);

    }

}
