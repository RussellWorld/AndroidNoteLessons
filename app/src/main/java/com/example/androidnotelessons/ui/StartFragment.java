package com.example.androidnotelessons.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.androidnotelessons.FragmentHandler;
import com.example.androidnotelessons.MainActivity;
import com.example.androidnotelessons.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

public class StartFragment extends Fragment {

    private static final int RC_SING_IN = 40404;
    private static final String TAG = "GoogleAuth";

    private GoogleSignInClient googleSignInClient;
    private SignInButton buttonSignInGoogle;
    private MaterialButton buttonSignOutGoogle;
    private TextView emailView;
    private MaterialButton continue_;

    public StartFragment() {
        // Required empty public constructor
    }


    public static StartFragment newInstance() {
        StartFragment fragment = new StartFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        initGoogleSingIn();
        initView(view);
        enableSign();
        return view;
    }

    private void initGoogleSingIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(getContext(), gso);
    }

    private void initView(View view) {
        buttonSignInGoogle = view.findViewById(R.id.google_sign_in_button);
        buttonSignInGoogle.setOnClickListener(v -> signIn());
        emailView = view.findViewById(R.id.email_text_view);
        continue_ = view.findViewById(R.id.continue_button);
        continue_.setOnClickListener(v -> FragmentHandler.replaceFragment(requireActivity(), new NotesFragment(), R.id.notes, false, true, false));
        buttonSignOutGoogle = view.findViewById(R.id.google_sign_out_button);
        buttonSignOutGoogle.setOnClickListener(v -> signOut());
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if (account != null) {
            disableSign();
            updateUI(account.getEmail());
        }
    }

    private void updateUI(String email) {
        emailView.setText(email);
    }

    private void signOut() {
        googleSignInClient.signOut().addOnCompleteListener(task -> {
            updateUI("");
            enableSign();
        });
    }

    private void signIn() {
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SING_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SING_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            disableSign();
            updateUI(account.getEmail());
        } catch (ApiException e) {
            Log.w(TAG, "singInResult:failed code = " + e.getStatusCode());
        }
    }

    private void enableSign() {
        buttonSignInGoogle.setEnabled(true);
        continue_.setEnabled(false);
        buttonSignOutGoogle.setEnabled(false);
        MainActivity activity = (MainActivity) requireActivity();
        activity.setAuthorized(false);
    }

    private void disableSign() {
        buttonSignInGoogle.setEnabled(false);
        continue_.setEnabled(true);
        buttonSignOutGoogle.setEnabled(true);
        MainActivity activity = (MainActivity) requireActivity();
        activity.setAuthorized(true);
    }

}