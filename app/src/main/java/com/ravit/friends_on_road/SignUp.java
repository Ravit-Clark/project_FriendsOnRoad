package com.ravit.friends_on_road;

import android.app.ProgressDialog;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ravit.friends_on_road.Model.Model;
import com.ravit.friends_on_road.Model.User;

public class SignUp extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_sign_up, container, false);

        final EditText name = view.findViewById(R.id.signUp_name);
        final EditText phone = view.findViewById(R.id.signUp_phone);
        final EditText email =view.findViewById(R.id.signUp_email);
        final EditText pass =view.findViewById(R.id.signUp_passWord);
        Button signUpBtn = view.findViewById(R.id.signUp_btnSignUp);
        final ProgressBar pBar = view.findViewById(R.id.signUp_progressBar);
        pBar.setVisibility(View.INVISIBLE);
        ImageView imageView;
        FirebaseAuth fAuth= FirebaseAuth.getInstance();


        if(fAuth.getCurrentUser()!=null){
            signUpBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_signUp_to_nav_home));

        }



        signUpBtn.setOnClickListener(new View.OnClickListener() {
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


                pBar.setVisibility(view.VISIBLE);

                User newUser= new User();
                newUser.setName(name.getText().toString());
                newUser.setPhone(phone.getText().toString());
                newUser.setEmail(email.getText().toString());
                newUser.setPassword(pass.getText().toString());
                newUser.setEventOpen(false);
                Toast.makeText(getContext(),"Loading..",Toast.LENGTH_SHORT).show();
                signUpBtn.setEnabled(false);
                FirebaseAuth fAuth= FirebaseAuth.getInstance();
                fAuth.createUserWithEmailAndPassword(newUser.getEmail(),newUser.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Model.instance.addUser(newUser, new Model.AddUserListener() {
                                @Override
                                public void onComplete(boolean success) {
                                    pBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getContext(),"User Created",Toast.LENGTH_SHORT).show();
                                    SignUpDirections.ActionSignUpToNavHome action = SignUpDirections.actionSignUpToNavHome(newUser.getEmail());
                                    Navigation.findNavController(view).navigate(action);
                                }
                            });

                        }else{
                            pBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getContext(),"Error!",Toast.LENGTH_SHORT).show();
                            signUpBtn.setEnabled(true);
                        }
                    }
                });
            }
        });







        return view;
    }
}