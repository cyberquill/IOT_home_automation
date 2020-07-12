package com.example.homeautomation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.logging.Level;

public class WaterActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference parentRef = database.getReference().child("water_tank");
    TextView water_percentage,indicator,nothing;
    LottieAnimationView meter;
    ArcProgress arcProgress;
    double distance,ref_min,ref_max;
    int percentage;
    String indi;
    RelativeLayout mast1,mast2;

    float current,wat=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);
        water_percentage=findViewById(R.id.water_percentage);
        nothing=findViewById(R.id.nothing);
        arcProgress=findViewById(R.id.arc_progress);
        indicator=findViewById(R.id.indicator);
        indicator.setVisibility(View.INVISIBLE);
//        meter.setProgress((float) 1.0);
//        meter.pauseAnimation();
        mast1=findViewById(R.id.mast1);
        mast2=findViewById(R.id.mast2);
        parentRef.child("ref_max").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ref_max=(Double) Double.valueOf(String.valueOf(dataSnapshot.getValue()));
                if(ref_max==0){
                    mast1.setVisibility(View.GONE);
                    mast2.setVisibility(View.GONE);
                    nothing.setVisibility(View.VISIBLE);
                }
                else{
                    mast1.setVisibility(View.VISIBLE);
                    mast2.setVisibility(View.VISIBLE);
                    nothing.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        parentRef.child("ref_min").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ref_min=(Double) Double.valueOf(String.valueOf(dataSnapshot.getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        parentRef.child("distance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                distance=(double) Double.valueOf(String.valueOf(dataSnapshot.getValue()));

                percentage= (int) ((distance - ref_min)*100/(ref_max-ref_min));

                water_percentage.setText("Level: "+ Double.valueOf(((double) (int)((distance - ref_min)*10000/(ref_max-ref_min)))/100) +"%");

                arcProgress.setProgress(percentage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {



            }
        });


        parentRef.child("msg").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                indi=String.valueOf(dataSnapshot.getValue());

                if(!indi.equalsIgnoreCase("none"))
                {
                indicator.setText(indi);
                indicator.setVisibility(View.VISIBLE);
                }
                else{
                    indicator.setVisibility(View.INVISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
