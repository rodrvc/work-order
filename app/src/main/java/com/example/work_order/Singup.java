package com.example.work_order;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;


import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Singup extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    FirebaseDatabase database;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


    FirebaseAuth a = User.obtenerAutentificacion();

    public static FirebaseUser u;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);


        u = a.getCurrentUser();

        if (u != null ){


          Intent intent = new Intent(getBaseContext(), home.class);
            intent.putExtra("name", u.getDisplayName());
            intent.putExtra("email", u.getEmail());
            intent.putExtra("uid", u.getUid());

            startActivity(intent);



        }
        else createSignInIntent();
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (u != null ){

            Intent intent = new Intent(getBaseContext(), home.class);
            intent.putExtra("name", u.getDisplayName());
            intent.putExtra("email", u.getEmail());
            intent.putExtra("uid", u.getUid());

            startActivity(intent);
        }




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

    // [START auth_fui_result]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            Log.d(this.getClass().getName(), "this user signid in with" + response.getProviderType());

            FirebaseUser usuarioInstancia = User.obtenerUsuario();

            final User usuario = new User();

            if (resultCode == RESULT_OK) {


                // Successfully signed in


                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.google_app_id))

                        .requestEmail()
                        .build();


                //Obtener datos del usuari
                //o







                String uid = usuarioInstancia.getUid();
                // Name, email address, and profile photo Url
                String name =usuarioInstancia.getDisplayName();
                String email = usuarioInstancia.getEmail();

                // Check if user's email is verified
                boolean emailVerified = usuarioInstancia.isEmailVerified();
                // The user's ID, unique to the Firebase project. Do NOT use this value to
                // authenticate with your backend server, if you have one. Use
                // FirebaseUser.getIdToken() instead.


                usuario.setUserId(uid);
                usuario.setNombre(name);
                usuario.setEmail(email);


                Intent intent = new Intent(getBaseContext(), home.class);
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                intent.putExtra("uid", uid);


                startActivity(intent);


                databaseReference.child("usuario")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot == null) {
                                    // el nodo no tiene hijos
                                    databaseReference.child("usuario").child(usuario.getUserId()).setValue(usuario);

                                } else {
                                    //hacer algo con el valor
                                    databaseReference.child("usuario").child(usuario.getUserId()).setValue(usuario);
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                finish();




                // ...
            } else {

                createSignInIntent();
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }

    }
    // [END auth_fui_result]

    public  void signOut() {
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

    public void delete() {
        // [START auth_fui_delete]
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
        // [END auth_fui_delete]
    }

    public void themeAndLogo() {
        List<AuthUI.IdpConfig> providers = Collections.emptyList();

        // [START auth_fui_theme_logo]
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                      // Set theme
                        .build(),
                RC_SIGN_IN);
        // [END auth_fui_theme_logo]
    }

    public void privacyAndTerms() {
        List<AuthUI.IdpConfig> providers = Collections.emptyList();
        // [START auth_fui_pp_tos]
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTosAndPrivacyPolicyUrls(
                                "https://example.com/terms.html",
                                "https://example.com/privacy.html")
                        .build(),
                RC_SIGN_IN);
        // [END auth_fui_pp_tos]
    }


    public static void salir(){
        FirebaseAuth.getInstance().signOut();
        User.salirSession();


    }
}
