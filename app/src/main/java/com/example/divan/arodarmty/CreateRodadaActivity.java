package com.example.divan.arodarmty;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;
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
    TextView datetext;
    TextView HourText;
    LinearLayout date;
    LinearLayout hour;
    Calendar selectedDate;
    Calendar selectedHour;
    ImageButton addImage;
    Button buttonSpeed, buttonDistance, buttonDuration;
    int currentKilometers = 0, currentMeters = 0 , currentHour = 0, currentMinutes = 0, currentKmHour = 0, currentMeterHour = 0, hora, minutos;
    double speed, kilometers;
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_create_rodada);
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        title = findViewById(R.id.titleInput);
        HourText = findViewById(R.id.HourText);
        description = findViewById(R.id.textDescription);
        addImage = findViewById(R.id.imageinput);
        datetext = findViewById(R.id.dateText);
        date = findViewById(R.id.pickadate);
        hour = findViewById(R.id.pickahour);
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
                final AlertDialog alertDialog = builder.create();
                final NumberPicker minutesPicker = durationView.findViewById(R.id.minutesPicker);
                final NumberPicker hoursPicker = durationView.findViewById(R.id.hoursPicker);
                Button confDuration = durationView.findViewById(R.id.okduration);

                //Number picker for Hours
                hoursPicker.setMaxValue(23);
                hoursPicker.setMinValue(0);
                hoursPicker.setValue(currentHour);
                hoursPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
                //Number picker for Minutes
                minutesPicker.setMaxValue(59);
                minutesPicker.setMinValue(0);
                minutesPicker.setValue(currentMinutes);
                minutesPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
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

        buttonDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View distancenView = inflater.inflate(R.layout.activity_numberpicker_distance,null,false);
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateRodadaActivity.this);
                builder.setView(distancenView);
                final AlertDialog alertDialog = builder.create();
                final NumberPicker kilometersPicker = distancenView.findViewById(R.id.kilometers);
                kilometersPicker.setMaxValue(999);
                kilometersPicker.setMinValue(0);
                kilometersPicker.setValue(currentKilometers);
                kilometersPicker.setWrapSelectorWheel(false);

                //Number picker for Meters
                final NumberPicker metersPicker = distancenView.findViewById(R.id.meters);
                metersPicker.setMaxValue(9);
                metersPicker.setMinValue(0);
                metersPicker.setDisplayedValues(floatNums);
                metersPicker.setValue(currentMeters);
                metersPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
                metersPicker.clearFocus();
                kilometersPicker.clearFocus();
                Button confDistance = distancenView.findViewById(R.id.okdistance);

                final NumberPicker unitsDistance = distancenView.findViewById(R.id.unitsdistance);
                unitsDistance.setDisplayedValues(new String[] {"km"});

                confDistance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        kilometersPicker.clearFocus();
                        metersPicker.clearFocus();
                        currentKilometers = kilometersPicker.getValue();
                        currentMeters = metersPicker.getValue();
                        kilometers = kilometersPicker.getValue() + Double.parseDouble(floatNums[metersPicker.getValue()]);
                        buttonDistance.setText(String.valueOf(kilometers));
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }

        });

        buttonSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View speedView = inflater.inflate(R.layout.activity_numpicker_speed,null,false);
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateRodadaActivity.this);
                builder.setView(speedView);
                final AlertDialog alertDialog = builder.create();
                final NumberPicker kmHourPicker = speedView.findViewById(R.id.kmhour);
                final NumberPicker metersHourPicker = speedView.findViewById(R.id.metershour);
                final NumberPicker unitsPicker = speedView.findViewById(R.id.unitspicker);
                Button confSpeed = speedView.findViewById(R.id.okspeed);

                //Number picker for Km/Hours
                kmHourPicker.setMaxValue(99);
                kmHourPicker.setMinValue(0);
                kmHourPicker.setValue(currentKmHour);

                //unitsPicker
                unitsPicker.setDisplayedValues(new String[] {"km/hr"});
                unitsPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

                //Number picker for Meters/Hours
                metersHourPicker.setMinValue(0);
                metersHourPicker.setMaxValue(9);
                metersHourPicker.setDisplayedValues(floatNums);
                metersHourPicker.setValue(currentMeterHour);
                metersHourPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

                confSpeed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        kmHourPicker.clearFocus();
                        currentKmHour = kmHourPicker.getValue();
                        currentMeterHour = metersHourPicker.getValue();
                        speed = kmHourPicker.getValue() + Double.parseDouble(floatNums[metersHourPicker.getValue()]);
                        buttonSpeed.setText(String.valueOf(speed));
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

        hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateRodadaActivity.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        selectedHour = Calendar.getInstance();
                        hora = selectedHour.get(Calendar.HOUR_OF_DAY);
                        minutos = selectedHour.get(Calendar.MINUTE);
                        HourText.setText(hourOfDay + ":" + minutes);
                    }
                }, hora, minutos, false);
                    timePickerDialog.show();
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        datetext.setText(dayOfMonth + "/" + month + "/" + year);
        selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, dayOfMonth);
    }

}
