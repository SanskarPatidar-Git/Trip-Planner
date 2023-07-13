package com.adventure.tripplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    Context context;
    ArrayList<CardModel> arrayList;
    public CardAdapter(Context context, ArrayList<CardModel> arrayList)
    {
        this.context=context;
        this.arrayList=arrayList;
    }
    @NonNull
    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.trip_cardview,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.ViewHolder holder, int position) {
        holder.destination.setText(arrayList.get(position).getDestination());
        holder.organization.setText(arrayList.get(position).getTeam());
        Picasso.get().load(arrayList.get(position).getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView destination,organization;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            destination=itemView.findViewById(R.id.cardTripName);
            organization=itemView.findViewById(R.id.cardOrganizerName);
            imageView=itemView.findViewById(R.id.cardTripImage);
        }
    }
}
