package com.example.appointmentmodule.DoctorViewAppointment;

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

import com.example.appointmentmodule.Patient.Patient;
import com.example.appointmentmodule.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PatientDetailsAdapter extends RecyclerView.Adapter<PatientDetailsAdapter.MyViewHolder> {

    Context context;
    ArrayList<PatientDetails> patientDetailsArrayList;
    SharedPreferences sp;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public PatientDetailsAdapter(Context context, ArrayList<PatientDetails> patientDetailsArrayList) {
        this.context = context;
        this.patientDetailsArrayList = patientDetailsArrayList;
        sp = context.getSharedPreferences("doctorData", Context.MODE_PRIVATE);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_doctor_view_appointment, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
         PatientDetails patientDetails = patientDetailsArrayList.get(position);

        Log.d("one", " pat_name " + patientDetails.patientName + " pat_id: " + patientDetails.patientId + " \n");
        holder.appointment_time.setText(patientDetails.time);
        holder.appointment_date.setText(patientDetails.date);
        holder.patient_name.setText(patientDetails.patientName);

        Log.d("one", " pat_name " + patientDetails.patientName + " pat_id: " + patientDetails.patientId + " \n");


    }

    @Override
    public int getItemCount() {
        return patientDetailsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView patient_name, appointment_date, appointment_time;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            patient_name = itemView.findViewById(R.id.patient_name);
            appointment_date = itemView.findViewById(R.id.appointment_date);
            appointment_time = itemView.findViewById(R.id.appointment_time);

        }

        @Override
        public void onClick(View view) {
            int position = this.getAdapterPosition();
            PatientDetails patientObj = patientDetailsArrayList.get(position);
            String date,time, slot, doc_email,pat_email;
            date = patientObj.getDate();
            time = patientObj.getTime();
            slot = changeTimeToSlot( patientObj.getTime() );

            SharedPreferences sp = view.getContext().getSharedPreferences("patientData", Context.MODE_PRIVATE);
            doc_email = sp.getString("patient_email", "");
            pat_email = patientObj.getPatientId();

            //Toast.makeText(context, "Position is: " + String.valueOf(position)+
                    //"doctor_mail is: " + doc_email + "pat_email: " + pat_email + "\ndate: " + date + " time: " + time , Toast.LENGTH_SHORT).show();
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
                System.out.println("Inside the appointment interval\n");


                //startActivity(intent);
                //Toast.makeText(Login.this, "email sent is: " + email, Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("meet_code", meet_code);
                editor.commit();
            }
            else if( today_date.after(updated) ){
                System.out.println("Meeting has ended");
            }
            else{
                System.out.println("Meeting yet to start");
            }


            /*
            Toast.makeText(context, "Appnt Date: " + sDate +
                    "\nToday: " + today_str + "\n Date: " + date
                    + "\nTime: " + time, Toast.LENGTH_SHORT).show();


             */



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
