package com.example.bluremote;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.R.color;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class MainActivity extends ActionBarActivity implements OnGestureListener,OnDoubleTapListener 
{
	
	Button left=null;
	Button bkspace=null;
	Button clr=null;
	static DataOutputStream out;
	GestureDetector mGestureDetector;
	Button right=null,on=null;
	static EditText et=null;
	static TextView trackpad;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final String DEVICE_NAME = "device_name";
	BluetoothAdapter adp=null;
	
	public static final int MENU_SEARCH = Menu.FIRST;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 left=(Button) findViewById(R.id.button1);
		 right=(Button) findViewById(R.id.button2);
		 mGestureDetector=new GestureDetector(getApplicationContext(), this);
		 mGestureDetector.setIsLongpressEnabled(false);
		 bkspace=(Button) findViewById(R.id.button3);
		 clr=(Button) findViewById(R.id.button4);
		// mGestureDetector.setOnDoubleTapListener(this);
		 trackpad=(TextView) findViewById(R.id.textView1);
		 trackpad.setEnabled(false);
		 trackpad.setBackgroundColor(Color.BLACK);
		 trackpad.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				Log.d("ontouch", "true");
				 if (out==null) 
				 {
					 Toast.makeText(getApplicationContext(), "You are not connected", Toast.LENGTH_SHORT).show();
				 }
				 else
				  return mGestureDetector.onTouchEvent(arg1);
				return true;
				 
			}
		});
		 
		 final TextWatcher tw=new TextWatcher() {
			
			 @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
				@Override
				public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
					// TODO Auto-generated method stub
					try {
						char c=arg0.charAt(arg1);
						out.writeInt(Protocol.ACTION_KEYBOARD);
						out.writeChar(c);
						out.flush();
					} catch (IndexOutOfBoundsException | IOException e) {
						// TODO Auto-generated catch block
						try {
							out.flush();
							out.writeInt(Protocol.ACTION_KEYBOARD_BK_SPACE);
							out.flush();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}catch (NullPointerException e2) {
							// TODO: handle exception
							Toast.makeText(getApplicationContext(), "You are not connected", Toast.LENGTH_SHORT).show();
						}
						
					}
					catch (NullPointerException e2) {
						// TODO: handle exception
						Toast.makeText(getApplicationContext(), "You are not connected", Toast.LENGTH_SHORT).show();
					}
				}
				
				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
						int arg3) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable arg0) {
					// TODO Auto-generated method stub
					
				}
			
		};
		 et=(EditText) findViewById(R.id.editText1);
		 et.addTextChangedListener(tw);
		 et.setEnabled(false);
		 clr.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					et.removeTextChangedListener(tw);
					et.setText("");
					et.addTextChangedListener(tw);
				}
			});
		 bkspace.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(out==null)
					Toast.makeText(getApplicationContext(), "You are not connected", Toast.LENGTH_SHORT).show();
				else
				{
					try {
						out.writeInt(Protocol.ACTION_KEYBOARD_BK_SPACE);
						out.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		});
		 adp=BluetoothAdapter.getDefaultAdapter();
		 if(!adp.isEnabled())
		 {
			Intent enablebt= new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enablebt,2);
		 }
		
		 left.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent e) {
				// TODO Auto-generated method stub
				if (out==null) 
				 {
					 Toast.makeText(getApplicationContext(), "You are not connected", Toast.LENGTH_SHORT).show();
				 }
				 else
				 {
					 switch(e.getAction())
					 {
					 case MotionEvent.ACTION_DOWN:
						 v.setPressed(true);
						 writeMessage(Protocol.ACTION_MOUSE_LEFT_BUTTON_DOWN);
						 return true;
					 case MotionEvent.ACTION_UP:
						 v.setPressed(false);
						 writeMessage(Protocol.ACTION_MOUSE_LEFT_BUTTON_UP);
						 return true;
					 }
				 }
				return false;
			}
		});
		 right.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent e) {
				// TODO Auto-generated method stub
				if (out==null) 
				 {
					 Toast.makeText(getApplicationContext(), "You are not connected", Toast.LENGTH_SHORT).show();
				 }
				 else
				 {
					 switch(e.getAction())
					 {
					 case MotionEvent.ACTION_DOWN:
						 v.setPressed(true);
						 writeMessage(Protocol.ACTION_MOUSE_RIGHT_BUTTON_DOWN);
						 return true;
					 case MotionEvent.ACTION_UP:
						 v.setPressed(false);
						 writeMessage(Protocol.ACTION_MOUSE_RIGHT_BUTTON_UP);
						 return true;
					 }
					 
				 }
				return false;
			}
		});
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	
	public static void setSocket(BluetoothSocket s) throws IOException 
	{
		out=new DataOutputStream(s.getOutputStream());
		et.setEnabled(true);
		trackpad.setEnabled(true);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		menu.add(Menu.NONE,MENU_SEARCH,Menu.NONE,"Search");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if(id==MENU_SEARCH)
			 startActivity(new Intent(getApplicationContext(),Search.class));
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		writeMessage(Protocol.ACTION_MOVE, (int) arg2, (int) arg3);

		
		return true;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDoubleTap(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	boolean mmDoubleClickConfirmed;
	long mmMovesAfterDoubleClick = 0;
	float myLastTouchX, myLastTouchY;
	final long doubleClickMovesErrorRate = 3;
	@Override
	public boolean onDoubleTapEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		final float xDelta, yDelta;
		switch (arg0.getAction()) 
		{
		case MotionEvent.ACTION_DOWN:
			mmDoubleClickConfirmed=true;
			mmMovesAfterDoubleClick=0;
			writeMessage(Protocol.ACTION_MOUSE_LEFT_BUTTON_DOWN);
			myLastTouchX=arg0.getX();
			myLastTouchY=arg0.getY();
			return true;
		case MotionEvent.ACTION_MOVE:
			mmDoubleClickConfirmed = false;
			mmMovesAfterDoubleClick++;
			xDelta = myLastTouchX - arg0.getX();
			yDelta = myLastTouchY - arg0.getY();
			writeMessage(Protocol.ACTION_MOVE, (int)xDelta, (int)yDelta);
			return true;
		case MotionEvent.ACTION_UP:
			if (mmDoubleClickConfirmed || mmMovesAfterDoubleClick > doubleClickMovesErrorRate)
			{
				// DoubleClick
				writeMessage(Protocol.ACTION_MOUSE_LEFT_BUTTON_UP);
				writeMessage(Protocol.ACTION_SINLE_TAPP);
			} else
			{
				// Drag
				writeMessage(Protocol.ACTION_MOUSE_LEFT_BUTTON_UP);
			}
			return true;

		

		default:
			break;
		}
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent arg0) {
		// TODO Auto-generated method stub
		writeMessage(Protocol.ACTION_SINLE_TAPP);

		return false;
	}

	private void writeMessage(int msg, int x, int y)
	{
		try
		{
			out.writeInt(msg);
			out.writeInt(x);
			out.writeInt(y);
			out.flush();
		} catch (IOException e)
		{
			Log.d("exception", e.getMessage());
		}
	}

	private void writeMessage(int msg)
	{
		try
		{
			out.writeInt(msg);
			out.flush();
		} catch (IOException e)
		{
			Log.d("exception", e.getMessage());
		}

	}




}
