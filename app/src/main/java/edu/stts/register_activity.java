package edu.stts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class register_activity extends AppCompatActivity {


    EditText et_user,et_pass,et_name,et_conpass,etage;
    String name,username,pass,conpass,age;
    String url ="http://proyektinder.000webhostapp.com/Proyek_Tinder/public/register";
    RequestQueue rq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        et_user = findViewById(R.id.et_user);
        et_pass = findViewById(R.id.et_pass);
        et_conpass=findViewById(R.id.et_conpass);
        etage = findViewById(R.id.et_bday);
        et_name = findViewById(R.id.et_name);
        rq = Volley.newRequestQueue(this);
    }

    public void Regis(View v){
        name = et_name.getText().toString();
        username = et_user.getText().toString();
        age = etage.getText().toString();
        pass = et_pass.getText().toString();
        conpass = et_conpass.getText().toString();
        et_conpass.setText("");
        et_name.setText("");
        etage.setText("");
        et_pass.setText("");
        et_user.setText("");
        if(name.equalsIgnoreCase("")||username.equalsIgnoreCase("")||age.equalsIgnoreCase("")||pass.equalsIgnoreCase("")||conpass.equalsIgnoreCase("")){
            Toast.makeText(getApplicationContext(),"Tidak boleh ada yang kosong",Toast.LENGTH_SHORT).show();
        }
        else{
            if(pass.equals(conpass)){
                JSONObject data = new JSONObject();
                try {
                    data.put("username",username);
                    data.put("password",pass);
                    data.put("nama_user",name);
                    data.put("tanggal_lahir",age);
                    data.put("deskripsi","");
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, data,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                String respon = "Register Failed";
                                try {
                                    respon = response.getString("message");
                                }catch(JSONException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(getApplicationContext(), respon, Toast.LENGTH_SHORT).show();
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
            else{
                Toast.makeText(getApplicationContext(),"Password dan Confirm Password harus bernilai sama",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void backLogin(View v){
        startActivity(new Intent(register_activity.this,MainActivity.class));
        finish();
    }
}
