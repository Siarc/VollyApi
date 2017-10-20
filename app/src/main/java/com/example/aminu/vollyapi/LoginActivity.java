package com.example.aminu.vollyapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aminu on 10/21/2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private EditText loginUsername, loginPassword;
    private Button buttonLogin;
    private ProgressBar progressBar;
    private TextView registerActivityLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsername = (EditText) findViewById(R.id.loginUsername);
        loginPassword = (EditText) findViewById(R.id.loginPassword);
        progressBar =(ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        registerActivityLink = (TextView) findViewById(R.id.registerActivityLink);

        buttonLogin.setOnClickListener(this);
        registerActivityLink.setOnClickListener(this);


    }

    private void loginUser() {

        final String username = loginUsername.getText().toString().trim();
        final String password = loginPassword.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(!jsonObject.getBoolean("error")){

                                SharedPrefManager.getmInstance(getApplicationContext())
                                        .userLogin(
                                                jsonObject.getInt("id"),
                                                jsonObject.getString("username"),
                                                jsonObject.getString("email")
                                        );
                                Log.d(TAG, "onResponse: Login success");
                                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                                finish();
                            }else {

                            }

                            Toast.makeText(getApplicationContext(),jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressBar.setVisibility(View.GONE);
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("username",username);
                params.put("password",password);

                return  params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }



    @Override
    public void onClick(View view) {

        if(view == buttonLogin){

            loginUser();

        } else if(view == registerActivityLink){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
    }


}
