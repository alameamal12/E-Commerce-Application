package projects.android.myshop.ui.order;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import projects.android.myshop.databinding.OrderProductItemBinding;
import projects.android.myshop.db.entity.ProductEntity;

// adapter for order product
public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.ViewHolder> {

    private final List<ProductEntity> products = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderProductItemBinding binding = OrderProductItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void updateProducts(@NonNull List<ProductEntity> products) {
        int len = this.products.size();
        this.products.clear();
        notifyItemRangeRemoved(0, len);
        len = products.size();
        this.products.addAll(products);
        notifyItemRangeInserted(0, len);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        OrderProductItemBinding binding;

        public ViewHolder(@NonNull OrderProductItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ProductEntity product) {
            binding.productNameTv.setText(product.getName());
            binding.productSellingPriceTv.setText(String.format(Locale.getDefault(), "%.2f", product.getPrice()));
            binding.productImg.setImageURI(product.getImageUri());
        }

    }

}
