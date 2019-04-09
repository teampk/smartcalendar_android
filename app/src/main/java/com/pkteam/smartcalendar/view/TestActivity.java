package com.pkteam.smartcalendar.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;

import com.pkteam.smartcalendar.R;

public class TestActivity extends AppCompatActivity {

    private boolean show = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.test_const);

        ImageView backgroundImage = findViewById(R.id.backgroundImage);
        backgroundImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(show)
                    hideComponents(); // if the animation is shown, we hide back the views
                else
                    showComponents(); // if the animation is NOT shown, we animate the views
            }
        });




    }

    private void showComponents(){
        show = true;

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this, R.layout.test_const_detail);

        ChangeBounds transition = new ChangeBounds();
        transition.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        transition.setDuration(1200);
        ConstraintLayout constraint = findViewById(R.id.constraint);
        TransitionManager.beginDelayedTransition(constraint, transition);
        constraintSet.applyTo(constraint);
        //here constraint is the name of view to which we are applying the constraintSet
    }

    private void hideComponents(){
        show = false;

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this, R.layout.test_const);

        ChangeBounds transition = new ChangeBounds();
        transition.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        transition.setDuration(1200);
        ConstraintLayout constraint = findViewById(R.id.constraint);

        TransitionManager.beginDelayedTransition(constraint, transition);
        constraintSet.applyTo(constraint); //here constraint is the name of view to which we are applying the constraintSet
    }

}
