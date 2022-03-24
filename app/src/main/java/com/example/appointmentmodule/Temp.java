package com.example.appointmentmodule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appointmentmodule.Patient.Patient;
import com.example.appointmentmodule.Patient.PatientAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class Temp extends AppCompatActivity {

    RecyclerView recyclerView;
    PatientAdapter patientAdapter;
    ArrayList<Patient> patientArrayList;
    DatabaseReference db;
    String updated_patient_email, patient_email, doc_name, doc_specialization;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        patient_email = "patient2@gmail.com";
        updated_patient_email = patient_email.replace('.',',');

        recyclerView = findViewById(R.id.recycleview1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this) );

        patientArrayList = new ArrayList<>();
        patientAdapter = new PatientAdapter(Temp.this, patientArrayList);

        recyclerView.setAdapter(patientAdapter);
        EventChangeListener();


    }

    private void EventChangeListener() {
        db = FirebaseDatabase.getInstance().getReference("AppointmentPatient").child(updated_patient_email);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    if(dataSnapshot.hasChildren()){
                        System.out.println("Date is: " + dataSnapshot.getKey() + "\n");
                        //System.out.println("Type of value is: " + dataSnapshot.getValue().getClass() + "\n");
                        HashMap<String, String> data = new HashMap<>();
                        data = (HashMap<String, String>) dataSnapshot.getValue();

                        for (Map.Entry<String, String> entry: data.entrySet()){
                            /*
                            HashMap<String, String> userMap = new HashMap<>();
                            userMap.put("date", dataSnapshot.getKey());
                            userMap.put("time", slotToTime(entry.getKey()));
                            userMap.put("doctorId", entry.getValue());

                             */

                            DocumentReference documentReference = firestore.collection("Doctors").document( entry.getValue() );
                            documentReference.addSnapshotListener(Temp.this, new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                    doc_name= value.getString("FirstName") + " " + value.getString("LastName");
                                    doc_specialization = value.getString("Specialization");

                                    Log.d("check1 ", "doctor is: " + entry.getValue() +
                                            " Name: " + doc_name + " Specialization: " + doc_specialization + "\n");

                                }
                            });


                            String a, b, c;
                            a = dataSnapshot.getKey();
                            b = slotToTime(entry.getKey());
                            c = entry.getValue();

                            Patient obj = new Patient(a, b, c);
                            patientArrayList.add(obj);

                            System.out.println("Slot: " + entry.getKey() + " Doctor Email: " + entry.getValue() + " \n");
                        }
                        patientAdapter.notifyDataSetChanged();
                        //System.out.println("Value is: " + dataSnapshot.getValue() + "\n");

                    }

                }
            }

            private String slotToTime(String key)  {
                int i = Integer.parseInt(key);
                String time = "";
                switch (i) {
                    case 1:
                        time = "08:00 AM"; break;
                    case 2:
                        time = "08:20 AM"; break;
                    case 3:
                        time = "08:40 AM"; break;
                    case 4:
                        time = "09:00 AM"; break;
                    case 5:
                        time = "09:20 AM"; break;
                    case 6:
                        time = "09:40 AM"; break;
                    case 7:
                        time = "10:00 AM"; break;
                    case 8:
                        time = "10:20 AM"; break;
                    case 9:
                        time = "10:40 AM"; break;
                    case 10:
                        time = "11:00 AM"; break;
                    case 11:
                        time = "11:20 AM"; break;
                    case 12:
                        time = "11:40 AM"; break;
                    case 13:
                        time = "02:00 PM"; break;
                    case 14:
                        time = "02:20 PM"; break;
                    case 15:
                        time = "02:40 PM"; break;
                    default: break;
                }
                return time;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}