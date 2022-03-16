package com.example.appointmentmodule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

import android.content.Intent;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

        /*
        Calendar calendar = Calendar.getInstance();
        calendar.clear();

        final long today = MaterialDatePicker.todayInUtcMilliseconds();

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select a Date");
        builder.setSelection(today);
        final   MaterialDatePicker materialDatePicker = builder.build();

        btn = findViewById(R.id.book_appointment_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        Intent selected_date = new Intent(DoctorView.this, Temp.class);
        String temp = materialDatePicker.getHeaderText();
        selected_date.putExtra("date", temp);
        startActivity(selected_date);
        finish();

        startActivity(new Intent(DoctorView.this, Temp.class));
        */
    }

}