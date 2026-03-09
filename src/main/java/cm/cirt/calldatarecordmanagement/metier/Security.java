/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.metier;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Harry Wanki
 */
public class Security {

    public static String md5(String md5) {
        if (md5 != null) {
            try {
                java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
                byte[] array = md.digest(md5.getBytes());
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < array.length; ++i) {
                    sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
                }
                return sb.toString();
            } catch (java.security.NoSuchAlgorithmException e) {
            }
        }
        return null;
    }

    public static int doubleToInt(double monDouble) {
        long l = Math.round(monDouble);
        String s = "" + l;
        int i;
        try {
            i = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            if (l < 0) {
                i = Integer.MIN_VALUE;
            } else {
                i = Integer.MAX_VALUE;
            }
        }
        return i;
    }

    public static Date getDateFromString(String stringDate) {
        String[] tab = stringDate.split(" ");
        String[] tab2 = tab[0].split("-");
        int year = Integer.parseInt(tab2[0]) + 2000;
        int month = Integer.parseInt(tab2[1]) - 1;
        int day = Integer.parseInt(tab2[2]);
        int hrs = Integer.parseInt(tab[1].replaceAll("h", ""));
        int min = Integer.parseInt(tab[2].replaceAll("mm", ""));
        int sec = Integer.parseInt(tab[3].replaceAll("s", ""));
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hrs, min, sec);
        Date date = calendar.getTime();
        return date;
    }

    public static String splitToComponentTimes(int seconds) {
//        long longVal = biggy.longValue();
//        int hours = (int) longVal / 3600;
//        int remainder = (int) longVal - hours * 3600;
//        int mins = remainder / 60;
//        remainder = remainder - mins * 60;
//        int secs = remainder;
//
//        int[] ints = {hours, mins, secs};
        int p1 = seconds % 60;
        int p2 = seconds / 60;
        int p3 = p2 % 60;
        p2 = p2 / 60;
//        System.out.print(p2 + ":" + p3 + ":" + p1);
//        System.out.print("\n");

        return p2 + ":" + p3 + ":" + p1;
    }
}
