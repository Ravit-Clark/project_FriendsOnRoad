package com.ravit.friends_on_road.Model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ravit.friends_on_road.SignUpDirections;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFirebase {

    public ModelFirebase(){ }

    public static void addUser(User user, Model.AddUserListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String,Object> data=new HashMap<String, Object>();
        data.put("name",user.getName());
        data.put("phone",user.getPhone());
        data.put("email",user.getEmail());
        data.put("passWord",user.getPassword());
        data.put("openEvent",false);
        data.put("myOpenEvent","0000");



        db.collection("users").document(user.getEmail()).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully written!");
                        listener.onComplete(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error writing document", e);
                        listener.onComplete(false);
                    }
                });

    }

//    public void createUser(User user,Model.CreateUserListener listener){
//        FirebaseAuth fAuth= FirebaseAuth.getInstance();
//        fAuth.createUserWithEmailAndPassword(user.getEmail(),user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful()){
//                    listener.onComplete(true);
//                }else{
//                    listener.onComplete(false);
//
//                }
//            }
//        });
//    }

    public static FirebaseUser getCurrentUser() {
        FirebaseAuth fauth=FirebaseAuth.getInstance();
        return fauth.getCurrentUser();
    }

    public void getUser(String email, Model.GetUserListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(email).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            User user = task.getResult().toObject(User.class);
                            listener.onComplete(user);
                        }else{
                            listener.onComplete(null);
                        }
                }
        });
    }

    public static String getUserEmail() {
        FirebaseAuth fauth=FirebaseAuth.getInstance();
        return fauth.getCurrentUser().getEmail();
    }


    public void updateUser(final User user,Model.UpdateUserListener listener){
        Log.d("TAG", "update: "+user.getName());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("TAG", "DocumentSnapshot successfully written!");
        db.collection("users").document(user.getEmail())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully written!");
                        listener.onComplete(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error writing document", e);
                        listener.onComplete(false);
                    }
                }
        );

    }


    public interface UploadImageListener{
        void onComplete(String url);

    }
    public static void uploadImage(Bitmap imageBmp, String fileName, final UploadImageListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference imagesRef = storage.getReference().child("images").child(fileName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.onComplete(null);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUrl = uri;
                        listener.onComplete(downloadUrl.toString());
                    }
                });
            }
        });
    }




}
