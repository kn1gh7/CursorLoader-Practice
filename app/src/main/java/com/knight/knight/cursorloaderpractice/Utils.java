package com.knight.knight.cursorloaderpractice;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mehul on 14/9/16.
 */
public class Utils {

    public static String formatDuration(String longTime) {
        String csongs_duration = "";
        if(String.valueOf(longTime) != null){
            try{
                Long time = Long.valueOf(longTime);
                long seconds = time/1000;
                long minutes = seconds/60;
                seconds = seconds % 60;

                if(seconds<10){
                    csongs_duration = String.valueOf(minutes) + ":0" + String.valueOf(seconds);
                    csongs_duration = csongs_duration;
                }else{
                    String ccsongs_duration = String.valueOf(minutes) + ":" + String.valueOf(seconds);
                    csongs_duration = ccsongs_duration;
                }
            }catch(NumberFormatException e){
                csongs_duration = longTime;
            }
        }else{
            String nothing = "0";
            csongs_duration = nothing;
        }

        return csongs_duration;
    }
}
