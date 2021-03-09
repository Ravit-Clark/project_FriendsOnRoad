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
import android.widget.TextView;
import android.widget.Toast;

import com.ravit.friends_on_road.Model.Car;
import com.ravit.friends_on_road.Model.Event;
import com.ravit.friends_on_road.Model.Model;
import com.ravit.friends_on_road.Model.ModelFirebase;
import com.ravit.friends_on_road.Model.User;
import com.ravit.friends_on_road.R;
import com.ravit.friends_on_road.ui.myCars.EditCarDirections;

import java.util.LinkedList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class EditMyOpenEvent extends Fragment {
    ImageView img;
    Event ev;
    EditText type;
    EditText descripion;
    EditText location;
    EditText status;
    EditText carView;
    Button chooseCar;
    String carChoosen;
    List<Car> cars;
    CharSequence[] options;
    Button saveBtn;
    String myEvent;
    ImageButton editImg;
    Button close;
    String userEmail;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_edit_my_open_event, container, false);
        type=view.findViewById(R.id.editEvent_type);
        descripion=view.findViewById(R.id.editEvent_descripion);
        location=view.findViewById(R.id.editEvent_location);
        carView=view.findViewById(R.id.editEvent_car);
        status=view.findViewById(R.id.editEvent_status);
        close=view.findViewById(R.id.editEvent_closeEventBtn);
        chooseCar=view.findViewById(R.id.editEvent_chooseCarBtn);
        saveBtn = view.findViewById(R.id.editEvent_saveBtn);

        myEvent = EditMyOpenEventArgs.fromBundle(getArguments()).getEventNum();

        editImg = view.findViewById(R.id.editEvent_editImgBtn);
        img=view.findViewById(R.id.editEvent_img);
        editImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editImage();
            }
        });

        getEvent();
        //userEmail = Model.instance.getUserEmail();
        chooseCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cars = new LinkedList<>();

                Model.instance.getCarsByEmailOwner(ev.getEmailOwner(), new Model.GetCarsByEmailOwnerListener() {
                    @Override
                    public void onComplete(List<Car> data) {
                        cars=data;
                        options = new CharSequence[cars.size()];
                        for(int i=0;i<options.length;i++){
                            options[i]=cars.get(i).getLicensePlateNum();
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Choose a Car:");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                carChoosen=options[item].toString();
                                Log.d("TAG","car choosen: "+carChoosen);
                                carView.setText(carChoosen);

                            }
                        });
                        builder.show();
                    }
                });

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event event=ev;
                Log.d("TAG","close: "+ev.getNumOfSpecificEvent());
                event.setType(type.getText().toString());
                event.setDescription(descripion.getText().toString());
                event.setLocation(location.getText().toString());
                event.setCar(carChoosen);

                Bitmap bitmap = ((BitmapDrawable)img.getDrawable()).getBitmap();
                Model.instance.uploadImage(bitmap, event.getNumOfSpecificEvent(), new ModelFirebase.UploadImageListener() {
                    @Override
                    public void onComplete(String url) {
                        if (url == null){
                            Toast.makeText(getContext(),"Save Image Filed!",Toast.LENGTH_SHORT).show();
                        }else{
                            event.setImgUrl(url);
                            Toast.makeText(getContext(),"Image Saved!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Model.instance.updateEvent(event, new Model.UpdateEventListener() {
                    @Override
                    public void onComplete(boolean success) {
                        if(success) {
                            Toast.makeText(getContext(), "Details Saved!", Toast.LENGTH_SHORT).show();
                            EditMyOpenEventDirections.ActionEditMyOpenEventToNavHome action =EditMyOpenEventDirections.actionEditMyOpenEventToNavHome(ev.getEmailOwner());
                            Navigation.findNavController(view).navigate(action);
                        }
                        else{
                            Toast.makeText(getContext(),"error save",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG","close: "+ev.getNumOfSpecificEvent());
                Model.instance.getEventByEventNum(myEvent, new Model.GetEventByEventNumListener() {
                    @Override
                    public void onComplete(Event event) {
                        ev=event;
                        Log.d("TAG","close: "+ev.getNumOfSpecificEvent());
                    }
                });
                ev.closeEvent();//change status to 'close'
                Model.instance.updateEvent(ev, new Model.UpdateEventListener() {
                    @Override
                    public void onComplete(boolean success) {
                        Toast.makeText(getContext(),"update event",Toast.LENGTH_SHORT).show();
                    }
                });
                Model.instance.getUser(ev.getEmailOwner(), new Model.GetUserListener() {
                    @Override
                    public void onComplete(User user) {
                        User user1=user;
                        user1.setEventOpen(false);
                        //user1.setMyOpenEvent(_numRun);
                        Model.instance.updateUser(user1 , new Model.UpdateUserListener(){
                            @Override
                            public void onComplete(boolean success) {
                                Toast.makeText(getContext(),"update user",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                Toast.makeText(getContext(),"The event closed!",Toast.LENGTH_SHORT).show();
                EditMyOpenEventDirections.ActionEditMyOpenEventToNavHome action =EditMyOpenEventDirections.actionEditMyOpenEventToNavHome(ev.getEmailOwner());
                Navigation.findNavController(view).navigate(action);
            }
        });






        return view;
    }


    public void getEvent(){
        Log.d("TAG","edit from the edit: "+myEvent);
        Model.instance.getEventByEventNum(myEvent, new Model.GetEventByEventNumListener() {
            @Override
            public void onComplete(Event event) {
                ev=event;
                Log.d("TAG","num close: "+event.getNumOfSpecificEvent());
                Log.d("TAG","num close: "+event.getNumOfSpecificEvent());
                type.setText(event.getType());
                descripion.setText(event.getDescription());
                location.setText(event.getLocation());
                Log.d("TAG","location: "+event.getLocation());
                carView.setText(event.getCar());
                status.setText(event.getStatus());
                Log.d("TAG","status: "+event.getStatus());
            }
        });

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