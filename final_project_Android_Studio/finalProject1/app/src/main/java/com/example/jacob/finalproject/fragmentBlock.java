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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;


public class fragmentBlock extends Fragment {
    Logger log = Logger.getAnonymousLogger();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static String getPrevBlock;
    static String getNextBlock;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String weblink;
    View v;
    public fragmentBlock() {
        // Required empty public constructor
    }


    public static fragmentBlock newInstance(String param1, String param2) {
        fragmentBlock fragment = new fragmentBlock();
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
         v =  inflater.inflate(R.layout.fragment_fragment_block, container, false);
        Button blockButton = (Button) v.findViewById(R.id.detailGenerate);
        Button prveButton = (Button) v.findViewById(R.id.prevButton);
        Button nextButton = (Button) v.findViewById(R.id.NextButton);

        final EditText blockNumber = (EditText) v.findViewById(R.id.blockNumber);
        blockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(){
                    public void run() {
                        try {
                            String link = "http://btc.blockr.io/api/v1/block/info/";
                            String numExten = blockNumber.getText().toString();
                            link = link.concat(numExten);
                            System.out.println("Get Link !!!!!!!!!!!!!!!!!!!!!~~~~~~~~~~~~~~~~~~~" + link  + "Nim exter: " + numExten);
                            URL url = new URL(link);
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
            }
        });

        prveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getPrevBlock != null) {
                    weblink = "http://btc.blockr.io/api/v1/block/info/";
                    weblink = weblink.concat(getPrevBlock.toString());
                    System.out.println("Inside Previous function: ~~~~~~~~~~~" + getPrevBlock);
                    System.out.println("Inside Previous url: ~~~~~~~~~~~" + weblink);

                    Thread t3 = new Thread() {

                        public void run() {

                            try {

                                function(weblink);

                            } catch (Exception e) {
                                System.out.println(e);
                            }

                        }
                    };
                    t3.start();
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weblink = "http://btc.blockr.io/api/v1/block/info/";
               if(getNextBlock != null) {

                   weblink = weblink.concat(getNextBlock.toString());

                   Thread t4 = new Thread() {

                       public void run() {
                           try {

                               function(weblink);
                           } catch (Exception e) {
                               System.out.println(e);
                           }
                       }
                   };

                   t4.start();
               }
            }
        });


        return v;
    }


    Handler responseHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            try {
                JSONObject coinObject = new JSONObject((String) msg.obj);
                //if don't know what type of data to get, use get()
                int nextBlockNum, prevBlockNum, currentBlockNum;
                String getString;
                String status = coinObject.getString("status");
                System.out.println("Status : !!!!!!!!!!!!!!!!!!!  : " + status);

                JSONObject data = coinObject.getJSONObject("data");

               // JSONObject coin = data.getJSONObject("data");
                getString = coinObject.getJSONObject("data").getString("hash");
                ((TextView) v.findViewById(R.id.currentHashBlockNum)).setText(getString.toString());
                currentBlockNum = data.getInt("nb");
                System.out.println("Number: ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + currentBlockNum);
                String str = Integer.toString(currentBlockNum);
                ((TextView) v.findViewById(R.id.currentBlock)).setText(str.toString());
                System.out.println("Current Block: " + str);
                currentBlockNum = coinObject.getJSONObject("data").getInt("prev_block_nb");
                str = Integer.toString(currentBlockNum);
                ((TextView) v.findViewById(R.id.preBlockDetail)).setText(str.toString());
                System.out.println("Previous Block : ~~~~~`" + str);
                getPrevBlock = str;
                prevBlockNum =  coinObject.getJSONObject("data").getInt("next_block_nb");
                str = Integer.toString(prevBlockNum);
                ((TextView) v.findViewById(R.id.nextBlockDetail)).setText(str.toString());
                System.out.println("Next Block : ~~~~~`" + str);
                getNextBlock =  str;
                getString = coinObject.getJSONObject("data").getString("prev_block_hash");
                TextView prev= ((TextView) v.findViewById(R.id.prevHashBlockNum));
                        prev.setText(getString.toString());
                getString = coinObject.getJSONObject("data").getString("next_block_hash");
                ((TextView) v.findViewById(R.id.nextHashBlockNum)).setText(getString.toString());
                getString = coinObject.getJSONObject("data").getString("size");
                ((TextView) v.findViewById(R.id.blockSize)).setText(getString.toString());
                getString = coinObject.getJSONObject("data").getString("fee");
                ((TextView) v.findViewById(R.id.blockFee)).setText(getString.toString());
                getString = coinObject.getJSONObject("data").getString("vout_sum");
                ((TextView) v.findViewById(R.id.v_sum)).setText(getString.toString());
                getString = coinObject.getJSONObject("data").getString("difficulty");
                ((TextView) v.findViewById(R.id.blockDifficulty)).setText(getString.toString());
                // v.log.info("coinName: " + coinName);
               // coinPrice = data.getJSONObject("markets").getJSONObject("coinbase").getDouble("value");
               // ((TextView) v.findViewById(R.id.priceText)).setText(String.valueOf(coinPrice));
               // Log.d("coinPrice: %d" + coinPrice, "");
                //  log.info("coinPrice: " + coinPrice);
            } catch (JSONException e) {

                System.out.println("Erooor ~~~~~~!!!!!!!!!!!!!!!!!!" + e);
            }
            return true;
        }
    });


    void function(String link) throws MalformedURLException {
        try {
         //   String link = "http://btc.blockr.io/api/v1/block/info/";
            //String numExten = blockNumber.getText().toString();
           // link = link.concat(block.toString());
            //System.out.println("Get Link !!!!!!!!!!!!!!!!!!!!!~~~~~~~~~~~~~~~~~~~" + link + " Nim exter : " + block);
            URL url = new URL(link);
            System.out.println("From Previous Funtion " + link);
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

        }
    catch (Exception e){
        System.out.println(e);
    }
    }

}
