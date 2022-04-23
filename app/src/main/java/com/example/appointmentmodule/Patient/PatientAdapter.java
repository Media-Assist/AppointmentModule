package com.example.appointmentmodule.Patient;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appointmentmodule.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


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

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView doctor_name, appointment_time, doctor_specialization, appointment_date;
        LinearLayout cancel_appointment, join_meeting;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //itemView.setOnClickListener(this);
            doctor_name = itemView.findViewById(R.id.patient_name);
            doctor_specialization = itemView.findViewById(R.id.doctor_specialization);
            appointment_time = itemView.findViewById(R.id.appointment_time);
            appointment_date = itemView.findViewById(R.id.appointment_date);
            cancel_appointment = itemView.findViewById(R.id.cancel_appointment);
            join_meeting = itemView.findViewById(R.id.join_meeting);

            cancel_appointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
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

                    databaseReference.child("AppointmentDoctor").child(doc_email).child(date).child(slot).removeValue();
                    databaseReference.child("AppointmentPatient").child(pat_email).child(date).child(slot).removeValue();
                    patientList.remove(position);
                    notifyDataSetChanged();

                    Toast.makeText(context, "Removed Successfully", Toast.LENGTH_SHORT).show();

                    // TODO: Make an alert box while the user is clicking on delete.

                }

            });

            join_meeting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Patient patientObj = patientList.get(position);
                    String date, time, doc_email,pat_email;
                    date = patientObj.getDate();
                    time = patientObj.getTime();

                    SharedPreferences sp = view.getContext().getSharedPreferences("patientData", Context.MODE_PRIVATE);
                    pat_email = sp.getString("patient_email", "");
                    doc_email = patientObj.doctorId;

                    String sDate = "";
                    Date appointment_date = null;
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                    try {
                        appointment_date = sdf.parse(date+" "+time);
                        sDate= sdf.format(appointment_date);

                    } catch (ParseException e) {
                        Toast.makeText(context, "Error parsing date", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    Date today_date = Calendar.getInstance().getTime();
                    String today_str = sdf.format(today_date);

                    Calendar c = Calendar.getInstance();
                    c.setTime(appointment_date);
                    c.add(Calendar.MINUTE, 20);

                    Date updated = c.getTime();


                    String updated_date = sdf.format(updated);
                    System.out.println("Updated date is: " + updated_date);

                    if ( ( today_date.equals(appointment_date) || today_date.after(appointment_date) ) &&
                            ( today_date.equals(updated) || today_date.before(updated) )
                    )   {

                        String meet_code = doc_email + pat_email + date + time;
                        System.out.println("Meet Code: " + meet_code);

                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("patient_meet_code", meet_code);
                        editor.commit();
                    }
                    else if( today_date.after(updated) ){
                        System.out.println("Meeting has ended");
                    }
                    else{
                        System.out.println("Meeting yet to start");
                    }

                }
            });

        }


        /*
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
            notifyDataSetChanged();
            //notifyItemRemoved(getAdapterPosition());

            //notifyItemRemoved(this.getLayoutPosition());




            Toast.makeText(context, "Removed Successfully", Toast.LENGTH_SHORT).show();

            // TODO: Make an alert box while the user is clicking on delete.

        }
        */
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
