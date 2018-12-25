package com.example.mechatronicse.projetocapstoneparte2.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import com.example.mechatronicse.projetocapstoneparte2.R;
import com.example.mechatronicse.projetocapstoneparte2.constants.Constants;
import com.example.mechatronicse.projetocapstoneparte2.fragments.CadastrarPetFragment;
import com.example.mechatronicse.projetocapstoneparte2.fragments.MeusAnunciosFragment;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.Alpha;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.NUMBER_OFFSET;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.Y;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.ZERO;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.alphaNumber;

public class MinhaDashboardActivity extends AppCompatActivity {
    Fragment fragment = null;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new MeusAnunciosFragment();
                    break;
                case R.id.navigation_dashboard:
                    fragment = new CadastrarPetFragment();
                    break;

            }
            return loadFragment(fragment);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_dashboard);

        Toolbar toolbar =   findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Configura BottomNavigation
        BottomNavigationView navigation =  findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        animateViewsIn();
        //loading the default fragment
        loadFragment(new MeusAnunciosFragment());
    }

    //effect de pagina
    private void animateViewsIn() {

        ViewGroup root = (ViewGroup) findViewById(R.id.navigation);
        int count = root.getChildCount();
        float offset = getResources().getDimensionPixelSize(R.dimen.offset_y);
        Interpolator interpolator = AnimationUtils.loadInterpolator(this, android.R.interpolator.linear_out_slow_in);

        for (int i = ZERO; i < count; i++) {
            View view = root.getChildAt(i);

            view.setTranslationY(offset);
            view.setAlpha(Alpha);
            view.animate()
                    .translationY(Y)
                    .alpha(alphaNumber)
                    .setInterpolator(interpolator)
                    .setDuration(Constants.DURATION)
                    .start();
            offset *= NUMBER_OFFSET;
        }
    }


    private boolean loadFragment(Fragment fragment) {

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}
