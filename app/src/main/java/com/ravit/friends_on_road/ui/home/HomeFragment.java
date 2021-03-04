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
import com.ravit.friends_on_road.R;
import com.ravit.friends_on_road.SignUpDirections;

import java.util.List;

public class HomeFragment extends Fragment {
    public User user;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ((MainActivity)getActivity()).putNameUser();
        final ImageButton addEvent = view.findViewById(R.id.home_addEventBtn);
        final ImageButton needHelp = view.findViewById(R.id.home_needHelp);
        TextView _numRun=view.findViewById(R.id.home_numRun);


        Model.instance.getNumRun(new Model.GetNumRunListener() {
            @Override
            public void onComplete(EventsNumRun numRun) {
                Log.d("TAG","num add: "+numRun.getNum());
                _numRun.setText(numRun.getNum());
            }

        });


        String userEmail=Model.instance.getUserEmail();

        //String userEmail = HomeFragmentArgs.fromBundle(getArguments()).getEmail();
        Log.d("TAG","user email is "+ userEmail);

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.instance.getAllEventsOpen(new Model.GetAllEventsOpenListener() {
                    @Override
                    public void onComplete(List<Event> data) {
                        int flag=0;
                        for(int i=0;i<data.size();i++){
                            if(data.get(i).getEmailOwner()==userEmail){
                                Toast.makeText(getContext(),"You already have an open fault!",Toast.LENGTH_SHORT).show();
                                flag=1;
                            }
                        }
                        if(flag==0){
                            HomeFragmentDirections.ActionNavHomeToAddEvent action=HomeFragmentDirections.actionNavHomeToAddEvent(userEmail);
                            Navigation.findNavController(view).navigate(action);
                        }
                    }
                });
            }
        });

        needHelp.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_home_to_needHelp));




        return view;
    }
}


