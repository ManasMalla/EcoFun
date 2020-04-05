package com.manasmalla.ecofun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class InfoActivity extends AppCompatActivity {

    ImageView btnNext;
    private ViewPager viewPager;
    private MyViewPagerAdapterInfoActivity myViewPagerAdapterInfo;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private MaterialButton btnSkip;
    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                btnSkip.setVisibility(View.GONE);
            } else {
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_info);

        viewPager = (ViewPager) findViewById(R.id.view_pager_infoActivity);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots_infoActivity);
        btnSkip = findViewById(R.id.btn_skip_infoActivity);
        btnNext = findViewById(R.id.btn_next_infoActivity);


        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.welcome_slide_problem,
                R.layout.welcome_slide_green_points,
                R.layout.welcome_slide_drive_less,
                R.layout.welcome_slide_ecofriendly,
                R.layout.welcome_slide_plant_trees,
                R.layout.welcome_slide_ocean,
                R.layout.welcome_slide_avengers_assemble};

        // adding bottom dots
        addBottomDots(0);

        myViewPagerAdapterInfo = new MyViewPagerAdapterInfoActivity();
        viewPager.setAdapter(myViewPagerAdapterInfo);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });

    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        Intent intent = new Intent(InfoActivity.this, InfoActivityScreen.class);
        intent.putExtra("presentUserContinent", getIntent().getStringExtra("presentUserContinent"));
        intent.putExtra("userLevel", getIntent().getStringExtra("userLevel"));
        startActivity(intent);

        finish();
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapterInfoActivity extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapterInfoActivity() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }


}