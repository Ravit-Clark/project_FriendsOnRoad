package com.ravit.friends_on_road.ui.myCars;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.ravit.friends_on_road.Model.Car;
import com.ravit.friends_on_road.Model.Model;
import com.ravit.friends_on_road.R;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class AddCar extends Fragment {
    final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView img;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_car, container, false);
        EditText model = view.findViewById(R.id.addCar_model);
        EditText carNum = view.findViewById(R.id.addCar_year);
        EditText year = view.findViewById(R.id.addCar_licensePlateNum);
        EditText engine = view.findViewById(R.id.addCar_engine);
        EditText places = view.findViewById(R.id.addCar_places);
        Button addBtn = view.findViewById(R.id.addCar_addBtn);
        ImageButton addImg=view.findViewById(R.id.addCar_img);
        img=view.findViewById(R.id.addCar_img);

        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editImage();
            }
        });


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Car car= new Car();
                car.setModel(model.getText().toString());
                car.setLicensePlateNum(carNum.getText().toString());
                car.setYear(year.getText().toString());
                car.setEngine(engine.getText().toString());
                car.setPlaces(places.getText().toString());
                String ownerEmail=Model.instance.getUserEmail();
                car.setEmailOwner(ownerEmail);
                addBtn.setEnabled(false);

                Model.instance.addCar(car, new Model.AddCarListener(){
                    @Override
                    public void onComplete(boolean success) {
                        Navigation.findNavController(view).navigate(R.id.action_addCar_to_nav_myCars);

                    }
                });
            }
        });






        return view;
    }


    void editImage()
    {
        //Intent takePictureIntent = new Intent(
        //      MediaStore.ACTION_IMAGE_CAPTURE);
        //if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
        //  startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose your profile picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0: //return from camera
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        img.setImageBitmap(selectedImage);
                    }
                    break;
                case 1: //return from gallery
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                img.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }




}