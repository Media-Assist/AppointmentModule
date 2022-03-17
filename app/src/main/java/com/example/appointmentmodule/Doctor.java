package com.example.appointmentmodule;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This is same as main activity
 */

public class Doctor extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<DoctorsList> doctorsListArrayList;
    DoctorListAdapter doctorListAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("fetching data... ");
        progressDialog.show();

        recyclerView = findViewById(R.id.recylerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        doctorsListArrayList = new ArrayList<DoctorsList>();
        doctorListAdapter = new DoctorListAdapter(Doctor.this, doctorsListArrayList);

        recyclerView.setAdapter(doctorListAdapter);

        EventChangeListener();
    }

    private void EventChangeListener() {

        db.collection("Doctors").orderBy("FirstName", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){

                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }

                            Log.e("Firststore error", error.getMessage());
                            return ;
                        }
                        int count = 0;
                        for (DocumentChange dc: value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                Log.d("counter", String.valueOf(++count));
                              doctorsListArrayList.add(dc.getDocument().toObject(DoctorsList.class));

                            }
                            doctorListAdapter.notifyDataSetChanged();
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                        }
                    }
                });

    }
}
