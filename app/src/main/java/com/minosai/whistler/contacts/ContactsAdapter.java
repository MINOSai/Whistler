package com.minosai.whistler.contacts;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.minosai.whistler.R;

import java.util.ArrayList;

/**
 * Created by minos.ai on 04/12/17.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private ArrayList<Contact> mContacts = new ArrayList<>();

    public ContactsAdapter(ArrayList<Contact> mContacts) {
        this.mContacts = mContacts;
    }

    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_detail_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ContactsAdapter.ViewHolder holder, int position) {
        final Contact currentContact = mContacts.get(position);
        holder.contactNumber.setText(currentContact.getContactNumber());

        String currentContactName = currentContact.getContactName();
        String[] nameParts = currentContactName.split(" ");
        holder.contactName.setText(nameParts[0]+" "+nameParts[1]+" "+nameParts[2]);

//        if(currentContact.getContactPhoto() != null) {
//            holder.contactPhoto.setImageBitmap(currentContact.getContactPhoto());
//        }

        holder.removeContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int removePosition = holder.getAdapterPosition();
                mContacts.remove(removePosition);
                notifyItemRemoved(removePosition);
                notifyItemRangeChanged(removePosition, mContacts.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView contactName,contactNumber,emptyText;
        ImageView contactPhoto,removeContact;

        public ViewHolder(View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contactName);
            contactNumber = itemView.findViewById(R.id.contactNumber);
            contactPhoto = itemView.findViewById(R.id.contact_photo_imageview);
            removeContact = itemView.findViewById(R.id.contact_delete_imageview);
            emptyText = itemView.findViewById(R.id.contactsEmptyTextView);
        }
    }
}
