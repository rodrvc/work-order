package com.example.work_order;

import android.content.Context;
import android.icu.text.CaseMap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.Objects;

public class MyHome extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView CTitle, CDescripcion;

    RecyListener recyLister ;

    public MyHome( @NonNull View itemView , RecyListener recyLister ) {
        super(itemView);

        CTitle = (TextView) itemView.findViewById(R.id.card_title);
        CDescripcion = itemView.findViewById(R.id.card_description);

        this.itemView.setPadding(1 , 1 , 10, 50);
        this.recyLister = recyLister;
        itemView.setOnClickListener(this);



    }

    @Override
    public void onClick(@NonNull View view) {
        recyLister.recyClick(getAdapterPosition());

    }


    public interface RecyListener {
        void recyClick(int position);
    }
}
