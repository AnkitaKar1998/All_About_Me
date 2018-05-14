package com.example.win8.allaboutme;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.RestrictTo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity
{

    LoginButton loginButton;
    TextView fbEmail,fbName,fbGender;
    ProfilePictureView profilePictureView;
    LinearLayout layout1,layout2,layout3;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.win8.allaboutme",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("mamon", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {

        }
        catch (NoSuchAlgorithmException e)
        {

        }
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        layout1 = (LinearLayout)findViewById(R.id.layout1);
        layout2 = (LinearLayout)findViewById(R.id.layout2);
        layout3 = (LinearLayout)findViewById(R.id.layout3);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        profilePictureView = (ProfilePictureView)findViewById(R.id.image);
        fbEmail = (TextView)findViewById(R.id.fb_email);
        fbName = (TextView)findViewById(R.id.fb_name);
        fbGender = (TextView)findViewById(R.id.fb_gender);

        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
                {
                    public  void  onSuccess(LoginResult loginResult)
                    {
                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback()
                        {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response)
                            {
                                Log.d("mamon",response.toString());
                                setProfileToView(object);
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields","id,name,email,gender,birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel()
                    {
                        Log.d("mamon","Logged out");
                    }

                    @Override
                    public void onError(FacebookException error)
                    {
                        Toast.makeText(MainActivity.this, "error to Login Facebook", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.d("mamon","mamon");
    }

    private  void  setProfileToView(JSONObject jsonObject)
    {
        try
        {
            fbEmail.setText(jsonObject.getString("email"));
            fbGender.setText(jsonObject.getString("gender"));
            fbName.setText(jsonObject.getString("name"));
            profilePictureView.setPresetSize(ProfilePictureView.NORMAL);
            profilePictureView.setProfileId(jsonObject.getString("id"));
            profilePictureView.setVisibility(View.VISIBLE);
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.VISIBLE);
            layout3.setVisibility(View.VISIBLE);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    /*private  void initializeControlsLI()
    {
        link_signin = (ImageView)findViewById(R.id.link_login_image);
        link_signin.setOnClickListener(this);
        link_logout_button = (Button)findViewById(R.id.link_logout);
        link_logout_button.setOnClickListener(this);
        link_details = (TextView)findViewById(R.id.link_details);
        link_profile_image = (ImageView)findViewById(R.id.link_profile_image);

    }*/

    /*@Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.link_login_image:
                break;


        }
    }

    public  void handleLinkLogin()
    {
        LISessionManager.getInstance(getApplicationContext()).init(thisActivity, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess()
            {
                // Authentication was successful.  You can now do
                // other calls with the SDK.
            }

            @Override
            public void onAuthError(LIAuthError error)
            {
                // Handle authentication errors
            }
        }, true);
    }*/

}
