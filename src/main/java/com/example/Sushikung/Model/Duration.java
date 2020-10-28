package com.example.Sushikung.Model;

public class Duration {

    private int seat;
    private boolean status;
    private String time;


    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Duration{" +
                "seat=" + seat +
                ", status=" + status +
                ", time='" + time + '\'' +
                '}';
    }
}
