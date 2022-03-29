package com.example.appointmentmodule.RealtimeDB;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appointmentmodule.Home.MainActivity2;
import com.example.appointmentmodule.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class BookAppointment extends AppCompatActivity implements  View.OnClickListener{
     CardView c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15;
     int flagChecked = 0;
     Button mConfirm;
    TextView editText;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference rootref;
    Boolean flag1 = false;
    ArrayList<Integer> alreadyBooked = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        SharedPreferences sp = getApplicationContext().getSharedPreferences("patientData", Context.MODE_PRIVATE);
        String updated_selected_doctor, updated_patient_email;
        String patient_email = sp.getString("patient_email", "");
        String selected_date = sp.getString("selected_date", "");
        String selected_doctor = sp.getString("selected_doctor", "");


        /*
        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(selected_date)){

                    System.out.println(snapshot.getKey());
                }
                else{
                    System.out.println("Child date doesn't exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

         */
        updated_selected_doctor = selected_doctor.replace('.',','); // updated_selected_doctor is updated selected_doctor

        updated_patient_email = patient_email.replace('.',',');

        Toast.makeText(BookAppointment.this, "selected date: "+ selected_date +
                "\nPatient Email: " + updated_patient_email +
                "\nSelected Doctor email: " + updated_selected_doctor, Toast.LENGTH_SHORT).show();


        setCards();


        editText = findViewById(R.id.title2);
        editText.setText(selected_date);

        firebaseDatabase = FirebaseDatabase.getInstance();
        rootref =  firebaseDatabase.getReference("AppointmentDoctor").child(updated_selected_doctor).child(selected_date);

        System.out.println("Hello World!" + "\n");

        ValueEventListener valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    // If the selected date has some slots booked in the realtime database


                    for(DataSnapshot snap: snapshot.getChildren()){

                        alreadyBooked.add(Integer.parseInt(snap.getKey()));
                        setColorRed( Integer.parseInt(snap.getKey()) );
                        System.out.println("Time: " + snap.getKey() + " Patient: " + snap.getValue());
                    }
                    System.out.println("ArrayList now is: " + alreadyBooked);
                    System.out.println("Size is: " + alreadyBooked.size());
                }else{
                    flag1 = true;
                    // If the date doesn't exist
                    System.out.println("Child doesn't exist");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.d("TAG", error.getMessage()); //Don't ignore errors!

            }
        };
        rootref.addListenerForSingleValueEvent(valueEventListener);

        mConfirm = findViewById(R.id.confirm_appointment);
        String finalVal = updated_selected_doctor;
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alreadyBooked.contains(flagChecked) ){
                    Toast.makeText(BookAppointment.this, "Slot is already booked, select another one", Toast.LENGTH_SHORT).show();
                }
                else if (flagChecked != 0){
                    //updatePatient(flagChecked);

                    if(flag1){ // There is no key for selected_date
                        HashMap<String, String > userMap = new HashMap<>();
                        userMap.put(String.valueOf(flagChecked), updated_patient_email);

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AppointmentDoctor").child(finalVal);
                        databaseReference.child(selected_date).setValue(userMap);
                    }else{

                        HashMap<String, Object > userMap = new HashMap<>();
                        userMap.put(String.valueOf(flagChecked), updated_patient_email);

                        rootref.updateChildren(userMap);
                    }

                    updatePatient(flagChecked);

                    // Both patient and doctor database entries complete, now going to see patient booked appointments

                    Intent intent = new Intent(BookAppointment.this , MainActivity2.class);
                    startActivity(intent);
                    //getSupportFragmentManager().beginTransaction().add(R.id.mainContainer, new BookedAppointmentFrag()).commit();
                    //FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    //fragmentTransaction.replace(R.id.mainContainer, new BookedAppointmentFrag()).commit();
                    //startActivity(new Intent(BookAppointment.this, BookedAppointmentFrag.class));
                    Toast.makeText(BookAppointment.this, "Slot :" + flagChecked, Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(BookAppointment.this, "Please Select Time Slot", Toast.LENGTH_SHORT).show();
                }
            }

            private void updatePatient(int flagChecked) {
                Log.d("Check2", "Inside update patient\n");
                DatabaseReference patientReference = FirebaseDatabase.getInstance().getReference("AppointmentPatient").child(updated_patient_email).child(selected_date);
                patientReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {



                        HashMap<String, Object > userMap = new HashMap<>();
                        userMap.put(String.valueOf(flagChecked), selected_doctor); // setting the slot and the doctor email id

                        if(snapshot.exists()){  // Means selected_date is already a key in the patient module
                            Log.d("Check2","date: " + selected_date + "exists. Key not generated in patient\n");
                            System.out.println("Already exists Patient date");
                            patientReference.updateChildren(userMap);
                        }
                        else{ // key doesn't exist and a new database ref has to be craeted
                            Log.d("Check2","date: " + selected_date + " doesn't exist.\n");
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AppointmentPatient").child(updated_patient_email);
                            System.out.println(" Patient date doesn't exist");
                            databaseReference.child(selected_date).setValue(userMap);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.c1: checkIsBooked(1); break;
            case R.id.c2: checkIsBooked(2); break;
            case R.id.c3: checkIsBooked(3); break;
            case R.id.c4: checkIsBooked(4); break;
            case R.id.c5: checkIsBooked(5); break;
            case R.id.c6: checkIsBooked(6); break;
            case R.id.c7: checkIsBooked(7); break;
            case R.id.c8: checkIsBooked(8); break;
            case R.id.c9: checkIsBooked(9); break;
            case R.id.c10: checkIsBooked(10); break;
            case R.id.c11: checkIsBooked(11); break;
            case R.id.c12: checkIsBooked(12); break;
            case R.id.c13: checkIsBooked(13); break;
            case R.id.c14: checkIsBooked(14); break;
            case R.id.c15: checkIsBooked(15); break;
        }
    }

    private void checkIsBooked(int i) {
        if( flagChecked != 0 ) {
            setDefaultColor(flagChecked);
        }
        flagChecked = i;
        setColorGreen(i);
    }

    private void setCards() {
        c1 = (CardView) findViewById(R.id.c1);
        c2 = (CardView) findViewById(R.id.c2);
        c3 = (CardView) findViewById(R.id.c3);
        c4 = (CardView) findViewById(R.id.c4);
        c5 = (CardView) findViewById(R.id.c5);
        c6 = (CardView) findViewById(R.id.c6);
        c7 = (CardView) findViewById(R.id.c7);
        c8 = (CardView) findViewById(R.id.c8);
        c9 = (CardView) findViewById(R.id.c9);
        c10 = (CardView) findViewById(R.id.c10);
        c11 = (CardView) findViewById(R.id.c11);
        c12 = (CardView) findViewById(R.id.c12);
        c13 = (CardView) findViewById(R.id.c13);
        c14 = (CardView) findViewById(R.id.c14);
        c15 = (CardView) findViewById(R.id.c15);

        c1.setOnClickListener(this);
        c2.setOnClickListener(this);
        c3.setOnClickListener(this);
        c4.setOnClickListener(this);
        c5.setOnClickListener(this);
        c6.setOnClickListener(this);
        c7.setOnClickListener(this);
        c8.setOnClickListener(this);
        c9.setOnClickListener(this);
        c10.setOnClickListener(this);
        c11.setOnClickListener(this);
        c12.setOnClickListener(this);
        c13.setOnClickListener(this);
        c14.setOnClickListener(this);
        c15.setOnClickListener(this);
    }

    private void setDefaultColor(int i) {

        switch (i) {
            case 1: c1.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c1.setEnabled(true);
                break;
            case 2:
                c2.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c2.setEnabled(true);
                break;
            case 3:
                c3.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c3.setEnabled(true);
                break;
            case 4:
                c4.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c4.setEnabled(true);
                break;
            case 5:
                c5.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c5.setEnabled(true);
                break;
            case 6:
                c6.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c6.setEnabled(true);
                break;
            case 7:
                c7.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c7.setEnabled(true);
                break;
            case 8:
                c8.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c8.setEnabled(true);
                break;
            case 9:
                c9.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c9.setEnabled(true);
                break;
            case 10:
                c10.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c10.setEnabled(true);
                break;
            case 11:
                c11.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c11.setEnabled(true);
                break;
            case 12:
                c12.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c12.setEnabled(true);
                break;
            case 13:
                c13.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c13.setEnabled(true);
                break;
            case 14:
                c14.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c14.setEnabled(true);
                break;
            case 15:
                c15.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c15.setEnabled(true);
                break;
            default:
                break;
        }
    }

    private void setColorRed(int i) {

        switch (i) {
            case 1: c1.setCardBackgroundColor(Color.RED);
                c1.setEnabled(false);
                break;
            case 2:
                c2.setCardBackgroundColor(Color.RED);
                c2.setEnabled(false);
                break;
            case 3:
                c3.setCardBackgroundColor(Color.RED);
                c3.setEnabled(false);
                break;
            case 4:
                c4.setCardBackgroundColor(Color.RED);
                c4.setEnabled(false);
                break;
            case 5:
                c5.setCardBackgroundColor(Color.RED);
                c5.setEnabled(false);
                break;
            case 6:
                c6.setCardBackgroundColor(Color.RED);
                c6.setEnabled(false);
                break;
            case 7:
                c7.setCardBackgroundColor(Color.RED);
                c7.setEnabled(false);
                break;
            case 8:
                c8.setCardBackgroundColor(Color.RED);
                c8.setEnabled(false);
                break;
            case 9:
                c9.setCardBackgroundColor(Color.RED);
                c9.setEnabled(false);
                break;
            case 10:
                c10.setCardBackgroundColor(Color.RED);
                c10.setEnabled(false);
                break;
            case 11:
                c11.setCardBackgroundColor(Color.RED);
                c11.setEnabled(false);
                break;
            case 12:
                c12.setCardBackgroundColor(Color.RED);
                c12.setEnabled(false);
                break;
            case 13:
                c13.setCardBackgroundColor(Color.RED);
                c13.setEnabled(false);
                break;
            case 14:
                c14.setCardBackgroundColor(Color.RED);
                c14.setEnabled(false);
                break;
            case 15:
                c15.setCardBackgroundColor(Color.RED);
                c15.setEnabled(false);
                break;

            default:
                break;
        }
    }

    private void setColorGreen(int i) {

        switch (i) {
            case 1: c1.setCardBackgroundColor(Color.GREEN);
                break;
            case 2:
                c2.setCardBackgroundColor(Color.GREEN);
                break;
            case 3:
                c3.setCardBackgroundColor(Color.GREEN);
                break;
            case 4:
                c4.setCardBackgroundColor(Color.GREEN);
                break;
            case 5:
                c5.setCardBackgroundColor(Color.GREEN);
                break;
            case 6:
                c6.setCardBackgroundColor(Color.GREEN);
                break;
            case 7:
                c7.setCardBackgroundColor(Color.GREEN);
                break;
            case 8:
                c8.setCardBackgroundColor(Color.GREEN);
                break;
            case 9:
                c9.setCardBackgroundColor(Color.GREEN);
                break;
            case 10:
                c10.setCardBackgroundColor(Color.GREEN);
                break;
            case 11:
                c11.setCardBackgroundColor(Color.GREEN);
                break;
            case 12:
                c12.setCardBackgroundColor(Color.GREEN);
                break;
            case 13:
                c13.setCardBackgroundColor(Color.GREEN);
                break;
            case 14:
                c14.setCardBackgroundColor(Color.GREEN);
                break;
            case 15:
                c15.setCardBackgroundColor(Color.GREEN);
                break;

            default:
                break;
        }
    }
}