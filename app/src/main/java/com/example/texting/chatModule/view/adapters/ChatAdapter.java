package com.example.texting.chatModule.view.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.texting.R;
import com.example.texting.common.pojo.Message;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 31/08/2020.
 * Derechos Reservados 2020
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private Context context;
    private List<Message> messages;
    private OnItemClickListener listener;

    private int lastPhoto = 0;

    public ChatAdapter(List<Message> messages, OnItemClickListener listener) {
        this.messages = messages;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Message message = messages.get(position);

        final int maxMarginHorizontal = context.getResources().getDimensionPixelSize(R.dimen.chat_margin_max_horizontal);
        final int maxMarginTop = context.getResources().getDimensionPixelSize(R.dimen.chat_margin_max_top);
        final int minMargin = context.getResources().getDimensionPixelSize(R.dimen.chat_margin_min);

        int gravity = Gravity.END;
        Drawable background = ContextCompat.getDrawable(context, R.drawable.background_chat_me);
        int marginStart = maxMarginHorizontal;
        int marginTop = minMargin;
        int marginEnd = minMargin;

        if (!message.isSendByMe()){
            gravity = Gravity.START;
            background = ContextCompat.getDrawable(context, R.drawable.background_chat_friend);
            marginEnd = maxMarginHorizontal;
            marginStart = minMargin;
        }

        if (position > 0 && message.isSendByMe() != messages.get(position -1).isSendByMe()){
            marginTop = maxMarginTop;
        }

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.tvMessage.getLayoutParams();
        params.gravity = gravity;
        params.setMargins(marginStart, marginTop, marginEnd, minMargin);

        if (message.getPhotoUrl() != null){
            holder.tvMessage.setVisibility(View.GONE);
            holder.imgPhoto.setVisibility(View.VISIBLE);

            if (position > lastPhoto){
                lastPhoto = position;
            }

            final int size = context.getResources().getDimensionPixelSize(R.dimen.chat_size_image);
            params.width = size;
            params.height = size;

            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_timer_sand_160)
                    .error(R.drawable.ic_emoticon_sad)
                    .centerCrop();

            Glide.with(context)
                    .asBitmap()
                    .load(message.getPhotoUrl())
                    .apply(options)
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            int dimension = size - context.getResources().getDimensionPixelSize(R.dimen.chat_padding_image);
                            Bitmap bitmap = ThumbnailUtils.extractThumbnail(resource, dimension, dimension);
                            holder.imgPhoto.setImageBitmap(bitmap);
                            if (!message.isLoaded()){
                                message.setLoaded(true);
                                if (position == lastPhoto){
                                    listener.onImageLoaded();
                                }
                            }
                            return true;
                        }
                    })
                    .into(holder.imgPhoto);

            holder.imgPhoto.setBackground(background);
            holder.setClickListener(message, listener);

        } else {
            holder.tvMessage.setVisibility(View.VISIBLE);
            holder.imgPhoto.setVisibility(View.GONE);

            params.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;

            holder.tvMessage.setBackground(background);
            holder.tvMessage.setText(message.getMsg());
        }

        holder.imgPhoto.setLayoutParams(params);
        holder.tvMessage.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void add(Message msg) {
        if (!messages.contains(msg)){
            messages.add(msg);
            notifyItemInserted(messages.size() - 1);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvMessage)
        TextView tvMessage;
        @BindView(R.id.imgPhoto)
        AppCompatImageView imgPhoto;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setClickListener(Message message, OnItemClickListener listener){
            imgPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickImage(message);
                }
            });
        }


    }
}
