package com.example.Sushikung.Controller;


import com.example.Sushikung.Model.Ticket;
import com.example.Sushikung.service.FirebaseInitalizer;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class RestaurantController {

    @Autowired
    FirebaseInitalizer db;

    @GetMapping("/getAllTicket")
    public List<Ticket> getAllTicket() throws ExecutionException, InterruptedException {
        List<Ticket> ticketList = new ArrayList<Ticket>();
        CollectionReference ticket =  db.getFirebase().collection(("Ticket"));
        ApiFuture<QuerySnapshot> querySnapshot = ticket.get();
        for(DocumentSnapshot doc: querySnapshot.get().getDocuments()){
            ticketList.add(doc.toObject(Ticket.class));
        }


        return ticketList;
    }

    @PostMapping("/getTicket")
    public Ticket getTicket(@RequestParam("Id") int id){
        return new Ticket();
    }

    @PostMapping("/addTicket")
    public int CreateTicket(@RequestBody Ticket ticket){
        return 1;
    }
}
