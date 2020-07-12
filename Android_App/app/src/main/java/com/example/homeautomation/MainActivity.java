package com.example.homeautomation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Set;


public class MainActivity extends AppCompatActivity {

    LottieAnimationView power,thermometer;
    FrameLayout off_mode,Geyser;
    LinearLayout masterLayout;
    TextView CO,LPG,Smoke;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference parentRef = database.getReference();
    double CO_value,LPG_value,Smoke_value;
    FrameLayout Water,Room,Settings,Configure;

    int powerflag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        FirebaseApp.initializeApp(this);
        power=findViewById(R.id.power_button);
        off_mode=findViewById(R.id.off_mode);
        CO=findViewById(R.id.MQ2_Stat_CO);
        LPG=findViewById(R.id.MQ2_Stat_LPG);
        Smoke=findViewById(R.id.MQ2_Stat_Smoke);


//        config();

        readings();

        buttonControl();

//        thermometer=findViewById(R.id.thermometer);
        masterLayout=findViewById(R.id.master_layout);
        power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeState();

                if(powerflag==1) {
//                    Toast.makeText(getBaseContext(), "OFF", Toast.LENGTH_SHORT).show();
                    parentRef.child("power").setValue("off");
                    masterLayout.setVisibility(View.GONE);
                    off_mode.setVisibility(View.VISIBLE);

                }else {
                    parentRef.child("power").setValue("on");
//                    Toast.makeText(getBaseContext(),"ON",Toast.LENGTH_SHORT).show();
                    masterLayout.setVisibility(View.VISIBLE);
                    off_mode.setVisibility(View.GONE);
                }
            }
        });


    }

    private void changeState(){
        if(powerflag==0){
            power.setMinAndMaxProgress(0f,0.45f);
            power.playAnimation();
            power.loop(false);
            powerflag=1;
        } else{
            power.setMinAndMaxProgress(0.5f,1f);
            power.playAnimation();
            power.loop(false);
            powerflag=0;
        }
    }

    private void readings(){

        CO=findViewById(R.id.MQ2_Stat_CO);
        LPG=findViewById(R.id.MQ2_Stat_LPG);
        Smoke=findViewById(R.id.MQ2_Stat_Smoke);

        parentRef.child("MQ2_stats").child("CO").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                CO_value=  ((double) ((int)( 1000 * (Double) dataSnapshot.getValue())))/1000; ;
                CO.setText("CO:"+String.valueOf(CO_value));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        parentRef.child("MQ2_stats").child("LPG").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                LPG_value=  ((double) ((int)( 1000 * (Double) dataSnapshot.getValue())))/1000; ;
                LPG.setText("LPG:"+String.valueOf(LPG_value));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        parentRef.child("MQ2_stats").child("smoke").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Smoke_value=  ((double) ((int)( 1000 * (Double) dataSnapshot.getValue())))/1000; ;
                Smoke.setText("Smoke:"+String.valueOf(Smoke_value));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void buttonControl(){


        Settings=findViewById(R.id.settings);
        Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,SettingActivity.class);
                startActivity(i);
            }
        });

        Water=findViewById(R.id.water);
        Water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,WaterActivity.class);
                startActivity(i);
            }
        });

        Room=findViewById(R.id.room);
        Room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,RoomActivity.class);
                startActivity(i);
            }
        });

        Geyser=findViewById(R.id.geyser);
        Geyser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,GeyserActivity.class);
                startActivity(i);
            }
        });


    }

//    private void config(){
//
//        Configure=findViewById(R.id.conf_switch);
//
//        Configure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                findViewById(R.id.conf_frame).setVisibility(View.VISIBLE);
//                Configure.setVisibility(View.GONE);
//            }
//        });
//
//    }
}
