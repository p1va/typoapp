package com.github.p1va.typoapp;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {

        assertEquals(4, 2 + 2);
    }

    @Test
    public void seekBarProgressRange_isCorrect() {


        float step = 0.5f;
        int min = -5;
        int max = 5;


        float seekbarMax = (max - min) / step;

        System.out.println("Seekbar max " + seekbarMax);


        for (float i = 0; i <= seekbarMax; i=i+step) {

            System.out.println("-> " + i);

            double value = min + (i * step);

            System.out.println("---> " + value);

            //float distanceFromMin = i - max;

            //float myDistanceFromMax = (distanceFromMax * myMax) / max;

            //System.out.println("---> " + (myDistanceFromMax));

        }
    }
}