package com.example.work_order;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    String idUsuario  = user.getUid();








    private static OrdenDeTrabajo ot = new OrdenDeTrabajo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //INICIALIZACION DE COMPONENTES
        final FloatingActionButton fab = findViewById(R.id.fab);
        Toolbar toolbar =findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("WorkOrder");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.colapsing);
        collapsingToolbar.setExpandedTitleMargin(130, 640 , 100 ,100);
        collapsingToolbar.setTitleEnabled(true);

        collapsingToolbar.setTitle("MIS OTS");





        //iniciar firebase
        inicializarFirebase();

        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));



        //boton fav
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                cambioForm();
            }

        });
        listarDatos();




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.cerrar:
                Toast.makeText(this ,"Cerrando Sesion " ,Toast.LENGTH_LONG).show();
                signOut();
                Intent SingIntent = new Intent(home.this, Singup.class);
                startActivity(SingIntent);
                finish();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    private void listarDatos() {
        databaseReference.child("WK").child(idUsuario).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                O.clear();
                for (DataSnapshot ob : dataSnapshot.getChildren()) {
                    String  uid, title, description , idUsuario , estado;

                    uid = ob.child("uid").getValue(String.class);
                    title = ob.child("title").getValue(String.class);
                    description = ob.child("description").getValue(String.class);
                    idUsuario = ob.child("idUsuario").getValue(String.class);
                    estado = ob.child("estado").getValue(String.class);


                    OrdenDeTrabajo ot = new OrdenDeTrabajo(uid, title , description , idUsuario , estado);

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

    //Abrir formulario para agregar
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
        String descriptionTareaEditar = o.getDescription();
        String tituloTareaEditar = o.getTitle();
        String idEditar = o.getUid();
        String prueba = "prueba de string ";

                Intent homeIntent = new Intent(home.this, EditarTarea.class);
                homeIntent.putExtra("titleEdit", tituloTareaEditar);
                homeIntent.putExtra("descriptionEdit" , descriptionTareaEditar);
                homeIntent.putExtra("idEdit" , idEditar);
                startActivityForResult(homeIntent, 1);
    }

    private void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }



    @Override // encarga decibir el parametro click en el recyclerview
    public void recyClick(int position , String accion) {
        OrdenDeTrabajo order ;
        if (accion == "apretarTitulo") {
            Toast.makeText(this, "Uted a precionado el titulo ", Toast.LENGTH_LONG).show();
        }else if (accion == "borrarItem"){

            order = O.get(position);

            databaseReference.child("WK").child(idUsuario).child(order.getUid()).removeValue();
            O.remove(position);
            adapter.notifyItemRemoved(position);
            adapter.notifyDataSetChanged();
            adapter.notifyItemRangeChanged(position, O.size());
            Toast.makeText(this, "Eliminado dato", Toast.LENGTH_LONG).show();



        } else if(accion == "EditItem"){
            OrdenDeTrabajo o = O.get(position);
            cambioFormEdit(o);



        } else if(accion == "changeState"){
            order = O.get(position);
            switch (order.getEstado()) {
                case "PENDIENTE":
                    order.setEstado("EN CURSO");
                    break;

                case "COMPLETADA":
                    order.setEstado("PENDIENTE");
                    break;
                case "EN CURSO":
                    order.setEstado("COMPLETADA");
                    break;
                    default:
                        order.setEstado("PENDIENTE");
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position, O.size());
                    Toast.makeText(this, "he" , Toast.LENGTH_LONG);





            }
            databaseReference.child("WK").child(idUsuario).child(order.getUid()).child("estado").setValue(order.getEstado());


        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);




        String titulo = "";
        String description = "";
        String id = "";
        String estado = "0";

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
            //si tiene id;
            if (data.hasExtra("id")) {
                Toast.makeText(this, data.getExtras().getString("id"),
                        Toast.LENGTH_SHORT).show();
                id = data.getExtras().getString("id");
                comprobarModificar(titulo, description , id);
                return;
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
                ot.setEstado("Pendiente");
                O.add(ot);
                databaseReference.child("WK").child(idUsuario).child(ot.getUid()).setValue(ot);
            }
        }
    }

    private void comprobarModificar(String titulo, String descripcion , String id) {

        if (titulo != null || descripcion != null) {
            if (titulo != "" || descripcion != "") {
                ot.setUid(id);
                ot.setTitle(titulo);
                ot.setDescription(descripcion);

                databaseReference.child("WK").child(idUsuario).child(ot.getUid()).setValue(ot);
                adapter.notifyDataSetChanged();

            }
        }
    }




    public void signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
        // [END auth_fui_signout]
    }



























    ///******BASURAAAAAAA

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

}
