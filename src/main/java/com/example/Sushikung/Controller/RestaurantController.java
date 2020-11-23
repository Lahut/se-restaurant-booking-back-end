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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        restaurant = document.toObject(Restaurant.class); //get object of Restaurant

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

    @CrossOrigin
    @PostMapping("/resetallseat")
    public String ResetALlSeat(){
        String[]  Allduration = {"11.00-12.30","12.30-14.000","14.00-15.30",
        "15.30-17.00","17.00-18.30","18.30-20.00","20.00-21.30"};
        String[] AllRestaurant = {"Major-Ratchayothin","Esplanade-Ngamwongwan-Khae-Rai","Pinklao"};

        for (String location : AllRestaurant){
            Map<String,Object> docData = new HashMap<>();
            List<Map> duration = new ArrayList<>();
            docData.put("name",location);
            for (String time : Allduration) {
                Map<String,Object> durationDetail = new HashMap<>();
                durationDetail.put("time",time);
                durationDetail.put("seat",15);
                durationDetail.put("status",true);
                duration.add(durationDetail);
            }
            docData.put("durations",duration);
            ApiFuture<WriteResult> future = db.getFirebase()
                    .collection("Restaurant")
                    .document(location)
                    .set(docData);
        }

        return "ทำการรีเซ็ทที่นั่งเรียบร้อย" ;

        }

    @CrossOrigin
    @PostMapping("/deleteTicket/{ticketId}")
    public String DeleteTicket(@PathVariable String ticketId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.getFirebase().collection("Ticket").document(ticketId);
        ApiFuture<DocumentSnapshot> future = docRef.get();


        Ticket ticket;
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            ticket = document.toObject(Ticket.class);
            Restaurant restaurant ;

            DocumentReference docRef_2 = db.getFirebase()
                    .collection("Restaurant")
                    .document(ticket.getLocation());

            ApiFuture<DocumentSnapshot> future_2 = docRef_2.get();

            DocumentSnapshot document_2 = future_2.get();

            restaurant = document_2.toObject(Restaurant.class); //get object of Restaurant

            List<Duration> allDuration = restaurant.getDurations();

            for ( Duration d : allDuration){
                //logger.info("result"+d.getTime()+ticket.getTime()+d.getSeat()+ticket.getSeat());
                if(d.getTime().equals(ticket.getTime())){
                    Integer newSeat = d.getSeat() + ticket.getSeat();
                    d.setSeat(newSeat);
                    if(d.getSeat() > 0){
                        d.setStatus(true);
                    }
                    restaurant.setDurations(allDuration);
                    ApiFuture<WriteResult> newDuration = db.getFirebase()
                            .collection("Restaurant")
                            .document(ticket.getLocation())
                            .set(restaurant);
                    return "ลบสำเเร็จ";
                }
            }



        }else{
            return "ขออภัยไม่พบเลขที่นั่งค่ะ";
        }





        return "ขออภัยไม่พบเลขที่นั่งค่ะ";


    }




}
