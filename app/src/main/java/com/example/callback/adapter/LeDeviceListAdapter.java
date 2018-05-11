package com.example.callback.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.callback.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2018-5-9.
 */
public class LeDeviceListAdapter extends ArrayAdapter<BluetoothDevice> {

    private Context mContext;
    /**
     * The resource indicating what views to inflate to display the content of this
     * array adapter.
     */
    private final int mResource;
    private List<BluetoothDevice> LeDevices = new ArrayList<>();

    public LeDeviceListAdapter(Context context, int resource, List<BluetoothDevice> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BluetoothDevice bluetoothDevice = getItem(position);
        View view;
        TextView bluetoothDeviceName;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(mResource, null);
            bluetoothDeviceName = view.findViewById(R.id.device_name);
            bluetoothDeviceName.setText(bluetoothDevice.getName());
            view.setTag(bluetoothDeviceName);
        } else {
            view = convertView;
            bluetoothDeviceName =(TextView) view.getTag();
        }
        bluetoothDeviceName.setText(bluetoothDevice.getName());
        return view;
    }




    public void addDevice(BluetoothDevice device) {
        if (device != null && device.getName() != null) {
            for (BluetoothDevice bluetoothDevice : LeDevices) {
                if (bluetoothDevice.equals(device)) return;
            }
            LeDevices.add(device);
            super.add(device);
        }
    }
}
