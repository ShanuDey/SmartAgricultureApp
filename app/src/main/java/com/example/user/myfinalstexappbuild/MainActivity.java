package com.example.user.myfinalstexappbuild;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    //variables
    private String uid;
    private TextView textView;
    private int paramId[]={R.id.temperature,R.id.sunlightintensity,R.id.pump,R.id.humidity,R.id.smoke,R.id.soilmoisture};
    private String[] params={"temperature","sunlightintensity","pump","humidity","smoke","soilmoisture"};
    private ImageView ivPumpImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //cast imageview
        ivPumpImage =(ImageView) findViewById(R.id.pumpImage);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
            // Toast.makeText(this, uid, Toast.LENGTH_SHORT).show();

            //load data
            for(int i=0;i<6;i++){
                setData(params[i],paramId[i]);
            }
        }
        else{
            //Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }

    }

    public void setData(final String param, final int id){
        // Read from the database
        FirebaseDatabase.getInstance().getReference(uid).child("sensor").child(param).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(Integer.class).toString();
                // Log.v("read",  param+" : " + value);
                if(param.equals("pump")){
                    if(value.equals("0")){
                        value="off";
                        ivPumpImage.setImageResource(R.drawable.tapoff);
                    }
                    else{
                        value="on";
                        ivPumpImage.setImageResource(R.drawable.tapon);
                    }
                    textView = (TextView) findViewById(id);
                    textView.setText(value);
                }
                else{
                    TextView textView = (TextView) findViewById(id);
                    textView.setText(value);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.v("read", "Failed to read value.", error.toException());
            }
        });

    }

    //my methods
    public void onButtonClick(View view){
        setPump(1);
    }

    public void offButtonClick(View view){
        setPump(0);
    }

    public void setPump(int data){
        // Write a message to the database
        FirebaseDatabase.getInstance().getReference(uid.toString()).child("sensor").child("pump").setValue(data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }
        */
        switch (id){
            case R.id.action_signout:
                //sign out
                FirebaseAuth.getInstance().signOut();
                // move to login page
                finish();
                startActivity(new Intent(this,LoginActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
