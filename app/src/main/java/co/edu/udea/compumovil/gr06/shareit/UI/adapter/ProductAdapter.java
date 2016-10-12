package co.edu.udea.compumovil.gr06.shareit.UI.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import co.edu.udea.compumovil.gr06.shareit.R;
import co.edu.udea.compumovil.gr06.shareit.UI.model.Product;

/**
 * Created by brayan on 12/10/16.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>  {
    private List<Product> products;
    private static OnItemClickListenerPropio onItemClickListenerPropio;

    public void setOnItemClickListenerPropio(OnItemClickListenerPropio listener) {
        onItemClickListenerPropio = listener;
    }

    public interface OnItemClickListenerPropio {
        void onItemClicked(View view, int position);
    }


    public static class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imgFoto;
        private TextView userName;
        private TextView productNAme;
        private Context contexto;


        public ProductViewHolder(View itemView, Context contexto) {
            super(itemView);
            imgFoto = (ImageView) itemView.findViewById(R.id.imgFoto);
            userName = (TextView) itemView.findViewById(R.id.userName);
            productNAme = (TextView) itemView.findViewById(R.id.productName);
            this.contexto = contexto;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = ProductViewHolder.super.getAdapterPosition();
            //onItemClickListenerPropio.onItemClicked(v, position);
        }

    }

    public ProductAdapter(List<Product> products) {

        this.products = products;
    }


    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, parent, false);
        return new ProductViewHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(ProductAdapter.ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.userName.setText(product.getNameUser());
        holder.productNAme.setText(product.getProductName());
        Picasso.with(holder.contexto).load(product.getPathPoto()).into(holder.imgFoto);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}

