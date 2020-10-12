package com.example.marketsimplified.view;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.marketsimplified.R;

public class DetailsActivity extends AppCompatActivity {

    TextView fragment_a,fragment_b,fragment_c;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkFragment((new FragmentA()));

        fragment_a = findViewById(R.id.fragment_a);
        fragment_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFragment((new FragmentA()));
            }
        });

        fragment_b = findViewById(R.id.fragment_b);
        fragment_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFragment((new FragmentB()));
            }
        });

        fragment_c = findViewById(R.id.fragment_c);
        fragment_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFragment((new FragmentC()));
            }
        });
    }

    public Fragment checkFragment(Fragment fragment) {
        Fragment currentFragment = this.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        FragmentTransaction transaction = null;
        if(fragment instanceof  FragmentA) {
            transaction = getSupportFragmentManager().beginTransaction();
            if(currentFragment != null)
                transaction.remove(currentFragment);
            transaction.add(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }else {
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        return fragment;
    }
}
