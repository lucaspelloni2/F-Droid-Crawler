package Crawler;

import java.util.ArrayList;

/**
 * Created by LuckyP on 03.08.17.
 */
public abstract class Util {

    public static void printList(ArrayList<String> apks) {
        apks.forEach(System.out::println);
    }
}
