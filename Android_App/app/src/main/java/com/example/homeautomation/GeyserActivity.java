package com.example.homeautomation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GeyserActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference GeyRef = database.getReference().child("geyser");
    MaterialButton mActive,mInactive,aActive,aInactive,onActive,onInactive,offActive,offInactive,mastON,mastOFF;
    FrameLayout masterFrame;
    TextView nothing;
    double distance;
    double reference;
    String mode,state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geyser);
        nothing=findViewById(R.id.nothing);
        mActive=findViewById(R.id.manual_active);
        mInactive=findViewById(R.id.manual_inactive);
        aActive=findViewById(R.id.auto_active);
        aInactive=findViewById(R.id.auto_inactive);

        onActive=findViewById(R.id.on_active);
        onInactive=findViewById(R.id.on_inactive);
        offActive=findViewById(R.id.off_active);
        offInactive=findViewById(R.id.off_inactive);

        masterFrame=findViewById(R.id.master_frame1);
        mastON=findViewById(R.id.mastON);
        mastOFF=findViewById(R.id.mastOFF);



        GeyRef.child("reference").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                reference=Double.valueOf(String.valueOf(dataSnapshot.getValue()));
                Log.v("ForceField",String.valueOf(reference));

                if(reference!=0){
                    nothing.setVisibility(View.GONE);
                    GeyRef.child("mode").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            mode=String.valueOf(dataSnapshot.getValue());

                            if(mode.equalsIgnoreCase("auto")){

                                aInactive.setVisibility(View.VISIBLE);
                                aActive.setVisibility(View.GONE);
                                mInactive.setVisibility(View.GONE);
                                mActive.setVisibility(View.VISIBLE);


                            }
                            else{

                                aInactive.setVisibility(View.GONE);
                                aActive.setVisibility(View.VISIBLE);
                                mInactive.setVisibility(View.VISIBLE);
                                mActive.setVisibility(View.GONE);



                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    aActive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            GeyRef.child("mode").setValue("auto");
                        }
                    });

                    mActive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            GeyRef.child("mode").setValue("manual");
                        }
                    });

                    GeyRef.child("state").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            state=String.valueOf(dataSnapshot.getValue());

                            if(state.equalsIgnoreCase("on")){

                                onInactive.setVisibility(View.VISIBLE);
                                onActive.setVisibility(View.GONE);
                                offInactive.setVisibility(View.GONE);
                                offActive.setVisibility(View.VISIBLE);


                            }
                            else{

                                onInactive.setVisibility(View.GONE);
                                onActive.setVisibility(View.VISIBLE);
                                offInactive.setVisibility(View.VISIBLE);
                                offActive.setVisibility(View.GONE);



                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    onActive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            GeyRef.child("state").setValue("on");
                        }
                    });

                    offActive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            GeyRef.child("state").setValue("off");
                        }
                    });


                }
                else{
                    nothing.setVisibility(View.VISIBLE);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        GeyRef.child("notify").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(String.valueOf(dataSnapshot.getValue()).equalsIgnoreCase("SHOW"))
                    masterFrame.setVisibility(View.VISIBLE);
                else
                    masterFrame.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mastON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GeyRef.child("notify").setValue("ON");
            }
        });

        mastOFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GeyRef.child("notify").setValue("OFF");
            }
        });


    }


//
//    public void operations1(String mode){
//
//        if(mode.equalsIgnoreCase("auto")){
//
//
//            GeyRef.child("state").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                    operations2(String.valueOf(dataSnapshot.getValue()));
//
//
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            })      ;
//
//        }
//        else{
//
//            // SET - State value on
//
//        }
//
//
//    }
//
//    void operations2(String state){
//
//        if(state.equalsIgnoreCase("on")){
//
//            GeyRef.child("vigilant").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                    operations3(String.valueOf(dataSnapshot.getValue()));
//
//
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            })      ;
//
//        }
//
//    }
//
//
//
//void operations3(String vigilant){
//
//
//
//    GeyRef.child("distance").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                distance=Double.valueOf((Double) dataSnapshot.getValue());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    GeyRef.child("reference").addValueEventListener(new ValueEventListener() {
//        @Override
//        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//            reference=Double.valueOf((Double) dataSnapshot.getValue());
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//        }
//    });
//
//    operations4(distance,reference);
//
//
//
//}
//
//void operations4(double distance,double reference){
//
//
//        if(distance>= reference){
//
//
//
//        }
//
//}




