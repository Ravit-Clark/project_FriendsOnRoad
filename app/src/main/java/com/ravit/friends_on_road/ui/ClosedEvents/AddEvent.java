package com.ravit.friends_on_road.ui.ClosedEvents;

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

import android.provider.MediaStore;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ravit.friends_on_road.Model.Car;
import com.ravit.friends_on_road.Model.Event;
import com.ravit.friends_on_road.Model.Model;
import com.ravit.friends_on_road.Model.ModelFirebase;
import com.ravit.friends_on_road.R;
import com.ravit.friends_on_road.ui.home.HomeFragmentArgs;

import java.util.LinkedList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class AddEvent extends Fragment  {
    //implements AdapterView.OnItemSelectedListener
    String[] items;
    List<Car> data;
    ImageView img;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);
        EditText type = view.findViewById(R.id.addEvent_type);
        EditText description = view.findViewById(R.id.addEvent_description);
        EditText location = view.findViewById(R.id.addEvent_location);
        Button saveBtn = view.findViewById(R.id.addEvent_saveBtn);
        ImageButton addImg=view.findViewById(R.id.addEvent_addImgBtn);
        img = view.findViewById(R.id.addEvent_img);
        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editImage();
            }
        });
        String ownEmail = AddEventArgs.fromBundle(getArguments()).getEmailOwner();
        //Spinner dropdown = view.findViewById(R.id.addEvent_carsSpinner);

//        Model.instance.getCarsByEmailOwner(ownEmail, new Model.GetCarsByEmailOwnerListener() {
//            @Override
//            public void onComplete(List<Car> _data) {
//                data = _data;
//                items = new String[data.size()];
//                for (int i = 0; i < data.size(); i++) {
//                    items[i] = data.get(i).getLicensePlateNum();
//                }
//            }
//        });

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, items);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        dropdown.setAdapter(adapter);


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event event = new Event();
                event.setType(type.getText().toString());
                event.setLocaion(location.getText().toString());
                event.setDescription(description.getText().toString());
                event.setEmailOwner(ownEmail);
                event.openEvent();//status-open
                event.setNumOfSpecificEvent(event.getEventNum());
                event.PromoteEventNum();
                Bitmap bitmap = ((BitmapDrawable)img.getDrawable()).getBitmap();
                Model.instance.uploadImage(bitmap, event.getNumOfSpecificEvent(), new ModelFirebase.UploadImageListener() {
                    @Override
                    public void onComplete(String url) {
                        if (url == null){
                            Toast.makeText(getContext(),"Save Image Filed!",Toast.LENGTH_SHORT).show();
                        }else{
                            event.setImgUrl(url);
                            Model.instance.addEvent(event, new Model.AddEventListener(){
                                @Override
                                public void onComplete(boolean success) {
                                    Toast.makeText(getContext(),"Image Saved!",Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    }
                });
            }
        });


        return view;
    }

//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        switch (position) {
//            case 0:
//                // Whatever you want to happen when the first item gets selected
//                break;
//            case 1:
//                // Whatever you want to happen when the second item gets selected
//                break;
//            case 2:
//                // Whatever you want to happen when the thrid item gets selected
//                break;
//
//        }
//    }

 //   @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//        data.get(1).getLicensePlateNum();
//    }


    void editImage() {
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
