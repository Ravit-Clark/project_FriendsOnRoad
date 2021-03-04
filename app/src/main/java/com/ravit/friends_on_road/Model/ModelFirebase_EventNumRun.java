package com.ravit.friends_on_road.Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ModelFirebase_EventNumRun {
    public static void addNumRun(EventsNumRun numRun, Model.AddNumRunListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> data=new HashMap<String, Object>();
        data.put("num",numRun.getNum());

        db.collection("eventsNumRun").document(numRun.getNum()).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully written! - numRun");
                        listener.onComplete(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error writing document - numRun", e);
                        listener.onComplete(false);
                    }
                });
    }



    public static void getNumRun(Model.GetNumRunListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("eventsNumRun").document("1").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            EventsNumRun numRun = task.getResult().toObject(EventsNumRun.class);
                            Log.d("TAG","num: "+numRun.getNum());
                            listener.onComplete(numRun);
                        }else{
                            listener.onComplete(null);
                        }
                    }
                });

    }

    public static void updateNumRun(String  numRun, Model.UpdateNumRunListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> data=new HashMap<String, Object>();
        data.put("num",numRun);

        db.collection("eventsNumRun").document("1")
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully change!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error chane document", e);
                    }
                });
    }
}
