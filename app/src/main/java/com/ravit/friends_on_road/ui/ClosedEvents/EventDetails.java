package com.ravit.friends_on_road.ui.ClosedEvents;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.ravit.friends_on_road.Model.Car;
import com.ravit.friends_on_road.Model.Event;
import com.ravit.friends_on_road.Model.Model;
import com.ravit.friends_on_road.R;
import com.ravit.friends_on_road.ui.myCars.CarDetailsArgs;
import com.ravit.friends_on_road.ui.myCars.CarDetailsDirections;
import com.ravit.friends_on_road.ui.myCars.EditCarDirections;
import com.squareup.picasso.Picasso;

public class EventDetails extends Fragment {
ImageView img;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_event_details, container, false);

        EditText type = view.findViewById(R.id.eventDetails_type);
        EditText description = view.findViewById(R.id.eventDetails_description);
        EditText location = view.findViewById(R.id.eventDetails_location);
        EditText car = view.findViewById(R.id.eventDetails_car);
        EditText status = view.findViewById(R.id.eventDetails_status);
        final ImageButton editEventBtn=view.findViewById(R.id.eventDetails_editBtn);
        img=view.findViewById(R.id.eventDetails_img);


        String eventNum =EventDetailsArgs.fromBundle(getArguments()).getEventNum();
        Model.instance.getEventByEventNum(eventNum, new Model.GetEventByEventNumListener() {
            @Override
            public void onComplete(Event event) {
                type.setText(event.getType());
                description.setText(event.getDescription());
                location.setText(event.getLocaion());
                car.setText(event.getCar());
                status.setText(event.getStatus());
                img.setTag(event.getImgUrl());
                if (event.getImgUrl() != null && event.getImgUrl() != ""){
                    if (event.getImgUrl() == img.getTag()) {
                        Picasso.get().load(event.getImgUrl()).into(img);
                    }
                }else{
                    img.setImageResource(R.drawable.help);
                }
            }
        });


        editEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CarDetailsDirections.ActionCarDetailsToEditCar action = CarDetailsDirections.actionCarDetailsToEditCar(carNum.getText().toString());
//                Navigation.findNavController(view).navigate(action);
//                EventDetailsDirections.actionEventDetailsToEditMyOpenEvent()
            }
        });






        return view;
    }
}