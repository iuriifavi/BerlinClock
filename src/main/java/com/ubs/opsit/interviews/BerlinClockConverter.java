package com.ubs.opsit.interviews;

/**
 * Created by iuriifavi on 11/30/15.
 * This program doesn't check input..
 * This realisation of parser will works faster then RegExp but ...
 * SimpleDateFormat can helps for validation but it's huge price of performance
 * Total Spent Time: 1h 45m
 * Lost time on understanding Quarter Light Problem: 20 min...
 */

public class BerlinClockConverter implements TimeConverter {
    //access time segments more readable
    final private static int HOUR = 0;
    final private static int MIN = 1;
    final private static int SEC = 2;

    public boolean isValidTime(int h, int min, int sec) {
        //One solar day on the earth is 24 h, 1 hour is 60 min, 1 min is 60 seconds, then 1 solar day 24 * 60 * 60 in seconds;
        return 24 * 60 * 60 >= (h * 60 + min) * 60 + sec;
    }

    public String convertTime(String aTime) {
        // Because 24:00:00 by SimpleDateFormat converting to 2 Jan 19XX 00:00:00
        // it will added additional logical checks into our code and make it more complex to understand
        // I will not use it and make more faster parser which will produce data what we exactly need.
        // If you have a problem and you think you can resolve it using RegExp - Bed news you have 2 problems ;)

        // Split string by ':' separator into 3 different segments Hours,Minutes,Seconds
        String[] parts = aTime.split(":");
        int[] time = new int[3];

        StringBuilder output = new StringBuilder();

        // Assert when input date incorrect
        assert(parts.length == 3);

        try {
            //Converting each part from string to int
            time[HOUR] = Integer.parseInt(parts[HOUR]);
            time[MIN] = Integer.parseInt(parts[MIN]);
            time[SEC] = Integer.parseInt(parts[SEC]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "Invalid Input";
        }

        assert( isValidTime(time[HOUR],time[MIN],time[SEC]) );

        // At each row we have some amount of lights
        // 1 row is second blinker it blinks for 1 second each 2 seconds
        // 2 row displays how much 5H parts of day past.
        // if in seconds row has no lights then it will be from 00:00:01 to 04:59:59
        // 3 row displays how much hours spent in current 5H
        // if in third line row has no lights then it will be only firs hour of five
        // 4 row is the same as seconds but for minutes it's present 5M parts.
        // 5 row is the same as third but for 1M parts.
        // Will look at hours 1PM = 13 hours from midnight = 2 times 5H and 3 times 1H
        // X = 13 / 5 = 2,  Y = 13 % 5 = 3,  X * 5 + Y = 13;
        // Then we will see that 5H parts we can get by: 13/5 = 2.33333 but when we use Integer it's will be 2.
        // And another part by 13 % 5 = 3
        // Almost all processors make Multiplication faster then Divide ( (X / 5) == (X * 1 / 5) == (X * 0.2) )
        boolean secondsBlinker = (time[SEC] %2) == 0;
        int hourLights5 = (int)(time[HOUR] * 0.2f);
        int hourLights1 = (time[HOUR] % 5);
        int minuteLights5 = (int)(time[MIN] * 0.2f);
        int minuteLights1 = (time[MIN] % 5);

        // fill 1 row
        if (secondsBlinker) // Yellow lamp at the top blinks once per 2 seconds (0,2,4,6,8,...) => %2
            output.append('Y');
        else
            output.append('O');

        output.append('\n');

        //fill 2 row
        for (int i = 0; i < 4; ++i)
            output.append(i >= hourLights5 ? 'O' : 'R');

        output.append('\n');

        // fill 3 row
        for (int i = 0; i < 4; ++i)
            output.append(i >= hourLights1 ? 'O' : 'R');

        output.append('\n');

        // fill 4 row
        for (int i = 0; i < 11; ++i) {
            boolean isQuarter = (i + 2) % 3 == 1; //wee need 2 5 8
            char rectColor = isQuarter ? 'R' : 'Y';
            output.append(i >= minuteLights5 ? 'O' : rectColor);
        }

        output.append('\n');

        // fill 5 row
        for (int i = 0; i < 4; ++i)
            output.append(i >= minuteLights1 ? 'O' : 'Y');


        return output.toString();
    }

}