package com.klakier.proRobIntranet;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;
import com.annimon.stream.function.Predicate;
import com.klakier.proRobIntranet.api.call.DeleteTimesheetRowCall;
import com.klakier.proRobIntranet.api.call.GetTimesheetCall;
import com.klakier.proRobIntranet.api.call.InsertTimesheetRowCall;
import com.klakier.proRobIntranet.api.call.OnResponseListener;
import com.klakier.proRobIntranet.api.call.UpdateTimesheetCall;
import com.klakier.proRobIntranet.api.response.StandardResponse;
import com.klakier.proRobIntranet.api.response.TimesheetResponse;
import com.klakier.proRobIntranet.api.response.TimesheetRow;
import com.klakier.proRobIntranet.api.response.TimesheetRowInsertedResponse;
import com.klakier.proRobIntranet.api.response.UserDataShort;
import com.klakier.proRobIntranet.database.DBProRob;
import com.klakier.proRobIntranet.fragments.DelegationFragment;
import com.klakier.proRobIntranet.fragments.HolidaysFragment;
import com.klakier.proRobIntranet.fragments.HomeFragment;
import com.klakier.proRobIntranet.fragments.OnFragmentInteractionListener;
import com.klakier.proRobIntranet.fragments.SigninFragment;
import com.klakier.proRobIntranet.fragments.TeamFragment;
import com.klakier.proRobIntranet.fragments.WorkingTimeFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener {

    private static final String CHECKED_DRAWER_ITEM = "checkedDrawerItem";

    private static final String SIGNIN_FRAGMENT_TAG = "signinFragment";
    private static final String HOME_FRAGMENT_TAG = "homeFragment";
    private static final String WORKING_TIME_FRAGMENT_TAG = "workingTimeFragment";
    private static final String DELEGATION_FRAGMENT_TAG = "delegationFragment";
    private static final String HOLIDAYS_FRAGMENT_TAG = "holidaysFragment";
    private static final String TEAM_FRAGMENT_TAG = "teamFragment";
    private SigninFragment signinFragment;

    private FragmentManager fragmentManager;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private LinearLayout navigationHeader;
    private Menu navigationMenu;

    private int mCheckedItem;

    @Override
    public void onFragmentInteraction(String action) {
        Toast.makeText(this, "Action: " + action, Toast.LENGTH_SHORT).show();
        switch (action) {
            case SigninFragment.SIGN_IN_ACTION: {
                signIn();
                break;
            }
            case HomeFragment.LOGOUT_ACTION: {
                logOut();
                break;
            }
            default: {
                throw new IllegalArgumentException(action);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = getLayoutInflater().inflate(R.layout.activity_main, null, false);
        setContentView(v);

        findViews();

        setSupportActionBar(toolbar);


        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) {
            mCheckedItem = savedInstanceState.getInt(CHECKED_DRAWER_ITEM, 0);
        }

        if (new Token(this).hasToken()) {
            signIn();
        } else {
            logOut();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        MenuItem checkedItem = navigationView.getCheckedItem();
        int checkedItemId = checkedItem.getItemId();
        outState.putInt(CHECKED_DRAWER_ITEM, checkedItemId);

        super.onSaveInstanceState(outState);
    }

    private void findViews() {
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navigationHeader = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.nav_header_main, navigationView, false);
        navigationView.addHeaderView(navigationHeader);
        navigationMenu = navigationView.getMenu();

        toolbar = findViewById(R.id.toolbar);
    }

    public void setDrawerEnabled(boolean enabled) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(toolbar);
        }
        int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED :
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        drawer.setDrawerLockMode(lockMode);
        toggle.setDrawerIndicatorEnabled(enabled);
    }

    private View getToolbarTitle() {
        int childCount = toolbar.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = toolbar.getChildAt(i);
            if (child instanceof TextView) {
                return child;
            }
        }

        return new View(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void signIn() {
        Token token = new Token(this);
        DBProRob dbProRob = new DBProRob(this, null);
        if (!token.hasToken() || !dbProRob.hasUser()) logOut();

        syncDrawerWithDB();
        setDrawerEnabled(true);

        if (mCheckedItem != 0) {
            navigationView.setCheckedItem(mCheckedItem);
        } else {
            MenuItem menuItem = navigationMenu.findItem(R.id.nav_home);
            menuItem.setChecked(true);
            changeFragment(HOME_FRAGMENT_TAG, HomeFragment.class);
        }
    }

    public void logOut() {
        Token token = new Token(this);
        token.resetToken();

        DBProRob dbProRob = new DBProRob(this, null);
        dbProRob.resetUser();

        syncDrawerWithDB();
        setDrawerEnabled(false);

        signinFragment = (SigninFragment) fragmentManager.findFragmentByTag(SIGNIN_FRAGMENT_TAG);
        if (signinFragment == null) {
            signinFragment = new SigninFragment();
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container, signinFragment, SIGNIN_FRAGMENT_TAG);
        fragmentTransaction.commit();
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
        final int id = item.getItemId();

        switch (id) {
            case R.id.action_settings: {
                Token token = new Token(this);
                if (token.hasToken())
                    Toast.makeText(this, "id: " + token.getId() + " role: " + token.getRole(), Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(this, "Token not set", Toast.LENGTH_LONG).show();
                return true;
            }

            case R.id.action_test: {

                DBProRob dbProRob = new DBProRob(this, null);
                UserDataShort user = dbProRob.getUser();
                if (user != null)
                    Toast.makeText(this, "name: " + user.getFirstName() + " " + user.getLastName(), Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(this, "User not set", Toast.LENGTH_LONG).show();
                return true;
            }

            case R.id.action_test1: {
                GetTimesheetCall getTimesheetCall = new GetTimesheetCall(getApplicationContext(), new Token(getApplicationContext()));
                getTimesheetCall.enqueue(new OnResponseListener() {
                    @Override
                    public void onSuccess(StandardResponse response) {
                        TimesheetResponse timesheetResponse = (TimesheetResponse) response;
                        List<TimesheetRow> ltsr = timesheetResponse.getData();
                        DBProRob dbProRob = new DBProRob(getApplicationContext(), null);
                        List<TimesheetRow> filtredLtsr = dbProRob.filterTimesheetRows(ltsr);
                        dbProRob.writeTimesheet(filtredLtsr);
                        Toast.makeText(getApplicationContext(), "zaimportowano z bazy", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(StandardResponse response) {
                        Toast.makeText(getApplicationContext(), "call failed", Toast.LENGTH_LONG).show();
                    }
                });
                return true;
            }

            case R.id.action_test2: {
                final String TAG_INSERT = "InsertingToExtDB";
                Log.d(TAG_INSERT, "start inserting");
                final DBProRob dbProRob = new DBProRob(getApplicationContext(), null);
                List<TimesheetRow> listToSync = dbProRob.readTimesheet(String.valueOf(0), true);
                for (final TimesheetRow tsr : listToSync) {
                    final InsertTimesheetRowCall insertTimesheetRowCall =
                            new InsertTimesheetRowCall(
                                    getApplicationContext(),
                                    new Token(getApplicationContext()),
                                    tsr);
                    insertTimesheetRowCall.enqueue(new OnResponseListener() {
                        @Override
                        public void onSuccess(StandardResponse response) {
                            TimesheetRowInsertedResponse timesheetRowInsertedResponse = (TimesheetRowInsertedResponse) response;
                            tsr.setIdExternal(timesheetRowInsertedResponse.getId());
                            dbProRob.updateTimesheetRow(tsr, String.valueOf(tsr.getIdLocal()));
                            Log.d(TAG_INSERT, "success insert local ID:" + tsr.getIdLocal() + " externarl ID:" + tsr.getIdExternal());
                        }

                        @Override
                        public void onFailure(StandardResponse response) {
                            Log.d(TAG_INSERT, "failed  insert local ID:" + tsr.getIdLocal() + " " + response.getMessage());
                        }
                    });
                }
                Log.d(TAG_INSERT, "stop inserting");
                return true;
            }

            case R.id.action_test3: {
                final String TAG_DELETE = "DeleteFromExtDB";
                Log.d(TAG_DELETE, "start deleting");
                final DBProRob dbProRob = new DBProRob(getApplicationContext(), null);
                List<TimesheetRow> listToDel = dbProRob.readTimesheetMarkedDelete();
                for (final TimesheetRow tsr : listToDel) {
                    DeleteTimesheetRowCall deleteTimesheetRowCall =
                            new DeleteTimesheetRowCall(
                                    getApplicationContext(),
                                    new Token(getApplicationContext()),
                                    tsr.getIdExternal() * -1
                            );
                    deleteTimesheetRowCall.enqueue(new OnResponseListener() {
                        @Override
                        public void onSuccess(StandardResponse response) {
                            dbProRob.deleteTimesheetRow(tsr.getIdLocal());
                            Log.d(TAG_DELETE, "success delete local ID:" + tsr.getIdLocal() + " external ID:" + tsr.getIdExternal());
                        }

                        @Override
                        public void onFailure(StandardResponse response) {
                            Log.d(TAG_DELETE, "!failed delete local ID:" + tsr.getIdLocal() + " external ID:" + tsr.getIdExternal()
                                    + " error:" + response.getError().toString() + " message:" + response.getMessage());
                        }
                    });
                }
                Log.d(TAG_DELETE, "stop deleting");
                return true;
            }

            case R.id.action_test4: {
                final String TAG_DELETE = "DeleteFromLocDB";
                Log.d(TAG_DELETE, "start deleting from locDB");
                final DBProRob dbProRob = new DBProRob(getApplicationContext(), null);
                GetTimesheetCall getTimesheetCall = new GetTimesheetCall(getApplicationContext(), new Token(getApplicationContext()));
                getTimesheetCall.enqueue(new OnResponseListener() {
                    @Override
                    public void onSuccess(StandardResponse response) {
                        TimesheetResponse timesheetResponse = (TimesheetResponse) response;

                        final List<TimesheetRow> tsrExtDb = timesheetResponse.getData();
                        final List<TimesheetRow> tsrLocDb = dbProRob.readTimesheet();

                        Predicate<TimesheetRow> predicateNotInExtDb = new Predicate<TimesheetRow>() {
                            @Override
                            public boolean test(final TimesheetRow s) {
                                return !Stream.of(tsrExtDb).anyMatch(new Predicate<TimesheetRow>() {
                                    @Override
                                    public boolean test(TimesheetRow t) {
                                        return s.getIdExternal().equals(t.getIdExternal());
                                    }
                                });
                            }
                        };

                        List<TimesheetRow> notInExtDb = Stream.of(tsrLocDb).filter(predicateNotInExtDb).toList();

                        Stream.of(notInExtDb).forEach(new Consumer<TimesheetRow>() {
                            @Override
                            public void accept(TimesheetRow q) {
                                Log.d(TAG_DELETE, "Timesheet row local id:" + q.getIdLocal() + " will be deleted");
                            }
                        });

                        int deletedSize = dbProRob.deleteTimesheetRows(notInExtDb);
                        Log.d(TAG_DELETE, "on list was:" + notInExtDb.size() + " and " + deletedSize + " is deleted ");
                    }

                    @Override
                    public void onFailure(StandardResponse response) {
                        Log.d(TAG_DELETE, "failed to get timesheet,"
                                + " error:" + response.getError().toString() + " message:" + response.getMessage());
                    }

                });
                return true;
            }
            case R.id.action_test5: {
                final String TAG_UPDATES = "UpdatesLocExt";
                Log.d(TAG_UPDATES, "Start checking updates");
                final DBProRob dbProRob = new DBProRob(getApplicationContext(), null);
                GetTimesheetCall getTimesheetCall = new GetTimesheetCall(getApplicationContext(), new Token(getApplicationContext()));
                getTimesheetCall.enqueue(new OnResponseListener() {
                    @Override
                    public void onSuccess(StandardResponse response) {
                        TimesheetResponse timesheetResponse = (TimesheetResponse) response;

                        final List<TimesheetRow> tsrExtDb = timesheetResponse.getData();
                        final List<TimesheetRow> tsrLocDb = dbProRob.readTimesheet();

                        if (tsrExtDb.size() != tsrLocDb.size()) {
                            Log.d(TAG_UPDATES, "Different size of Db");
                            return;
                        }

                        final List<TimesheetRow> newerExt = new ArrayList<TimesheetRow>();
                        final List<TimesheetRow> newerLoc = new ArrayList<TimesheetRow>();
                        final List<TimesheetRow> equals = new ArrayList<TimesheetRow>();
                        final List<TimesheetRow> notequals = new ArrayList<>();

                        Stream.of(tsrLocDb).forEach(new Consumer<TimesheetRow>() {
                            @Override
                            public void accept(final TimesheetRow l) {
                                TimesheetRow e = Stream.of(tsrExtDb).filter(new Predicate<TimesheetRow>() {
                                    @Override
                                    public boolean test(TimesheetRow t) {
                                        return l.getIdExternal().equals(t.getIdExternal());
                                    }
                                }).findFirst()
                                        .get();

                                int compare = l.getUpdatedAt().compareTo(e.getUpdatedAt());

                                if (compare == 0) {
                                    if (l.hashCode() == e.hashCode())
                                        equals.add(l);
                                    else
                                        notequals.add(l);
                                } else if (compare < 0) {
                                    e.setIdLocal(l.getIdLocal());
                                    newerExt.add(e);
                                } else {
                                    newerLoc.add(l);
                                }
                            }
                        });
                        Log.d(TAG_UPDATES, "****** COMPARE RESULTS ******");
                        Log.d(TAG_UPDATES, "Newer in LocDB: " + newerLoc.size());
                        Log.d(TAG_UPDATES, "Newer in ExtDB: " + newerExt.size());
                        Log.d(TAG_UPDATES, "Equals timestamps: " + equals.size());
                        Log.d(TAG_UPDATES, "Equals timestamps, different hashes: " + notequals.size());
                        Log.d(TAG_UPDATES, "*****************************");

                        //update Local
                        final int[] rowsUpdatedLocal = {0};
                        for (TimesheetRow tsr : newerExt) {
                            rowsUpdatedLocal[0] += dbProRob.updateTimesheetRow(tsr, String.valueOf(tsr.getIdLocal()));
                        }
                        Log.d(TAG_UPDATES, "Updated " + rowsUpdatedLocal[0] + " in local DB");

                        //update External
                        for (final TimesheetRow tsr : newerLoc) {
                            UpdateTimesheetCall updateTimesheetCall = new UpdateTimesheetCall(getApplicationContext(), new Token(getApplicationContext()), tsr);
                            updateTimesheetCall.enqueue(new OnResponseListener() {
                                @Override
                                public void onSuccess(StandardResponse response) {
                                    Log.d(TAG_UPDATES, "success update local ID:" + tsr.getIdLocal() + " external ID:" + tsr.getIdExternal());
                                }

                                @Override
                                public void onFailure(StandardResponse response) {
                                    Log.d(TAG_UPDATES, "!failed update local ID:" + tsr.getIdLocal() + " external ID:" + tsr.getIdExternal()
                                            + " error:" + response.getError().toString() + " message:" + response.getMessage());
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(StandardResponse response) {
                        Log.d(TAG_UPDATES, "failed to get timesheet,"
                                + " error:" + response.getError().toString() + " message:" + response.getMessage());
                    }
                });

                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            changeFragment(HOME_FRAGMENT_TAG, HomeFragment.class);
        } else if (id == R.id.nav_working_time) {
            changeFragment(WORKING_TIME_FRAGMENT_TAG, WorkingTimeFragment.class);
        } else if (id == R.id.nav_delegations) {
            changeFragment(DELEGATION_FRAGMENT_TAG, DelegationFragment.class);
        } else if (id == R.id.nav_holidays) {
            changeFragment(HOLIDAYS_FRAGMENT_TAG, HolidaysFragment.class);
        } else if (id == R.id.nav_team) {
            changeFragment(TEAM_FRAGMENT_TAG, TeamFragment.class);
        } else if (id == R.id.nav_logout) {
            logOut();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void syncDrawerWithDB() {
        TextView textViewNavUserEmail = navigationHeader.findViewById(R.id.textViewNavBarUserEmail);
        TextView textViewNavUserName = navigationHeader.findViewById(R.id.textViewNavBarUserName);
        ImageView imageViewNavUserAvatar = navigationHeader.findViewById(R.id.imageViewNavBarAvatar);

        DBProRob dbProRob = new DBProRob(this, null);

        if (dbProRob.hasUser()) {
            UserDataShort user = dbProRob.getUser();
            textViewNavUserEmail.setText(user.getEmail());
            String fullName = user.getFirstName() + " " + user.getLastName();
            textViewNavUserName.setText(fullName);
        } else {
            textViewNavUserEmail.setText("");
            textViewNavUserName.setText("");
            //imageViewNavUserAvatar.setImageResource(R.drawable.missing);
        }
    }

    public void changeFragment(String tag, Class c) {
        try {
            Fragment fragment = fragmentManager.findFragmentByTag(tag);
            if (fragment == null) {
                fragment = (Fragment) c.newInstance();
            }

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_container, fragment, tag);
            fragmentTransaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
