package com.example.appointmentmodule.RealtimeDB;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appointmentmodule.Patient.Patient;
import com.example.appointmentmodule.Patient.PatientAdapter;
import com.example.appointmentmodule.R;
import com.example.appointmentmodule.Temp;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class BookedAppointmentFrag extends Fragment {


    public BookedAppointmentFrag() { }

    private RecyclerView recyclerView;
    private PatientAdapter patientAdapter;
    private ArrayList<Patient> patientArrayList;
    private ArrayAdapter<String> arrayAdapter;
    Boolean flag = false;
    DatabaseReference db;

    String updated_patient_email, patient_email, doc_name, doc_specialization;
    TextView textView;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_booked_appointment, container, false);

        SharedPreferences sp = this.getActivity().getSharedPreferences("patientData", Context.MODE_PRIVATE);
        patient_email = sp.getString("patient_email", "");
        updated_patient_email = patient_email.replace('.', ',');

        recyclerView = view.findViewById(R.id.recycleview1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        patientArrayList = new ArrayList<>();
        patientAdapter = new PatientAdapter(getContext(), patientArrayList);

        textView = view.findViewById(R.id.textView3);

        recyclerView.setAdapter(patientAdapter);
        EventChangeListener();

        return view;
    }

    private void EventChangeListener()  {
        db = FirebaseDatabase.getInstance().getReference("AppointmentPatient").child(updated_patient_email);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    /**
                     * TODO: update the method to remove an item from the list for delete appointment
                     * This is not a correct method to update the recycle view.
                     * This is fetching the list all again when an item is removed from the recyele view.
                     * Update this if you find a solution.
                     * */
                    patientArrayList.clear();
                    flag = false;
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
                                        flag = true;
                                        Log.d("check1 ", "doctor is: " + entry.getValue() +
                                                " Name: " + doc_name + " Specialization: " + doc_specialization + "\n");
                                        textView.setVisibility(TextView.INVISIBLE);
                                        patientAdapter.notifyDataSetChanged();
                                    }else{
                                        Toast.makeText(getContext(), "Row not found", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "Data couldn't be fetched", Toast.LENGTH_SHORT).show();
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
        showText();
    }

    private void showText() {
        if ( !flag){
            Log.d("checking_text", "No children\n");
            textView.setVisibility(TextView.VISIBLE);
        }else{
            Log.d("checking_text", "Children, they are: " + patientArrayList + "\n");
            textView.setVisibility(TextView.INVISIBLE);
        }
    }


}