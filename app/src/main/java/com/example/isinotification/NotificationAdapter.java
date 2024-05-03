package com.example.isinotification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    ArrayList<IsiNotification> notifications;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildListener;

    public NotificationAdapter() {
       // FirebaseUtil.openFbReference("isinotification", activity);
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;
        notifications = FirebaseUtil.mNotifications;

        mChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @NonNull String s) {
                IsiNotification in = dataSnapshot.getValue(IsiNotification.class);
                in.setId(dataSnapshot.getKey());
                Log.e("Notification: ", in.getTitle());
                notifications.add(in);
                notifyItemInserted(notifications.size()-1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabaseReference.addChildEventListener(mChildListener);

    }
    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.rv_row, parent, false);
        return new NotificationViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(NotificationAdapter.NotificationViewHolder holder, int position) {
        IsiNotification notification = notifications.get(position);
        holder.bind(notification);
    }

    @Override
    public int getItemCount() {
        if (notifications == null) {
            return 0;
        }
        return notifications.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvCategory;
        TextView tvTitle;
        TextView tvBody;
        TextView tvPriority;

        public NotificationViewHolder(View itemView) {
            super(itemView);
            tvCategory = (TextView) itemView.findViewById(R.id.tvCategory);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvPriority = (TextView) itemView.findViewById(R.id.tvPriority);
            itemView.setOnClickListener(this);
        }

        public void bind (IsiNotification notification) {
            tvCategory.setText(notification.getCategory());
            tvTitle.setText(notification.getTitle());
            tvBody.setText(notification.getBody());
            tvPriority.setText(notification.getPriority());

        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Log.d("Click", String.valueOf(position));
            IsiNotification selectedNotification = notifications.get(position);
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            intent.putExtra("Notification", selectedNotification);
            view.getContext().startActivity(intent);
        }
    }


}
