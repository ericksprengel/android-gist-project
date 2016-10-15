package br.com.ericksprengel.aa.retrofit;

import android.app.Fragment;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import br.com.ericksprengel.aa.retrofit.model.Gist;


public class RetainedFragment extends Fragment {
    private List<Gist> mGists = null;


    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    public void setGists(List<Gist> gists) {
        this.mGists = gists;
    }

    public List<Gist> getGists() {
        return mGists;
    }

    public Gist getGist(String id) {
        for(int i = 0; i < mGists.size(); i++) {
            Gist gist = mGists.get(i);
            if(gist.id == id) {
                return gist;
            }
        }
        throw new IllegalArgumentException("Invalid gist id");
    }
}
