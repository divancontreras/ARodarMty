package com.example.divan.arodarmty;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.divan.arodarmty.model.Rodada;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateRodadaActivity extends AppCompatActivity implements DatePickerDialog
        .OnDateSetListener {
    FirebaseFirestore db;
    FirebaseStorage storage;
    String[] floatNums = {".0",".1", ".2", ".3", ".4", ".5", ".6", ".7", ".8", ".9"};
    EditText title;
    EditText description;
    TextView date;
    Calendar selectedDate;
    ImageButton addImage;
    String duration;
    Button buttonSpeed, buttonDistance, buttonDuration, confDuration;
    int currentKilometers = 0, currentMeters = 0 , currentHour = 0, currentMinutes = 0;
    double distance, speed, kilometers;
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
        addImage = findViewById(R.id.imageinput);
        date = findViewById(R.id.dateText);
        buttonDistance = findViewById(R.id.button_distance);
        buttonSpeed = findViewById(R.id.button_speed);
        buttonDuration = findViewById(R.id.button_duration);

        buttonDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SimpleDateFormat dateFormatIn = new SimpleDateFormat("HH:mm");
                final SimpleDateFormat dateFormatOut = new SimpleDateFormat("HH:mm");

                LayoutInflater inflater = getLayoutInflater();
                View durationView = inflater.inflate(R.layout.activity_numpicker_duration,null,false);
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateRodadaActivity.this);
                builder.setView(durationView);
                builder.setTitle("Duración");
                final AlertDialog alertDialog = builder.create();
                final NumberPicker minutesPicker = durationView.findViewById(R.id.minutesPicker);
                final NumberPicker hoursPicker = durationView.findViewById(R.id.hoursPicker);
                Button confDuration = durationView.findViewById(R.id.okduration);

                //Number picker for Hours
                hoursPicker.setMaxValue(23);
                hoursPicker.setMinValue(0);
                hoursPicker.setValue(currentHour);

                //Number picker for Minutes
                minutesPicker.setMaxValue(59);
                minutesPicker.setMinValue(0);
                minutesPicker.setValue(currentMinutes);
                confDuration.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        minutesPicker.clearFocus();
                        hoursPicker.clearFocus();
                        currentHour = hoursPicker.getValue();
                        currentMinutes = minutesPicker.getValue();
                        Date date;
                        String dateIn = String.valueOf(hoursPicker.getValue()) + ":" + String.valueOf(minutesPicker.getValue());
                        try {
                            date = dateFormatIn.parse(dateIn);
                            buttonDuration.setText(dateFormatOut.format(date));
                        }catch(ParseException e){
                            throw new IllegalArgumentException("Invalid date Format " + dateIn);
                        }
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }

        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateRodadaActivity
                        .this, CreateRodadaActivity.this, 2017, 5, 20);
                datePickerDialog.show();
            }
        });

        buttonDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout LL = new LinearLayout(CreateRodadaActivity.this);
                LL.setOrientation(LinearLayout.HORIZONTAL);
                //Number picker for Kilometers
                final NumberPicker kilometersPicker = new NumberPicker(CreateRodadaActivity.this);
                kilometersPicker.setMaxValue(999);
                kilometersPicker.setMinValue(0);
                kilometersPicker.setValue(currentKilometers);

                //Number picker for Meters
                final NumberPicker metersPicker = new NumberPicker(CreateRodadaActivity.this);
                metersPicker.setMaxValue(9);
                metersPicker.setMinValue(0);
                metersPicker.setDisplayedValues(floatNums);
                metersPicker.setValue(currentMeters);
                metersPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
                metersPicker.clearFocus();
                kilometersPicker.clearFocus();

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(50, 50);
                params.gravity = Gravity.CENTER;
                LinearLayout.LayoutParams kilometersParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                kilometersParams.weight = 1;

                LinearLayout.LayoutParams metersParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                metersParams.weight = 1;

                LL.setLayoutParams(params);
                LL.addView(kilometersPicker,kilometersParams);
                LL.addView(metersPicker,metersParams);
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateRodadaActivity.this);
                builder.setView(LL);
                kilometersPicker.setWrapSelectorWheel(false);
                builder.setTitle("Distancia (Km)");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        kilometersPicker.clearFocus();
                        metersPicker.clearFocus();
                        currentKilometers = kilometersPicker.getValue();
                        currentMeters = metersPicker.getValue();
                        kilometers = kilometersPicker.getValue() + Double.parseDouble(floatNums[metersPicker.getValue()]);
                        buttonDistance.setText(String.valueOf(kilometers));
                    }
                });
                builder.show();
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
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
                        .toString(), kilometers,
                        selectedDate.getTime());
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
        selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, dayOfMonth);
    }
}
