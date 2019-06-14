package edu.stts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonIOException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText etuser,etpass;
    String username,password;
    String url ="http://proyektinder.000webhostapp.com/Proyek_Tinder/public/login";
    RequestQueue rq;
    private SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etuser= findViewById(R.id.et_user_login);
        etpass = findViewById(R.id.etPassLogin);
        rq = Volley.newRequestQueue(this);
        pref = getApplicationContext().getSharedPreferences("loginpref", Context.MODE_PRIVATE);
    }

    public void Login(View v){
        username =etuser.getText().toString();
        password=etpass.getText().toString();
        if(username.equalsIgnoreCase("") || password.equalsIgnoreCase("")){
            Toast.makeText(this,"Tidak Boleh Kosong",Toast.LENGTH_SHORT).show();
        }
        else{
            JSONObject data = new JSONObject();
            try {
                data.put("username",username);
                data.put("password",password);
            }
            catch (JSONException e){
                e.printStackTrace();
            }

            JsonObjectRequest jor= new JsonObjectRequest(Request.Method.POST, url, data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String cek="";
                            try {
                                cek = response.getString("message");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if(cek.equalsIgnoreCase("berhasil login")){
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("username",username);
                                editor.apply();
                                startActivity(new Intent(MainActivity.this,home.class));
                                finish();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"User dan Pasword salah",Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> header = new HashMap<String, String>();
                    header.put("Content-Type", "application/json;charset=utf-8");
                    return header;
                }
            };
            rq.add(jor);
        }
    }

    public void Register(View v){
        startActivity(new Intent(MainActivity.this,register_activity.class));
    }
}
