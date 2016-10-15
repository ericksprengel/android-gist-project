package br.com.ericksprengel.aa.retrofit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.ericksprengel.aa.retrofit.model.Gist;

/**
 * A placeholder fragment containing a simple view.
 */
public class GistFragment extends BaseAppFragment {

    public static final String ARG_GIST_ID = "ARG_GIST_ID";

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getActionBar().setTitle("Gist");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.app_fragment_gist, container, false);

        String id = getArguments().getString(ARG_GIST_ID);
        Gist gist = getAppActivity().getDataFragment().getGist(id);

        TextView description = (TextView) mainView.findViewById(R.id.description);
        description.setText(gist.description);

        return mainView;
    }
}
