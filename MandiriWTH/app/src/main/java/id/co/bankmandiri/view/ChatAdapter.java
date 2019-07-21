package id.co.bankmandiri.view;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import id.co.bankmandiri.R;
import id.co.bankmandiri.common.api.model.Chat;

import static android.text.format.DateUtils.getRelativeTimeSpanString;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<Chat> mChats;

    public ChatAdapter() {
        mChats = new ArrayList<>();
    }

    public void add(Chat object) {
        mChats.add(object);
    }

    public List<Chat> getData() {
        return mChats;
    }

    public Chat getItem(int index) {
        return this.mChats.get(index);
    }

    public void setData(List<Chat> newData) {
        mChats = newData;
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        holder.bind(mChats.get(position));
    }

    @Override
    public int getItemCount() {
        return this.mChats.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvDate;
        private TextView tvChat;
        private LinearLayout llTextContainer;
        private ImageView ivLeft;
        private ImageView ivRight;
        private LinearLayout wrapper;

        public ChatViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvChat = itemView.findViewById(R.id.tv_chat);
            llTextContainer = itemView.findViewById(R.id.ll_text_container);
            ivLeft = itemView.findViewById(R.id.iv_profile_left);
            ivRight = itemView.findViewById(R.id.iv_profile_right);
            wrapper = itemView.findViewById(R.id.ll_wrapper);
        }

        public void bind(Chat chat) {
            final Context context = itemView.getContext();

            tvName.setText(chat.getName());
            tvDate.setText(getRelativeTimeSpanString(chat.getDate()));
            tvChat.setText(chat.getContent());

            if (chat.isMe()) {
                ivLeft.setVisibility(View.GONE);
                ivRight.setVisibility(View.VISIBLE);
                loadImage(context, chat.getImage(), ivRight, false);
            } else {
                ivLeft.setVisibility(View.VISIBLE);
                ivRight.setVisibility(View.GONE);
                // TODO for now bot image will always flipped
                loadImage(context, chat.getImage(), ivLeft, true);
            }

            llTextContainer.setBackgroundResource(chat.isMe() ? R.drawable.ic_baloon_right : R.drawable.ic_baloon_left);
            wrapper.setGravity(chat.isMe() ? Gravity.RIGHT : Gravity.LEFT);

            setReadStyle(chat);
        }

        private void setReadStyle(Chat chat) {
            if (chat.getStatus_read() == 0) {
                tvChat.setTypeface(null, Typeface.BOLD);
                tvDate.setTypeface(null, Typeface.BOLD);
            } else {
                tvChat.setTypeface(null, Typeface.NORMAL);
                tvDate.setTypeface(null, Typeface.NORMAL);
            }
        }

        private void loadImage(Context context, Object imageUrl, ImageView target, boolean flip) {
            if (imageUrl != null) {
                if (imageUrl instanceof String
                        && ((String) imageUrl).toLowerCase().endsWith("gif")) {
                    loadGifImage(context, imageUrl, target, flip);
                } else {
                    loadGeneralImage(context, imageUrl, target, flip);
                }
            }
        }

        private void loadGifImage(Context context, Object imageUrl, ImageView target, boolean flip) {
            RequestOptions requestOptions = new RequestOptions()
                    .fitCenter()
                    .error(R.drawable.ic_default_profile);

            Glide.with(context)
                    .load(imageUrl)
                    .apply(requestOptions)
                    .into(target);

            if (flip) {
                target.setScaleX(-1f);
            } else {
                target.setScaleX(1f);
            }
        }

        private void loadGeneralImage(Context context, Object imageUrl, ImageView target, boolean flip) {
            RequestOptions requestOptions = new RequestOptions()
                    .fitCenter()
                    .error(R.drawable.ic_default_profile)
                    .circleCrop();

            Glide.with(context)
                    .load(imageUrl)
                    .apply(requestOptions)
                    .into(target);

            if (flip) {
                target.setScaleX(-1f);
            } else {
                target.setScaleX(1f);
            }
        }
    }
}