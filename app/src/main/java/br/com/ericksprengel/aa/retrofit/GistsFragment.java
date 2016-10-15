package br.com.ericksprengel.aa.retrofit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.com.ericksprengel.aa.retrofit.api.GistService;
import br.com.ericksprengel.aa.retrofit.api.GistServiceBuilder;
import br.com.ericksprengel.aa.retrofit.model.Gist;
import br.com.ericksprengel.aa.retrofit.preferences.AppSharedPreferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class GistsFragment extends BaseAppFragment implements Callback<List<Gist>>, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, ChooseUserDialogFragment.ChooseUserDialogListener {

    private GistsFragmentAdapter mAdapter;
    private boolean mLoading;


    private View.OnClickListener mErrorListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            requestGists();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(BuildConfig.LOG_TAG, "GistsFragment: onCreateView");
        List<Gist> gists = getAppActivity().getDataFragment().getGists();

        View mainView = inflater.inflate(R.layout.app_fragment_gists, container, false);

        mAdapter = new GistsFragmentAdapter(gists, inflater);

        ListView gistListView = (ListView) mainView.findViewById(android.R.id.list);
        gistListView.setAdapter(mAdapter);
        gistListView.setOnItemClickListener(this);
        gistListView.setOnItemLongClickListener(this);

        if(gists == null) {
            requestGists();
        }

        return mainView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_app, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_about:
                Toast.makeText(getActivity(), "Developed by Erick M. Sprengel.\nContact: erick.sprengel@gmail.com", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_user:
                ChooseUserDialogFragment dialog = new ChooseUserDialogFragment(this);
                dialog.show(getFragmentManager(), "ChooseUserDialogFragment");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getActionBar().setTitle(R.string.app_name);
    }

    private void requestGists() {
        mLoading = true;

        String userLogin = AppSharedPreferences.getUserLogin(getActivity());

        GistService service = GistServiceBuilder.build();
        Call<List<Gist>> c = service.listGists(userLogin, null);
        c.enqueue(this);
    }

    private void openGist(Gist gist) {
        Bundle bundle = new Bundle();
        bundle.putString(GistFragment.ARG_GIST_ID, gist.id);
        getAppActivity().goToFragment(AppActivity.FRAGMENT_GIST, bundle, true);
    }

    @Override
    public void onResponse(Call<List<Gist>> call, Response<List<Gist>> response) {
        if(getAppActivity() == null) {
            return;
        }

        mLoading = false;
        if(!response.isSuccessful()) {
            showError("Erro interno. (" + response.code() + ")", mErrorListener, "Tentar Novamente");
            Log.e(BuildConfig.LOG_TAG, "Erro interno.!");
            return;
        }
        List<Gist> gists = response.body();
        mAdapter.setGists(gists);
        this.getAppActivity().getDataFragment().setGists(gists);
        showMainFragment();
        Log.d(BuildConfig.LOG_TAG, "Gists loded!");
    }

    @Override
    public void onFailure(Call<List<Gist>> call, Throwable t) {
        if(getAppActivity() == null) {
            return;
        }

        t.printStackTrace();

        showError("Erro de conexão.", mErrorListener, "Tentar Novamente");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Gist gist = (Gist) mAdapter.getItem(position);
        openGist(gist);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Gist gist = (Gist) mAdapter.getItem(position);
        DialogFragment dialogFragment = new GistDialog(gist, mAdapter);
        dialogFragment.show(getFragmentManager(), "gistDialog");
        return true;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String userLogin) {
        Toast.makeText(getActivity(), "Usuário: " + userLogin, Toast.LENGTH_LONG).show();
        AppSharedPreferences.setUserLogin(getActivity(), userLogin);
        requestGists();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Toast.makeText(getActivity(), "cancelado...", Toast.LENGTH_LONG).show();
    }


    @SuppressLint("ValidFragment")
    public static class GistDialog extends DialogFragment {

        Gist mGist;
        GistsFragmentAdapter mAdapter;

        public GistDialog(Gist gist, GistsFragmentAdapter adapter) {
            this.mGist = gist;
            this.mAdapter = adapter;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Deseja remover este Gist?")
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mAdapter.remove(mGist);
                            Toast.makeText(getActivity(), "boa", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}
