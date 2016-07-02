package com.example.mahdiye.csns;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.mahdiye.csns.utils.SharedPreferencesUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginActivityFragment extends Fragment {

    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;
    int sourceActivity;

    public LoginActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getActivity().getIntent();
        if(intent != null && intent.hasExtra(getString(R.string.source_activity))){
            sourceActivity = intent.getIntExtra(getString(R.string.source_activity), LoginActivity.SourceActivity.MAIN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        loginButton = (Button)rootView.findViewById(R.id.button_login);
        loginButton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        //v.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        //v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                loginButton.setAlpha(.5f);
                loginButton.setEnabled(false);
                usernameEditText = (EditText)rootView.findViewById(R.id.edittext_username);
                passwordEditText = (EditText)rootView.findViewById(R.id.edittext_password);
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if(username == null || password == null) {
                    /* show an error in login page username and password must be filled */
                }else{
                    /* check user credentials */
                    validateUser(username, password);
                }
            }
        });

        return rootView;
    }

    public void validateUser(String username, String password) {
        AuthenticationTask authTask = new AuthenticationTask();
        authTask.execute(username, password);
    }

    public class AuthenticationTask extends AsyncTask<String, Void, String> {

        private String LOG_TAG = AuthenticationTask.class.getSimpleName();

        LinearLayout linearLayoutHeaderProgress = (LinearLayout) getActivity().findViewById(R.id.linearlayoutHeaderProgress);
        @Override
        protected void onPreExecute() {
            //linearLayoutHeaderProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String token = null;

            try {
                Uri buildUri = Uri.parse(BuildConfig.LOGIN_BASE_URL).buildUpon()
                        .appendQueryParameter("username", params[0])
                        .appendQueryParameter("password", params[1]).build();

                URL url = new URL(buildUri.toString());

                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                int status = connection.getResponseCode();
                if(status == HttpURLConnection.HTTP_OK) {
                    InputStream is = connection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if(is != null) {
                        reader = new BufferedReader(new InputStreamReader(is));
                        String line;

                        buffer.append(reader.readLine());
                    }

                    if(buffer.length() > 0){
                        token = buffer.toString();
                    }
                }else{
                    Log.e(LOG_TAG, "status : " + status);
                    /* show alert in login page */
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog.Builder alert  = new AlertDialog.Builder(getActivity());
                            alert.setMessage("Invalid Username or Password");
                            alert.setTitle("Authentication Faild");
                            alert.setPositiveButton("OK", null);
                            alert.setCancelable(true);
                            alert.create().show();

                            alert.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                        }
                    });
                }
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "MalformedURLException", e);
            } catch (IOException e) {
                Log.e(LOG_TAG, "IOException", e);
            }finally{
                if(connection != null){
                    connection.disconnect();
                }
                if(reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return token;
        }

        @Override
        protected void onPostExecute(String token) {
            //linearLayoutHeaderProgress.setVisibility(View.GONE);
            loginButton.setAlpha(1.f);
            loginButton.setEnabled(true);

            if(token != null) {
                saveUserToken(token);
                if(sourceActivity == LoginActivity.SourceActivity.MAIN) {
                    /* kill old version of main activity and start a new one */
                    finishMainActivity();
                    startMainActivity();
                }else if(sourceActivity == LoginActivity.SourceActivity.SURVEY){
                    /* finish the activity and it's parents redirect to main */
                    finishMainActivity();
                    finishSurveyActivity();
                    startMainActivity();
                }else if(sourceActivity == LoginActivity.SourceActivity.SURVEY_DECRIPTION){
                    /* finish the activity and it's parents redirect to main */
                    finishMainActivity();
                    finishSurveyActivity();
                    finishSurveyDescriptionActivity();
                    startMainActivity();
                }
            }else {
                Log.e(LOG_TAG, "User is not valid");
            }
        }

        public void saveUserToken(String token){
            SharedPreferencesUtil.setSharedValues("check", "check", getContext());
            SharedPreferencesUtil.setSharedValues(getString(R.string.user_token_key), token, getContext());
        }

        public void finishMainActivity(){
            MainActivity.mainActivity.finish();
        }

        private void startMainActivity() {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

        public void finishSurveyActivity(){
            SurveyActivityFragment.surveyActivity.finish();
        }

        public void finishSurveyDescriptionActivity(){
            SurveyDescriptionActivityFragment.surveyDescriptionActivity.finish();
        }

        private void startSurveyActivity() {
            Intent intent = new Intent(getActivity(), SurveyActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

    }
}
