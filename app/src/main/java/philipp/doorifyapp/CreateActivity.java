package philipp.doorifyapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText nameEditText, emailEditText, passwortEditText, passwortWEditText, codeEditText;
    private Button createButton;
    private TextView zuLogin;

    private String TAG = "DOORIFY_FIREBASE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        nameEditText = (EditText) findViewById(R.id.create_name);
        emailEditText = (EditText) findViewById(R.id.create_email);
        passwortEditText = (EditText) findViewById(R.id.create_passwort);
        passwortWEditText = (EditText) findViewById(R.id.create_w_passwort);
        codeEditText = (EditText) findViewById(R.id.create_code);
        createButton = (Button) findViewById(R.id.create_create);
        zuLogin = (TextView) findViewById(R.id.create_login);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    startActivity(new Intent(CreateActivity.this, MainActivity.class));
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String passwort = passwortEditText.getText().toString();
                String passwortW = passwortWEditText.getText().toString();
                String code = codeEditText.getText().toString();

                if(!name.isEmpty() && !email.isEmpty() && !passwort.isEmpty() && !passwortW.isEmpty()){
                    if(passwort.equalsIgnoreCase(passwortW)){
                        createNewUser(email, passwort);
                    }else{
                        Snackbar.make(v, "Die Passwörter müssen übereinstimmen!", Snackbar.LENGTH_SHORT).show();
                    }
                }else {
                    Snackbar.make(v, "Alle Felder müssen ausgefüllt sein!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
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

    private void createNewUser(String email, String passwort){
        mAuth.createUserWithEmailAndPassword(email, passwort).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                if (!task.isSuccessful()) {
                    Toast.makeText(CreateActivity.this, "Fehler bei dem erstellen des Acounts", Toast.LENGTH_SHORT).show();
                }
                // ...
            }
        });
    }
}
