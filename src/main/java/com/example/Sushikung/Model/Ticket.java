package com.example.Sushikung.Model;


import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Ticket {
    private String Id ;
    private String name;
    private String tel;
    private String time;

    public Ticket(){
        Random r = new Random();
        String randomNumber = String.format("%04d", (Object) Integer.valueOf(r.nextInt(1001)));
        this.Id = randomNumber;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
