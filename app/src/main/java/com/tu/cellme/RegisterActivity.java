package com.tu.cellme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {
    private EditText mNameField;
    private EditText mEmailfield;
    private EditText mPasswordField;
    private Button mRegisterBtn;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();
        mProgress=new ProgressDialog(this);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");


        mNameField=(EditText)findViewById(R.id.nameField);
        mEmailfield=(EditText)findViewById(R.id.emailField);
        mPasswordField=(EditText)findViewById(R.id.passwordField);
        mRegisterBtn=(Button)findViewById(R.id.registerBtn);
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegister();
            }
        });
    }

    private void startRegister() {
        final String name=mNameField.getText().toString().trim();
        String email=mEmailfield.getText().toString().trim();
        String password=mPasswordField.getText().toString().trim();
        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            mProgress.setMessage("Signing Up..");
            mProgress.show();
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String userId=mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user_db=mDatabase.child(userId);
                        current_user_db.child("name").setValue(name);


                        mProgress.dismiss();


                        Intent main_Intent=new Intent(RegisterActivity.this,MainActivity.class);
                        main_Intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(main_Intent);
                    }
                }
            });
        }
    }
}
