package com.minosai.whistler;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by minos.ai on 29/10/17.
 */

public class EmergencyContactAdapter extends RecyclerView.Adapter<EmergencyContactAdapter.MyViewHolder> {

    List<String> contactNames;
    List<String> contactNumbers;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView contactNameText, contactNumberText;
        public RelativeLayout viewBackground;
        public ConstraintLayout viewForeground;

        public MyViewHolder(View itemView) {
            super(itemView);
            contactNameText = (TextView)itemView.findViewById(R.id.contactName);
            contactNumberText = (TextView)itemView.findViewById(R.id.contactNumber);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
        }
    }

    public EmergencyContactAdapter(List<String> contactNames, List<String> contactNumbers){
        this.contactNames = contactNames;
        this.contactNumbers = contactNumbers;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_detail_layout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.contactNameText.setText(contactNames.get(position));
        holder.contactNumberText.setText(contactNumbers.get(position));
    }

    @Override
    public int getItemCount() {
        return contactNumbers.size();
    }

    public void removeItem(int position) {
        contactNames.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(String name, String number, int position) {
        contactNames.add(position, name);
        contactNumbers.add(position, number);
        notifyItemInserted(position);
    }
}
