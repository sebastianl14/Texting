package com.example.texting.mainModule.view;

import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.texting.R;
import com.example.texting.addModule.view.AddFragment;
import com.example.texting.common.pojo.User;
import com.example.texting.common.utils.UtilsCommon;
import com.example.texting.loginModule.view.LoginActivity;
import com.example.texting.mainModule.MainPresenter;
import com.example.texting.mainModule.MainPresenterClass;
import com.example.texting.mainModule.view.adapters.OnItemClickListener;
import com.example.texting.mainModule.view.adapters.RequestAdapter;
import com.example.texting.mainModule.view.adapters.UserAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements OnItemClickListener, MainView {

    @BindView(R.id.imgProfile)
    CircleImageView imgProfile;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rvRequest)
    RecyclerView rvRequest;
    @BindView(R.id.rvUsers)
    RecyclerView rvUsers;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.contentMain)
    CoordinatorLayout contentMain;

    private UserAdapter userAdapter;
    private RequestAdapter requestAdapter;

    private MainPresenter presenter;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        Toolbar toolbar = findViewById(R.id.toolbar);

        presenter = new MainPresenterClass(this);
        presenter.onCreate();
        user = presenter.getCurrentUser();

        configToolbar();
        configAdapter();
        configRecyclerView();
//
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void configToolbar() {
        toolbar.setTitle(user.getUserNameValid());
        UtilsCommon.loadImage(this, user.getPhotoUrl(), imgProfile);
        setSupportActionBar(toolbar);
    }

    private void configAdapter() {
        userAdapter = new UserAdapter(new ArrayList<>(), this);
        requestAdapter = new RequestAdapter(new ArrayList<>(), this);
    }

    private void configRecyclerView() {
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        rvUsers.setAdapter(userAdapter);

        rvRequest.setLayoutManager(new LinearLayoutManager(this));
        rvRequest.setAdapter(requestAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                presenter.signOff();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.action_profile:

                break;
            case R.id.action_about:
                openAbout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openAbout() {
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_about, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.DialogFragmentTheme)
                .setTitle(R.string.main_menu_about)
                .setView(view)
                .setPositiveButton(R.string.common_label_ok, null)
                .setNeutralButton(R.string.about_privacy, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/sebastianl14"));
                        startActivity(intent);
                    }
                });

        builder.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
        clearNotifications();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    private void clearNotifications() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
    }

    @OnClick(R.id.fab)
    public void onAddClicked() {
        new AddFragment().show(getSupportFragmentManager(), getString(R.string.addFriend_title));
    }

    /*
     *  Main View
     * */
    @Override
    public void friendAdded(User user) {
        userAdapter.add(user);
    }

    @Override
    public void friendUpdated(User user) {
        userAdapter.update(user);
    }

    @Override
    public void friendRemoved(User user) {
        userAdapter.remove(user);

    }

    @Override
    public void requestAdded(User user) {
        requestAdapter.add(user);
    }

    @Override
    public void requestUpdated(User user) {
        requestAdapter.update(user);
    }

    @Override
    public void requestRemoved(User user) {
        requestAdapter.remove(user);
    }

    @Override
    public void showRequestAccepted(String userName) {
        Snackbar.make(contentMain, getString(R.string.main_message_request_accepted, userName),
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showRequestDenied() {
        Snackbar.make(contentMain, R.string.main_message_request_denied, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showFriendRemoved() {
        Snackbar.make(contentMain, R.string.main_message_user_removed, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showError(int rsgMsg) {
        Snackbar.make(contentMain, rsgMsg, Snackbar.LENGTH_LONG).show();
    }

    /*
     *  OnItemClickListener
     * */
    @Override
    public void onItemClick(User user) {

    }

    @Override
    public void onItemLongClick(User user) {
        new AlertDialog.Builder(this, R.style.DialogFragmentTheme)
                .setTitle(R.string.main_dialog_title_confirmDelete)
                .setMessage(String.format(Locale.ROOT, getString(R.string.main_dialog_message_confirmDelete),
                        user.getUserNameValid()))
                .setPositiveButton(R.string.main_dialog_accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.removeFriend(user.getUid());
                    }
                })
                .setNegativeButton(R.string.common_label_cancel, null)
                .show();
    }

    @Override
    public void onAcceptRequest(User user) {
        presenter.acceptRequest(user);
    }

    @Override
    public void onDenyRequest(User user) {
        presenter.denyRequest(user);
    }

}
