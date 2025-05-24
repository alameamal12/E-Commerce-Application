package projects.android.myshop.ui.user;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import projects.android.myshop.databinding.UserItemBinding;
import projects.android.myshop.db.entity.UserEntity;

// adapter for user
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final OnUserItemClickListener listener;
    private final ArrayList<UserEntity> users = new ArrayList<>();

    public UserAdapter(OnUserItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UserItemBinding binding = UserItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        holder.bind(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void updateUsers(ArrayList<UserEntity> users) {
        int len = this.users.size();
        this.users.clear();
        notifyItemRangeRemoved(0, len);
        len = users.size();
        this.users.addAll(users);
        notifyItemRangeInserted(0, len);
    }

    public interface OnUserItemClickListener {
        void onUserClicked(UserEntity user);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        UserItemBinding binding;

        public ViewHolder(@NonNull UserItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(UserEntity user) {
            binding.nameTv.setText(user.getFullName());
            binding.emailTv.setText(user.getEmail());
            binding.getRoot().setOnClickListener(v -> listener.onUserClicked(user));
        }

    }

}
