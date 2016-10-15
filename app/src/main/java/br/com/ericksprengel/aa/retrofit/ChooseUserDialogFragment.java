package br.com.ericksprengel.aa.retrofit;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class ChooseUserDialogFragment extends DialogFragment {


    public interface ChooseUserDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String userLogin);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    private final ChooseUserDialogListener mListener;

    public ChooseUserDialogFragment(ChooseUserDialogListener listener) {
        mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setMessage("Deseja remover este Gist?")
                .setView(R.layout.app_fragment_choose_user)
                .setPositiveButton("Atualizar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // ação: atualzar usuário
                        EditText userLoginEditText = (EditText) getDialog().findViewById(R.id.user_login);
                        String userLogin = userLoginEditText.getText().toString();
                        mListener.onDialogPositiveClick(ChooseUserDialogFragment.this, userLogin);
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // ação: cancelar dialog
                        mListener.onDialogNegativeClick(ChooseUserDialogFragment.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
