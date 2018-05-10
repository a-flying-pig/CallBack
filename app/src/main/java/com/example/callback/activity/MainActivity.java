package com.example.callback.activity;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.callback.callBackInterface.StartFinishCallBackInterface;
import com.example.callback.R;
import com.example.callback.workClass.WorkClass;

/**
 * MainActivity
 */
public class MainActivity extends Activity implements StartFinishCallBackInterface{

    private final String TAG = "MainActivity";
    private WorkClass workClass;
    private BluetoothAdapter mBluetoothAdapter;
    private Button mButton;
    private Context mContext;

    private final int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        workClass = new WorkClass();
        workClass.write(this);

        mContext = this;
        mButton = findViewById(R.id.scan);
        final BluetoothManager bluetoothManager =
                (BluetoothManager)getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityIntent = new Intent();
                activityIntent.setClass(mContext, DeviceListAdapterActivity.class);
                startActivity(activityIntent);
            }
        });
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


}
