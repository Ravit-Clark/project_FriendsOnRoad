package com.ravit.friends_on_road.ui.home;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.ravit.friends_on_road.MainActivity;
import com.ravit.friends_on_road.Model.Event;
import com.ravit.friends_on_road.Model.EventsNumRun;
import com.ravit.friends_on_road.Model.Model;
import com.ravit.friends_on_road.Model.User;
import com.ravit.friends_on_road.NeedHelp;
import com.ravit.friends_on_road.R;
import com.ravit.friends_on_road.SignUpDirections;

import java.util.LinkedList;
import java.util.List;

public class HomeFragment extends Fragment {

    User user;
    String userEmail;

    Button editOpenEvent;
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ((MainActivity)getActivity()).putNameUser();
        final ImageButton addEvent = view.findViewById(R.id.home_addEventBtn);
        final ImageButton needHelp = view.findViewById(R.id.home_needHelp);
        TextView _numRun=view.findViewById(R.id.home_numRun);
        editOpenEvent=view.findViewById(R.id.home_editOpenEventBtn);

        userEmail=Model.instance.getUserEmail();
        //String userEmail = HomeFragmentArgs.fromBundle(getArguments()).getEmail();
        Log.d("TAG","user email is "+ userEmail);


        Model.instance.getUser(userEmail, new Model.GetUserListener() {
            @Override
            public void onComplete(User _user) {
                user=_user;
                Log.d("TAG","on complete - name: "+user.getName());
                if(_user.isEventOpen()){
                    editOpenEvent.setVisibility(View.VISIBLE);


                }
            }
        });
//
//
//        Log.d("TAG","email: "+user.getEmail());
//        Log.d("TAG","pass: "+user.getPassword());
//        Log.d("TAG","phone: "+user.getPhone());
//        Log.d("TAG","img: "+user.getImageUrl());
//        Log.d("TAG","open: "+user.isEventOpen());
//        Log.d("TAG","myEvent: "+user.getMyOpenEvent());


        Model.instance.getNumRun(new Model.GetNumRunListener() {
            @Override
            public void onComplete(EventsNumRun numRun) {
                Log.d("TAG","num add: "+numRun.getNum());
                _numRun.setText(numRun.getNum());

            }

        });

        editOpenEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HomeFragmentDirections.ActionNavHomeToEditMyOpenEvent action=HomeFragmentDirections.actionNavHomeToEditMyOpenEvent(user.getMyOpenEvent());
                Navigation.findNavController(view).navigate(action);
            }
        });






        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(user.isEventOpen()){
//                    Toast.makeText(getContext(),"You already have an open event!",Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    HomeFragmentDirections.ActionNavHomeToAddEvent action = HomeFragmentDirections.actionNavHomeToAddEvent(userEmail);
//                    Navigation.findNavController(view).navigate(action);
//                }

                HomeFragmentDirections.ActionNavHomeToAddEvent action = HomeFragmentDirections.actionNavHomeToAddEvent(userEmail);
                Navigation.findNavController(view).navigate(action);
            }
        });

        needHelp.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_home_to_needHelp));






        return view;
    }
}


