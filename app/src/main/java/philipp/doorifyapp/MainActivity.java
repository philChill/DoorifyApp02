package philipp.doorifyapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FrameLayout rootLayout;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    String TAG = "DOORIFY_FIREBASE";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    rootLayout.removeAllViews();
                    View v1 = getLayoutInflater().inflate(R.layout.main_tueren, null);
                    rootLayout.addView(v1);
                    runTuerenView();
                    return true;
                case R.id.navigation_dashboard:
                    rootLayout.removeAllViews();
                    View v2 = getLayoutInflater().inflate(R.layout.main_dashboard, null);
                    rootLayout.addView(v2);
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootLayout = (FrameLayout) findViewById(R.id.main_content);
        rootLayout.removeAllViews();
        View view = getLayoutInflater().inflate(R.layout.main_tueren, null);
        rootLayout.addView(view);
        runTuerenView();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    startActivity(new Intent(MainActivity.this, CreateActivity.class));
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void runTuerenView(){

        //ListView erstellen und füllen
        final ListView tuerenList;
        tuerenList = (ListView) findViewById(R.id.main_tueren_list);

        final ArrayList<mainListViewDataModel> data = new ArrayList<>();
        data.add(new mainListViewDataModel("Peter", "Zugriff immer erlaubt"));
        data.add(new mainListViewDataModel("Franz", "Zugriff immer erlaubt"));
        data.add(new mainListViewDataModel("Franz", "Zugriff zwischen 8 Uhr und 9 Uhr erlaubt"));
        if(data.isEmpty()){data.add(new mainListViewDataModel("Zugriffsrechte hinzufügen um Einträge zu sehen!", ""));}
        final mainListViewCustomAdapter adapter = new mainListViewCustomAdapter(data, this);
        tuerenList.setAdapter(adapter);



        //Listview Klickchek und Alert Dialog öffnen
        tuerenList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                String[] items = {"Einstellungen ändern", "Zutrittsrechte entziehen"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Einstellungen " + data.get(position).getName());
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 1){
                            Snackbar.make(view, "Zugriffsrechte für " + data.get(position).getName() + " wurden entfernt!", Snackbar.LENGTH_SHORT).show();
                            data.remove(position);
                            if(data.isEmpty()){data.add(new mainListViewDataModel("Zugriffsrechte hinzufügen um Einträge zu sehen!", ""));}
                            final mainListViewCustomAdapter adapter1 = new mainListViewCustomAdapter(data, MainActivity.this);
                            tuerenList.setAdapter(adapter1);
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

}
