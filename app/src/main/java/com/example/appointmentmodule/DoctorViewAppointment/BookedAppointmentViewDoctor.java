package com.example.appointmentmodule.DoctorViewAppointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appointmentmodule.Patient.Patient;
import com.example.appointmentmodule.Patient.PatientAdapter;
import com.example.appointmentmodule.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookedAppointmentViewDoctor extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PatientDetailsAdapter patientDetailsAdapter;
    private ArrayList<PatientDetails> patientDetailsArrayList;
    private ArrayAdapter<String> arrayAdapter;

    Boolean flag = false;
    DatabaseReference db;
    String updated_doctor_email, doctor_email, pat_name, doc_specialization;
    TextView textView;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_appointment_view_doctor);

        /*
        * Here even though the name is patientData it's the data of user who has login. I am not changing it for now.
        * */
        SharedPreferences sp = this.getSharedPreferences("patientData", Context.MODE_PRIVATE);
        doctor_email = sp.getString("patient_email", "");
        updated_doctor_email = doctor_email.replace('.', ',');
        Log.d("one", "updated_doctor_email: " + updated_doctor_email + "\n" );

        recyclerView = findViewById(R.id.recycleViewDoctor);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        patientDetailsArrayList = new ArrayList<>();
        patientDetailsAdapter = new PatientDetailsAdapter(this, patientDetailsArrayList);
        recyclerView.setAdapter(patientDetailsAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        db = FirebaseDatabase.getInstance().getReference("AppointmentDoctor").child(updated_doctor_email);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dates : snapshot.getChildren  ()) {
                    patientDetailsArrayList.clear();
                    flag = false;

                    Log.d("one", "date: " + dates.getKey());
                    for(DataSnapshot dates_data : dates.getChildren()){
                        Log.d("one", " key: " + dates_data.getKey() + "  value: " + dates_data.getValue() );

                        DocumentReference documentReference = firestore.collection("Patient").document( dates_data.getValue().toString() );
                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists()){
                                    pat_name = documentSnapshot.getString("FirstName") + " " + documentSnapshot.getString("LastName");


                                    String a, b, c;
                                    a = dates.getKey().toString();
                                    b = slotToTime(dates_data.getKey() );
                                    c = dates_data.getValue().toString();

                                    PatientDetails obj = new PatientDetails(a, b, c, pat_name );
                                    System.out.println("name: " + pat_name  + "\n");

                                    patientDetailsArrayList.add(obj);
                                    flag = true;


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

        /**
         * Function showText() is useless currently. As I've not added a textbox.
         * */
        //showText();

    }

    private void showText() {
        if ( !flag){
            Log.d("checking_text", "No children\n");
            textView.setVisibility(TextView.VISIBLE);
        }else{
            Log.d("checking_text", "Children, they are: " + patientDetailsArrayList + "\n");
            textView.setVisibility(TextView.INVISIBLE);
        }
    }
}