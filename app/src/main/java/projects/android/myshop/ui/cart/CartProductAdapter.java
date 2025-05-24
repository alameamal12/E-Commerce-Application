package projects.android.myshop.ui.cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import projects.android.myshop.databinding.CartProductItemBinding;
import projects.android.myshop.db.entity.ProductEntity;


// adapter for cart products
public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.ViewHolder> {

    private final OnCartProductItemClickListener listener;
    private final List<ProductEntity> products = new ArrayList<>();
    private final boolean isCheckout;

    public CartProductAdapter(OnCartProductItemClickListener listener, boolean isCheckout) {
        this.listener = listener;
        this.isCheckout = isCheckout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each cart product item
        CartProductItemBinding binding = CartProductItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind the data of the product to the ViewHolder
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        // Return the total number of cart products
        return products.size();
    }

    public void updateCartProducts(@NonNull List<ProductEntity> products) {
        int len = this.products.size();
        // Clear the current list of products
        this.products.clear();
        // Notify the adapter of the removed items
        notifyItemRangeRemoved(0, len);
        len = products.size();
        // Add the new list of products
        this.products.addAll(products);
        // Notify the adapter of the inserted items
        notifyItemRangeInserted(0, len);
    }

    public interface OnCartProductItemClickListener {
        void onCartProductClicked(@NonNull ProductEntity product);

        void onRemoveProductClicked(@NonNull ProductEntity product);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CartProductItemBinding binding;

        public ViewHolder(@NonNull CartProductItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ProductEntity product) {
            // Bind the product data to the views in the ViewHolder
            binding.productImg.setImageURI(product.getImageUri());
            binding.productNameTv.setText(product.getName());
            binding.productDescTv.setText(product.getDescription());
            binding.priceTv.setText(product.getPrice() + "");

            if (isCheckout) {
                // Hide the remove button if it is checkout mode
                binding.removeBtn.setVisibility(View.GONE);
            } else {
                binding.removeBtn.setOnClickListener(v -> {
                    // Disable the remove button to prevent multiple clicks
                    binding.removeBtn.setEnabled(false);
                    // Call the listener when the remove button is clicked
                    listener.onRemoveProductClicked(product);
                });
            }
            binding.getRoot().setOnClickListener(v -> listener.onCartProductClicked(product));
        }

    }

}
