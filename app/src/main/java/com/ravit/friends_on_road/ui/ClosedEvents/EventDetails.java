package com.ravit.friends_on_road.ui.ClosedEvents;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ravit.friends_on_road.Model.Event;
import com.ravit.friends_on_road.Model.Model;
import com.ravit.friends_on_road.R;
import com.squareup.picasso.Picasso;

public class EventDetails extends Fragment {
    TextView type;
    TextView description;
    TextView location;
    TextView status;
    TextView car;
    ImageView img;
    Button delete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_event_details, container, false);

        type = view.findViewById(R.id.eventDetails_type);
        description = view.findViewById(R.id.eventDetails_descripion);
        location = view.findViewById(R.id.eventDetails_location);
       TextView car = view.findViewById(R.id.eventDetails_car);
        status = view.findViewById(R.id.evenDetails_status);
        delete=view.findViewById(R.id.eventDatails_deleteBtn);
        img=view.findViewById(R.id.eventDetails_img);


        String eventNum =EventDetailsArgs.fromBundle(getArguments()).getNumOfSpecificEvent();
        Model.instance.getEventByEventNum(eventNum, new Model.GetEventByEventNumListener() {
            @Override
            public void onComplete(Event event) {
                type.setText(event.getType());
                description.setText(event.getDescription());
                location.setText(event.getLocation());
                car.setText(event.getCar());
                status.setText(event.getStatus());
//                img.setTag(event.getImgUrl());
//                if (event.getImgUrl() != null && event.getImgUrl() != ""){
//                    if (event.getImgUrl() == img.getTag()) {
//                        Picasso.get().load(event.getImgUrl()).into(img);
//                    }
//                }else{
//                    img.setImageResource(R.drawable.help);
//                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Model.instance.deleteEvent("10014", new Model.DeleteEventListener() {
                   @Override
                   public void onComplete(boolean success) {
                      Toast.makeText(getContext(),"The event has been deleted!",Toast.LENGTH_SHORT).show();

                   }
               });
            }
        });







        return view;
    }
}