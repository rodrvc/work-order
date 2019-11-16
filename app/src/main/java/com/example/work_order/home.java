package com.example.work_order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

public class home extends AppCompatActivity implements MyHome.RecyListener {
    private TextView tx;
    RecyclerView recycler;
    myAdapter adapter;
    public static ArrayList<OrdenDeTrabajo> O = new ArrayList<>();

    FirebaseDatabase database;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference rf = databaseReference.child("texto");


    private MaterialCardView card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        String title ;
        String descripcion;

        final FloatingActionButton fab = findViewById(R.id.fab);

            tx = (TextView) findViewById(R.id.textView);

        //firebase
        inicializarFirebase();

        recycler =  findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));


        // se trae los datos desde la activity Form
        title = recibirTitulo();
        descripcion = recibirDescription();

        // Si los datos no son nulos se agregan a fire base
        if (title != null || descripcion != null ){

            ArrayList<OrdenDeTrabajo> a = home.O;

            OrdenDeTrabajo ot = new OrdenDeTrabajo();
            ot.setUid(UUID.randomUUID().toString());
            ot.setTitle(title);
            ot.setDescription(descripcion);
            O.add(ot);
            databaseReference.child("OT").child(ot.getUid()).setValue(ot);
        }


        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
               cambioForm();
            }

        });


        listarDatos();
    }

    private void listarDatos(){
        databaseReference.child("OT").orderByChild("title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                O.clear();
                for (DataSnapshot ob : dataSnapshot.getChildren()){
                    OrdenDeTrabajo ot = ob.getValue(OrdenDeTrabajo.class);

                        O.add(ot);







                    adapter = new myAdapter(home.this, O , home.this );
                    //recycler.setVisibility(O.size() == 0 ? View.VISIBLE : View.GONE);




                    recycler.setAdapter(adapter);




                    adapter.notifyDataSetChanged();



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private ArrayList<OrdenDeTrabajo> getCard(){

        ArrayList<OrdenDeTrabajo> trabajos = home.O;

        OrdenDeTrabajo o = new OrdenDeTrabajo();
        o.setTitle("Titulo");
        o.setDescription("Descripcion");
        trabajos.add(o);

        OrdenDeTrabajo o1 = new OrdenDeTrabajo();
        o1.setTitle("Titulo");
        o1.setDescription("Descripcion1");
        trabajos.add(o1);

        OrdenDeTrabajo o2 = new OrdenDeTrabajo();
        o2.setTitle("Titulo2");
        o2.setDescription("Descripcion");
        trabajos.add(o2);

        o2.setTitle("Titulo2");
        o2.setDescription("Descripcion");
        trabajos.add(o2);

        o2.setTitle("Titulo2");
        o2.setDescription("Descripcion");
        trabajos.add(o2);

        o2.setTitle("Titulo2");
        o2.setDescription("Descripcion");
        trabajos.add(o2);
        o2.setTitle("Titulo2");
        o2.setDescription("Descripcion");
        trabajos.add(o2);

        o2.setTitle("Titulo2");
        o2.setDescription("Descripcion");
        trabajos.add(o2);

        return trabajos;
    }


    private void cambioForm(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {



                Intent homeIntent = new Intent(home.this, Form.class);
                startActivity(homeIntent);


            }
        }, 500 );

    }





    private void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }

    private String recibirTitulo(){
        String title = getIntent().getStringExtra("title");
       //  String description = getIntent().getStringExtra("description");
        return title;
    }

    private String recibirDescription(){

        String description = getIntent().getStringExtra("description");
        return description;
    }


    @Override
    public void recyClick(int position) {

        OrdenDeTrabajo order = O.get(position);

        databaseReference.child("OT").child(order.getUid()).removeValue();
        O.remove(position);



        adapter.notifyItemRemoved(position);
        adapter.notifyDataSetChanged();
        adapter.notifyItemRangeChanged(position, O.size());
        Toast.makeText(this, "Eliminado dato" , Toast.LENGTH_LONG).show();

    }



}
