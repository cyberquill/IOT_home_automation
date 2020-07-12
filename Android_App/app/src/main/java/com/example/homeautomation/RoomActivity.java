package com.example.homeautomation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RoomActivity extends AppCompatActivity {


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference parentRef = database.getReference();
    TextView humidity, temperature;
    MaterialButton bulb,fan;
    LottieAnimationView bulbA,fanA;
    String bulb_stat,fan_stat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        readings();

        applinceControl();


    }

    private void readings(){

        humidity=findViewById(R.id.humidity);
        temperature =findViewById(R.id.temprature);

        parentRef.child("room_stats").child("room1").child("humidity").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                humidity.setText(String.valueOf(dataSnapshot.getValue())+"%");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {



            }
        });

        parentRef.child("room_stats").child("room1").child("temperature").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                temperature.setText(String.valueOf(dataSnapshot.getValue())+(char) 0x00B0+"C");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {



            }
        });

    }

    private void applinceControl(){

        bulb=findViewById(R.id.bulb);
        bulbA=findViewById(R.id.bulb_animation);

        bulb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(String.valueOf(bulb.getText()).equalsIgnoreCase("off")){
                parentRef.child("room_stats").child("room1").child("bulb").setValue("on");

                }
                else{

                    parentRef.child("room_stats").child("room1").child("bulb").setValue("off");
                }

            }
        });


        parentRef.child("room_stats").child("room1").child("bulb").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                bulb_stat=String.valueOf(dataSnapshot.getValue());
                if(bulb_stat.equalsIgnoreCase("on"))
                bulbA.playAnimation();
                else{

                    bulbA.setProgress((float)0.37);
                    bulbA.pauseAnimation();

                }
                bulb.setText(String.valueOf(dataSnapshot.getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fan=findViewById(R.id.fan);
        fanA=findViewById(R.id.fan_animation);
        fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(String.valueOf(fan.getText()).equalsIgnoreCase("off"))
                    parentRef.child("room_stats").child("room1").child("fan").setValue("on");
                else
                    parentRef.child("room_stats").child("room1").child("fan").setValue("off");

            }
        });

        parentRef.child("room_stats").child("room1").child("fan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fan.setText(String.valueOf(dataSnapshot.getValue()));

                fan_stat=String.valueOf(dataSnapshot.getValue());
                if(fan_stat.equalsIgnoreCase("on"))
                    fanA.playAnimation();
                else{

                    fanA.setProgress((float) 0.0);
                    fanA.pauseAnimation();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
