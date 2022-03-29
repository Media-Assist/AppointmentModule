package com.example.appointmentmodule.Patient;

import android.content.Context;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.MyViewHolder > {
    ArrayList<Patient> patientList;
    Context context;
    SharedPreferences sp;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    public PatientAdapter(Context context, ArrayList<Patient> patientList){
        this.patientList = patientList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_show_appointment, parent, false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Patient patient = patientList.get(position);


        Log.d("check21", " doc_name " + patient.doctorName + " doc_specz: " + patient.doctorSpecialization + " \n");
        holder.appointment_time.setText(patient.time);
        holder.appointment_date.setText(patient.date);
        holder.doctor_name.setText(patient.doctorName);
        holder.doctor_specialization.setText(patient.doctorSpecialization);

        Log.d("check22", " doc_name " + patient.doctorName + " doc_specz: " + patient.doctorSpecialization + " \n");
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView doctor_name, appointment_time, doctor_specialization, appointment_date;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            doctor_name = itemView.findViewById(R.id.doctor_name);
            doctor_specialization = itemView.findViewById(R.id.doctor_specialization);
            appointment_time = itemView.findViewById(R.id.appointment_time);
            appointment_date = itemView.findViewById(R.id.appointment_date);
        }


        @Override
        public void onClick(View view)  {
            //Log.d("Check1", "clicked");



            int position = this.getAdapterPosition();
            Patient patientObj = patientList.get(position);
            String date, slot, doc_email,pat_email;
            date = patientObj.getDate();
            slot = changeTimeToSlot( patientObj.getTime() );

            SharedPreferences sp = view.getContext().getSharedPreferences("patientData", Context.MODE_PRIVATE);
            pat_email = sp.getString("patient_email", "");
            pat_email = pat_email.replace('.',',');

            doc_email = patientObj.doctorId.replace('.',',');
            Toast.makeText(context, "Position is: " + String.valueOf(position)+
                    "doctor_mail is: " + patientObj.doctorId, Toast.LENGTH_SHORT).show();

            //mDatabase.child("Appointment").child(doctorID).child(Appointment_date).child(slot).removeValue();
            //mDatabase.child("Booked_Appointments").child(currentUID).child(BookedAPKey).removeValue();
            databaseReference.child("AppointmentDoctor").child(doc_email).child(date).child(slot).removeValue();
            databaseReference.child("AppointmentPatient").child(pat_email).child(date).child(slot).removeValue();
            patientList.remove(position);
            

            Toast.makeText(context, "Removed Successfully", Toast.LENGTH_SHORT).show();

            // TODO: Make an alert box while the user is clikcing on on delete.

        }

        private String changeTimeToSlot(String time) {
            String slot = "";
            switch (time){

                case "08:00 AM":
                    slot = "1";
                    break;
                case "08:20 AM":
                    slot = "2";
                    break;
                case "08:40 AM":
                    slot = "3";
                    break;
                case "09:00 AM":
                    slot = "4";
                    break;
                case "09:20 AM":
                    slot = "5";
                    break;
                case "09:40 AM":
                    slot = "6";
                    break;
                case "10:00 AM":
                    slot = "7";
                    break;
                case "10:20 AM":
                    slot = "8";
                    break;
                case "10:40 AM":
                    slot = "9";
                    break;
                case "11:00 AM":
                    slot = "10";
                    break;
                case "11:20 AM":
                    slot = "11";
                    break;
                case "11:40 AM":
                    slot = "12";
                    break;
                case "02:00 PM":
                    slot = "13";
                    break;
                case "02:20 PM":
                    slot = "14";
                    break;
                case "02:40 PM":
                    slot = "15";
                    break;

                default:
                    break;
            }
            return  slot;
        }
    }


}
