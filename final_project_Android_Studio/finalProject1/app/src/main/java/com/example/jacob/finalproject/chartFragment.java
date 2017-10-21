package com.example.jacob.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;


public class chartFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    URL url;
    Thread t2;

    boolean threadValidity;
    //    boolean oneDay = false;
    //  boolean fiveDays = false;
    ImageView imageView;
    View trialView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String mainUrl;
    public chartFragment() {
    }


    // TODO: Rename and change types and number of parameters
    public static chartFragment newInstance(String param1, String param2) {
        chartFragment fragment = new chartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_chart, container, false);
        trialView = v;
        //  receiverThread.start();
        threadValidity = true;
        //final  Context c = getApplicationContext();
        final Activity activity = getActivity();
        final RadioButton button1 = (RadioButton) v.findViewById(R.id.radioButton1);
        final RadioButton button2 = (RadioButton) v.findViewById(R.id.radioButton2);
        final Button goButton = (Button) v.findViewById(R.id.goButton);
        //v.findViewById(R.id.serach_bar).invalidate();
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(button1.isChecked()){
//                               Toast.makeText(activity, "Selected 1 day", Toast.LENGTH_SHORT).show();
                    try {

                        url= new URL("http://chart.yahoo.com/z?s=BTCUSD=X&t=1d");
                        Toast.makeText(activity, "Selected 1 days", Toast.LENGTH_SHORT).show();

                        //   Toast.makeText(activity,msg.obj.toString(), Toast.LENGTH_SHORT).show();
                        imageView = (ImageView) trialView.findViewById(R.id.imageView);
                        Picasso.with(getActivity()).load(url.toString()).into(imageView);
                        mainUrl = "http://chart.yahoo.com/z?s=BTCUSD=X&t=1d";

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    System.out.println("=---------------------------------------------------------------------");
                }
                else{
                    try {

                        url= new URL("http://chart.yahoo.com/z?s=BTCUSD=X&t=5d");
                        Toast.makeText(activity, "Selected 5 days", Toast.LENGTH_SHORT).show();

                        //   Toast.makeText(activity,msg.obj.toString(), Toast.LENGTH_SHORT).show();
                        imageView = (ImageView) trialView.findViewById(R.id.imageView);
                        Picasso.with(getActivity()).load(url.toString()).into(imageView);

                        Message msg = Message.obtain();
                 //       msg.obj = url;
                        //receiverHandler.sendMessage(msg);
                        mainUrl = "http://chart.yahoo.com/z?s=BTCUSD=X&t=5d";
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                }


            }
        });
         t2 = new Thread() {
            @Override
            public void run() {
                super.run();
                while(threadValidity == true) {
                    try {
                        Thread.sleep(15000);
                        System.out.println("--------------------------" + "Handler");

                        Message msg = Message.obtain();
                        receiverHandler.sendEmptyMessage(1);

                        if(threadValidity == false)
                        {
                            Thread.interrupted();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t2.start();

        return v;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        threadValidity = false;
    }

    Handler receiverHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(mainUrl != null) {
                System.out.println("Inside the handler ----------------" + mainUrl);
                try {
                    url = new URL(mainUrl);
                    // Toast.makeText(activity, "Selected 5 days", Toast.LENGTH_SHORT).show();
                     Toast.makeText(getActivity().getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();

                    //   Toast.makeText(activity,msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    imageView = (ImageView) trialView.findViewById(R.id.imageView);
                    Picasso.with(getActivity()).load(url.toString()).into(imageView);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            return false;
        }

    });
}

