package com.ravit.friends_on_road.ui.myCars;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.ravit.friends_on_road.Model.Car;
import com.ravit.friends_on_road.Model.Model;
import com.ravit.friends_on_road.Model.ModelFirebase;
import com.ravit.friends_on_road.R;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class EditCar extends Fragment {
    ImageView img;
    String emailOwn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_edit_car, container, false);

        EditText model = view.findViewById(R.id.editCar_model);
        EditText carNum = view.findViewById(R.id.editCar_licensePlateNum);
        EditText year = view.findViewById(R.id.editCar_year);
        EditText engine = view.findViewById(R.id.editCar_engine);
        EditText places = view.findViewById(R.id.editCar_places);
        Button saveBtn = view.findViewById(R.id.editCar_saveBtn);

        ImageButton editImg = view.findViewById(R.id.editCar_editImgBtn);
        img=view.findViewById(R.id.editCar_img);
        editImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editImage();
            }
        });

        String num = EditCarArgs.fromBundle(getArguments()).getLicensePlateNum();

        Model.instance.getCarByNum(num, new Model.GetCarByNumListener(){
            @Override
            public void onComplete(Car _car) {
                Car car=_car;
                Log.d("TAG","model: " + car.getModel());
                model.setText(car.getModel());
                Log.d("TAG","num: " + car.getLicensePlateNum());
                carNum.setText(car.getLicensePlateNum());
                Log.d("TAG","year: " + car.getYear());
                year.setText(car.getYear());
                Log.d("TAG","engine: " + car.getEngine());
                engine.setText(car.getEngine());
                Log.d("TAG","places: " + car.getPlaces());
                places.setText(car.getPlaces());
                emailOwn=car.getEmailOwner();

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Car car=new Car();
                car.setEmailOwner(emailOwn);
                car.setModel(model.getText().toString());
                car.setLicensePlateNum(carNum.getText().toString());
                car.setYear(year.getText().toString());
                car.setEngine(engine.getText().toString());
                car.setPlaces(places.getText().toString());
                Bitmap bitmap = ((BitmapDrawable)img.getDrawable()).getBitmap();
                Model.instance.uploadImage(bitmap, car.getLicensePlateNum(), new ModelFirebase.UploadImageListener() {
                    @Override
                    public void onComplete(String url) {
                        if (url == null){
                            Toast.makeText(getContext(),"Save Image Filed!",Toast.LENGTH_SHORT).show();
                        }else{
                            car.setImgUrl(url);
                            Toast.makeText(getContext(),"Image Saved!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Model.instance.updateCar(car, new Model.UpdateCarListener() {
                    @Override
                    public void onComplete(boolean success) {
                        if(success) {
                            Toast.makeText(getContext(), "Details Saved!", Toast.LENGTH_SHORT).show();
                            EditCarDirections.ActionEditCarToCarDetails action = EditCarDirections.actionEditCarToCarDetails(carNum.getText().toString());
                            Navigation.findNavController(view).navigate(action);
                        }
                        else{
                            Toast.makeText(getContext(),"error save",Toast.LENGTH_SHORT).show();
                        }
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