package com.ravit.friends_on_road.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ravit.friends_on_road.Model.Model;
import com.ravit.friends_on_road.Model.User;
import com.ravit.friends_on_road.R;
import com.squareup.picasso.Picasso;

public class Profile extends Fragment {
    TextView name;
    TextView phone;
    TextView email;
    Button editProfileBtn;
    ImageView img;
    User user1;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        name = view.findViewById(R.id.profile_name);
        phone = view.findViewById(R.id.profile_phone);
        email = view.findViewById(R.id.profile_email);
        editProfileBtn = view.findViewById(R.id.profile_editBtn);
        img=view.findViewById(R.id.profile_img);

        String userEmail=Model.instance.getUserEmail();
        Log.d("TAG","email: "+userEmail);
        Model.instance.getUser(userEmail, new Model.GetUserListener() {
            @Override
            public void onComplete(User user) {
                Log.d("TAG",""+user.getName());
                name.setText(user.getName());
                phone.setText(user.getPhone());
                email.setText(user.getEmail());
                img.setTag(user.getImageUrl());
                if (user.getImageUrl() != null && user.getImageUrl() != ""){
                    if (user.getImageUrl() == img.getTag()) {
                        Picasso.get().load(user.getImageUrl()).into(img);
                    }
                }else{
                    img.setImageResource(R.drawable.user);
                }

            }
        });


        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileDirections.ActionNavProfileToEditProfile action = ProfileDirections.actionNavProfileToEditProfile(userEmail);
                Navigation.findNavController(view).navigate(action);
            }
        });




        return view;
    }



}