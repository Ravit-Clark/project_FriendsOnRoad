package com.ravit.friends_on_road.Model;

import android.graphics.Bitmap;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class Model {

    public final static Model instance = new Model();
    ModelFirebase modelFirebase = new ModelFirebase();

    private Model() {

    }

//////////////////User/////////////////
    public interface AddUserListener{
        void onComplete(boolean success);
    }
    public User addUser(User user, AddUserListener listener) {
        modelFirebase.addUser(user, listener);
        return null;
   }

    public FirebaseUser getCurrentUser() {
        return modelFirebase.getCurrentUser();
    }


    public String getUserEmail() {
        return modelFirebase.getUserEmail();
    }


    public interface GetUserListener{
        void onComplete(User use);
    }
    public User getUser(String email, GetUserListener listener) {
        modelFirebase.getUser(email, listener);
        return null;
    }

    public interface UpdateUserListener {
        void onComplete(boolean success);
    }

    public void updateUser(final User user, UpdateUserListener listener) {
        modelFirebase.updateUser(user, listener);
    }



    public void uploadImage(Bitmap imageBmp, String fileName, final ModelFirebase.UploadImageListener listener) {
        ModelFirebase.uploadImage(imageBmp, fileName, listener);
    }


/////////////Car////////////////////////////

    public interface AddCarListener{
        void onComplete(boolean success);
    }
    public Car addCar(Car car, AddCarListener listener) {
        ModelFirebase_Car.addCar(car, listener);
        return null;
    }


    public interface GetCarsByEmailOwnerListener {
        void onComplete(List<Car> data);
    }
    public void getCarsByEmailOwner(final String email,GetCarsByEmailOwnerListener listener) {
        ModelFirebase_Car.getCarsByEmailOwner(email,listener);
    }


    public interface GetCarByNumListener {
        void onComplete(Car car);
    }
    public void getCarByNum(final String num,GetCarByNumListener listener) {
        ModelFirebase_Car.getCarByNum(num,listener);
    }

    public interface UpdateCarListener {
        void onComplete(boolean success);
    }
    public void updateCar(final Car car,UpdateCarListener listener) {
        ModelFirebase_Car.updateCar(car,listener);
    }




//////////////Event///////////////////

    public interface AddEventListener{
        void onComplete(boolean success);
    }
    public void addEvent(Event event , AddEventListener listener){
        ModelFirebase_Event.addEvent(event,listener);
    }

    public interface GetEventsByEmailOwnerListener {
        void onComplete(List<Event> data);
    }
    public void getEventsByEmailOwner(final String email,GetEventsByEmailOwnerListener listener) {
        ModelFirebase_Event.getEventsByEmailOwner(email,listener);
    }

    public interface GetAllEventsOpenListener {
        void onComplete(List<Event> data);
    }
    public void getAllEventsOpen(GetAllEventsOpenListener listener) {
        ModelFirebase_Event.getAllEventsOpen(listener);
    }


    public interface GetEventByEventNumListener {
        void onComplete(Event event);
    }
    public void getEventByEventNum(final String num,GetEventByEventNumListener listener) {
        ModelFirebase_Event.getEventByNumOfSpecificEvent(num,listener);
    }


//////////Event Num Run//////////////////

    public interface AddNumRunListener{
        void onComplete(boolean success);
    }
    public void addNumRun(EventsNumRun numRun,AddNumRunListener listener){
        ModelFirebase_EventNumRun.addNumRun(numRun,listener);
    }

    public interface GetNumRunListener{
        void onComplete(EventsNumRun numRun);
    }
    public void getNumRun(GetNumRunListener listener){
        ModelFirebase_EventNumRun.getNumRun(listener);
    }


    public interface UpdateNumRunListener {
        void onComplete(boolean success);
    }
    public void updateNumRun(final String numRun,UpdateNumRunListener listener) {
        ModelFirebase_EventNumRun.updateNumRun(numRun,listener);
    }













}
