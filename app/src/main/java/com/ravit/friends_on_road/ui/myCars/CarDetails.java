package com.ravit.friends_on_road.ui.myCars;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.ravit.friends_on_road.Model.Car;
import com.ravit.friends_on_road.Model.Model;
import com.ravit.friends_on_road.R;
import com.squareup.picasso.Picasso;


public class CarDetails extends Fragment {
    ImageButton img;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_car_details, container, false);

        EditText model = view.findViewById(R.id.editCar_model);
        EditText carNum = view.findViewById(R.id.editCar_licensePlateNum);
        EditText year = view.findViewById(R.id.editCar_licensePlateNum);
        EditText engine = view.findViewById(R.id.editCar_engine);
        EditText places = view.findViewById(R.id.editCar_places);
        final ImageButton editCarBtn=view.findViewById(R.id.carDetails_editCarBtn);
        //editCarBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_carDetails_to_editCar));
        img=view.findViewById(R.id.event_image);


        String num = CarDetailsArgs.fromBundle(getArguments()).getLicensePlateNum();
        Model.instance.getCarByNum(num, new Model.GetCarByNumListener() {
            @Override
            public void onComplete(Car car) {
                model.setText(car.getModel());
                carNum.setText(car.getLicensePlateNum());
                year.setText(car.getYear());
                engine.setText(car.getEngine());
                places.setText(car.getPlaces());
                img.setTag(car.getImgUrl());
                if (car.getImgUrl() != null && car.getImgUrl() != ""){
                    if (car.getImgUrl() == img.getTag()) {
                        Picasso.get().load(car.getImgUrl()).into(img);
                    }
                }else{
                    img.setImageResource(R.drawable.car10);
                }
            }
        });


        editCarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarDetailsDirections.ActionCarDetailsToEditCar action = CarDetailsDirections.actionCarDetailsToEditCar(carNum.getText().toString());
                Navigation.findNavController(view).navigate(action);
            }
        });



        return view;
    }
}