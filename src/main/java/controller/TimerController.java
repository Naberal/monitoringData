package controller;

import service.Service;

import java.util.Timer;

public class TimerController {
    private Timer timer;
    public void agen(){
        timer=new Timer();
        Service service=new Service();
        timer.schedule(service, (long)(1.16*Math.pow(10,8)));
    }

}
