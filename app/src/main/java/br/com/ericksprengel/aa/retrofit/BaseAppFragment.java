package br.com.ericksprengel.aa.retrofit;

import android.app.Fragment;
import android.view.View;

public abstract class BaseAppFragment extends Fragment {

    private static final int FRAGMENT_STATE_MAIN = 0;
    private static final int FRAGMENT_STATE_ERROR = 1;

    private int mFragmentState = FRAGMENT_STATE_MAIN;
    private String mErrorMessage;
    private String mErrorButtonText;
    private View.OnClickListener mErrorOnClickListener;

    @Override
    public void onResume() {
        super.onResume();

        AppActivity appActivity = getAppActivity();
        switch (mFragmentState) {
            case FRAGMENT_STATE_MAIN:
                appActivity.showMainFragment();
                break;
            case FRAGMENT_STATE_ERROR:
                appActivity.showError(mErrorMessage, mErrorOnClickListener, mErrorButtonText);
                break;

            default:
                break;
        }
    }

    public void showError(String message, View.OnClickListener onClickListener, String errorButtonText) {
        mFragmentState = FRAGMENT_STATE_ERROR;
        mErrorMessage = message;
        mErrorButtonText = errorButtonText;
        mErrorOnClickListener = onClickListener;
        getAppActivity().showError(message, onClickListener, errorButtonText);
    }

    public void showMainFragment() {
        mFragmentState = FRAGMENT_STATE_MAIN;
        getAppActivity().showMainFragment();
    }

    protected AppActivity getAppActivity() {
        return (AppActivity) this.getActivity();
    }
}
