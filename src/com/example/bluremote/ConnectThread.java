package com.example.bluremote;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class ConnectThread extends Thread
{
	private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    BluetoothAdapter mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
    UUID MY_UUID = UUID.fromString("04c6093b-0000-1000-8000-00805f9b34fb");
	private Context context;
    public BluetoothSocket getMmSocket() {
		return mmSocket;
	}
    public BluetoothDevice getMmDevice() {
		return mmDevice;
	}
    public ConnectThread(BluetoothDevice device,Context cont) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
    	context=cont;
        BluetoothSocket tmp = null;
        mmDevice = device;
 
        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) { }
        mmSocket = tmp;
    }
 
    public void run() {
        // Cancel discovery because it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();
 
        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
        	
            mmSocket.connect();
            Toast.makeText(context, "Connected to "+mmDevice.getName(), Toast.LENGTH_SHORT).show();
        } catch (IOException connectException) 
        {
        	
            // Unable to connect; close the socket and get out
            try {
            	Toast.makeText(context, "Could not connect", Toast.LENGTH_SHORT).show();
                mmSocket.close();
                
            } catch (IOException closeException) { }
            return;
        }
 
        // Do work to manage the connection (in a separate thread)
       // Chat.  manageConnectedSocket(mmSocket);
        try {
			MainActivity.setSocket(mmSocket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      
    }
 
    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }

}
