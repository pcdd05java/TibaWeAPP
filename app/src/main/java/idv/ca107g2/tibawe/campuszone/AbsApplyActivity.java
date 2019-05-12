package idv.ca107g2.tibawe.campuszone;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;

import idv.ca107g2.tibawe.R;

public class AbsApplyActivity extends AppCompatActivity {
    public static ViewPager pager;
    private int mPagerPosition;
    private int mPagerOffsetPixels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abs_apply);

        // Init the swipe back
        SwipeBack.attach(this, Position.LEFT)
                .setSwipeBackView(R.layout.swipeback_toolbarandtab)
                .setOnInterceptMoveEventListener(
                        new SwipeBack.OnInterceptMoveEventListener() {
                            @Override
                            public boolean isViewDraggable(View v, int dx,
                                                           int x, int y) {
                                if (v == pager) {
                                    return !(mPagerPosition == 0 && mPagerOffsetPixels == 0)
                                            || dx < 0;
                                }

                                return false;
                            }
                        });

        Toolbar toolbar = findViewById(R.id.toolbar_abs_apply);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        AbsApplyPagerAdapter pagerAdapter = new AbsApplyPagerAdapter(getSupportFragmentManager());
        pager =  findViewById(R.id.vpAbsApply);
        pager.setOffscreenPageLimit(0);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mPagerPosition = position;
                mPagerOffsetPixels = positionOffsetPixels;}

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(0);

        TabLayout tabLayout = findViewById(R.id.tbAbsApply);
        tabLayout.setupWithViewPager(pager);



    }


    private class AbsApplyPagerAdapter extends FragmentStatePagerAdapter {

        public AbsApplyPagerAdapter (FragmentManager fm){
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new AbsApplyFragment();
                case 1:
                    return new AbsApplyQueryFragment();
            }
            return null;
        }

        public CharSequence getPageTitle(int position){
            switch (position){
                case 0:
                    return getResources().getText(R.string.absence_apply);
                case 1:
                    return getResources().getText(R.string.absence_record);
            }
            return null;
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }



    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.swipeback_stack_to_front,
                R.anim.swipeback_stack_right_out);
    }
}
