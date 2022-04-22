package com.example.appointmentmodule.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appointmentmodule.Doctor.DoctorFrag;
import com.example.appointmentmodule.DoctorViewAppointment.BookedAppointmentViewDoctor;
import com.example.appointmentmodule.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText emailBox, passwordBox;
    Button loginBtn, signupBtn;
    SharedPreferences sp;
    FirebaseAuth auth;

    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait... ");
        auth = FirebaseAuth.getInstance();

        emailBox = findViewById(R.id.emailBox);
        passwordBox = findViewById(R.id.passwordBox);

        loginBtn = findViewById(R.id.alreadyButton);
        signupBtn = findViewById(R.id.createButton);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                String email, password;
                email = emailBox.getText().toString();
                password = passwordBox.getText().toString();

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.dismiss();
                        if (task.isSuccessful()){
                            //String userId = auth.getCurrentUser().getUid();

                            Intent intent = new Intent(Login.this, DoctorFrag.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("email", email);
                            intent.putExtras(bundle);

                            sp = getSharedPreferences("patientData", Context.MODE_PRIVATE);
                            //startActivity(intent);
                            //Toast.makeText(Login.this, "email sent is: " + email, Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("patient_email", email);
                            editor.commit();

                            // changing to mainactiviy2.class to bookapp...
                            //startActivity(new Intent(Login.this, BookedAppointmentViewDoctor.class));
                            startActivity(new Intent(Login.this, MainActivity2.class));
                        }
                        else{
                            Toast.makeText(Login.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}