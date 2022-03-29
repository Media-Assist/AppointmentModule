package com.example.appointmentmodule.Specialization;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appointmentmodule.Doctor.DoctorsList;
import com.example.appointmentmodule.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SpecializationFrag extends Fragment implements AdapterCallback{
    public SpecializationFrag() {

    }

    RecyclerView recyclerView;
    ArrayList<DoctorsList> doctorsListArrayList;
    SpecializationAdapter specializationAdapter;
    ProgressDialog progressDialog;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_specialization, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("fetching data... ");
        progressDialog.show();


        recyclerView = view.findViewById(R.id.specialization_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();
        doctorsListArrayList = new ArrayList<DoctorsList>();
        specializationAdapter = new SpecializationAdapter(getContext(), doctorsListArrayList);

        recyclerView.setAdapter(specializationAdapter);
        EventChangeListener();

        return view;
    }

    private void EventChangeListener()  {

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

                        for (DocumentChange dc: value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                //Log.d("counter", String.valueOf(++count));
                                if (! (doctorsListArrayList.contains( dc.getDocument().toObject(DoctorsList.class) )) )
                                    doctorsListArrayList.add(dc.getDocument().toObject(DoctorsList.class));
                            }
                            specializationAdapter.notifyDataSetChanged();
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                        }
                    }
                });

    }

    @Override
    public void onMethodCallback( ) {
        Toast.makeText(getContext(), "Inside fragment, methodcallback ", Toast.LENGTH_SHORT).show();

    }
    /*
    public static class DoctorListViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public DoctorListViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDoctorName(String doctorName) {
            TextView userName = mView.findViewById(R.id.doctor_name);
            userName.setText(doctorName);
        }

        public void setSpecialization(String specialization) {
            TextView userName = mView.findViewById(R.id.special_id_single_user);
            userName.setText(specialization);
        }
    }


    public class SpecializationViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public SpecializationViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setSpecialization(String special) {
            TextView userName = mView.findViewById(R.id.special_id_single_user);
            userName.setText(special);
        }

        public void setImage(int i) {

            CircleImageView imageView = mView.findViewById(R.id.profile_id_single_user);
            if (i == 1) {
                imageView.setImageDrawable(getResources().getDrawable(R.mipmap.stethoscope));
            } else {
                imageView.setImageDrawable(getResources().getDrawable(R.mipmap.injection));
            }

        }
    }


    private void EventChangeListener() {

        db.collection("Doctors")
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
                        }
                    }
                });

    }
    */
}