package com.ravit.friends_on_road;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_login, container, false);
        final EditText email = view.findViewById(R.id.login_email);
        final EditText pass = view.findViewById(R.id.login_passWord);
        //final ProgressBar pBar = view.findViewById(R.id.signUp_progressBar);

        Button loginBtn = view.findViewById(R.id.login_btnLogin);

        Button signUpBtn = view.findViewById(R.id.login_btnSignUp);
        signUpBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_login_to_signUp));

        loginBtn.setOnClickListener(new View.OnClickListener() {
            FirebaseAuth mAuth= FirebaseAuth.getInstance();
            @Override
            public void onClick(View v) {
                String mEmail=email.getText().toString().trim();
                String mPass=pass.getText().toString().trim();
                if(TextUtils.isEmpty(mEmail)){
                    email.setError("Email is Required!");
                    return;
                }
                if(TextUtils.isEmpty(mPass)){
                    pass.setError("Password is Required!");
                    return;
                }
                if(mPass.length()<6){
                    pass.setError("Password must be >= 6 Characters");
                    return;
                }

                mAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("TAG", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    loginDirections.ActionLoginToNavHome action = loginDirections.actionLoginToNavHome(email.getText().toString());
                                    Navigation.findNavController(view).navigate(action);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("TAG", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(getContext(),"Error!",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }




        });

        return view;
    }
}