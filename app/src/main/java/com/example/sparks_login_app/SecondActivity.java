package com.example.sparks_login_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class SecondActivity extends AppCompatActivity {
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView name, email;
    Button signOutBtn;
    CircleImageView photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email2);
        photo=findViewById(R.id.profileIcon);
        signOutBtn = findViewById(R.id.sign_out_btn);



        //here we access information from facebook account
        AccessToken accessToken= AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                        try {
                           String accountName;
                            accountName = object.getString("name");
                            String accountEmail;
                            accountEmail = object.getString("email");
                            String accountPicture;
                            accountPicture=object.getJSONObject("picture").getJSONObject("data").getString("url");
                            name.setText(accountName);
                           email.setText(accountEmail);
                           Picasso.with(getApplicationContext()).load(accountPicture).placeholder(R.drawable.ic_person).into(photo);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,email,picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();






        //here we access information from google account
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {

            //get data
            String acctName = acct.getDisplayName();
            String acctEmail = acct.getEmail();
            Uri acctPicture = acct.getPhotoUrl();

            //set data
            name.setText(acctName);
            email.setText(acctEmail);
            Picasso.with(getApplicationContext()).load(acctPicture).placeholder(R.drawable.ic_person).into(photo);
        }


        //sign out button
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });




    }
    //function for signing out
    void signOut(){
        LoginManager.getInstance().logOut();
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete( Task<Void> task) {
                finish();
                startActivity(new Intent(SecondActivity.this,MainActivity.class));
            }
        });
    }
}

