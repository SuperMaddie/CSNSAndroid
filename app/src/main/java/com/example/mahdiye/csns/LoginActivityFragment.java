package com.example.mahdiye.csns;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

    boolean userValid = false;

    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;

    public LoginActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        loginButton = (Button)rootView.findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                usernameEditText = (EditText)rootView.findViewById(R.id.edittext_username);
                passwordEditText = (EditText)rootView.findViewById(R.id.edittext_password);
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                /* check user credentials */
                validateUser(username, password);
                if(userValid) {
                    startMainActivity();
                }else {
                    /* show error and stay in login */
                }
            }
        });

        return rootView;
    }

    private void startMainActivity() {
        getActivity().finish();
    }

    public void validateUser(String username, String password) {
        AuthenticationTask authTask = new AuthenticationTask();
        authTask.execute(username, password);
    }

    public class AuthenticationTask extends AsyncTask<String, Void, Void> {

        private String LOG_TAG = AuthenticationTask.class.getSimpleName();

        @Override
        protected Void doInBackground(String... params) {
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
                        saveUserToken(token);
                    }
                }else{
                    /* go back to login page */
                    Log.e(LOG_TAG, "status : " + status);
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
            return null;
        }

        public void saveUserToken(String token){
            SharedPreferencesUtil.setSharedValues(getString(R.string.user_token_key), token, getContext());
            userValid = true;
        }

    }
}
