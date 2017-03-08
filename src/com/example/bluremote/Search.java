package com.example.bluremote;

import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Search extends Activity 
{
	TextView tv;
	Button search;
	ListView list;
	BluetoothAdapter adp;
	ArrayAdapter<String> aa;
	ArrayList<BluetoothDevice> devices;
	Set<BluetoothDevice> pairedDevices ;
	BroadcastReceiver mReceiver;
	IntentFilter devfound;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		list=(ListView) findViewById(R.id.listView1);
		adp=BluetoothAdapter.getDefaultAdapter();
		aa=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
		list.setAdapter(aa);
		devices= new ArrayList<BluetoothDevice>();
		devfound=new IntentFilter(BluetoothDevice.ACTION_FOUND);
		/*IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
       
		this.registerReceiver(mReceiver, filter);


        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
         this.registerReceiver(mReceiver, filter);*/
        
			
			//at=new AcceptThread();
			//at.start();
			
      //   search.setOnClickListener(new OnClickListener() {
			
			//@Override
			//public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				Set<BluetoothDevice> pairedDevices = adp.getBondedDevices();
				if (pairedDevices.size() > 0) {
				    // Loop through paired devices
				    for (BluetoothDevice device : pairedDevices) {
				        // Add the name and address to an array adapter to show in a ListView
				        aa.add(device.getName() + "\n" + device.getAddress());
				    	devices.add(device);
				        aa.notifyDataSetChanged();
				    }
				}

				adp.startDiscovery();
		 mReceiver=new BroadcastReceiver() {
					
					@Override
					public void onReceive(Context context, Intent intent) {
						// TODO Auto-generated method stub
						String action=intent.getAction();
						if(BluetoothDevice.ACTION_FOUND.equals(action))
						{
							BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
							if(!devices.contains(device))
							{
								aa.add(device.getName() + "\n" + device.getAddress());
								devices.add(device);
								aa.notifyDataSetChanged();
							}
						}
					}
				};
				registerReceiver(mReceiver, devfound);
			//}
	//	});
         
         list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				//BluetoothDevice tmp=devices.get(arg2);
				Toast.makeText(getApplicationContext(), "Connecting...", Toast.LENGTH_SHORT).show();
				ConnectThread ct=new ConnectThread(devices.get(arg2),getApplicationContext());
				ct.run();
				
				finish();
					//Toast.makeText(getApplicationContext(), "Device connectd", Toast.LENGTH_SHORT).show();
					/*Intent i=new Intent(getApplicationContext(),Chat.class);
					startActivity(i);*/
				
				
				/*BluetoothSocket tmp=ct.getSocket();
				try {
					OutputStream os=tmp.getOutputStream();
					String s="hello";
					InputStream ip=tmp.getInputStream();
					
					byte [] buffer=new byte[1024];
					ip.read(buffer);
					String tp=new String(buffer);
					tv.setText(tp+"");
					os.write(s.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			}
		});
        
         
    }
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(mReceiver);
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		registerReceiver(mReceiver, devfound);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		registerReceiver(mReceiver,devfound);
		unregisterReceiver(mReceiver);
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);

		
		
		return true;
	}
}