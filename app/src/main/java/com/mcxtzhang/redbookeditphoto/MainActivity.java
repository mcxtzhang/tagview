package com.mcxtzhang.redbookeditphoto;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ViewPager mViewPager = findViewById(R.id.viewPager);
        final TagPhotoViewAdapter tagPhotoViewAdapter = new TagPhotoViewAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(tagPhotoViewAdapter);

        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tagPhotoViewAdapter.saveTags(mViewPager.getCurrentItem());
            }
        });
    }

    private static class TagPhotoViewAdapter extends FragmentPagerAdapter {
        private static Map<Integer, PhotoEditFragment> sIntegerPhotoEditFragmentMap = new HashMap<>();

        public TagPhotoViewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            PhotoEditFragment photoEditFragment = PhotoEditFragment.newInstance(position);
            sIntegerPhotoEditFragmentMap.put(position, photoEditFragment);
            return photoEditFragment;
        }

        @Override
        public int getCount() {
            return 5;
        }

//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
//            sIntegerPhotoEditFragmentMap.remove(position);
//        }

        public void saveTags(int index) {
            PhotoEditFragment photoEditFragment = sIntegerPhotoEditFragmentMap.get(index);
            if (null != photoEditFragment) {
                photoEditFragment.onSaveClick();
            }
        }
    }

}
