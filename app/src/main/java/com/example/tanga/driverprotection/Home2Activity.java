package com.example.tanga.driverprotection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class Home2Activity extends AppCompatActivity {
Button pred , diag , decclare,profile,logoff,chatroom,currentdata;
    SessionManager session;

    User u ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        u = new User();
        session=new SessionManager(this);
        session.getLogin(u);
        pred = (Button) findViewById(R.id.predbut);
        diag=(Button) findViewById(R.id.diagnosbut);
        decclare = (Button) findViewById(R.id.decclareacc);
        profile = (Button) findViewById(R.id.profilebut);
        logoff = (Button) findViewById(R.id.logoff);
        chatroom=(Button) findViewById(R.id.chatroombut);
        currentdata = findViewById(R.id.currentdata);
       /* pred.setVisibility(View.INVISIBLE);

        diag.setVisibility(View.INVISIBLE);
        decclare.setVisibility(View.INVISIBLE);
        profile.setVisibility(View.INVISIBLE);
*/

       diag.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(Home2Activity.this, com.example.tanga.driverprotection.activity.MainActivity.class));
           }
       });

       currentdata.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(Home2Activity.this, SpeedControllerActivity.class));
           }
       });
        pred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home2Activity.this, PredictionActivity.class);
                startActivity(intent);
            }
        });
        decclare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home2Activity.this, DeclareAccident.class);
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home2Activity.this, Profile.class);
                startActivity(intent);
            }
        });
        chatroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home2Activity.this, ChatRoomActivity.class);
                startActivity(intent);
            }
        });

        logoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                session.logoff();
                Intent intent = new Intent(Home2Activity.this, com.example.tanga.driverprotection.MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
