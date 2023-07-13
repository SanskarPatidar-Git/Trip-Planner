package com.adventure.tripplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseNetworkException;

public class TravelerProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextInputEditText name, mob, address;
    private AppCompatButton btnSave;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traveler_profile);

        //set up custom toolbar
        toolbar = findViewById(R.id.toolbarID);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");


        //find id of views
        name = findViewById(R.id.nameID);
        mob = findViewById(R.id.mobID);
        address = findViewById(R.id.addressID);
        btnSave = findViewById(R.id.btnsave);
        progressBar = findViewById(R.id.progressBar);



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                String inputName = name.getText().toString().trim();
                String inputMob = mob.getText().toString().trim();
                String inputAddress = address.getText().toString().trim();

                if (TextUtils.isEmpty(inputAddress) || TextUtils.isEmpty(inputMob) || TextUtils.isEmpty(inputName))
                    Toast.makeText(TravelerProfileActivity.this, "Please fill all the details.", Toast.LENGTH_SHORT).show();
                else {

                    progressBar.setVisibility(View.VISIBLE);


                    Intent get=getIntent(); //login to this activity taking email
                    final String KEY="emailKey";

                    //email-key
                    final String emailKey=get.getStringExtra(KEY).replace(".","^");


                    //getting reference of traveler database
                    FirebaseConnectivity firebaseConnectivity = new FirebaseConnectivity(FirebaseConnectivity.getTravelerDatabase());

                    //set the value in traveler model class
                    TravelerProfileModel travelerModel = new TravelerProfileModel(inputName, inputMob, inputAddress);

                    //insert data in traveler database
                    firebaseConnectivity.getReference().child(emailKey).setValue(travelerModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                //if the data is inserted in DB then update the status of user to traveler.
                                FirebaseConnectivity.updateUserStatus(FirebaseConnectivity.STATUS_TRAVELER,emailKey);

                                //update the value of shared reference also to not to show again login activity and go to traveler activity.
                                Raw.share(1);


                                Toast.makeText(TravelerProfileActivity.this, "Record Updated", Toast.LENGTH_SHORT).show();

                                progressBar.setVisibility(View.GONE);

                                Intent intent = new Intent(TravelerProfileActivity.this, TravelerMainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                try {  throw task.getException(); }
                                catch (FirebaseNetworkException firebaseNetworkException)
                                {  View view=getLayoutInflater().inflate(R.layout.custom_toast_internet,(ViewGroup)findViewById(R.id.customToastInternet));
                                    new Raw().internetToast(view,TravelerProfileActivity.this); } //no internet
                                 catch (Exception e) {
                                    Toast.makeText(TravelerProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });
    }

}
