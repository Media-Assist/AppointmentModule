package com.example.appointmentmodule;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class DoctorView extends AppCompatActivity {
    TextView fullname, experience, phone, specialization, city, education;
    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    String userId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view);
        /*
        Toast.makeText(DoctorView.this, "DoctoView working correctly: ", Toast.LENGTH_SHORT).show();

        fullname = findViewById(R.id.patient_doctorProfile_name);
        specialization = findViewById(R.id.patient_doctorProfile_specialization);
        experience = findViewById(R.id.patient_doctorProfile_experiance);
        education = findViewById(R.id.patient_doctorProfile_education);
        phone = findViewById(R.id.patient_doctorProfile_contact);
        city = findViewById(R.id.patient_doctorProfile_city);


        fauth = FirebaseAuth.getInstance();
        //userId = fauth.getCurrentUser().getUid();
        userId = "E72zjzSvSvXXVO1yJpiaVcB01Aq1";

        DocumentReference documentReference = fstore.collection("Doctors").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                experience.setText(value.getString("Experience"));
                //String fname = value.get("FirstName").toString() + " " + value.get("LastName").toString();
                //fullname.setText(fname);
                specialization.setText(value.getString("Specialization"));
                education.setText(value.getString("Education"));
                phone.setText(value.getString("Mobile No"));
                city.setText(value.getString("City"));

            }
        });
        */

    }

}