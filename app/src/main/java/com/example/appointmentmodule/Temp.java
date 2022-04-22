package com.example.appointmentmodule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.appointmentmodule.DoctorViewAppointment.BookedAppointmentViewDoctor;
import com.example.appointmentmodule.DoctorViewAppointment.PatientDetails;
import com.example.appointmentmodule.Patient.Patient;
import com.example.appointmentmodule.Patient.PatientAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        updated_patient_email = patient_email.replace('.', ',');

        recyclerView = findViewById(R.id.recycleview1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        patientArrayList = new ArrayList<>();
        patientAdapter = new PatientAdapter(Temp.this, patientArrayList);

        recyclerView.setAdapter(patientAdapter);

    }

    /*
    public void temp(){
        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


            flag = false;
            if (dataSnapshot.hasChildren()) {
                HashMap<String, String> data = new HashMap<>();
                data = (HashMap<String, String>) dataSnapshot.getValue();
                Log.d("one", "data is: " + data.toString() + "\n");
                for (Map.Entry<String, String> entry: data.entrySet()){
                    Log.d("one", "entry is: " + entry.toString() + "\n");
                    DocumentReference documentReference = firestore.collection("Patient").document( entry.getValue() );
                    Log.d("one", "entry value: " + entry.getValue().toString() + "\n");

                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()){
                                patient_name = documentSnapshot.getString("FirstName") + " " + documentSnapshot.getString("LastName");

                                String a, b, c;
                                a = dataSnapshot.getKey();
                                b = slotToTime(entry.getKey());
                                c = entry.getValue();
                                PatientDetails obj = new PatientDetails(a, b, c, patient_name);

                                Log.d("check3",
                                        obj.getPatientId() + " " + obj.getPatientName() + "\n");
                                patientDetailsArrayList.add(obj);
                                flag = true;
                                Log.d("check1 ", "doctor is: " + entry.getValue() +
                                        " Name: " + doc_name + " Specialization: " + doc_specialization + "\n");

                                patientDetailsAdapter.notifyDataSetChanged();
                            }else{
                                Toast.makeText(BookedAppointmentViewDoctor.this, "Row not found", Toast.LENGTH_SHORT).show();
                            }
                        }

                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(BookedAppointmentViewDoctor.this, "Data couldn't be fetched", Toast.LENGTH_SHORT).show();
                                }
                            });
                    Log.d("check1 after", "patient is: " + entry.getValue() +
                            " Name: " + patient_name  + "\n");

                }

            }
        }
    }

     */
}

