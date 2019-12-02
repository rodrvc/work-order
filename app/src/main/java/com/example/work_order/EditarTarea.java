
package com.example.work_order;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

public class EditarTarea extends AppCompatActivity {

    TextInputEditText txtTitleEdit;
    TextInputEditText txtDescriptionEdit;
    Button btnAgregarTareaEdit;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_tarea);

        txtTitleEdit = (TextInputEditText) findViewById(R.id.textEdit_title);
        txtDescriptionEdit = (TextInputEditText) findViewById(R.id.textEdit_description);
        btnAgregarTareaEdit = findViewById(R.id.buttonEditAcept);

      
        String tituloEditar = getIntent().getStringExtra("titleEdit");
        String descripcionEditar = getIntent().getStringExtra("descriptionEdit");

        txtTitleEdit.setText(tituloEditar);
        txtDescriptionEdit.setText(descripcionEditar);


        btnAgregarTareaEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aceptarTarea();
            }
        });


    }

    public void aceptarTarea(){

        Bundle bundle = new Bundle();

        String title = txtTitleEdit.getText().toString();
        String description = txtDescriptionEdit.getText().toString();
        String id = getIntent().getStringExtra("idEdit");

        Intent homeIntent = new Intent( );
        homeIntent.putExtra("title",title);
        homeIntent.putExtra("description" , description);
        homeIntent.putExtra("id" , id);

        setResult(RESULT_OK, homeIntent);

        finish();
    }
}