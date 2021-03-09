package com.ravit.friends_on_road;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ravit.friends_on_road.Model.Car;
import com.ravit.friends_on_road.Model.Event;
import com.ravit.friends_on_road.Model.Model;
import com.ravit.friends_on_road.ui.myCars.MyCars;
import com.ravit.friends_on_road.ui.myCars.MyCarsDirections;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;


public class NeedHelp extends Fragment {
    List<Event> data = new LinkedList<>();
    ListView list;
    NeedHelp.EventAdapter adapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_need_help, container, false);
        list = view.findViewById(R.id.needHelp_listView);

        Model.instance.getAllEventsOpen(new Model.GetAllEventsOpenListener() {
            @Override
            public void onComplete(List<Event> _data) {
                data = _data;
                adapter = new NeedHelp.EventAdapter();
                list.setAdapter(adapter);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                NeedHelpDirections.ActionNeedHelpToEventDetails action = NeedHelpDirections.actionNeedHelpToEventDetails(data.get(i).getNumOfSpecificEvent());
                Navigation.findNavController(view).navigate(action);
            }
        });




        return view;
    }



    class EventAdapter extends BaseAdapter {

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

            Log.d("TAG", "type: "+data.get(i).getType());
            Log.d("TAG", "location: "+data.get(i).getLocation());
            Log.d("TAG", "descr: "+data.get(i).getDescription());
            Log.d("TAG", "num: "+data.get(i).getNumOfSpecificEvent());
            Log.d("TAG", "email: "+data.get(i).getEmailOwner());

            Event event = data.get(i);
            TextView type = view.findViewById(R.id.eventRow_type);
            TextView location = view.findViewById(R.id.eventRow_location);
            TextView status = view.findViewById(R.id.eventRow_status);

            type.setText(data.get(i).getType());
            location.setText(data.get(i).getLocation());
            status.setText(data.get(i).getStatus());

            ImageView imagev = view.findViewById(R.id.eventRow_img);
            imagev.setTag(event.getImgUrl());
            if (event.getImgUrl() != null && event.getImgUrl() != ""){
                if (event.getImgUrl() == imagev.getTag()) {
                    Picasso.get().load(event.getImgUrl()).into(imagev);
                }
            }else{
                imagev.setImageResource(R.drawable.help);
            }
            return view;



        }
    }
}