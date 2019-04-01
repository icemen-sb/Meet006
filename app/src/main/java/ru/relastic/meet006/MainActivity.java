package ru.relastic.meet006;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements FragmentInfo.onUserEvents{
    private static final String SERVICE_STARTED = "service started";
    private BroadcastReceiver mReceiver;
    private IntentFilter mIntentFilter;

    private EditText mEditText;
    private Fragment fragment_1, fragment_2;
    private int container;
    FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!(savedInstanceState != null && savedInstanceState.getBoolean(SERVICE_STARTED))) {
            startService(MyService.newIntent(this));
            Log.v("LOG:", "SERVICE MyService: Started");
        }
        initViews();
        initListeners();
        init();
    }
    private void initViews() {
    }

    private void initListeners() {
    }

    private void init( ) {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                hasStatusChangeByBroadcast(intent.getStringExtra(MyService.MSG_SERVICE_RNDVALUE));
            }
        };
        mIntentFilter = new IntentFilter("Meet006");

        fragment_1 = getSupportFragmentManager().findFragmentById(R.id.fr_one);
        fragment_2 = getSupportFragmentManager().findFragmentById(R.id.fr_two);

        container = R.id.fr_info;

        mEditText = (EditText)fragment_1.getActivity().findViewById(R.id.editText);

        manager = getSupportFragmentManager();

    }
    private void hasStatusChangeByBroadcast(String newStatus){
        mEditText.setText(newStatus);
    }
    protected void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();
    }
    @Override
    protected void onResume() {
        registerReceiver(mReceiver, mIntentFilter, null, null);
        super.onResume();
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(SERVICE_STARTED,true);
        //savedInstanceState.putString("mTextView",mTextView.getText().toString());
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        //mTextView.setText(savedInstanceState.getString("mTextView"));
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onTimeout(Fragment fragment) {
        manager.beginTransaction()
                .remove(fragment)
                .commit();
    }

    @Override
    public void onViewInfo() {
        Bundle bundle = new Bundle();
        bundle.putString(FragmentInfo.FRAGMENT_INFO_KEY,mEditText.getText().toString());
        Fragment fragment = FragmentInfo.newInstance(bundle);
        manager.beginTransaction()
                .replace(container,fragment)
                .commit();
    }
}
