package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.LayoutInflaterCompat;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class UserNameDialogFragment extends DialogFragment {
    public interface UserNameDialogListener {
        public void onDialogPositiveClick(UserNameDialogFragment dialog);
    }

    private UserNameDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (UserNameDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(requireActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText userName = view.findViewById(R.id.userNameText);
        if (getArguments() != null && TextUtils.isEmpty(getArguments().getString("userName"))) {
            userName.setText(getArguments().getString("userName"));
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction.
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_layout, null);
        EditText userName = layout.findViewById(R.id.userNameText);
        EditText serverUrl = layout.findViewById(R.id.serverUrlText);
        if (getArguments() != null) {
            userName.setText(getArguments().getString("userName"));
            serverUrl.setText(getArguments().getString("serverUrl"));
        }
        builder.setView(layout)
                .setMessage(R.string.dialog_caption)
                .setPositiveButton(R.string.ok, (dialog, id) -> {
                    if (getArguments() != null) {
                        getArguments().putString("userName", userName.getText().toString());
                        getArguments().putString("serverUrl", serverUrl.getText().toString());
                    }
                    listener.onDialogPositiveClick(this);
                    dialog.cancel();
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    dialog.cancel();
                });
        // Create the AlertDialog object and return it.
        return builder.create();
    }
}