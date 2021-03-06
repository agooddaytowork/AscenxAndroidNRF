/*
 * Copyright (c) 2018, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.example.odc.ascenxnrf.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.odc.ascenxnrf.R;

public class DialogFragmentGlobalNetworkName extends DialogFragment {

    private static final String NETWORK_NAME = "NETWORK_NAME";

    //UI Bindings
    @BindView(R.id.text_input_layout)
    TextInputLayout networkNameInputLayout;
    @BindView(R.id.text_input)
    TextInputEditText networkNameInput;

    private String mNetworkName;

    public static DialogFragmentGlobalNetworkName newInstance(final String networkName) {
        DialogFragmentGlobalNetworkName fragmentNetworkKey = new DialogFragmentGlobalNetworkName();
        final Bundle args = new Bundle();
        args.putString(NETWORK_NAME, networkName);
        fragmentNetworkKey.setArguments(args);
        return fragmentNetworkKey;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNetworkName = getArguments().getString(NETWORK_NAME);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final View rootView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_name, null);

        //Bind ui
        ButterKnife.bind(this, rootView);
        networkNameInputLayout.setHint(getString(R.string.hint_global_network_name));
        networkNameInput.setText(mNetworkName);
        networkNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                if (TextUtils.isEmpty(s.toString())) {
                    networkNameInputLayout.setError(getString(R.string.error_empty_network_key));
                } else {
                    networkNameInputLayout.setError(null);
                }
            }

            @Override
            public void afterTextChanged(final Editable s) {

            }
        });

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext()).setView(rootView)
                .setPositiveButton(R.string.ok, null).setNegativeButton(R.string.cancel, null);

        alertDialogBuilder.setIcon(R.drawable.ic_lan_black_alpha_24dp);
        alertDialogBuilder.setTitle(R.string.title_network_name);
        alertDialogBuilder.setMessage(R.string.summary_network_name);

        final AlertDialog alertDialog = alertDialogBuilder.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
            final String networkKey = networkNameInput.getText().toString();
            if (validateInput(networkKey)) {
                if(getParentFragment() == null) {
                    ((DialogFragmentNetworkNameListener) getActivity()).onNetworkNameEntered(networkKey);
                } else {
                    ((DialogFragmentNetworkNameListener) getParentFragment()).onNetworkNameEntered(networkKey);
                }
                dismiss();
            }
        });

        return alertDialog;
    }

    private boolean validateInput(final String input) {
        if(TextUtils.isEmpty(input)){
            networkNameInputLayout.setError(getString(R.string.error_empty_network_name));
            return false;
        }

        return true;
    }

    public interface DialogFragmentNetworkNameListener {

        void onNetworkNameEntered(final String networkKey);

    }
}
