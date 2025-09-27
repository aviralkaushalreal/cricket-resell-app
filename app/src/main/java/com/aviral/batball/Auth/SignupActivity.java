package com.aviral.batball.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.aviral.batball.Logic.MainActivity;
import com.aviral.batball.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    TextView reg,signup;

    EditText email,name,pass;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        reg=findViewById(R.id.btnRegisterlog);
        signup=findViewById(R.id.btnRegister);
        email=findViewById(R.id.signupemail);
        name=findViewById(R.id.signupemail);
        pass=findViewById(R.id.signuppassword);


        signup.setOnClickListener(v->{
            String email_ = email.getText().toString();
            String pass_ = pass.getText().toString();
            String name_ = name.getText().toString();


            signupUser(name_,email_,pass_);
        });




        reg.setOnClickListener(v->{
            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void signupUser(String name, String email, String pass) {
        auth.createUserWithEmailAndPassword(email,pass).addOnSuccessListener(success ->{
            Map<String,String> username= new HashMap<>();
            username.put("username",name);
            firestore.collection("users").document(auth.getCurrentUser().getUid()).set(username);
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }).addOnFailureListener(e->{
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });

    }
}