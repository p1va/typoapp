package com.github.p1va.typoapp.utils;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Stefano Piva on 10/05/2019.
 */
public class ListsUtils {

    /**
     * Picks a random item from a list
     * @param list The list to pick from
     * @param <T> The generic type
     * @return The random item of the list
     */
    public static <T> T pickRandomItem(ArrayList<T> list) {

        // Get the index of the random item
        int index = new Random().nextInt(list.size());

        // Return the random item
        return list.get(index);
    }
}
