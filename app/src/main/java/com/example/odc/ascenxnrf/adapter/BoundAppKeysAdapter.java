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

package com.example.odc.ascenxnrf.adapter;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import no.nordicsemi.android.meshprovisioner.configuration.MeshModel;
import com.example.odc.ascenxnrf.ModelConfigurationActivity;
import com.example.odc.ascenxnrf.R;
import com.example.odc.ascenxnrf.widgets.RemovableViewHolder;

public class BoundAppKeysAdapter extends RecyclerView.Adapter<BoundAppKeysAdapter.ViewHolder> {

    private final ArrayList<String> mAppKeys = new ArrayList<>();
    private final Context mContext;
    private Map<Integer, String> mBoundAppKeys = new LinkedHashMap<>();
    private OnItemClickListener mOnItemClickListener;

    public BoundAppKeysAdapter(final ModelConfigurationActivity activity, final LiveData<MeshModel> meshModelLiveData) {
        this.mContext = activity;
        meshModelLiveData.observe(activity, meshModel -> {
            if(meshModel != null) {
                if (meshModel.getBoundAppkeys() != null) {
                    mBoundAppKeys.putAll(meshModel.getBoundAppkeys());
                    mAppKeys.clear();
                    mAppKeys.addAll(meshModel.getBoundAppkeys().values());
                    notifyDataSetChanged();
                }
            }
        });
    }

    public void setOnItemClickListener(final BoundAppKeysAdapter.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public BoundAppKeysAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View layoutView = LayoutInflater.from(mContext).inflate(R.layout.bound_app_key_item, parent, false);
        return new BoundAppKeysAdapter.ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(final BoundAppKeysAdapter.ViewHolder holder, final int position) {
        if(mAppKeys.size() > 0) {
            final String appKey = mAppKeys.get(position);
            final Integer appKeyIndex = getAppKeyIndex(appKey);
            if(appKeyIndex != null) {
                holder.appKeyId.setText(mContext.getString(R.string.app_key_index_item, appKeyIndex));
                holder.appKey.setText(appKey.toUpperCase());
            }
        }
    }

    private Integer getAppKeyIndex(final String appKey){
        for(Integer key : mBoundAppKeys.keySet()){
            if(mBoundAppKeys.get(key).equals(appKey)){
                return key;
            }
        }
        return null;
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mAppKeys.size();
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public String getAppKey(final int position) {
        if(!mAppKeys.isEmpty()){
            return mAppKeys.get(position);
        }
        return null;
    }

    @FunctionalInterface
    public interface OnItemClickListener {
        void onItemClick(final int position, final String appKey);
    }

    public final class ViewHolder extends RemovableViewHolder {

        @BindView(R.id.app_key_id)
        TextView appKeyId;
        @BindView(R.id.app_key)
        TextView appKey;

        private ViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.findViewById(R.id.removable).setOnClickListener(v -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(getAdapterPosition(), mAppKeys.get(getAdapterPosition()));
                }
            });
        }
    }
}