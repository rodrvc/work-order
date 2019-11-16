package com.example.work_order;

import android.content.Context;
import android.icu.text.CaseMap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.Objects;

public class MyHome extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView CTitle, CDescripcion;



    public MyHome( View itemView) {
        super(itemView);

        CTitle = (TextView) itemView.findViewById(R.id.card_title);
        CDescripcion = itemView.findViewById(R.id.card_description);

        this.itemView.setPadding(1 , 1 , 10, 50);

        itemView.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

    }
}
