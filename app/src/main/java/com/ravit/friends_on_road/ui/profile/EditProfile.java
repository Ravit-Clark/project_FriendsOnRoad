package com.ravit.friends_on_road.ui.profile;

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

import com.ravit.friends_on_road.Model.Model;
import com.ravit.friends_on_road.Model.ModelFirebase;
import com.ravit.friends_on_road.Model.User;
import com.ravit.friends_on_road.R;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class EditProfile extends Fragment {
    final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView img;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_edit_profile, container, false);
        EditText name=view.findViewById(R.id.editProfile_name);
        EditText phone=view.findViewById(R.id.editProfile_phone);
        EditText email=view.findViewById(R.id.editProfile_email);
        Button saveBtn = view.findViewById(R.id.editProfile_saveBtn);
        ImageButton editImageBtn = view.findViewById(R.id.editProfile_editImg);
        img=view.findViewById(R.id.profile_img);

        editImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editImage();
            }
        });






        String userEmail = EditProfileArgs.fromBundle(getArguments()).getEmail();
        Model.instance.getUser(userEmail, new Model.GetUserListener() {
            @Override
            public void onComplete(User user) {
                Log.d("TAG",""+user.getName());
                name.setText(user.getName());
                phone.setText(user.getPhone());
                email.setText(user.getEmail());
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User u=new User();
                u.setName(name.getText().toString());
                u.setPhone(phone.getText().toString());
                u.setEmail(email.getText().toString());
                Bitmap bitmap = ((BitmapDrawable)img.getDrawable()).getBitmap();
                Model.instance.uploadImage(bitmap, u.getEmail(), new ModelFirebase.UploadImageListener() {
                    @Override
                    public void onComplete(String url) {
                        if (url == null){
                            Toast.makeText(getContext(),"Save Image Filed!",Toast.LENGTH_SHORT).show();
                        }else{
                            u.setImageUrl(url);
                            Model.instance.addUser(u, new Model.AddUserListener() {
                                @Override
                                public void onComplete(boolean success) {
                                    Toast.makeText(getContext(),"Image Saved!",Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    }
                });
                Model.instance.updateUser(u, new Model.UpdateUserListener() {
                    @Override
                    public void onComplete(boolean success) {
                        //Navigation.findNavController(view).navigate(R.id.action_editProfile_pop);
                        Navigation.findNavController(view) .popBackStack(R.id.nav_Profile,false);
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