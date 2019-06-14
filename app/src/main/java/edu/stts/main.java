package edu.stts;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link main.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link main#newInstance} factory method to
 * create an instance of this fragment.
 */
public class main extends Fragment implements View.OnClickListener {
    private SharedPreferences pref;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView tvnama,tvbday,tvdesk;
    String url = "http://proyektinder.000webhostapp.com/Proyek_Tinder/public/home";
    String urlgambar ="http://proyektinder.000webhostapp.com/Proyek_Tinder/public/uploads/";
    String user="";
    RequestQueue rq;
    String[] arrnama;
    private OnFragmentInteractionListener mListener;

    public main() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment main.
     */
    // TODO: Rename and change types and number of parameters
    public static main newInstance(String param1, String param2) {
        main fragment = new main();
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
    Button like,nope;
    int ctr=0;
    ImageView img;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_main, container, false);
        tvbday = v.findViewById(R.id.txtBdayHome);
        img = v.findViewById(R.id.imageDownload);
        tvdesk=v.findViewById(R.id.txtDes);
        tvnama =v.findViewById(R.id.txtuserhome);
        like = v.findViewById(R.id.btnlike);
        nope = v.findViewById(R.id.btnnope);
        like.setOnClickListener(this);
        nope.setOnClickListener(this);
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = requireContext().getSharedPreferences("loginpref", Context.MODE_PRIVATE);
        user = pref.getString("username","");
        rq = Volley.newRequestQueue(requireContext());
        ctr=0;
        request();
    }
    int max;
    public void request(){
        JSONArray data = new JSONArray();
        JsonArrayRequest jor = new JsonArrayRequest(Request.Method.GET, url, data,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        max = response.length();
                        String asd = response.toString();
                        String Nama="";
                        try {
                            Nama = response.getString(ctr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        arrnama = Nama.split(",");
                        if(arrnama[1].substring(11).equals("\""+user+"\"")) {
                            try {
                                ctr++;
                                if(ctr>=max){
                                    ctr=0;
                                }
                                Nama = response.getString(ctr);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            arrnama = Nama.split(",");
                        }
                        imagereq(arrnama[1].substring(11).replace("\"",""));
                        String bday = arrnama[4].substring(17).replace("\"","");
                        String nama = arrnama[3].substring(13).replace("\"","");
                        String deskripsi =arrnama[5].substring(13).replace("\"","");
                        deskripsi = deskripsi.replace("}","");
                        tvbday.setText("Ulang tahun: "+bday);
                        tvnama.setText("Nama: "+nama);
                        tvdesk.setText("Deskripsi: "+deskripsi);
                    }
                },
                new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(requireContext(),error.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
        rq.add(jor);
    }

    public void imagereq(String now){
        ImageRequest ir = new ImageRequest(urlgambar + now+".jpg",
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        img.setImageBitmap(response);
                    }
                },0,0,null,Bitmap.Config.RGB_565,
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        img.setImageResource(R.drawable.ic_launcher_background);
                        error.printStackTrace();
                    }
                });
        rq.add(ir);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    String urllike;
    String idku,idhome;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnlike:
                ctr++;
                if(ctr>=max){
                    ctr=0;
                }
                request();
                break;
            case R.id.btnnope:
                ctr++;
                if(ctr>=max){
                    ctr=0;
                }
                request();
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
