package com.example.work_order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

import static com.example.work_order.R.layout.card;

public class myAdapter extends RecyclerView.Adapter<MyHome> {

    Context c;
    ArrayList<OrdenDeTrabajo> order;
    private View.OnClickListener listener;
    private MyHome.RecyListener recyListener;
    private int lastPosition = -1;




    public myAdapter(Context c, ArrayList<OrdenDeTrabajo> order , MyHome.RecyListener recyListener) {
        this.c = c;
        this.order = order;
        this.recyListener = recyListener;

    }

    @NonNull
    @Override
    public MyHome onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(card, parent , false); //
        // this

        return new MyHome(view, recyListener);
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


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);


    }



    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
