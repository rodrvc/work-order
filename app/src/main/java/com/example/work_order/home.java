package com.example.work_order;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
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


    private static OrdenDeTrabajo ot = new OrdenDeTrabajo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        String title;
        String descripcion;
        //components
        final FloatingActionButton fab = findViewById(R.id.fab);
        tx = (TextView) findViewById(R.id.textView);

        //iniciar firebase
        inicializarFirebase();

        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        //************CODIGO DE PRUEBA OBSOLETO
        // se trae los datos desde la activity Form
    /*
        title = ot.getTitle();
        descripcion = ot.getDescription();

        // Si los datos no son nulos se agregan a fire base
        if (ot.getTitle() != null || ot.getDescription() != null) {
            ot.setUid(UUID.randomUUID().toString());
            ot.setTitle(title);
            ot.setDescription(descripcion);
            O.add(ot);
            databaseReference.child("OT").child(ot.getUid()).setValue(ot);
            ot = null;
        }

        /*
     *****************************************************/


        //boton fav .. abrir nueva activity
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                cambioForm();
            }

        });
        listarDatos();
    }

    private void listarDatos() {
        databaseReference.child("OT").orderByChild("title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                O.clear();
                for (DataSnapshot ob : dataSnapshot.getChildren()) {
                    OrdenDeTrabajo ot = ob.getValue(OrdenDeTrabajo.class);

                    O.add(ot);

                    adapter = new myAdapter(home.this, O, home.this);
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


    private void cambioForm() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent homeIntent = new Intent(home.this, Form.class);
                startActivityForResult(homeIntent, 1);

            }
        }, 500);

    }

    //Abrir formulario de modificacion
    private void cambioFormEdit(OrdenDeTrabajo o ) {
        final String descriptionTareaEditar = o.getDescription();
        final String tituloTareaEditar = o.getTitle();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent homeIntent = new Intent(home.this, EditarTarea.class);
                startActivityForResult(homeIntent, 1);
                homeIntent.putExtra("title", tituloTareaEditar);
                homeIntent.putExtra("description" , descriptionTareaEditar);

            }
        }, 500);

    }



    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }

    private String recibirTitulo() {
        String title = null;

        //title = bundle.getString("title");
        title = getIntent().getStringExtra("title");
        //  String description = getIntent().getStringExtra("description");

        return title;
    }

    private String recibirDescription() {

        String description = null;


        description = getIntent().getStringExtra("description");
        //
        //description= bundle.getString("description");

        return description;
    }


    @Override // encarga decibir el parametro click en el recyclerview
    public void recyClick(int position , String accion) {

        if (accion == "apretarTitulo") {
            Toast.makeText(this, "Uted a precionado el titulo ", Toast.LENGTH_LONG).show();
        }else if (accion == "borrarItem"){

            OrdenDeTrabajo order = O.get(position);

            databaseReference.child("OT").child(order.getUid()).removeValue();
            O.remove(position);


            adapter.notifyItemRemoved(position);
            adapter.notifyDataSetChanged();
            adapter.notifyItemRangeChanged(position, O.size());
            Toast.makeText(this, "Eliminado dato", Toast.LENGTH_LONG).show();
        } else if(accion == "EditItem"){
            OrdenDeTrabajo o = O.get(position);
            cambioFormEdit(o);

        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String titulo = "";
        String description = "";

        if (resultCode == RESULT_OK && requestCode == 1) {
            if (data.hasExtra("title")) {
                if (data != null) {
                    titulo = data.getExtras().getString("title");
                }

            }
            if (data.hasExtra("description")) {
                Toast.makeText(this, data.getExtras().getString("description"),
                        Toast.LENGTH_SHORT).show();
                description = data.getExtras().getString("description");
            }
        }
        comprobarAgregar(titulo, description);
    }



    //agrega tarea
    private void comprobarAgregar(String titulo, String descripcion) {

        if (titulo != null || descripcion != null) {
            if (titulo != "" || descripcion != "") {
                ot.setUid(UUID.randomUUID().toString());
                ot.setTitle(titulo);
                ot.setDescription(descripcion);
                O.add(ot);
                databaseReference.child("OT").child(ot.getUid()).setValue(ot);
            }

        }
    }



    private ArrayList<OrdenDeTrabajo> getCard() {

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
}
