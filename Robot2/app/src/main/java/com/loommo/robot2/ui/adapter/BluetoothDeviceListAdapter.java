package com.loommo.robot2.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loommo.robot2.R;
import com.loommo.robot2.bean.Device;
import com.loommo.robot2.listener.OnClickDevicesItemListener;

import java.util.List;

/**
 * Created by loommo on 16/3/25.
 */
public class BluetoothDeviceListAdapter extends RecyclerView.Adapter<BluetoothDeviceListAdapter.ViewHolder> {
    private Context context;
    private List<Device> deviceList;
    private OnClickDevicesItemListener onClickDevicesItemListener;

    public BluetoothDeviceListAdapter(Context context, List<Device> deviceList, OnClickDevicesItemListener onClickDevicesItemListener) {
        this.context = context;
        this.deviceList = deviceList;
        this.onClickDevicesItemListener = onClickDevicesItemListener;
    }

    public void setDeviceList(List<Device> deviceList) {
        this.deviceList = deviceList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_device, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.initData(deviceList.get(position));
    }

    @Override
    public int getItemCount() {
        if (deviceList == null) {
            return 0;
        }
        return deviceList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    final class ViewHolder extends RecyclerView.ViewHolder {

        private TextView itemName;
        private TextView itemAddress;
        private TextView itemBonded;
        private LinearLayout itemLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            itemName = (TextView) itemView.findViewById(R.id.text_device_name);
            itemAddress = (TextView) itemView.findViewById(R.id.text_device_address);
            itemBonded = (TextView) itemView.findViewById(R.id.text_device_bonded);
            itemLayout = (LinearLayout) itemView.findViewById(R.id.layout_device);
        }

        public void initData(final Device device) {
            itemName.setText(device.getName());
            itemAddress.setText(device.getAddress());
            if (device.isBonded()) {
                itemBonded.setVisibility(View.VISIBLE);
            } else {
                itemBonded.setVisibility(View.GONE);
            }
            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickDevicesItemListener != null) {
                        onClickDevicesItemListener.OnDeviceItemClick(device.getAddress());
                    }
                }
            });
        }
    }
}