//    double dist,ref;
//    String geyMode;
//    CountDownTimer cTimer = null, cTimer2=null;
//    TextView timer;
//    FrameLayout master_frame1,master_frame2;
//    MaterialButton mastYes,mastNo;

//    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialog, int which) {
//            switch (which){
//                case DialogInterface.BUTTON_POSITIVE:
//                    //Yes button clicked
//                    break;
//
//                case DialogInterface.BUTTON_NEGATIVE:
//                    //No button clicked
//                    break;
//            }
//        }
//    };

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_geyser);
//











//        master_frame1=findViewById(R.id.master_frame1);
//        mastYes=findViewById(R.id.mastYes);
//        mastNo=findViewById(R.id.mastNo);
//
//
//        GeyRef.child("distance").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                dist=Double.valueOf((Double) dataSnapshot.getValue());
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//
//
//            }
//        });
//
//        GeyRef.child("reference").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                ref=Double.valueOf((Double) dataSnapshot.getValue());
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//
//
//            }
//        });
//
//        GeyRef.child("mode").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                geyMode =String.valueOf(dataSnapshot.getValue());
//                if(geyMode.equalsIgnoreCase("auto"))
//                    operations();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//
//
//            }
//        });
//





//
//    private void operations(){
//
//        if(dist>=ref){
//
//            startTimer();
//
//
//        }
//
//    }
//
//    void startTimer() {
//        cTimer = new CountDownTimer(5000, 1000) {
//            public void onTick(long millisUntilFinished) {
//
//                if(dist<ref){
//
//                    cTimer.cancel();
//                    cTimer.start();
//                    timer.setText(String.valueOf(30));
//
//                }
//                    timer=findViewById(R.id.timer);
//                    timer.setText(String.valueOf(millisUntilFinished/1000));
//            }
//            public void onFinish() {
////                AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
////                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
////                        .setNegativeButton("No", dialogClickListener).show();
//                master_frame1.setVisibility(View.VISIBLE);
//                operations2();
//            }
//        };
//        cTimer.start();
//    }
//
//    void cancelTimer() {
//        if(cTimer!=null)
//            cTimer.cancel();
//    }
//
//    void startTimer2() {
//        cTimer2 = new CountDownTimer(5000, 1000) {
//            public void onTick(long millisUntilFinished) {
//
//
//
//            }
//            public void onFinish() {
////                AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
////                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
////                        .setNegativeButton("No", dialogClickListener).show();
//                GeyRef.child("state").setValue("off");
//                Toast.makeText(getBaseContext(),"Geyser Switched off",Toast.LENGTH_SHORT).show();
//                master_frame1.setVisibility(View.GONE);
//            }
//        };
//        cTimer2.start();
//    }
//
//    void cancelTimer2() {
//        if(cTimer2!=null)
//            cTimer2.cancel();
//    }
//
//    private void operations2(){
//
//        mastYes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                GeyRef.child("state").setValue("off");
//                Toast.makeText(getBaseContext(),"Geyser Switched off",Toast.LENGTH_SHORT).show();
//                master_frame1.setVisibility(View.GONE);
//            }
//        });
//
//        mastNo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                GeyRef.child("mode").setValue("manual");
//                Toast.makeText(getBaseContext(),"Geyser Switched to manual Mode",Toast.LENGTH_SHORT).show();
//                master_frame1.setVisibility(View.GONE);
//            }
//        });
//
//        startTimer2();
//
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        cancelTimer();
//        super.onDestroy();
//    }
//
//

}
