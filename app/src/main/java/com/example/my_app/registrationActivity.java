package com.example.my_app;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class registrationActivity extends AppCompatActivity {
    TextInputEditText emailEditText,passwordEditText;
    Button regbtn;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth=FirebaseAuth.getInstance();
        emailEditText=findViewById(R.id.email);
        passwordEditText=findViewById(R.id.password);
        regbtn=findViewById(R.id.reg_btn);
        progressBar=findViewById(R.id.progressBar);
        textView =findViewById(R.id.loginNow);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getApplicationContext(), loginActivity.class);
                startActivity(intent);
                finish();
            }
        });

      regbtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              progressBar.setVisibility(View.VISIBLE);
              String email,password;
              email = String.valueOf(emailEditText.getText());
              password=String.valueOf(passwordEditText.getText());

              if (TextUtils.isEmpty(email)) {
                  showToast("Enter email");
                  progressBar.setVisibility(View.GONE);
                  return;
              }

              // Check for valid email using a simple regular expression
              if (!isValidEmail(email)) {
                  showToast("Enter a valid email address");
                  progressBar.setVisibility(View.GONE);
                  return;
              }

              if (TextUtils.isEmpty(password)) {
                  showToast("Enter password");
                  progressBar.setVisibility(View.GONE);
                  return;
              }

              // Check for password strength (you can customize these requirements)
              if (!isValidPassword(password)) {
                  showToast("The Password should have a upper&lowercase,numbers and special Characters");
                  progressBar.setVisibility(View.GONE);
                  return;
              }
              mAuth.createUserWithEmailAndPassword(email, password)
                      .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                          @Override
                          public void onComplete(@NonNull Task<AuthResult> task) {
                              progressBar.setVisibility(View.GONE);

                              if (task.isSuccessful()) {
                                  Toast.makeText(registrationActivity.this, "Registraion successful.",
                                          Toast.LENGTH_SHORT).show();

                              } else {
                                  // If sign in fails, display a message to the user.
                                  Toast.makeText(registrationActivity.this, "Authentication failed.",
                                          Toast.LENGTH_SHORT).show();
                              }
                          }
                      });


          }
      });
    }
    private void showToast(String message) {
        Toast.makeText(registrationActivity.this, message, Toast.LENGTH_SHORT).show();
    }
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return Pattern.matches(emailPattern, email);
    }
    // Helper method to validate password (customize the requirements as needed)
    private boolean isValidPassword(String password) {
        String passwordPattern ="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
        return Pattern.matches(passwordPattern, password);
    }
}
