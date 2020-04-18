package com.example.mirek.smarter;

import java.util.Calendar;

/**
 * Created by Mirek on 29/04/2018.
 */

public class Generator {
    private Double fridgeUsage;
    private Double acUsage;
    private Double wmUsage;
    private boolean flag;
    private int wmCount;
    private int acCount;
    private int coutinuesHours;

    public Generator()
    {
        fridgeUsage = ((int)(Math.random() * 101)) * 0.005 + 0.3;
        acUsage = ((int)(Math.random() * 101)) * 0.009 + 0.4;
        wmUsage = ((int)(Math.random() * 101)) * 0.04 + 1.0;
        flag = true;
        wmCount = (int)(Math.random() * 4);
        acCount = (int)(Math.random() * 11);
    }


    public Double[] allUsageGenerator(int time, Double temp){
        Double fridgeNow = fridgeUsage;
        Double acNow = 0.0;
        Double wmNow = 0.0;
        if (time >=9 && time <=22 && temp > 20.0){
            int random = (int)(Math.random() * 2);
            if (random == 1 && acCount > 0){
                acNow = acUsage;
                acCount -= 1;
            }
        }
        if (time >=6 && time <=20){
            int random = (int)(Math.random() * 2);
            if (random == 1 && wmCount > 0 && flag){
                wmNow = wmUsage;
                wmCount -= 1;
                flag = false;
            }
            if (wmCount > 0 && !flag){
                wmNow = wmUsage;
                wmCount -= 1;
            }
        }
        if (time == 0)
            new Generator();
        return new Double[]{fridgeNow, acNow, wmNow};
    }
}
