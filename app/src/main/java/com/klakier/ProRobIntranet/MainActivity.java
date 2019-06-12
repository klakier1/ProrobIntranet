package com.klakier.ProRobIntranet;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String SIGNIN_FRAGMENT_TAG = "signinFragment";
    private static final String SIGNEDIN_FRAGMENT_TAG = "signedinFragment";
    private static final String SUBSCRIBERS_FRAGMENT_TAG = "subscribersFragment";
    private SigninFragment signinFragment;
    private SubscribersFragment subscribersFragment;
    private SignedinFragment signedinFragment;

    private FragmentManager fragmentManager;
    //private Layout mainContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(view, "Jeszcze nie wymyśliłem do czego to sie przyda", Snackbar.LENGTH_LONG)
                        .setAction("Action", null);

                // Change the text message color
                View mySbView = snackbar.getView();
                TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);

                // We can apply the property of TextView
                textView.setTextColor(getResources().getColor(R.color.secondaryTextColor));

                snackbar.show();

            }
        });

        initVars();

        fragmentManager = getSupportFragmentManager();

        if (new Token(this).hasToken()) {
            signIn();
        } else {
            logOut();
        }

/*
        signinFragment = (SigninFragment) fragmentManager.findFragmentByTag(SIGNIN_FRAGMENT_TAG);
        if (signinFragment == null) {
            signinFragment = new SigninFragment();
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_container, signinFragment, SIGNIN_FRAGMENT_TAG);
        fragmentTransaction.commit();
*/

    }

    public void signIn() {
        Token token = new Token(this);
        if (!token.hasToken()) return;

        signedinFragment = (SignedinFragment) fragmentManager.findFragmentByTag(SIGNEDIN_FRAGMENT_TAG);
        if (signedinFragment == null) {
            signedinFragment = new SignedinFragment();
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container, signedinFragment, SIGNEDIN_FRAGMENT_TAG);
        fragmentTransaction.commit();

    }

    public void logOut() {
        Token token = new Token(this);
        token.resetToken();

        signinFragment = (SigninFragment) fragmentManager.findFragmentByTag(SIGNIN_FRAGMENT_TAG);
        if (signinFragment == null) {
            signinFragment = new SigninFragment();
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container, signinFragment, SIGNIN_FRAGMENT_TAG);
        fragmentTransaction.commit();

    }


    private void initVars() {

        //mainContainer = findViewById(R.id.main_container);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Token token = new Token(this);
            if (token.hasToken())
                Toast.makeText(this, "id: " + token.getId() + " role: " + token.getRole(), Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "Token not set", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
