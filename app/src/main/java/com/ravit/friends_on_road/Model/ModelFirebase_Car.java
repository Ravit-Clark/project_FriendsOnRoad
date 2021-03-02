package com.ravit.friends_on_road.Model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFirebase_Car {


    public static void addCar(Car car, Model.AddCarListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String,Object> data=new HashMap<String, Object>();
        data.put("model",car.getModel());
        data.put("licensePlateNum",car.getLicensePlateNum());
        data.put("year",car.getYear());
        data.put("engine",car.getEngine());
        data.put("numPlaces",car.getPlaces());
        data.put("emailOwner",car.getEmailOwner());


        db.collection("cars").document(car.getLicensePlateNum()).set(data)
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


//    public static void getAllCarsByOwner(final String email,final Model.GetCarsByEmailOwnerListener listener) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("cars").document(email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()){
//                    List<Car> carList = new LinkedList<Car>();
//                    for (QueryDocumentSnapshot doc: task.getResult()) {
//                        Log.d("TAG","car id: " + doc.get("id"));
//                        Car car = doc.toObject(Car.class);
//                        carList.add(car);
//                    }
//                    listener.onComplete(carList);
//                }else{
//                    Log.d("TAG", "failed getting cars from fb");
//                    listener.onComplete(null);
//                }
//            }
//
//        });
//    }

    public static void getCarsByEmailOwner(String email,Model.GetCarsByEmailOwnerListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("cars").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    List<Car> carList = new LinkedList<Car>();
                    for (QueryDocumentSnapshot doc: task.getResult()) {
                        Car car = doc.toObject(Car.class);
                        if(car.getEmailOwner().equals(email))
                            carList.add(car);
                    }
                    listener.onComplete(carList);
                }else{
                    Log.d("TAG", "failed getting cars ");
                    listener.onComplete(null);
                }
            }
        });
    }

    public static void getCarByNum(String licensePlateNum, Model.GetCarByNumListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("cars").document(licensePlateNum).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            Car car = task.getResult().toObject(Car.class);
                            listener.onComplete(car);
                        }else{
                            listener.onComplete(null);
                        }
                    }
                });

    }

    public static void updateCar(Car car, Model.UpdateCarListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> data=new HashMap<String, Object>();
        data.put("model",car.getModel());
        data.put("licensePlateNum",car.getLicensePlateNum());
        data.put("year",car.getYear());
        data.put("engine",car.getEngine());
        data.put("numPlaces",car.getPlaces());
        data.put("emailOwner",car.getEmailOwner());

        db.collection("cars").document(car.getLicensePlateNum())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error writing document", e);
                    }
                });

    }


    public interface UploadImageListener{
        void onComplete(String url);

    }
    public static void uploadImage(Bitmap imageBmp, String fileName, final ModelFirebase.UploadImageListener listener){
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
