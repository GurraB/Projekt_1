package se.mah.projekt_1.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import java.util.ArrayList;

/**
 * Created by Gustaf Bohlin on 06/04/2016.
 * Handles the transition between Log and Graph fragments
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> fragments = new ArrayList<>();
    private final ArrayList<String> fragmentTitles = new ArrayList<>();

    /**
     * Constructor
     * @param manager Fragmentmanager
     */
    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    /**
     * Returns the fragment at position
     * @param position the position
     * @return the requested fragment
     */
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    /**
     * Returns the amount of fragments
     * @return the amount of fragments
     */
    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(Fragment fragment, String title) {
        fragments.add(fragment);
        fragmentTitles.add(title);
    }

    /**
     * Returns the title of the page at position
     * @param position the position
     * @return the title of the page at position
     */
    public CharSequence getPageTitle(int position) {
        return fragmentTitles.get(position);
    }
}
