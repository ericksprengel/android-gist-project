package br.com.ericksprengel.aa.retrofit;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AppActivity extends Activity {

    public static final int FRAGMENT_GISTS = 0x10;
    public static final int FRAGMENT_GIST = 0x20;

    private View mFragment;
    private RetainedFragment mDataFragment;

    private View mAppErrorView;
    private TextView mAppErrorMessageView;
    private Button mAppErrorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        mAppErrorView = findViewById(R.id.layout_app_error_main_view);
        mAppErrorButton = ((Button) mAppErrorView.findViewById(R.id.layout_app_error_button));
        mAppErrorMessageView = (TextView) findViewById(R.id.layout_app_error_message);

        mFragment = findViewById(R.id.app_fragment_container);

        // find the retained fragment on activity restarts
        FragmentManager fm = getFragmentManager();
        mDataFragment = (RetainedFragment) fm.findFragmentByTag("data");

        // create the fragment and data the first time
        if (mDataFragment == null) {
            // add the fragment
            mDataFragment = new RetainedFragment();
            fm.beginTransaction().add(mDataFragment, "data").commit();

            goToFragment(FRAGMENT_GISTS, null, true);
        }

    }

    @Override
    public void onBackPressed(){
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStack();
        } else {
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Toast.makeText(this, "Developed by Erick M. Sprengel.\nContact: erick.sprengel@gmail.com", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    public void goToFragment(int fragmentCode, Bundle args, boolean back ) {
        Fragment fragment;
        switch (fragmentCode) {
            case FRAGMENT_GISTS:
                fragment = new GistsFragment();
                break;
            case FRAGMENT_GIST:
                fragment = new GistFragment();
                break;

            default:
                throw new Error("Fragment with code " + fragmentCode + " not found");
        }

        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(back) {
            transaction.addToBackStack(fragment.getClass().getName());
        }
        transaction.replace(R.id.app_fragment_container, fragment)
                .commit();

    }

    public void showError(String message, View.OnClickListener onClickListener, String errorButtonText) {
        mAppErrorMessageView.setText(message);
        mAppErrorView.setVisibility(View.VISIBLE);
        mFragment.setVisibility(View.GONE);

        if (onClickListener != null){
            mAppErrorButton.setOnClickListener(onClickListener);
            mAppErrorButton.setText(errorButtonText);
            mAppErrorButton.setVisibility(View.VISIBLE);
        } else {
            mAppErrorButton.setVisibility(View.GONE);
        }
    }

    public void showMainFragment() {
        mAppErrorView.setVisibility(View.GONE);
        mFragment.setVisibility(View.VISIBLE);
    }

    public RetainedFragment getDataFragment() {
        return mDataFragment;
    }

    public void backFragment() {
        getFragmentManager().popBackStack();
    }
}
