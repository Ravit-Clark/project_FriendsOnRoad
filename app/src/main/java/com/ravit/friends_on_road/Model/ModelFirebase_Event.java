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

public class ModelFirebase_Event {


    public static void getEventsByEmailOwner(String email, Model.GetEventsByEmailOwnerListener listener) {
        Log.d("TAG","get user by email: "+email);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    List<Event> eventList = new LinkedList<Event>();
                    for (QueryDocumentSnapshot doc: task.getResult()) {
                        Event event = doc.toObject(Event.class);
                        if(event.getEmailOwner().equals(email))
                            eventList.add(event);
                    }
                    listener.onComplete(eventList);
                }else{
                    Log.d("TAG", "failed getting events ");
                    listener.onComplete(null);
                }
            }
        });


    }


    public static void addEvent(Event event, Model.AddEventListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> data=new HashMap<String, Object>();
        data.put("type",event.getType());
        data.put("description",event.getDescription());
        data.put("location",event.getLocation());
        data.put("car",event.getCar());
        data.put("emailOwner",event.getEmailOwner());
        data.put("status",event.getStatus());
        data.put("numOfSpecificEvent",event.getNumOfSpecificEvent());



        db.collection("events").document(event.getNumOfSpecificEvent()).set(data)
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

    public static void getAllEventsOpen(Model.GetAllEventsOpenListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    List<Event> eventList = new LinkedList<Event>();
                    for (QueryDocumentSnapshot doc: task.getResult()) {
                        Event event = doc.toObject(Event.class);
                        if(event.getStatus().equals("open"))
                            eventList.add(event);
                    }
                    listener.onComplete(eventList);
                }else{
                    Log.d("TAG", "failed getting events ");
                    listener.onComplete(null);
                }
            }
        });


    }

    public static void getEventByNumOfSpecificEvent(String numOfSpecificEvent, Model.GetEventByEventNumListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("TAG","numOfSpecificEvent: "+numOfSpecificEvent);
        db.collection("events").document(numOfSpecificEvent).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            Event event = task.getResult().toObject(Event.class);
                            Log.d("TAG","get num: "+event.getNumOfSpecificEvent());
                            listener.onComplete(event);
                        }else{
                            listener.onComplete(null);
                        }
                    }
                });

    }

    public static void updateEvent(Event event, Model.UpdateEventListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("events").document(event.getNumOfSpecificEvent())
                .set(event)
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
