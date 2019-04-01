package ru.relastic.meet006;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

public class FragmentInfo extends Fragment {
    private static onUserEvents activity;
    public final static int THE_VIEW_TIMEOUT=5000;
    public final static String FRAGMENT_INFO_KEY="fragment_info_key";
    private volatile boolean interrupted = false;

    public static FragmentInfo newInstance() {
        FragmentInfo fragment = new FragmentInfo();
        return fragment;
    }
    public static FragmentInfo newInstance(Bundle bundle) {
        FragmentInfo fragment = new FragmentInfo();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView mTextView = view.findViewById(R.id.textView);
        mTextView.setText(this.getArguments().getString(FRAGMENT_INFO_KEY));
    }



    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            activity = (onUserEvents)context;
        }catch (ClassCastException cce) {
            throw new ClassCastException(context.toString()+ " must implement activity onUserEvents interface");
        }
        Thread t = new Thread() {
            @Override
            public void run(){
                try {
                    Thread.sleep(THE_VIEW_TIMEOUT);
                    FragmentInfo.this.interrupt();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public void interrupt(){
        if (!interrupted) {
            activity.onTimeout(FragmentInfo.this);
        }
        interrupted=true;
    }


    public interface onUserEvents {
        public void onTimeout(Fragment fragment);
        public void onViewInfo();
    }
}
