package com.example.work_order;

import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
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
        holder.CButton.setText(order.get(position).getEstado());

        Log.println(Log.ASSERT , "a", order.get(position).getEstado());





        if(holder.CButton.getText().equals("COMPLETADA")){
            holder.CButton.setTextColor(Color.parseColor("#4AC000"));
            //holder.CButton.setBackgroundResource(R.drawable.colorsucefull);
            //holder.CButton.setBackgroundDrawable(c.getResources().getDrawable(R.drawable.colorsucefull));
            //holder.CButton.setBackgroundColor(Color.GREEN);
            //holder.CButton.setBackgroundTintList(c.getResources().getColorStateList(R.color.colorCompletada));



            //MediaPlayer mp= MediaPlayer.create(c, R.raw.a );
            //mp.start();
        }
        if (holder.CButton.getText().equals("PENDIENTE")){
            holder.CButton.setTextColor(Color.parseColor("#C03700"));

        }
    }


    @Override
    public int getItemCount() {
        return order.size();
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void sound(){


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
