package com.tpddl.thermalscanning;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FeederInput extends AppCompatActivity{

    public String feedersName;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeder_name);

        Toast welcomeMsg = Toast.makeText(getApplicationContext(), "Developed By Anubhav Jindal for COS TPDDL", Toast.LENGTH_LONG);
        welcomeMsg.setGravity(Gravity.CENTER, 0, 0);
        welcomeMsg.show();

        final EditText feederInputText = (EditText) findViewById(R.id.feederInputText);
        Button feederSubmit = (Button) findViewById(R.id.feederSubmit);

        feederSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedersName = feederInputText.getText().toString();
                if(!feedersName.isEmpty()){
                    Intent intent = new Intent(getBaseContext(),ThermalScanning.class);
                    intent.putExtra("fn",feedersName);
                    startActivity(intent);

                    feederInputText.setText("");
                }
                else{
                    Toast.makeText(getBaseContext(),"Feeder Name cannot be empty, Please enter a feeder name!",Toast.LENGTH_SHORT).show();
                }
                }
        });

    }
}
