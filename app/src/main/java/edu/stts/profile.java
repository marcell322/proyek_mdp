package edu.stts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link profile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int RESULT_LOAD_IMAGE = 1;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public profile() {
        // Required empty public constructor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_LOAD_IMAGE && resultCode==RESULT_OK && data!=null){
            Uri selected_image = data.getData();
            imageupload.setImageURI(selected_image);
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile.
     */
    // TODO: Rename and change types and number of parameters
    public static profile newInstance(String param1, String param2) {
        profile fragment = new profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private SharedPreferences pref;
    String user;
    String url = "http://proyektinder.000webhostapp.com/Proyek_Tinder/public/home";
    String urlupdate ="http://proyektinder.000webhostapp.com/Proyek_Tinder/public/updateprofile";
    String urluploadimage="http://proyektinder.000webhostapp.com/Proyek_Tinder/public/updategambar";
    String urlgambar ="http://proyektinder.000webhostapp.com/gambar/";
    RequestQueue rq;
    String id="0";
    int ctr;

    public void uploadimg(){
        Bitmap image = ((BitmapDrawable)imageupload.getDrawable()).getBitmap();
        ByteArrayOutputStream bytearr= new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG,100,bytearr);
        String encode = Base64.encodeToString(bytearr.toByteArray(),Base64.DEFAULT);
        JSONObject data= new JSONObject();
        try {
            data.put("username",user);
            data.put("imageurl",encode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, urluploadimage, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(requireContext(), "Sukses", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(requireContext(),error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }
        );
        jor.setRetryPolicy(new DefaultRetryPolicy(5000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        rq.add(jor);
    }

    public void update(){
        final String onUser=etuser.getText().toString();
        String onPass=etpass.getText().toString();
        String onName=etname.getText().toString();
        String onBday=etbday.getText().toString();
        String onDesc=etdesk.getText().toString();
        JSONObject data = new JSONObject();
        try {
            data.put("id",id);
            data.put("username",onUser);
            data.put("password",onPass);
            data.put("nama_user",onName);
            data.put("tanggal_lahir",onBday);
            data.put("deskripsi",onDesc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, urlupdate, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("username",onUser);
                        editor.apply();
                        user = pref.getString("username","");
//                        request();
//                        Toast.makeText(requireContext(),"Update Sukses",Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(requireContext(),error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }
        );
        rq.add(jor);
    }

    public void requestimage(){

        ImageRequest ir = new ImageRequest(urlgambar + user+".jpg",
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageupload.setImageBitmap(response);
                    }
                },0,0,null,Bitmap.Config.RGB_565,
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        imageupload.setImageResource(R.drawable.ic_launcher_background);
                        error.printStackTrace();
                    }
                });
        rq.add(ir);
    }


    public void request(){
        JSONArray data = new JSONArray();
        JsonArrayRequest jor = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        String on="";
                        String onPass="";
                        String onName="";
                        String onBday="";
                        String onDesc="";
                        for (int i=0;i<response.length();i++){
                            String cek="";
                            try {
                                cek = response.getString(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String[] arrcek = cek.split(",");
                            cek = arrcek[1].substring(11).replace("\"","");
                            if(cek.equalsIgnoreCase(user)){
                                on=cek;
                                onPass = arrcek[2].substring(12).replace("\"","");
                                onBday = arrcek[4].substring(17).replace("\"","");
                                onDesc = arrcek[5].substring(13).replace("\"","");
                                onName = arrcek[3].substring(13).replace("\"","");
                                onDesc= onDesc.replace("}","");
                                etuser.setText(on);
                                etpass.setText(onPass);
                                etname.setText(onName);
                                etdesk.setText(onDesc);
                                etbday.setText(onBday);
                                id=(i+1)+"";
                                requestimage();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(requireContext(),error.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
        rq.add(jor);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        pref = requireContext().getSharedPreferences("loginpref", Context.MODE_PRIVATE);
        rq = Volley.newRequestQueue(requireContext());
        user = pref.getString("username","");
        imageupload = v.findViewById(R.id.image_upload);
        etbday = v.findViewById(R.id.etBday);
        etname=v.findViewById(R.id.etName);
        etdesk = v.findViewById(R.id.etDesk);
        etpass = v.findViewById(R.id.etPass);
        etuser = v.findViewById(R.id.etUsername);
        uploadimage=v.findViewById(R.id.btnuploadimage);
        imageupload.setOnClickListener(this);
        uploadimage.setOnClickListener(this);
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    ImageView imageupload;
    Button uploadimage;
    EditText etuser,etname,etpass,etbday,etdesk;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = requireContext().getSharedPreferences("loginpref", Context.MODE_PRIVATE);
        user = pref.getString("username","");
        rq = Volley.newRequestQueue(requireContext());
        request();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_upload:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE);
                break;
            case R.id.btnuploadimage:
                update();

//                uploadimg();
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
