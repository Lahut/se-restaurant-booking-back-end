package com.example.Sushikung.Controller;


import com.example.Sushikung.Model.Duration;
import com.example.Sushikung.Model.Restaurant;
import com.example.Sushikung.Model.Ticket;
import com.example.Sushikung.service.FirebaseInitalizer;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class RestaurantController {

    private static Logger logger = LoggerFactory.getLogger(RestaurantController.class);



    @Autowired
    FirebaseInitalizer db;

    @CrossOrigin
    @GetMapping("/getTicket/{TicketId}")
    public Ticket getTicketById (@PathVariable String TicketId) throws ExecutionException, InterruptedException {

            Ticket ticket;
            DocumentReference docRef = db.getFirebase().collection("Ticket").document(TicketId);
            ApiFuture<DocumentSnapshot> future = docRef.get();

            DocumentSnapshot document = future.get();

            if (document.exists()) {
                ticket = document.toObject(Ticket.class);
                return ticket;
            }

            return null;




    }

    @CrossOrigin
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

    @CrossOrigin
    @GetMapping("/getDurations/{location}")
    public Restaurant getAllDuration(@PathVariable String location) throws ExecutionException, InterruptedException {
        Restaurant restaurant ;
        logger.info("location:" + location);

        DocumentReference docRef = db.getFirebase().collection("Restaurant").document(location);
        ApiFuture<DocumentSnapshot> future = docRef.get();

        DocumentSnapshot document = future.get();

        restaurant = document.toObject(Restaurant.class);

        return restaurant;
    }

    @CrossOrigin
    @PostMapping("/getTicket")
    public Ticket getTicket(@RequestParam("Id") String id){

        return new Ticket();
    }

    @CrossOrigin
    @PostMapping("/addTicket")
    public String CreateTicket(@RequestBody Ticket ticket ) throws ExecutionException, InterruptedException {

        CollectionReference ticketFB =  db.getFirebase().collection(("Ticket"));





        Restaurant restaurant ;

        DocumentReference docRef = db.getFirebase()
                .collection("Restaurant")
                .document(ticket.getLocation());

        ApiFuture<DocumentSnapshot> future = docRef.get();

        DocumentSnapshot document = future.get();

        restaurant = document.toObject(Restaurant.class);

        List<Duration> allDuration = restaurant.getDurations();

        for ( Duration d : allDuration){
            //logger.info("result"+d.getTime()+ticket.getTime()+d.getSeat()+ticket.getSeat());
            if(d.getTime().equals(ticket.getTime())){
                if(d.getSeat() >= ticket.getSeat()){
                    ticketFB.document(String.valueOf(ticket.getId())).set(ticket);
                    Integer sum_seat = d.getSeat() - ticket.getSeat();
                    //logger.info("sum: "+sum_seat);

                    d.setSeat(sum_seat);
                    if(d.getSeat() == 0) {
                        d.setStatus(false);
                    }
                    restaurant.setDurations(allDuration);
                    ApiFuture<WriteResult> newDuration = db.getFirebase()
                            .collection("Restaurant")
                            .document(ticket.getLocation())
                            .set(restaurant);
                }else {
                    return "ขออภัยจำนวนที่นั่งไม่พอค่ะ";
                }
            }
        }




        return ticket.getId();
    }
}
