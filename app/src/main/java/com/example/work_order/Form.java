package com.example.work_order;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class Form extends AppCompatActivity {

    TextInputEditText txtTitle;
    TextInputEditText txtDescription;
    Button btnAgregarTarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        txtTitle = (TextInputEditText) findViewById(R.id.text_title);
        txtDescription = (TextInputEditText) findViewById(R.id.text_description);
        btnAgregarTarea = findViewById(R.id.button);

        final Bundle bundle = new Bundle();

        btnAgregarTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aceptarTarea();
            }
        });

    }

    public void aceptarTarea(){


        String title = txtTitle.getText().toString();
        String description = txtDescription.getText().toString();

        if(title.trim().equals("") ){
            txtTitle.requestFocus();
            Toast.makeText(this , "Ingrese un titulo para su ot" , Toast.LENGTH_LONG).show();
            return;
        }
        if(description.trim().equals("") ){
            txtDescription.requestFocus();
            Toast.makeText(this , "Ingrese una descripcion su ot" , Toast.LENGTH_LONG).show();
            return;
        }




        Intent homeIntent = new Intent( );
        homeIntent.putExtra("title",title);
        homeIntent.putExtra("description" , description);

        setResult(RESULT_OK, homeIntent);

        finish();
    }
}
