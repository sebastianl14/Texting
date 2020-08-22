package com.example.texting.mainModule.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.texting.R;
import com.example.texting.common.pojo.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 15/08/2020.
 * Derechos Reservados 2020
 */
public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private List<User> users;
    private Context context;
    private OnItemClickListener listener;

    public RequestAdapter(List<User> users, OnItemClickListener listener) {
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request,
                parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);

        holder.setOnClickListener(user, listener);
        holder.tvName.setText(user.getUsername());
        holder.tvEmail.setText(user.getEmail());

        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .error(R.drawable.ic_emoticon_sad)
                .placeholder(R.drawable.ic_emoticon_tongue);

        Glide.with(context)
                .load(user.getPhotoUrl())
                .apply(options)
                .into(holder.imgPhoto);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void add(User user){
        if (!users.contains(user)){
            users.add(user);
            notifyItemInserted(users.size() - 1);
        } else {
            update(user);
        }
    }

    public void update(User user) {
        if (users.contains(user)){
            int index = users.indexOf(user);
            users.set(index, user);
            notifyItemChanged(index);
        }
    }

    public void remove(User user){
        if (users.contains(user)){
            int index = users.indexOf(user);
            users.remove(index);
            notifyItemRemoved(index);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgPhoto)
        CircleImageView imgPhoto;
        @BindView(R.id.btnDeny)
        AppCompatImageButton btnDeny;
        @BindView(R.id.btnAccept)
        AppCompatImageButton btnAccept;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvEmail)
        TextView tvEmail;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setOnClickListener(User user, OnItemClickListener listener){
            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onAcceptRequest(user);
                }
            });

            btnDeny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDenyRequest(user);
                }
            });
        }
    }
}
