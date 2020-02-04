package com.example.work_order;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class home extends AppCompatActivity implements MyHome.RecyListener {
    private TextView tx;

    RecyclerView recycler;
    private static final int RC_SIGN_IN = 123;

    myAdapter adapter;
    public static ArrayList<OrdenDeTrabajo> O = new ArrayList<>();

    FirebaseDatabase database;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference rf = databaseReference.child("texto");




    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

                recyclerView.setLayoutAnimation(controller);
                recyclerView.getAdapter().notifyDataSetChanged();
                recyclerView.scheduleLayoutAnimation();
    }


    public static FirebaseAuth u = User.obtenerAutentificacion();
    public static FirebaseUser user = u.getCurrentUser();



    String idUsuario ;










    private static OrdenDeTrabajo ot = new OrdenDeTrabajo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        idUsuario = getIntent().getStringExtra("uid");

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
            //icono toolbar
            case R.id.cerrar:
                Toast.makeText(this ,"Cerrando Sesion " ,Toast.LENGTH_LONG).show();
                signOut();
                Intent SingIntent = new Intent(home.this, Singup.class);
                startActivity(SingIntent);
                finish();
                return true;

                default:
                    onBackPressed();


        }
        return super.onOptionsItemSelected(item);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        runLayoutAnimation(recycler);
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Presiona nuevamente para salir", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
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
        String estado = o.getEstado();
        String prueba = "prueba de string ";

                Intent homeIntent = new Intent(home.this, EditarTarea.class);
                homeIntent.putExtra("titleEdit", tituloTareaEditar);
                homeIntent.putExtra("descriptionEdit" , descriptionTareaEditar);
                homeIntent.putExtra("idEdit" , idEditar);
                homeIntent.putExtra("estadoEdit" , estado);


                startActivityForResult(homeIntent, 1);
        runLayoutAnimation(recycler);
    }

    private void inicializarFirebase(){

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
            //Renderiza el estado de la tarea
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
                    //adapter.notifyItemRangeChanged(position, O.size()); // Verificando utilizdad

                    Toast.makeText(this, "nueva tarea" , Toast.LENGTH_LONG);
            }


            databaseReference.child("WK").child(idUsuario).child(order.getUid()).child("estado").setValue(order.getEstado());


        }
    }







    @Override
    //se trae los datos de modificar
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);




        String titulo = "";
        String description = "";
        String id = "";
        String estado = "";

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

                id = data.getExtras().getString("id");
                estado = data.getExtras().getString("estado");
                comprobarModificar(titulo, description , id , estado);
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
        runLayoutAnimation(recycler);
    }

    private void comprobarModificar(String titulo, String descripcion , String id ,String estado) {

        if (titulo != null || descripcion != null) {
            if (titulo != "" || descripcion != "") {
                ot.setUid(id);
                ot.setTitle(titulo);
                ot.setDescription(descripcion);
                ot.setEstado(estado);

                databaseReference.child("WK").child(idUsuario).child(ot.getUid()).setValue(ot);
                adapter.notifyDataSetChanged();
                runLayoutAnimation(recycler);
            }
        }
    }






    public void signOut() {
        // [START auth_fui_signout]

        FirebaseAuth.getInstance().signOut();

        AuthUI.getInstance()
                .signOut(this)

                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        User us = new User();
                        us.salir();
                        User.salirSession();
                        FirebaseAuth.getInstance().signOut();
                        u.signOut();
                        Singup.salir();

                        //home.super.onDestroy();

                        Intent intent = new Intent(getBaseContext(), Singup.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);



                    }
                });

        User us = new User();
        us.salir();
        User.salirSession();
        FirebaseAuth.getInstance().signOut();
        u.signOut();
        Singup.salir();
        createSignInIntent();



        //home.super.onDestroy();





        Intent intent = new Intent(getBaseContext(), Singup.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

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


    public void createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.estadistica)

                        .build(),
                RC_SIGN_IN);
        // [END auth_fui_create_intent]
    }

}
