package com.example.appointmentmodule.Specialization;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appointmentmodule.Doctor.DoctorsList;
import com.example.appointmentmodule.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SpecializationAdapter extends RecyclerView.Adapter<SpecializationAdapter.MyViewHolder>{
    Context context;
    ArrayList<DoctorsList> doctorsListArrayList;
    SharedPreferences sp;
    View v;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private AdapterCallback adapterCallback;


    public SpecializationAdapter(Context context, ArrayList<DoctorsList> doctorsListArrayList){
        this.context = context;
        this.doctorsListArrayList = doctorsListArrayList;
        try{
            adapterCallback = ((AdapterCallback) context);
        }catch (ClassCastException e) {
            Log.d("Error", e.toString());
        }
    }

    @NonNull
    @Override
    public SpecializationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(context).inflate(R.layout.single_specialization_list, parent, false); //changed this

        return new MyViewHolder(v);

    }


    @Override
    public void onBindViewHolder(@NonNull SpecializationAdapter.MyViewHolder holder, int position) {
        DoctorsList doctorsList = doctorsListArrayList.get(position);
        if (position % 2 == 1){
            setImage(0);
        }else{
            setImage(1);
        }
        holder.Specialization.setText(doctorsList.getSpecialization());

        // commented so that I can show the working app.
        /*
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
         */


    }

    @Override
    public int getItemCount() {
        return doctorsListArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        TextView Specialization, FullName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //itemView.setOnClickListener(this);

            Specialization = itemView.findViewById(R.id.special_id_single_user);
            FullName = itemView.findViewById(R.id.special_id_single_user);
        }

        @Override
        public void onClick(View view) {
            int position = this.getAdapterPosition();
            DoctorsList doctorsList = doctorsListArrayList.get(position);
            String specialization = doctorsList.getSpecialization();
            try {
                adapterCallback.onMethodCallback();
            } catch (ClassCastException e) {
                Log.d("Error", e.toString());
            }
            //Toast.makeText(context, "specialization: " + specialization, Toast.LENGTH_SHORT).show();

        }

        /*
        private void alertDialog(String specialization) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View myView = LayoutInflater.from(context).inflate(R.layout.doctor_list_dialog, null);


            RecyclerView alertRecyclerView = myView.findViewById(R.id.doctor_dialog);
            alertRecyclerView.setHasFixedSize(true);
            alertRecyclerView.setLayoutManager(new LinearLayoutManager( context ));

            builder.setView(myView);

            //db.collection("Doctors").whereEqualTo("Specialization", specialization).addSnapshotListener(new )
        }

         */
    }

    public void setImage(int i) {

        CircleImageView imageView = v.findViewById(R.id.profile_id_single_user);
        if (i == 1) {
            imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.stethoscope));
        } else {
            imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.injection));
        }

    }

}