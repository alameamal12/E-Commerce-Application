package projects.android.myshop.ui.order;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import projects.android.myshop.databinding.OrderItemBinding;
import projects.android.myshop.model.OrderWithFirstProduct;

// adapter for order
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private final List<OrderWithFirstProduct> orders = new ArrayList<>();
    private final OnOrderItemClickListener listener;

    public OrderAdapter(OnOrderItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderItemBinding binding = OrderItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        holder.bind(orders.get(position));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void updateOrders(List<OrderWithFirstProduct> orders) {
        int len = this.orders.size();
        this.orders.clear();
        notifyItemRangeRemoved(0, len);
        len = orders.size();
        this.orders.addAll(orders);
        notifyItemRangeInserted(0, len);
    }

    public interface OnOrderItemClickListener {
        void onOrderClicked(OrderWithFirstProduct order);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        OrderItemBinding binding;

        public ViewHolder(@NonNull OrderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(OrderWithFirstProduct order) {
            binding.img.setImageURI(order.getFirstProduct().getImageUri());
            binding.orderNameTv.setText(order.getFirstProduct().getName());
            binding.orderDescTv.setText(order.getFirstProduct().getDescription());
            binding.getRoot().setOnClickListener(v -> listener.onOrderClicked(order));
        }
    }

}
