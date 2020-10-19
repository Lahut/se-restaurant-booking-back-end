package com.example.Sushikung.service;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FirebaseInitalizer {

    @PostConstruct
    private void initDB() throws IOException {

        InputStream serviceAccount =
                        this.getClass()
                        .getClassLoader()
                        .getResourceAsStream("./se-restaurant-booking-firebase-adminsdk-qrfyj-9eb2a841e1.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://se-restaurant-booking.firebaseio.com")
                .build();


        if(FirebaseApp.getApps().isEmpty()){
            FirebaseApp.initializeApp(options);
        }


    }

    public Firestore getFirebase(){

        return FirestoreClient.getFirestore();

    }

}
