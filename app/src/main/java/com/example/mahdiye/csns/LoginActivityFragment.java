package com.example.mahdiye.csns;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

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
                if(validateUser(username, password)) {
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

    public boolean validateUser(String username, String password) {
        AuthenticationTask authTask = new AuthenticationTask();
        authTask.execute(username, password);
        return userValid;
    }

    public class AuthenticationTask extends AsyncTask<String, Void, Void> {

        private String LOG_TAG = AuthenticationTask.class.getSimpleName();

        @Override
        protected Void doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String jsonString = null;

            try {
                Uri buildUri = Uri.parse(BuildConfig.LOGIN_BASE_URL).buildUpon()
                        .appendQueryParameter("username", params[0])
                        .appendQueryParameter("password", params[1]).build();

                URL url = new URL(buildUri.toString());

                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("POST");
                connection.connect();

                InputStream is = connection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(is != null) {
                    reader = new BufferedReader(new InputStreamReader(is));
                    String line;

                    while((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                    }
                }

                if(buffer.length() > 0){
                    jsonString = buffer.toString();
                    saveUserToken(jsonString);
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

        public void saveUserToken(String jsonString){
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                if(jsonObject.getString("status").equals("200")) {
                    setSharedValues(getString(R.string.user_token_key), jsonObject.getString("token"), getContext());

                    /*SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(getString(R.string.user_token_key), jsonObject.getString("token"));
                    editor.commit();*/
                    userValid = true;
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error Parsing JSON", e);
            }
        }

        public void setSharedValues(String key, String value, Context context) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(key, value);
            editor.commit();
        }

        public String getSharedValues(String key, Context context) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getString(key, null);
        }
    }
}
