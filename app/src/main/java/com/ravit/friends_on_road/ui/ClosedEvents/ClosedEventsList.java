package com.ravit.friends_on_road.ui.ClosedEvents;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ravit.friends_on_road.Model.Event;
import com.ravit.friends_on_road.Model.Model;
import com.ravit.friends_on_road.R;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;


public class ClosedEventsList extends Fragment {

    List<Event> data = new LinkedList<>();
    ListView list;
    eventAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_closed_events_list, container, false);
        list = view.findViewById(R.id.closedEvents_eventsList);


        String userEmail= Model.instance.getUserEmail();
        Model.instance.getEventsByEmailOwner(userEmail,new Model.GetEventsByEmailOwnerListener() {
            @Override
            public void onComplete(List<Event> _data) {
                data = _data;
                adapter = new eventAdapter();
                list.setAdapter(adapter);
            }
        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                ClosedEventsListDirections.ActionNavClosedEventsListToEventDetails action = ClosedEventsListDirections.actionNavClosedEventsListToEventDetails(data.get(i).getNumOfSpecificEvent());
                Navigation.findNavController(view).navigate(action);

            }
        });


        return view;
    }




    class eventAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int i/*row index*/, View view/*reusable view*/, ViewGroup parent) {
            if (view == null) {
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.event_row, null);
                Log.d("TAG", "create new row view");
            } else {
                Log.d("TAG", "reusing old row view");
            }

            Event event = data.get(i);
            TextView type = view.findViewById(R.id.carRow_model);
            TextView location = view.findViewById(R.id.carRow_num);

            type.setText(data.get(i).getType());
            location.setText(data.get(i).getLocaion());

            ImageView imagev = view.findViewById(R.id.carRow_image);
            imagev.setTag(event.getImgUrl());
            if (event.getImgUrl() != null && event.getImgUrl() != ""){
                if (event.getImgUrl() == imagev.getTag()) {
                    Picasso.get().load(event.getImgUrl()).into(imagev);
                }
            }else{
                imagev.setImageResource(R.drawable.car10);
            }
            return view;



        }
    }

}