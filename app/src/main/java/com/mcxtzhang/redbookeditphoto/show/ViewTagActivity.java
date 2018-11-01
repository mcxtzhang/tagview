package com.mcxtzhang.redbookeditphoto.show;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.mcxtzhang.redbookeditphoto.R;

import java.util.HashMap;
import java.util.Map;

public class ViewTagActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tag);

        final ViewPager mViewPager = findViewById(R.id.viewPager);
        final TagPhotoViewAdapter tagPhotoViewAdapter = new TagPhotoViewAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(tagPhotoViewAdapter);
    }

    private class TagPhotoViewAdapter extends FragmentPagerAdapter {
        private Map<Integer, FeedDetailFragment> sIntegerPhotoEditFragmentMap = new HashMap<>();

        public TagPhotoViewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            FeedDetailFragment photoEditFragment = FeedDetailFragment.newInstance(position);
            sIntegerPhotoEditFragmentMap.put(position, photoEditFragment);
            return photoEditFragment;
        }

        @Override
        public int getCount() {
            return 5;
        }

    }
}
