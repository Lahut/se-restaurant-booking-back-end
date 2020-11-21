package com.example.Sushikung.service;


import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FirebaseInitalizer {

    @PostConstruct
    private void initDB() throws IOException {

        InputStream serviceAccount =
                        this.getClass()
                        .getClassLoader()
                        .getResourceAsStream("./se-restaurant-booking-firebase-adminsdk-qrfyj-9996a7c2e3.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://se-restaurant-booking.firebaseio.com")
                .build();


        if(FirebaseApp.getApps().isEmpty()){
            FirebaseApp.initializeApp(options);
        }


//  Initialize Seat for all major

//        String[]  Allduration = {"11.00-12.30","12.30-14.000","14.00-15.30",
//        "15.30-17.00","17.00-18.30","18.30-20.00","20.00-21.30"};
//        String[] AllRestaurant = {"Major-Ratchayothin","Esplanade-Ngamwongwan-Khae-Rai","Pinklao"};
//
//        for (String location : AllRestaurant){
//            Map<String,Object> docData = new HashMap<>();
//            List<Map> duration = new ArrayList<>();
//            docData.put("name",location);
//            for (String time : Allduration) {
//                Map<String,Object> durationDetail = new HashMap<>();
//                durationDetail.put("time",time);
//                durationDetail.put("seat",15);
//                durationDetail.put("status",true);
//                duration.add(durationDetail);
//            }
//            docData.put("durations",duration);
//            ApiFuture<WriteResult> future = getFirebase()
//                    .collection("Restaurant")
//                    .document(location)
//                    .set(docData);
//        }









    }

    public Firestore getFirebase(){

        return FirestoreClient.getFirestore();

    }

}
