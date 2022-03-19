package com.example.appointmentmodule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class DoctorView extends AppCompatActivity {
    TextView fullname, experience, phone, specialization, city, education;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view);


        fullname = findViewById(R.id.patient_doctorProfile_name);
        specialization = findViewById(R.id.patient_doctorProfile_specialization);
        experience = findViewById(R.id.patient_doctorProfile_experiance);
        education = findViewById(R.id.patient_doctorProfile_education);
        phone = findViewById(R.id.patient_doctorProfile_contact);
        city = findViewById(R.id.patient_doctorProfile_city);

        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("email", "Default");

        DocumentReference documentReference = db.collection("Doctors").document(email);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    Toast.makeText(DoctorView.this, "email is: " + email, Toast.LENGTH_SHORT).show();
                    String temp = value.getString("FirstName") + " " + value.getString("LastName");
                    fullname.setText(temp);
                    education.setText(value.getString("Education"));
                    experience.setText(value.getString("Experience"));
                    specialization.setText(value.getString("Specialization"));
                    education.setText(value.getString("Education"));
                    phone.setText(value.getString("Mobile No"));
                    city.setText(value.getString("City"));

                /*
                else{
                    Toast.makeText(DoctorView.this, "Error hai bhai! Email is: " + email,
                            Toast.LENGTH_SHORT).show();
                }*/
            }
        });

        final Calendar calendar = Calendar.getInstance();
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int month=calendar.get(Calendar.MONTH);
        int year=calendar.get(Calendar.YEAR);

        btn = findViewById(R.id.book_appointment_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog picker = new DatePickerDialog(DoctorView.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                         month = month + 1;
                         // Strange error here. The sent value is yyyymmdd instead of ddmmyyyy. Don't know the erason.
                        // Also textbox is not visible when using getintent(). It's working with bundles.
                         String date = day + "-" + month + "-" + year;
                        Log.d("checkpoint1", "Date1 here is: " + date + "\n");
                        experience.setText(date);
                    }
                }, year, month, day);
                picker.show();


                picker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "-" + month + "-" + year;
                        Intent selected_date = new Intent(DoctorView.this, Temp.class);
                        Log.d("checkpoint1", "Date2 here is: " + date + "\n");
                        selected_date.putExtra("finaldate", date);
                        startActivity(selected_date);
                        finish();
                        Toast.makeText(DoctorView.this, "Final date: " + date, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DoctorView.this, Temp.class));
                    }
                });


            }
        });
    }

}