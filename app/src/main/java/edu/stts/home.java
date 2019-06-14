package edu.stts;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class home extends AppCompatActivity implements main.OnFragmentInteractionListener,chat.OnFragmentInteractionListener,profile.OnFragmentInteractionListener{

    private FragmentTransaction Frag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Frag = getSupportFragmentManager().beginTransaction();
        Frag.replace(R.id.mainFrag,new main());
        Frag.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.logout){
            startActivity(new Intent(home.this,MainActivity.class));
            finish();
        }
        else {
            Frag = getSupportFragmentManager().beginTransaction();
            if(item.getItemId()==R.id.chat) {
                Frag.replace(R.id.mainFrag, new chat());
            }
            else if(item.getItemId()==R.id.Home){
                Frag.replace(R.id.mainFrag,new main());
            }
            else if(item.getItemId()==R.id.profile){
                Frag.replace(R.id.mainFrag,new profile());
            }
            Frag.commit();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
