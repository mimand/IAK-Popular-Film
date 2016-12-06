package com.kejar.iman.filmpopuler;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.kejar.iman.filmpopuler.popularmovie.PopularFragment;
import com.kejar.iman.filmpopuler.ratefragment.RateFragment;
import com.kejar.iman.filmpopuler.upcomingfragment.UpcomingFragment;

/**
 * Created by iman on 29/11/2016.
 */

public class PagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private String[] titlesfilm ={"Popular","Rate","Upcoming","Favorite"};
    private int[] iconsFilm ={R.drawable.ic_supervisor_account_white_48dp,
                            R.drawable.ic_grade_white_48dp,
                            R.drawable.ic_today_white_48dp,
                            R.drawable.ic_favorite_white_48dp};

    private int heightIcon;
    Toolbar toolbar;

    private boolean checkNetwork;
    public PagerAdapter(FragmentManager fm, Context c, Toolbar toolbar,boolean checkNetwork) {
        super(fm);
        this.checkNetwork = checkNetwork;
        mContext =c;
        double scale =c.getResources().getDisplayMetrics().density;
        heightIcon =(int) (24*scale+0.5f);
        this.toolbar =toolbar;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0){
            PopularFragment popularFragment =new PopularFragment();
            Bundle args = new Bundle();
            args.putBoolean("getConnection",checkNetwork);
            popularFragment.setArguments(args);
            return  popularFragment;
        }else if (position == 1){
            RateFragment rateFragment = new RateFragment();
            Bundle args = new Bundle();
            args.putBoolean("getConnection",checkNetwork);
            rateFragment.setArguments(args);
            return  rateFragment;
        }else if (position == 2){
            UpcomingFragment upcomingFragment=  new UpcomingFragment();

            Bundle args = new Bundle();
            args.putBoolean("getConnection",checkNetwork);
            upcomingFragment.setArguments(args);
            return  upcomingFragment;
        }else if (position == 3){
            FavoriteFragment favoriteFragment = new FavoriteFragment();
            Bundle args = new Bundle();
            args.putBoolean("getConnection",checkNetwork);
            favoriteFragment.setArguments(args);
            return favoriteFragment;

        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable d = mContext.getResources().getDrawable(iconsFilm[position]);
        d.setBounds(0,0,heightIcon,heightIcon);

        ImageSpan i = new ImageSpan(d);

        SpannableString spannableString =new SpannableString("  ");
        spannableString.setSpan(i,0,spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return (spannableString);
    }
}

