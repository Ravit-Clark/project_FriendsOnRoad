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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.ravit.friends_on_road.Model.Model;
import com.ravit.friends_on_road.Model.User;
import com.ravit.friends_on_road.R;
import com.ravit.friends_on_road.SignUpDirections;

public class HomeFragment extends Fragment {
    public User user;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        final ImageButton addEvent = view.findViewById(R.id.home_addEventBtn);
        final ImageButton needHelp = view.findViewById(R.id.home_needHelp);



        String userEmail=Model.instance.getUserEmail();

        //String userEmail = HomeFragmentArgs.fromBundle(getArguments()).getEmail();
        Log.d("TAG","user email is "+ userEmail);

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragmentDirections.ActionNavHomeToAddEvent action=HomeFragmentDirections.actionNavHomeToAddEvent(userEmail);
                Navigation.findNavController(view).navigate(action);
            }
        });

        needHelp.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_home_to_needHelp));
        //garages.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_home_to_garagesListFragment));
//        Model.instance.getUser(userEmail,new Model.GetUserListener() {
//            @Override
//            public void onComplete(User _user) {
//                user=_user;
//                Log.d("TAG","user name is "+ user.getName());
//                name.setText(user.getName());
//            }
//        });




        return view;
    }
}


