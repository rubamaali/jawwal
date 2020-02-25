package com.lite.pits_jawwal.pitstracklite;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class Feedback extends Fragment implements View.OnClickListener {
    View view;
    ImageButton f1,f2,f3,f4,f5;
    Button send;
    EditText msg;
    public int rating=0;
    public Feedback() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_feedback, container, false);
        f1=(ImageButton)view.findViewById(R.id.f1);
        f1.setOnClickListener(this);
        f2=(ImageButton)view.findViewById(R.id.f2);
        f2.setOnClickListener(this);
        f3=(ImageButton)view.findViewById(R.id.f3);
        f3.setOnClickListener(this);
        f4=(ImageButton)view.findViewById(R.id.f4);
        f4.setOnClickListener(this);
        f5=(ImageButton)view.findViewById(R.id.f5);
        f5.setOnClickListener(this);
        send=(Button)view.findViewById(R.id.but_send);
        send.setOnClickListener(this);
        send.setEnabled(false);
        send.setTextColor(Color.RED);
        msg=(EditText)view.findViewById(R.id.ed_msg);
        return view;
    }

    @Override
    public void onClick(View view) {
        f1.setImageResource(0);
        f2.setImageResource(0);
        f3.setImageResource(0);
        f4.setImageResource(0);
        f5.setImageResource(0);
        if(view.getId()==R.id.f1){rating=1;send.setEnabled(true); send.setTextColor(Color.GREEN);f1.setImageResource(R.drawable.highlight);}
        else if(view.getId()==R.id.f2){ rating=2;send.setEnabled(true);send.setTextColor(Color.GREEN);f2.setImageResource(R.drawable.highlight);}
        else if(view.getId()==R.id.f3){ rating=3;send.setEnabled(true);send.setTextColor(Color.GREEN);f3.setImageResource(R.drawable.highlight);}
        else if(view.getId()==R.id.f4){ rating=4;send.setEnabled(true);send.setTextColor(Color.GREEN);f4.setImageResource(R.drawable.highlight);}
        else if(view.getId()==R.id.f5){ rating=5;send.setEnabled(true);send.setTextColor(Color.GREEN);f5.setImageResource(R.drawable.highlight);}
        if(view.getId()==R.id.but_send){
            if(rating==0){
                Toast.makeText(getActivity(),"Please Rating",Toast.LENGTH_LONG).show();
            }
            else{
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String userName = prefs.getString(Preferences.USER_NAME, "");
                String password = prefs.getString(Preferences.PASSWORD, "");
                String imei=SendRatingToDB.getIMEI(getContext());
                SendRatingToDB sendRatingToDB=new SendRatingToDB();
                sendRatingToDB.sendfeedback(rating,msg.getText().toString(),imei,"",userName,password);
                Toast.makeText(getActivity(),"Thank You For Rating",Toast.LENGTH_LONG).show();
                send.setEnabled(false);
                send.setTextColor(Color.RED);
            }
        }
    }
}
