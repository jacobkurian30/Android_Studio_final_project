package com.example.jacob.finalproject;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Logger;


public class fragmentPrice extends Fragment {
    Logger log = Logger.getAnonymousLogger();
    View trialView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public fragmentPrice() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static fragmentPrice newInstance(String param1, String param2) {
        fragmentPrice fragment = new fragmentPrice();
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
        View v = inflater.inflate(R.layout.fragment_fragment_price, container, false);
        trialView = v;
        Thread t = new Thread(){
            public void run() {
                try {
                    URL url = new URL("http://btc.blockr.io/api/v1/coin/info");
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream());
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    url.openStream()));

                    String tmpString = "";
                    String response = "";
                    int num = 0;
                    while (tmpString != null) {
                        response.concat(tmpString);
                        response = response + tmpString;
                        tmpString = reader.readLine();
                    }

                    Message msg = Message.obtain();
                    msg.obj = response;

                    Log.d("downloaded data", response);
                    responseHandler.sendMessage(msg);
                } catch (Exception e) {
                    log.info("EROOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOORRRRRRRRRRRRRRRRRRRRRRRRRRRRR " + e);

                }
            }
        };
        t.start();


        return v;
    }
    Handler responseHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            try {
                JSONObject coinObject = new JSONObject((String) msg. obj);
                String coinName;
                double coinPrice;
                JSONObject data = coinObject.getJSONObject("data");
                JSONObject coin = data.getJSONObject("coin");
                coinName = coin.getString("name");
               // ((TextView) trialView.findViewById(R.id.coinName)).setText(coinName);
                log.info("coinName: " + coinName);
                coinPrice = data.getJSONObject("markets").getJSONObject("coinbase").getDouble("value");
                ((TextView) trialView.findViewById(R.id.priceText)).setText(String.valueOf(coinPrice));
                //Log.d("coinPrice: %d" + coinPrice, "");
                log.info("coinPrice: " + coinPrice);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
    });

}
