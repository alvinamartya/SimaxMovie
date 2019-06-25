package com.example.moviedicoding.Activity;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.example.moviedicoding.R;

public class SplashScreenActivity extends AppCompatActivity {

    private Thread splashTread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        StartAnimations();
    }

    private void intentToHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.splash_alpha_animation);
        Animation animBeta = AnimationUtils.loadAnimation(this, R.anim.splash_alpha_animation);
        anim.reset();
        animBeta.reset();

        ConstraintLayout splashLayout = findViewById(R.id.clSplash);
        ImageView imgLogo = findViewById(R.id.imgLogo);

        imgLogo.setVisibility(View.VISIBLE);

        splashLayout.clearAnimation();
        splashLayout.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.splash_translate_animation);
        anim.reset();


        imgLogo.clearAnimation();
        imgLogo.startAnimation(anim);

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 1800) {
                        sleep(100);
                        waited += 100;
                    }

                    intentToHome();

                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    //refreshActivity();
                    //finish();
                }

            }
        };
        splashTread.start();

    }
}
