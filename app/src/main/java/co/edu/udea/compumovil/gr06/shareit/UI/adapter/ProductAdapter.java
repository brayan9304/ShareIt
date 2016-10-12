package co.edu.udea.compumovil.gr06.shareit.UI.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import co.edu.udea.compumovil.gr06.shareit.R;
import co.edu.udea.compumovil.gr06.shareit.UI.model.Product;

/**
 * Created by brayan on 12/10/16.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> products;

    public static class ProductViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgFoto;
        private TextView userName;
        private TextView productNAme;


        public ProductViewHolder(View itemView) {
            super(itemView);
            imgFoto = (ImageView) itemView.findViewById(R.id.imgFoto);
            userName = (TextView) itemView.findViewById(R.id.userName);
            productNAme = (TextView) itemView.findViewById(R.id.productName);
        }
    }

    public ProductAdapter(List<Product> products) {

        this.products = products;
    }

    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductAdapter.ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.userName.setText(product.getNameUser());
        holder.productNAme.setText(product.getProductName());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}

