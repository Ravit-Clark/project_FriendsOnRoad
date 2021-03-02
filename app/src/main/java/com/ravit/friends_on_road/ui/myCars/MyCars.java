package com.ravit.friends_on_road.ui.myCars;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.ravit.friends_on_road.Model.Model;
import com.ravit.friends_on_road.R;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;


public class MyCars extends Fragment {
    List<Car> data = new LinkedList<>();
    ListView list;
    carAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_cars, container, false);
        list = view.findViewById(R.id.myCars_carList);
        ImageButton add=view.findViewById(R.id.evenClosedList_addBtn);
        add.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_myCars_to_addCar));

        String ownerEmail=Model.instance.getUserEmail();
        Model.instance.getCarsByEmailOwner(ownerEmail,new Model.GetCarsByEmailOwnerListener() {
            @Override
            public void onComplete(List<Car> _data) {
                data = _data;
                adapter = new carAdapter();
                list.setAdapter(adapter);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                MyCarsDirections.ActionNavMyCarsToCarDetails action = MyCarsDirections.actionNavMyCarsToCarDetails(data.get(i).getLicensePlateNum());
                Navigation.findNavController(view).navigate(action);
            }
        });


        return view;
    }


    class carAdapter extends BaseAdapter {

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
                view = inflater.inflate(R.layout.car_row, null);
                Log.d("TAG", "create new row view");
            } else {
                Log.d("TAG", "reusing old row view");
            }

            Car car = data.get(i);
            TextView model = view.findViewById(R.id.eventRow_type);
            TextView num = view.findViewById(R.id.eventRow_location);

            model.setText(data.get(i).getModel());
            num.setText(data.get(i).getYear());

            ImageView imagev = view.findViewById(R.id.eventRow_image);
            imagev.setTag(car.getImgUrl());
            if (car.getImgUrl() != null && car.getImgUrl() != ""){
                if (car.getImgUrl() == imagev.getTag()) {
                    Picasso.get().load(car.getImgUrl()).into(imagev);
                }
            }else{
                imagev.setImageResource(R.drawable.car10);
            }
            return view;



        }
    }
}