package br.com.ericksprengel.aa.retrofit;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import br.com.ericksprengel.aa.retrofit.api.GistService;
import br.com.ericksprengel.aa.retrofit.api.GistServiceBuilder;
import br.com.ericksprengel.aa.retrofit.model.Gist;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class GistsFragment extends BaseAppFragment implements Callback<List<Gist>>, AdapterView.OnItemClickListener {

    private GistsFragmentAdapter mAdapter;
    private boolean mLoading;


    private View.OnClickListener mErrorListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            requestGists();
        }
    };

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

        if(gists == null) {
            requestGists();
        }

        return mainView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getActionBar().setTitle(R.string.app_name);
    }

    private void requestGists() {
        mLoading = true;
        GistService service = GistServiceBuilder.build();
        Call<List<Gist>> c = service.listGists("ericksprengel", null);
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
}
