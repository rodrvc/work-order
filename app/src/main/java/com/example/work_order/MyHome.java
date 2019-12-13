package com.example.work_order;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.text.CaseMap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.dynamic.IFragmentWrapper;

public class MyHome extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView CTitle, CDescripcion;
    ImageView CDelete;
    Button CButton;
    Context c;

    RecyListener recyLister ;

    private int lastPosition = -1;
    private Context context;

    public MyHome( @NonNull View itemView , RecyListener recyLister ) {
        super(itemView);

        CTitle = itemView.findViewById(R.id.card_title);
        CDescripcion = itemView.findViewById(R.id.card_description);
        CDelete = itemView.findViewById(R.id.card_delete);
        CButton = itemView.findViewById(R.id.ButtonEstado);


        //this.itemView.setPadding(1 , 1 , 10, 50);
        this.recyLister = recyLister;

        CTitle.setOnClickListener(MyHome.this);
        CDelete.setOnClickListener(MyHome.this);
        CButton.setOnClickListener(MyHome.this);
        itemView.setOnClickListener(this);


        setAnimation(this.itemView, getAdapterPosition());


    }

    @Override
    public void onClick(@NonNull View view) {

        String accion = "";


        switch (view.getId()){
            case R.id.card_title:
                Log.i("hola","Titulo" );
                //home.O.remove(getAdapterPosition());
                accion = "apretarTitulo";
                break;


            case R.id.card_delete:
                accion = "borrarItem";
                break;



            case R.id.ButtonEstado:
                // Colores cambio de estado prueba;
                accion = "changeState";





                break;

            default:
                accion = "EditItem";
                break;




        }


        recyLister.recyClick(getAdapterPosition(), accion);




    }



    public interface RecyListener {
        void recyClick(int position , String a);
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
