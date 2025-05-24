package projects.android.myshop.ui.category;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import projects.android.myshop.databinding.CategoryItemBinding;
import projects.android.myshop.db.entity.CategoryEntity;
import projects.android.myshop.model.CategoryWithProductCount;

// adapter for categories
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final List<CategoryWithProductCount> categoryWithProductCounts = new ArrayList<>();
    private final OnCategoryItemClickListener listener;

    public CategoryAdapter(OnCategoryItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the category item layout
        CategoryItemBinding binding = CategoryItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind the data to the ViewHolder
        holder.bind(categoryWithProductCounts.get(position));
    }

    @Override
    public int getItemCount() {
        // Return the size of the category list
        return categoryWithProductCounts.size();
    }

    public void updateCategory(List<CategoryWithProductCount> categoryWithProductCounts) {
        // Update the category list with new data
        int len = this.categoryWithProductCounts.size();
        this.categoryWithProductCounts.clear();
        notifyItemRangeRemoved(0, len);
        len = categoryWithProductCounts.size();
        this.categoryWithProductCounts.addAll(categoryWithProductCounts);
        notifyItemRangeInserted(0, len);
    }

    public interface OnCategoryItemClickListener {
        // Interface for category item click events

        void onCategoryClicked(@NonNull CategoryEntity category);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CategoryItemBinding binding;

        public ViewHolder(@NonNull CategoryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(@NonNull CategoryWithProductCount categoryWithProductCount) {
            // Bind category data to the ViewHolder
            binding.categoryImg.setImageURI(categoryWithProductCount.category.getImageUri());
            binding.categoryNameTv.setText(categoryWithProductCount.category.getName());
            binding.productNumbersTv.setText(String.format(Locale.getDefault(), "%d products", categoryWithProductCount.productCount));
            binding.getRoot().setOnClickListener(v -> listener.onCategoryClicked(categoryWithProductCount.category));
        }

    }

}
