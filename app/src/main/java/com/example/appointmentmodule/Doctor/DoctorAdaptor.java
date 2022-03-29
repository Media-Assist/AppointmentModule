package com.example.appointmentmodule.Doctor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appointmentmodule.R;

import java.util.ArrayList;


public class DoctorAdaptor extends RecyclerView.Adapter<DoctorAdaptor.MyViewHolder> {
    private Context context;
    ArrayList<DoctorsList> doctorsListArrayList;
    SharedPreferences sp;
    public DoctorAdaptor(Context context, ArrayList<DoctorsList> doctorsListArrayList) {
        this.context = context;
        this.doctorsListArrayList = doctorsListArrayList;
    }

    @NonNull
    @Override
    public DoctorAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.single_doctor_list, parent, false); //changed this
        //View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAdaptor.MyViewHolder holder, int position) {
          DoctorsList doctorsList = doctorsListArrayList.get(position);

          //holder.FirstName.setText(doctorsList.FirstName + " " + doctorsList.LastName); changed this
          holder.FullName.setText(doctorsList.FirstName + " " + doctorsList.LastName);
          holder.Specialization.setText(doctorsList.Specialization);
    }

    @Override
    public int getItemCount() {
        Log.e("doctor_count: ", String.valueOf(doctorsListArrayList.size()));
        return doctorsListArrayList.size();
    }

    public void filterList(ArrayList<DoctorsList> filteredList){
        doctorsListArrayList = filteredList;
        notifyDataSetChanged();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView FullName, Specialization;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            FullName = itemView.findViewById(R.id.doctor_name);
            Specialization = itemView.findViewById(R.id.doctor_specialization);
        }


        @Override
        public void onClick(View view) {
            //Log.d("Check1", "clicked");

            int position = this.getAdapterPosition();
            DoctorsList doctorsList = doctorsListArrayList.get(position);
            String email = doctorsList.getEmail();
            Toast.makeText(context, "Position is: " + String.valueOf(position)+
                    "doctor_mail is: " + email, Toast.LENGTH_SHORT).show();

            sp = context.getSharedPreferences("patientData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("selected_doctor", email);
            editor.commit();

            Intent intent = new Intent(context, DoctorView.class);
            context.startActivity(intent);

        }
    }



}
