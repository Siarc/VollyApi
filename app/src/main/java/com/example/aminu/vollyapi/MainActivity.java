package com.example.aminu.vollyapi;

import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private EditText registerUsername,registerPassword,registerEmail;
    private Button buttonRegister;
    private ProgressBar progressBar;
    private TextView loginActivityLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(SharedPrefManager.getmInstance(this).isLoggedIn()){

            Log.d(TAG, "onCreate: if the user logged in move to profile activity");
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
            return;
        }

        registerUsername = (EditText) findViewById(R.id.registerUsername);
        registerPassword = (EditText) findViewById(R.id.registerPassword);
        registerEmail = (EditText) findViewById(R.id.registerEmail);
        progressBar =(ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        loginActivityLink = (TextView) findViewById(R.id.loginActivityLink);

        buttonRegister.setOnClickListener(this);
        loginActivityLink.setOnClickListener(this);
    }

    private void registerUser() {

        final String email = registerEmail.getText().toString().trim();
        final String username = registerUsername.getText().toString().trim();
        final String password = registerPassword.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

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
                params.put("email", email);
                params.put("password",password);

                return  params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public void onClick(View view) {

        if(view == buttonRegister){
            registerUser();
        } else if(view == loginActivityLink){
            startActivity(new Intent(this,LoginActivity.class));
        }
    }


}
