package com.example.jacob.finalproject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.*;
import java.lang.String;
public class fragmentBalance extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<String> address ;
    ArrayList<String> readPublicKeyList ;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button balanceButton;
    String urlString;
    String filename = "newFile1";
    FileOutputStream fileOutputStream;
    FileInputStream fileInputStream;
    ObjectInputStream objectInputStream;
    File file;
    Context context ;
    EditText balanceText;
    TextView balanceBox;
    ObjectOutputStream objectOutputStream;
    String[] content = {"1dice8EMZmqKvrGE4Qc9bUFf9PX3xaYDp", "1dice97ECuByXAvqXpaYzSaQuPVvrtmz6", "198aMn6ZYAczwrE5NvNTUMyJ5qkfy4g3Hi",
            "1dice7fUkz5h4z2wPc1wLMPWgB5mDwKDx"};
    Thread t2;
    Button scanButton;
    View v;
    ArrayAdapter<String> arrayAdapter;
    ListView listView;
    public fragmentBalance() {
        // Required empty public constructor
    }
 public static fragmentBalance newInstance(String param1, String param2) {
        fragmentBalance fragment = new fragmentBalance();
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
         v =  inflater.inflate(R.layout.fragment_fragment_balance, container, false);
        listView = (ListView) v.findViewById(R.id.listView);
        balanceButton = (Button) v.findViewById(R.id.balanceGoButton);
        balanceBox = (TextView) v.findViewById(R.id.balanceRepotBox);
        addkeyToList();
        createFile();
        readfromfile();

        System.out.println("Done------------------------");
      //  address = new ArrayList<>(Arrays.asList(content));
        balanceText = (EditText) v.findViewById(R.id.blockNumber);
        final Context activity = getActivity().getApplicationContext();
        // passing retrived string from array.
        arrayAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1,readPublicKeyList);
        listView.setAdapter(arrayAdapter);


        balanceButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("--------------- "+ balanceText.getText().toString());


            threadDataCollector();

                if(balanceText.getText().toString() != null &&(balanceText.getText().toString()).length() > 0 ) {
                    if(readPublicKeyList.contains(balanceText.getText().toString())== true){
                        Toast.makeText(getActivity().getApplicationContext(), "Alreay added in the list", Toast.LENGTH_LONG).show();
                    }
                    else {
                        readPublicKeyList.add(0, balanceText.getText().toString());
                        address.add(0, balanceText.getText().toString());
                        Toast.makeText(getActivity().getApplicationContext(), "Added to the list", Toast.LENGTH_LONG).show();

                        try {
                            updateTheStorage();
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println(" Error from the  main................................." + e);
                        }
                        arrayAdapter.notifyDataSetChanged();


                        System.out.println("Oh You are fine");
                    }
                }
                else{

                    System.out.println("OOOOOOOOPSSSSSSSSSSSSSS");
                }


            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                System.out.println("Item I clicked -----------------------" + readPublicKeyList.get(position) );
                balanceText.setText(readPublicKeyList.get(position).toString());
                threadDataCollector();

            }
        });


         return v;
    }



    Handler responseHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            try {
                JSONObject coinObject = new JSONObject((String) msg. obj);
                String balance;
                double coinPrice;
                JSONObject data = coinObject.getJSONObject("data");
                balance = data.getString("balance");
                // ((TextView) trialView.findViewById(R.id.coinName)).setText(coinName);
                System.out.println("Balnce: ~~~~~~~~~~~~~~~~~~~~~~~" + balance);
                balanceBox.setText(balance);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
    });



 public void threadDataCollector(){

     Thread t = new Thread(){
         public void run() {

             if (balanceText != null) {


                 urlString = balanceText.getText().toString();
                 try {
                     String baseurl = "http://btc.blockr.io/api/v1/address/info/";
                     baseurl = baseurl.concat(urlString);
                     URL url = new URL(baseurl);
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
                 }catch(Exception e){
                     System.out.println("EROOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOORRRRRRRRRRRRRRRRRRRRRRRRRRRRR " + e);

                 }
             }
         }
     };
     t.start();



 }


public void createFile(){
    context = getActivity().getApplicationContext();
    file = getActivity().getFileStreamPath(filename);
    //address = new ArrayList<>(Arrays.asList(content));
if(!file.exists()) {
    try {

        System.out.println("Creating new file-----------------");
        file = new File(filename);
        // writing to the file
        fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
        objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(address);
        objectOutputStream.flush();
        objectOutputStream.close();
        fileOutputStream.close();

    } catch (Exception e) {
        System.out.println("Error in file : " + e);
    }
}
    else{

    System.out.println("WHATTTTTTTTTTTTTTTT FILE  EXIST ??????????????????????????????????????");

}
}

public void readfromfile(){
    context = getActivity().getApplicationContext();
    file = getActivity().getFileStreamPath(filename);
   // address = new ArrayList<>(Arrays.asList(content));

    try{
    if(file.exists()){
        System.out.println("File have already created");
        //read from file
        fileInputStream = context.openFileInput(filename);
        objectInputStream = new ObjectInputStream(fileInputStream);
        readPublicKeyList = (ArrayList) objectInputStream.readObject();

        objectInputStream.close();
        fileInputStream.close();
        System.out.println("__________Reading Done b_____________" + readPublicKeyList.toString());
    }
    }
    catch (Exception e){
        System.out.println("Error ~~~~~" + e);
    }
}
    public void addkeyToList(){
        address = new ArrayList<>();
       // address.add("Jacob");

        for(int i =0; i <4;i++)
            address.add(content[i]);

    }

      public void updateTheStorage() throws IOException {
        context = getActivity().getApplicationContext();

        File dir = getActivity().getFilesDir();
        File file = new File(dir, filename);
        try {
            fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(readPublicKeyList);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
}

