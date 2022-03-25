package com.example.appointmentmodule.RealtimeDB;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appointmentmodule.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Reference: https://www.geeksforgeeks.org/how-to-save-data-to-the-firebase-realtime-database-in-android/
 * */

public class Login2 extends AppCompatActivity {

    EditText check_email, doctor_email;
    Button doc_btn, submit;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference1, databaseReference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        //patient_email = findViewById(R.id.patient_email);
        doctor_email = findViewById(R.id.doctor_email);
        check_email = findViewById(R.id.check_email);

        firebaseDatabase = FirebaseDatabase.getInstance();

        // Now updating for the patientts, doctor already done.

        databaseReference1 =  firebaseDatabase.getReference("AppointmentPatient");
        //databaseReference2 =  firebaseDatabase.getReference("AppointmentPatient"); // No use for now.

        doc_btn = findViewById(R.id.doc_btn);
        submit = findViewById(R.id.submit);
        //ptn_btn = findViewById(R.id.ptn_btn);

        doc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = doctor_email.getText().toString();
                Log.d("Check1", email);
                if (TextUtils.isEmpty(email) ) {
                    // if the text fields are empty then show the below message.
                    Toast.makeText(Login2.this, "Please add some data.", Toast.LENGTH_SHORT).show();
                } else {

                    HashMap<String, String > userMap = new HashMap<>();
                    userMap.put("Email", email);
                    String new_email = email.replace('.',',');
                    databaseReference1.child(new_email).setValue(userMap);
                    doctor_email.getText().clear();
                    Toast.makeText(Login2.this, "doctor data added", Toast.LENGTH_SHORT).show();

                }
                doctor_email.getText().clear();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String val = check_email.getText().toString();
                val = val.replace('.','_');
                Log.d("TAG", "Updated email is: " + val + "\n");
                String selected_date = "22-02-2022";
                //DatabaseReference checkEmail = firebaseDatabase.getReference("AppointmentDoctor").child(val);
                DatabaseReference patientReference = FirebaseDatabase.getInstance().getReference("AppointmentPatient").child(val).child(selected_date);
                String finalVal = val;
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                         if(snapshot.exists()){ // if date key  exists.

                             Log.d("TAG", "id exists" + "\n");
                             HashMap<String, Object > userMap = new HashMap<>();
                             //userMap.put("1", "patient1@gmail.com");
                             //userMap.put("2", "patient3@gmail.com");
                             userMap.put("3", "doctor1@gmail.com");
                             userMap.put("4", "doctor2@gmail.com");
                             patientReference.updateChildren(userMap);

                         }else{ // Create a key date
                             DatabaseReference dfr = FirebaseDatabase.getInstance().getReference("AppointmentPatient").child(finalVal);
                             HashMap<String, String > userMap = new HashMap<>();
                             userMap.put("1", "patient1@gmail.com");
                             userMap.put("2", "patient3@gmail.com");
                             dfr.child(selected_date).setValue(userMap);
                             Log.d("TAG", "Not exists" + "\n");
                         }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Log.d("TAG", error.getMessage()); //Don't ignore errors!

                    }
                };
                patientReference.addListenerForSingleValueEvent(valueEventListener);
            }
        });

        DatabaseReference emailFetch = firebaseDatabase.getReference("AppointmentDoctor");
        emailFetch.orderByChild("Email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                Log.d("TAG", "Data is: " + data);
                //Log.d("TAG", "Email is: " + data.get("PatientId"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        }
}