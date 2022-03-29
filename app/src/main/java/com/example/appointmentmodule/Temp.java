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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appointmentmodule.Doctor.DoctorsList;
import com.example.appointmentmodule.Patient.Patient;
import com.example.appointmentmodule.Patient.PatientAdapter;
import com.example.appointmentmodule.Specialization.AdapterCallback;
import com.example.appointmentmodule.Specialization.SpecializationAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.lang.reflect.Array;
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
        updated_patient_email = patient_email.replace('.', ',');

        recyclerView = findViewById(R.id.recycleview1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (dataSnapshot.hasChildren()) {

                        HashMap<String, String> data = new HashMap<>();
                        data = (HashMap<String, String>) dataSnapshot.getValue();

                        for (Map.Entry<String, String> entry: data.entrySet()){
                            DocumentReference documentReference = firestore.collection("Doctors").document( entry.getValue() );


                            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if(documentSnapshot.exists()){
                                        doc_name= documentSnapshot.getString("FirstName") + " " + documentSnapshot.getString("LastName");
                                        doc_specialization = documentSnapshot.getString("Specialization");

                                        String a, b, c;
                                        a = dataSnapshot.getKey();
                                        b = slotToTime(entry.getKey());
                                        c = entry.getValue();

                                        Patient obj = new Patient(a, b, c, doc_name, doc_specialization);
                                        System.out.println("name: " + doc_name + " specialization: " + doc_specialization + "\n");
                                        Log.d("check3",
                                                obj.getDoctorId() + " " + obj.getDoctorName() + " " + obj.getDoctorSpecialization() + "\n");
                                        patientArrayList.add(obj);

                                        Log.d("check1 ", "doctor is: " + entry.getValue() +
                                                " Name: " + doc_name + " Specialization: " + doc_specialization + "\n");

                                        patientAdapter.notifyDataSetChanged();
                                    }else{
                                        Toast.makeText(Temp.this, "Row not found", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Temp.this, "Data couldn't be fetched", Toast.LENGTH_SHORT).show();
                                        }
                                    });


                            Log.d("check1 after", "doctor is: " + entry.getValue() +
                                    " Name: " + doc_name + " Specialization: " + doc_specialization + "\n");

                        }

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
    /*
    public class SpecializationFrag extends Fragment implements AdapterCallback{

        // This is minimum reproducable code.
        // other code which I found unnecessary are skipped

        @Override
        public void onMethodCallback( ) {
            Toast.makeText(getContext(), "Inside fragment, methodcallback ", Toast.LENGTH_SHORT).show();

        }
    }

    public class SpecializationAdapter extends RecyclerView.Adapter<SpecializationAdapter.MyViewHolder> {
        ArrayList<DoctorsList> doctorsListArrayList;
        private AdapterCallback adapterCallback;

        // some more code that I've skipped

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public SpecializationAdapter(Context context, ArrayList<DoctorsList> doctorsListArrayList){ // constructor for initialization
                this.context = context;
                this.doctorsListArrayList = doctorsListArrayList;
                try{
                    adapterCallback = ((AdapterCallback) context);
                }catch (ClassCastException e) {
                    Log.d("Error", e.toString());
                }
            }

            @Override
            public void onBindViewHolder(@NonNull com.example.appointmentmodule.Specialization.SpecializationAdapter.MyViewHolder holder, int position) {

                // setting other variables using holder

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try{
                            adapterCallback.onMethodCallback();

                        }catch (ClassCastException e){
                            Log.d("Error", e.toString());
                        }
                    }
                });

            }
        }

        }

     */

    }
