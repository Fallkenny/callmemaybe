package com.example.callmemaybe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
    private final ArrayList<Contact> contactList;
    private  ContactsClickListener contactsClickListener;

    public ContactsAdapter(ArrayList<Contact> contacts, ContactsClickListener contactsClickListener) {
        this.contactList = contacts;
        this.contactsClickListener = contactsClickListener;
    }

    @NonNull
    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_layout,parent,false);
        return new ViewHolder(view, contactsClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.ViewHolder holder, int position) {
        holder.setData(contactList.get(position));
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private TextView txtContactName;
        private ImageView imageViewContact;
        private TextView txtContactPhone;

        ContactsClickListener contactsClickListener;
        public ViewHolder(@NonNull View itemView,  ContactsClickListener onContactListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.contactsClickListener = onContactListener;
            imageViewContact = itemView.findViewById(R.id.imageViewContact);
            txtContactName = itemView.findViewById(R.id.contactName);
            txtContactPhone = itemView.findViewById(R.id.contactPhone);
        }
        private void setData(Contact contact) {
            txtContactName.setText(contact.getName());
            txtContactPhone.setText(contact.getPhoneNumber());
            imageViewContact.setImageBitmap(contact.getProfilePicture());
        }

        public void onClick(View view){
            contactsClickListener.onContactClick(getAdapterPosition());
        }
    }

}