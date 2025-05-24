package projects.android.myshop.ui.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import projects.android.myshop.databinding.HomeItemBinding;
import projects.android.myshop.db.entity.CategoryEntity;

// adapter for home items
public class HomeItemAdapter extends RecyclerView.Adapter<HomeItemAdapter.ViewHolder> {

    private final HomeItemClickListener clickListener;
    private final List<CategoryEntity> categoryEntities = new ArrayList<>();

    public HomeItemAdapter(@NonNull HomeItemClickListener clickListener) {
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public HomeItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the home item layout
        HomeItemBinding binding = HomeItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeItemAdapter.ViewHolder holder, int position) {
        // Bind the data to the ViewHolder
        holder.bind(categoryEntities.get(position));
    }

    @Override
    public int getItemCount() {
        // Return the size of the category list
        return categoryEntities.size();
    }

    public void updateCategories(@NonNull List<CategoryEntity> categoryEntities) {
        // Update the category list with new data
        int len = this.categoryEntities.size();
        this.categoryEntities.clear();
        notifyItemRangeRemoved(0, len);
        len = categoryEntities.size();
        this.categoryEntities.addAll(categoryEntities);
        notifyItemRangeInserted(0, len);
    }

    public interface HomeItemClickListener {
        // Interface for home item click events

        void onCategoryClicked(@NonNull CategoryEntity category);

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        protected final HomeItemBinding binding;

        public ViewHolder(@NonNull HomeItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(@NonNull CategoryEntity category) {
            // Bind category data to the ViewHolder

            binding.categoryImg.setImageURI(category.getImageUri());
            binding.categoryTv.setText(category.getName());
            binding.getRoot().setOnClickListener(v -> clickListener.onCategoryClicked(category));
        }

    }

}
