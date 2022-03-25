package com.example.appointmentmodule.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.appointmentmodule.DoctorProfile.DoctorAdaptor;
import com.example.appointmentmodule.DoctorProfile.DoctorsList;
import com.example.appointmentmodule.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DoctorFrag extends Fragment {

    public DoctorFrag() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    ArrayList<DoctorsList> doctorsListArrayList;
    DoctorAdaptor doctorAdaptor;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_doctor, container, false);

        EditText editText = view.findViewById(R.id.doctor_search);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("fetching data... ");
        progressDialog.show();

        recyclerView = view.findViewById(R.id.recylerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();
        doctorsListArrayList = new ArrayList<DoctorsList>();
        doctorAdaptor = new DoctorAdaptor(getContext(), doctorsListArrayList);

        recyclerView.setAdapter(doctorAdaptor);

        EventChangeListener();


        return view;
    }

    private  void  filter(String text){
        ArrayList<DoctorsList> filteredList = new ArrayList<>();
        for(DoctorsList item: doctorsListArrayList){
            if(item.getFirstName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        doctorAdaptor.filterList(filteredList);
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
                                //Log.d("counter", String.valueOf(++count));

                                doctorsListArrayList.add(dc.getDocument().toObject(DoctorsList.class));

                            }
                            doctorAdaptor.notifyDataSetChanged();
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                        }
                    }
                });

    }
}