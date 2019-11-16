package com.example.work_order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

import static com.example.work_order.R.layout.card;

public class myAdapter extends RecyclerView.Adapter<MyHome> implements View.OnClickListener {

    Context c;
    ArrayList<OrdenDeTrabajo> order;
    private View.OnClickListener listener;





    public myAdapter(Context c, ArrayList<OrdenDeTrabajo> order) {
        this.c = c;
        this.order = order;
    }

    @NonNull
    @Override
    public MyHome onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(card, parent , false); //
        // this
        view.setOnClickListener(this);
        return new MyHome(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHome holder, int position) {

        holder.CTitle.setText(order.get(position).getTitle());
        holder.CDescripcion.setText(order.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return order.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener ;
    }

    @Override
    public void onClick(View view) {
        if (listener!=null){
            listener.onClick(view);
        }

    }
}
