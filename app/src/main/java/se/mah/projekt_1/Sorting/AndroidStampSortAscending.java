package se.mah.projekt_1.Sorting;

import java.util.Comparator;

import se.mah.projekt_1.Models.AndroidStamp;

/**
 * Created by Gustaf on 20/04/2016.
 */
public class AndroidStampSortAscending implements Comparator<AndroidStamp> {
    @Override
    public int compare(AndroidStamp obj1, AndroidStamp obj2) {
        return (int)(obj1.getDate().getTimeInMillis() - obj2.getDate().getTimeInMillis());
    }
}
