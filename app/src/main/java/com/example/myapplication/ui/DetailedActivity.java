package com.example.myapplication.ui;

import android.os.Build;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.fragments.IngredientFragment;
import com.example.myapplication.fragments.MethodFragment;
import com.example.myapplication.utils.CONSTANT;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailedActivity extends AppCompatActivity {

    private ConstraintLayout root;
    private ConstraintSet constraint1, constraint2;
    private boolean set = false;
    private String tag = this.getClass().getSimpleName();

    private CircleImageView profile_image;
    private TextView introTextView, nameTextView;
    private View view;
    private FloatingActionButton floatingActionButton;

    private int rec_id, rec_bgcolor;
    private String rec_name, rec_img, rec_ingredients, rec_steps, rec_cat, rec_subcat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        initView();

        getBundleData();

        setupAnimation();

        setupViewPager();

    }

    private void initView() {
        view = findViewById(R.id.view);
        profile_image = findViewById(R.id.profile_image);
        introTextView = findViewById(R.id.introTextView);
        nameTextView = findViewById(R.id.nameTextView);
        floatingActionButton = findViewById(R.id.floatingActionButton);
    }

    private void getBundleData() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            rec_id = bundle.getInt(CONSTANT.rec_id);
            rec_name = bundle.getString(CONSTANT.rec_name);
            rec_img = bundle.getString(CONSTANT.rec_img);
            rec_ingredients = bundle.getString(CONSTANT.rec_ingredients);
            rec_steps = bundle.getString(CONSTANT.rec_steps);
            rec_cat = bundle.getString(CONSTANT.rec_cat);
            rec_subcat = bundle.getString(CONSTANT.rec_subcat);
            rec_bgcolor = bundle.getInt(CONSTANT.rec_bgcolor);

            Log.d(tag, "rec_id = " + rec_id);
            Log.d(tag, "rec_name = " + rec_name);
            Log.d(tag, "rec_img = " + rec_img);
            Log.d(tag, "rec_ingredients = " + rec_ingredients);
            Log.d(tag, "rec_steps = " + rec_steps);
            Log.d(tag, "rec_cat = " + rec_cat);
            Log.d(tag, "rec_subcat = " + rec_subcat);
            Log.d(tag, "rec_bgcolor = " + rec_bgcolor);


            introTextView.setText(rec_ingredients);
            nameTextView.setText(rec_name);
            Glide.with(getApplicationContext()).load(rec_img).into(profile_image);
            view.setBackgroundColor(rec_bgcolor);

            //new loadBitmapFromUrl().execute();

        } else {
            Log.d(tag, "bundle is null");
        }

    }

    private void setupAnimation() {
        root = findViewById(R.id.root);

        constraint1 = new ConstraintSet();
        constraint1.clone(root);
        constraint2 = new ConstraintSet();
        constraint2.clone(this, R.layout.activity_detailed_alt);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                    Transition changeBound = new ChangeBounds();
                    changeBound.setInterpolator(new OvershootInterpolator());

                    TransitionManager.beginDelayedTransition(root, changeBound);

                    ConstraintSet constraint;

                    if (set) {
                        constraint = constraint1;
                        introTextView.setVisibility(View.VISIBLE);
                        //floatingActionButton.setBackgroundResource(R.drawable.ic_arrow_forward);
                    } else {
                        constraint = constraint2;
                        introTextView.setVisibility(View.GONE);
                        //floatingActionButton.setBackgroundResource(R.drawable.ic_arrow_back);
                    }

                    constraint.applyTo(root);
                    set = !set;
                } else {
                    Log.d(this.getClass().getSimpleName(), "animation not supported");
                }
            }
        });
    }

    private void setupViewPager() {

        ViewPager viewpager = findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new IngredientFragment(rec_ingredients), "Ingredient");
        adapter.addFragment(new MethodFragment(rec_steps), "Method");
        viewpager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setBackgroundColor(rec_bgcolor);

        tabLayout.setSelectedTabIndicator(R.color.colorWhite);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tabLayout.setTabTextColors(getResources().getColor(R.color.colorGray, null), getColor(R.color.colorWhite));
        }
        tabLayout.setupWithViewPager(viewpager);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}