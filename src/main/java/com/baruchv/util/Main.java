package com.baruchv.util;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Main {
    public static void main(String[] args) {
        //    "startDate": "2020-09-11",
        //    "endDate": "2023-01-09",
        LocalDate start = LocalDate.parse("2020-09-11");
        LocalDate end = LocalDate.parse("2023-01-09");

        LocalDate result = LocalDate.parse("2023-01-08");
        long resultDay = ChronoUnit.DAYS.between(start,end);
        System.out.println(resultDay);

        long result_3 = ChronoUnit.DAYS.between(start,result);
        System.out.println(result_3);
        long ahuz = 100 / ( resultDay + 1);
        System.out.println("Ahuz " + ahuz);
        long ahuz_hachon = ahuz * result_3;
        System.out.println("Ahuz nachon " + ahuz_hachon);

        double ahuz2 = 100 / (ChronoUnit.DAYS.between(start,end) + 1.0) * ChronoUnit.DAYS.between(start,result);
        ahuz2 =Double.parseDouble(new DecimalFormat("##.##").format(ahuz2));
        System.out.println("Ahuz : " + ahuz2);
    }
}
