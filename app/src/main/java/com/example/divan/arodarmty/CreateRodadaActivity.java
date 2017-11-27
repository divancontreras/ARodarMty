package com.example.divan.arodarmty;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.divan.arodarmty.model.Rodada;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateRodadaActivity extends AppCompatActivity implements DatePickerDialog
        .OnDateSetListener {
    FirebaseFirestore db;
    FirebaseStorage storage;
    EditText title;
    EditText description;
    TextView date;
    EditText kilometers;
    Calendar selecteddate;
    Button addimage;
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_create_rodada);
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        title = findViewById(R.id.titleInput);
        description = findViewById(R.id.textDescription);
        kilometers = findViewById(R.id.kilometers);
        addimage = findViewById(R.id.imageinput);
        date = findViewById(R.id.dateText);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateRodadaActivity
                        .this, CreateRodadaActivity.this, 2017, 5, 20);
                datePickerDialog.show();
            }
        });

        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.actionsave:
                Rodada rodada = new Rodada(title.getText().toString(), description.getText()
                        .toString(), Double.parseDouble(kilometers.getText().toString()),
                        selecteddate.getTime());
                db.collection("rodadas")
                        .add(rodada)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CreateRodadaActivity.this, "Error, while creating " +
                                        "new rodada", Toast.LENGTH_SHORT).show();
                            }
                        });

        }
        ;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        date.setText(dayOfMonth + "/" + month + "/" + year);
        selecteddate = Calendar.getInstance();
        selecteddate.set(year, month, dayOfMonth);
    }
}
