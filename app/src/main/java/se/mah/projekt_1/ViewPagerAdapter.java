package se.mah.projekt_1;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Gustaf on 06/04/2016.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> fragments = new ArrayList<>();
    private final ArrayList<String> fragmentTitles = new ArrayList<>();
    private FragmentManager manager;
    private Context context;

    public ViewPagerAdapter(FragmentManager manager, Context context) {
        super(manager);
        this.manager = manager;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position).instantiate(context, fragments.get(position).getClass().getName());
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(Fragment fragment, String title) {
        fragments.add(fragment);
        fragmentTitles.add(title);
    }

    public CharSequence getPageTitle(int position) {
        return fragmentTitles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
