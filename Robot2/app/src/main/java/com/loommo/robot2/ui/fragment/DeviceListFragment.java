package com.loommo.robot2.ui.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loommo.robot2.R;
import com.loommo.robot2.bean.Device;
import com.loommo.robot2.listener.OnClickDevicesItemListener;
import com.loommo.robot2.ui.adapter.BluetoothDeviceListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceListFragment extends DialogFragment {

    @Bind(R.id.list_device)
    RecyclerView listDevice;
    @Bind(R.id.progress_find_device)
    ProgressBar progressFindDevice;

    private List<Device> mDevicesInfo = new ArrayList<>();
    private List<BluetoothDevice> mDeviceList = new ArrayList<>();
    private BluetoothDeviceListAdapter mDevicesAdapter = null;

    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothReceiver mBluetoothReceiver = new BluetoothReceiver();

    private OnClickDevicesItemListener onClickDevicesItemListener = null;
    private Handler mHandler = new Handler();

    public DeviceListFragment() {
        // Required empty public constructor
    }

    public void setOnClickDevicesItemListener(OnClickDevicesItemListener onClickDevicesItemListener) {
        this.onClickDevicesItemListener = onClickDevicesItemListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_device_list, container, false);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initData() {
        //注册广播接收器
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mBluetoothReceiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getActivity().registerReceiver(mBluetoothReceiver, filter);

        listDevice.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (mDevicesAdapter == null) {
            mDevicesAdapter = new BluetoothDeviceListAdapter(getActivity(), mDevicesInfo, onClickDevicesItemListener);
        }
        listDevice.setAdapter(mDevicesAdapter);
    }

    @OnClick(R.id.button_find_device)
    public void OnButtonFindDeviceClick() {
        mHandler.post(showProgress);
        searchDevice();
    }

    @OnClick(R.id.button_cancel)
    public void OnButtonCancleClick() {
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }
        this.dismiss();
    }

    private void searchDevice() {
        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
                Toast.makeText(getActivity(), "您的设备不支持蓝牙", Toast.LENGTH_SHORT);
            }
        }

        mDeviceList.clear();
        mDevicesInfo.clear();

        //加载已匹配设备
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                Device deviceInfo = new Device(device.getName(), device.getAddress(), true);
                mDevicesInfo.add(deviceInfo);
                mDeviceList.add(device);
            }
            mDevicesAdapter.notifyDataSetChanged();
        }

        //搜索设备
        mBluetoothAdapter.startDiscovery();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(mBluetoothReceiver);
        ButterKnife.unbind(this);
    }

    private class BluetoothReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //找到设备
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    Device deviceInfo = new Device(device.getName(), device.getAddress());
                    mDevicesInfo.add(deviceInfo);
                    mDeviceList.add(device);
                    mHandler.post(refeshDevices);
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //搜索完成
                mHandler.post(hideProgress);
            }
        }
    }

    Runnable refeshDevices = new Runnable() {
        @Override
        public void run() {
            if (mDevicesAdapter != null) {
                mDevicesAdapter.notifyDataSetChanged();
            }
        }
    };

    Runnable showProgress = new Runnable() {
        @Override
        public void run() {
            if (progressFindDevice != null) {
                progressFindDevice.setVisibility(View.VISIBLE);
            }
        }
    };

    Runnable hideProgress = new Runnable() {
        @Override
        public void run() {
            if (progressFindDevice != null) {
                progressFindDevice.setVisibility(View.GONE);
            }
        }
    };
}
