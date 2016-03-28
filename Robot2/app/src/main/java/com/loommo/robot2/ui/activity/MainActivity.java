package com.loommo.robot2.ui.activity;

import android.support.v4.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.loommo.robot2.R;
import com.loommo.robot2.constant.Constant;
import com.loommo.robot2.listener.OnClickDevicesItemListener;
import com.loommo.robot2.ui.fragment.DeviceListFragment;

import java.io.IOException;
import java.io.OutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements OnClickDevicesItemListener {

    @Bind(R.id.edit_order)
    EditText editOrder;

    private DeviceListFragment deviceListFragment = null;

    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothSocket bluetoothSocket = null;
    private OutputStream outStream = null;

    private long exittime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //检查蓝牙设备是否打开
        initBluetooth();
    }

    private void initBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            showToast("您的设备不支持蓝牙", Toast.LENGTH_SHORT);
            finish();
            return;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.e(TAG, "- ON DESTROY -");
        if (outStream != null) {
            try {
                outStream.flush();
            } catch (IOException e) {
                Log.e(TAG, "ON PAUSE: Couldn't flush output stream.", e);
            }
        }

        if (bluetoothSocket != null) {
            try {
                bluetoothSocket.close();
            } catch (IOException e2) {
                Log.e(TAG, "ON PAUSE: Couldn't close bluetoothSocket.", e2);
            }
        }
    }

    @OnClick(R.id.button_top)
    public void onButtonTopClick() {
        if (bluetoothSocket != null) {
            try {
                outStream = bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Output stream creation failed.", e);
            }

            String message = "top";//发送内容

            byte[] msgBuffer = message.getBytes();

            try {
                outStream.write(msgBuffer);
                showToast("指令发送成功", Toast.LENGTH_SHORT);
            } catch (IOException e) {
                showToast("指令发送失败", Toast.LENGTH_SHORT);
                Log.e(TAG, "Exception during write.", e);
            }
        } else {
            showToast("请先连接设备", Toast.LENGTH_SHORT);
        }
    }

    @OnClick(R.id.button_down)
    public void onButtonDownClick() {
        if (bluetoothSocket != null) {
            try {
                outStream = bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Output stream creation failed.", e);
            }

            String message = "down";//发送内容

            byte[] msgBuffer = message.getBytes();

            try {
                outStream.write(msgBuffer);
                showToast("指令发送成功", Toast.LENGTH_SHORT);
            } catch (IOException e) {
                showToast("指令发送失败", Toast.LENGTH_SHORT);
                Log.e(TAG, "Exception during write.", e);
            }
        } else {
            showToast("请先连接设备", Toast.LENGTH_SHORT);
        }
    }

    @OnClick(R.id.button_left)
    public void onButtonLeftClick() {
        if (bluetoothSocket != null) {
            try {
                outStream = bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Output stream creation failed.", e);
            }

            String message = "left";//发送内容

            byte[] msgBuffer = message.getBytes();

            try {
                outStream.write(msgBuffer);
                showToast("指令发送成功", Toast.LENGTH_SHORT);
            } catch (IOException e) {
                showToast("指令发送失败", Toast.LENGTH_SHORT);
                Log.e(TAG, "Exception during write.", e);
            }
        } else {
            showToast("请先连接设备", Toast.LENGTH_SHORT);
        }
    }

    @OnClick(R.id.button_right)
    public void onButtonRightClick() {
        if (bluetoothSocket != null) {
            try {
                outStream = bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Output stream creation failed.", e);
            }

            String message = "right";//发送内容

            byte[] msgBuffer = message.getBytes();

            try {
                outStream.write(msgBuffer);
                showToast("指令发送成功", Toast.LENGTH_SHORT);
            } catch (IOException e) {
                showToast("指令发送失败", Toast.LENGTH_SHORT);
                Log.e(TAG, "Exception during write.", e);
            }
        } else {
            showToast("请先连接设备", Toast.LENGTH_SHORT);
        }
    }

    @OnClick(R.id.button_send_order)
    public void onButtonSendOrderClick() {
        if (bluetoothSocket != null) {
            try {
                outStream = bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Output stream creation failed.", e);
            }

            String message = editOrder.getText().toString();//发送内容

            byte[] msgBuffer = message.getBytes();

            try {
                outStream.write(msgBuffer);
                showToast("指令发送成功", Toast.LENGTH_SHORT);
            } catch (IOException e) {
                showToast("指令发送失败", Toast.LENGTH_SHORT);
                Log.e(TAG, "Exception during write.", e);
            }
        } else {
            showToast("请先连接设备", Toast.LENGTH_SHORT);
        }
    }

    @OnClick(R.id.button_choose_device)
    public void OnButtonChooseDevice() {
        if (deviceListFragment == null) {
            deviceListFragment = new DeviceListFragment();
            deviceListFragment.setOnClickDevicesItemListener(this);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();

        deviceListFragment.show(fragmentManager, "deviceListDialog");
    }

    @Override
    public void OnDeviceItemClick(String address) {
        Log.e(TAG, "ABOUT TO ATTEMPT CLIENT CONNECT");

        //连接MAC地址

        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

        mBluetoothAdapter.cancelDiscovery();
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bluetoothSocket = null;
        }

        try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(Constant.BLUETOOTH.M_UUID);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (!bluetoothSocket.isConnected()) {
                bluetoothSocket.connect();
            }
            showToast("设备连接成功", Toast.LENGTH_SHORT);
            deviceListFragment.dismiss();
        } catch (IOException e) {
            e.printStackTrace();
            showToast("设备连接失败，请重新尝试", Toast.LENGTH_SHORT);
            try {
                bluetoothSocket.close();
                bluetoothSocket = null;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            /*try {
                Log.e("", "trying fallback...");
                bluetoothSocket = (BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(device, 1);
                bluetoothSocket.connect();
                showToast("设备连接成功", Toast.LENGTH_SHORT);
                deviceListFragment.dismiss();
            } catch (Exception e2) {
                Log.e(TAG, "Couldn't establish Bluetooth connection!");
                showToast("设备连接失败，请重新尝试", Toast.LENGTH_SHORT);
                try {
                    bluetoothSocket.close();
                    bluetoothSocket = null;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }*/
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (System.currentTimeMillis() - exittime > 2000) {//已经点击两次
            showToast("再按一次退出程序", Toast.LENGTH_SHORT);
            exittime = System.currentTimeMillis();
        } else {
            ActivityCollector.finishAll();
        }
    }
}
