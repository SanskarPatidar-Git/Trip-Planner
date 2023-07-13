package com.adventure.tripplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrganizerMainActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FloatingActionButton fac;
    TextView name,mob,email,team;
    View headerView;
    ShapeableImageView imageView;
    private OrganizerProfileModel profile;
    RecyclerView recyclerView;

    private ArrayList<CardModel> arrayCardModel =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_main);

        toolbar=findViewById(R.id.toolbarID);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Your Trips");


        drawerLayout=findViewById(R.id.drawerID);
        navigationView=findViewById(R.id.navigation);
        fac=findViewById(R.id.facBtn);


        //for accessing items of navigation header we need to inflate a header to another view.
        headerView=navigationView.getHeaderView(0);

        name= headerView.findViewById(R.id.header_nameID);
        mob=  headerView.findViewById(R.id.header_mobID);
        email=headerView.findViewById(R.id.header_emailID);
        team= headerView.findViewById(R.id.header_teamID);
        imageView = headerView.findViewById(R.id.imageProfile);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        FirebaseAuth auth=FirebaseAuth.getInstance();
        FirebaseUser user=auth.getCurrentUser();
        final String emailKey=user.getEmail().replace(".","^");

        FirebaseConnectivity firebaseConnectivity=new FirebaseConnectivity(FirebaseConnectivity.getOrganizerDatabase());
        firebaseConnectivity.getReference().child(emailKey).child("Other_Data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    profile = snapshot.getValue(OrganizerProfileModel.class);

                  name.setText(profile.getName());
                  mob.setText("+91 "+profile.getMobileNo());
                  email.setText(profile.getEmail());
                  team.setText(profile.getTeamName());

                  firebaseConnectivity.getReference().child(emailKey).child("Image").addValueEventListener(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot snapshot) {
                          String image=snapshot.getValue(String.class);
                          Picasso.get().load(image).into(imageView);
                          //imageView.setRotation(Float.parseFloat("90f"));
                          getData();
                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError error) {
                          Toast.makeText(OrganizerMainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                      }
                  });
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        fac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrganizerMainActivity.this,NewTripActivity.class);
                intent.putExtra("TeamName",profile.getTeamName());
                startActivity(intent);
            }
        });



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId=item.getItemId();
                if(itemId==R.id.menu_profile) {
                    Intent intent =new Intent(OrganizerMainActivity.this,OrganizerProfileActivity.class);
                    intent.putExtra("emailKey",emailKey);
                    startActivity(intent);
                }else if(itemId==R.id.menu_about) {

                } else if(itemId==R.id.menu_help) {

                }else if(itemId==R.id.menu_logOut) {

                }
                else {

                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
          super.onBackPressed();
    }


    private void getData()
    {
        recyclerView=findViewById(R.id.recyclerCard);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseConnectivity connectivity=new FirebaseConnectivity(FirebaseConnectivity.getDatabaseTrip());
        connectivity.getReference().child(profile.getTeamName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    for (DataSnapshot teamSnapshot : snapshot.getChildren()) {
                        String image = teamSnapshot.child("Images").child("image0").getValue().toString();
                        String destination = teamSnapshot.child("TripData").child("destination").getValue().toString();
                        CardModel cardModel = new CardModel(image, destination, profile.getTeamName());
                        arrayCardModel.add(cardModel);

                    }

                    CardAdapter cardAdapter=new CardAdapter(OrganizerMainActivity.this,arrayCardModel);
                    recyclerView.setAdapter(cardAdapter);
                }
                else
                    Toast.makeText(OrganizerMainActivity.this, "No trip found", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrganizerMainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}