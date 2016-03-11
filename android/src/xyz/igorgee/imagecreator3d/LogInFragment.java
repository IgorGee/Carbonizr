package xyz.igorgee.imagecreator3d;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.Token;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.igorgee.shapwaysapi.Client;

import static xyz.igorgee.utilities.UIUtilities.makeSnackbar;

public class LogInFragment extends Fragment {
    public static final String EXTRA_URL = "xyz.igorgee.imagecreator3d.URL";

    Client client;

    @Bind(R.id.pin) EditText pin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_log_in, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.auth_url_button)
    public void signIn(View view) {
        new connectToClient().execute();
    }

    @OnClick(R.id.authorize)
    public void authenticate(View view) {
        if (client != null) {
            String verification = pin.getText().toString();
            if (verification.equals("")) {
                makeSnackbar(view, "Please enter your pin.");
            } else {
                client.setVerifier(verification);
                new ObtainAccessToken(view).execute();
            }
        } else {
            makeSnackbar(view, "Please click the \"Get Pin\" Button first.");
        }
    }

    private class connectToClient extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            client = new Client();
            Log.d("CLIENT", "successfully created");
            String url = client.getAuthorizationUrl();

            Intent intent = new Intent(getActivity(), AuthorizationWebViewActivity.class);
            intent.putExtra(EXTRA_URL, url);
            startActivity(intent);

            return null;
        }
    }

    private class ObtainAccessToken extends AsyncTask<Void, Void, Void> {

        View view;

        // For the Snackbar
        public ObtainAccessToken(View view) {
            this.view = view;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Log.d("CLIENT", "Gettin dat Access Token");

                client.retrieveAccessToken();
                Token accessToken = client.getAccessToken();

                if (accessToken.getToken() == null) {
                    throw new OAuthException("Didn't get Access Token.");
                } else {
                    SharedPreferences.Editor editor = getActivity().
                            getSharedPreferences(MainActivity.MY_PREF_NAME, Context.MODE_PRIVATE)
                            .edit();
                    editor.putString(MainActivity.ACCESS_TOKEN_VALUE, accessToken.getToken());
                    editor.putString(MainActivity.ACCESS_TOKEN_SECRET, accessToken.getSecret());
                    editor.apply();
                }

                HomePageFragment fragment = new HomePageFragment();

                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentPlaceholder, fragment)
                        .commit();
            } catch (OAuthException e) {
                makeSnackbar(view, "Sorry, try again.");
                e.printStackTrace();
            }

            return null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((DrawerLayout) getActivity().findViewById(R.id.drawerLayout)).setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        ButterKnife.unbind(this);
    }

}
