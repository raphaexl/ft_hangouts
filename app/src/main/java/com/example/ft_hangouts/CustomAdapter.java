package com.example.ft_hangouts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter <CustomAdapter.ContactViewHolder> {
    private Context context;
    Activity activity;
    private ArrayList contact_ids, names, surnames, tels, emails, abouts, addresses;
    private Animation translate_anim;

    CustomAdapter(Activity activity, Context context,  ArrayList contact_ids,  ArrayList names,  ArrayList surnames,  ArrayList tels, ArrayList emails, ArrayList abouts, ArrayList addresses){
        this.activity = activity;
        this.context = context;
        this.contact_ids = contact_ids;
        this.names = names;
        this.surnames = surnames;
        this.tels = tels;
        this.emails = emails;
        this.abouts = abouts;
        this.addresses = addresses;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.contact_row, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.contact_id_txt.setText(String.valueOf(position + 1));
        holder.name_txt.setText(String.valueOf(names.get(position)));
        holder.surname_txt.setText(String.valueOf(surnames.get(position)));
        holder.tel_txt.setText(String.valueOf(tels.get(position)));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayActivity.class);
                intent.putExtra("id", String.valueOf(contact_ids.get(position)));
                intent.putExtra("name", String.valueOf(names.get(position)));
                intent.putExtra("surname", String.valueOf(surnames.get(position)));
                intent.putExtra("tel", String.valueOf(tels.get(position)));
                intent.putExtra("email", String.valueOf(emails.get(position)));
                intent.putExtra("about", String.valueOf(abouts.get(position)));
                intent.putExtra("address", String.valueOf(addresses.get(position)));
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contact_ids.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        View mainLayout;
        TextView contact_id_txt, name_txt, surname_txt, tel_txt;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            contact_id_txt = itemView.findViewById(R.id.contact_id_txt);
            name_txt = itemView.findViewById(R.id.name_txt);
            surname_txt = itemView.findViewById(R.id.surname_txt);
            tel_txt = itemView.findViewById(R.id.phone_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            //RecyclerView Animation
            translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            mainLayout.setAnimation(translate_anim);
        }
    }
}
