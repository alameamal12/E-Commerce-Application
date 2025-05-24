package projects.android.myshop.ui.product;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import projects.android.myshop.databinding.ProductItemBinding;
import projects.android.myshop.db.entity.ProductEntity;

// adapter for product
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private final OnProductItemClickListener listener;

    private final List<ProductEntity> products = new ArrayList<>();

    public ProductAdapter(OnProductItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductItemBinding binding = ProductItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void updateProducts(List<ProductEntity> products) {
        int len = this.products.size();
        this.products.clear();
        notifyItemRangeRemoved(0, len);
        len = products.size();
        this.products.addAll(products);
        notifyItemRangeInserted(0, len);
    }

    public interface OnProductItemClickListener {
        void onProductClicked(@NonNull ProductEntity product);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ProductItemBinding binding;

        public ViewHolder(@NonNull ProductItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ProductEntity product) {
            binding.productTitleTv.setText(product.getName());
            binding.productImg.setImageURI(product.getImageUri());
            binding.getRoot().setOnClickListener(v -> listener.onProductClicked(product));
        }

    }

}
