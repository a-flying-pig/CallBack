package com.example.callback.activity;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.callback.callBackInterface.StartFinishCallBackInterface;
import com.example.callback.R;
import com.example.callback.workClass.WorkClass;

import org.w3c.dom.Text;

import java.util.UUID;

/**
 * MainActivity
 */
public class MainActivity extends Activity implements StartFinishCallBackInterface, View.OnClickListener{

    private final String TAG = "MainActivity";
    private WorkClass workClass;
    private BluetoothAdapter mBluetoothAdapter;
    private Button mScanButton;
    private Button mAdvertiseButton;
    private TextView mText;
    private Context mContext;

    private final int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        workClass = new WorkClass();
        workClass.write(this);

        mContext = this;
        mScanButton = findViewById(R.id.scan);
        mAdvertiseButton = findViewById(R.id.advertise);
        mText = findViewById(R.id.text);
        final BluetoothManager bluetoothManager =
                (BluetoothManager)getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        mScanButton.setOnClickListener(this);
        mAdvertiseButton.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult (int requestCode,int resultCode,Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (requestCode == RESULT_CANCELED) finish();
        }
    }

    @Override
    public void onStartWork() {
        // start work
        Log.d(TAG, "onStartWork");
    }

    @Override
    public void onFinishWork() {
        // finish work
        Log.d(TAG, "onFinishWork");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scan:
                Intent activityIntent = new Intent();
                activityIntent.setClass(mContext, DeviceListAdapterActivity.class);
                startActivity(activityIntent);
                break;
            case R.id.advertise:
                advertise();
                break;
            default:
                break;
        }
    }

    private void advertise() {
        if( !mBluetoothAdapter.isMultipleAdvertisementSupported() ) {
            Toast.makeText( this, "Multiple advertisement not supported", Toast.LENGTH_SHORT ).show();
            mAdvertiseButton.setEnabled( false );
        }
        BluetoothLeAdvertiser bluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();

        AdvertiseSettings advertiseSettings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
                .setConnectable(false)
                .setTimeout(1000)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
                .build();

        ParcelUuid parcelUuid = new ParcelUuid(UUID.fromString("CDB7950D-73F1-4D4D-8E47-C090502DBD63"));
        byte[] serviceData = "This is a test".getBytes();
        AdvertiseData advertiseData = new AdvertiseData.Builder()
                .addServiceUuid(parcelUuid)
                .addServiceData(parcelUuid, serviceData)
                .setIncludeDeviceName(true)
                .build();

    }
}
