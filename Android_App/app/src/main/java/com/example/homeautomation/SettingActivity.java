package com.example.homeautomation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingActivity extends AppCompatActivity {


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference SetRef = database.getReference().child("configure");
    MaterialButton geyser,tank_min,tank_max,geyser1,tank_min1,tank_max1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        geyser=findViewById(R.id.geyser);
        tank_min=findViewById(R.id.tank_min);
        tank_max=findViewById(R.id.tank_max);

        geyser1=findViewById(R.id.geyser1);
        tank_min1=findViewById(R.id.tank_min1);
        tank_max1=findViewById(R.id.tank_max1);


        SetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.getValue().toString().equalsIgnoreCase("none")){

                    tank_max.setVisibility(View.GONE);
                    tank_min.setVisibility(View.GONE);
                    geyser.setVisibility(View.GONE);


                    tank_max1.setVisibility(View.VISIBLE);
                    tank_min1.setVisibility(View.VISIBLE);
                    geyser1.setVisibility(View.VISIBLE);



                }
                else{


                    tank_max1.setVisibility(View.GONE);
                    tank_min1.setVisibility(View.GONE);
                    geyser1.setVisibility(View.GONE);

                    tank_max.setVisibility(View.VISIBLE);
                    tank_min.setVisibility(View.VISIBLE);
                    geyser.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        geyser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetRef.setValue("geyser");
                Toast.makeText(getBaseContext(),"Geyser Configured",Toast.LENGTH_SHORT).show();
            }
        });

        tank_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetRef.setValue("tank_min");
                Toast.makeText(getBaseContext(),"tank_min Configured",Toast.LENGTH_SHORT).show();
            }
        });

        tank_max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetRef.setValue("tank_max");
                Toast.makeText(getBaseContext(),"tank_max Configured",Toast.LENGTH_SHORT).show();
            }
        });



    }
}
