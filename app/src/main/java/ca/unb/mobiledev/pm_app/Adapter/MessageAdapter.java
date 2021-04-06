package ca.unb.mobiledev.pm_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ca.unb.mobiledev.pm_app.CreateTask;
import ca.unb.mobiledev.pm_app.GroupchatPage;
import ca.unb.mobiledev.pm_app.MessageDetail;
import ca.unb.mobiledev.pm_app.Model.Chat;

import ca.unb.mobiledev.pm_app.R;
import ca.unb.mobiledev.pm_app.TasksPage;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyHolder> {

    private Context context;
    private List<Chat> mChat;
    private FirebaseUser currentUser;

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    public MessageAdapter(Context context, List<Chat> mChat){
        this.context = context;
        this.mChat = mChat;
    }

    @NonNull
    @Override
    public MessageAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.MyHolder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.MyHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MyHolder holder, int position) {
        Chat chat = mChat.get(position);

        holder.show_message.setText(chat.getMessage());

        if(chat.getImageURL() != null){
            if(chat.getImageURL().equals("default")){
                holder.profile_image.setImageResource(R.mipmap.ic_launcher);
            }
            else {
                Glide.with(context).load(chat.getImageURL()).into(holder.profile_image);
            }
        }

        Date d = new Date(chat.getTimestamp());
        assert d != null;
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context.getApplicationContext());
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(context.getApplicationContext());
/*
        Date d = new Date(chat.getTimestamp());
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(d);
        StringBuilder dayString = new StringBuilder();
        dayString.append(calendar.get(Calendar.DAY_OF_MONTH)).append("/");
        dayString.append(calendar.get(Calendar.MONTH)+1).append("/");
        dayString.append(calendar.get(Calendar.YEAR));

        StringBuilder timeString = new StringBuilder();
        timeString.append(calendar.get(Calendar.HOUR)).append(".");
        timeString.append(calendar.get(Calendar.MINUTE)).append(" ");
        if (calendar.get(Calendar.PM) == 1){
            timeString.append("p.m.");
        }
        else{
            timeString.append("a.m.");
        }
*/
        //********* MIGHT WANT TO RECONSIDER ADDING LATER
        //holder.day_sent.setText(dateFormat.format(d));
        //holder.time_sent.setText(timeFormat.format(d));
        ////
        holder.day_sent.setVisibility(View.INVISIBLE);
        holder.time_sent.setVisibility(View.INVISIBLE);


        if(holder.name_of_sender != null){
            holder.name_of_sender.setText(chat.getSenderName());
        }


        holder.show_message.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(context, MessageDetail.class);
                intent.putExtra("senderId", chat.getSender());
                intent.putExtra("timeSent", timeFormat.format(d));
                intent.putExtra("daySent", dateFormat.format(d));
                intent.putExtra("projectId", chat.getProjectId());
                intent.putExtra("messageId", chat.getMessageId());
                intent.putExtra("message", chat.getMessage());
                intent.putExtra("sender", chat.getSenderName());
                context.startActivity(intent);
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView profile_image;
        public TextView day_sent;
        public TextView time_sent;
        public TextView name_of_sender;

        public MyHolder(@NonNull View itemView){
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
            day_sent = itemView.findViewById(R.id.tv_daysent);
            time_sent = itemView.findViewById(R.id.tv_timesent);
            name_of_sender = itemView.findViewById(R.id.tv_nameofsender);
        }

    }

    @Override
    public int getItemViewType(int position) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(currentUser.getUid())){
            return MSG_TYPE_RIGHT;

        }
        else{
            return MSG_TYPE_LEFT;
        }
    }
}
