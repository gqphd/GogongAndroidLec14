package com.gqphd.samplecopyandtrans;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.widget.Toast;

/**
 * @author GQ
 * http://developer.android.com/guide/components/services.html
 */
public class ClipboardWatchService extends Service{
	private Looper mServiceLooper;
	private ServiceHandler mServiceHandler;
	
	ClipboardManager clipboard_mgr;
	private String clipboard_text;
	Thread thread;

	// Handler that receives messages from the thread
	private final class ServiceHandler extends Handler {
		public ServiceHandler(Looper looper) {
			super(looper);
		}
		@Override
		public void handleMessage(Message msg) {
			// Normally we would do some work here, like download a file.
			// For our sample, we just sleep for 5 seconds.
			
			Log.i(this.getClass().getName(),"Service task started");
			
			String txt = (String)msg.obj;
			//do something with this string.
			//translate?
			Toast.makeText(getApplicationContext(), "txt : " + txt, Toast.LENGTH_SHORT).show();
			
			Log.i(this.getClass().getName(),"Service task end");
			// Stop the service using the startId, so that we don't stop
			// the service in the middle of handling another job
			stopSelf(msg.arg1);
		}
	}

	@Override
	public void onCreate() {
		Log.i(this.getClass().getName(),"Service onCreate called");
		// Start up the thread running the service.  Note that we create a
		// separate thread because the service normally runs in the process's
		// main thread, which we don't want to block.  We also make it
		// background priority so CPU-intensive work will not disrupt our UI.
		HandlerThread thread = new HandlerThread("ServiceStartArguments");
				//Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();

		// Get the HandlerThread's Looper and use it for our Handler
		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);
		
		clipboard_mgr = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
		Log.i(this.getClass().getName(),"Service onStartCommand called");

		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(!Thread.interrupted()){
					long sleep_time = 2000;//2 sec
					
					//http://stackoverflow.com/questions/8617404/paste-from-clipboard-in-android
					ClipData clipData = clipboard_mgr.getPrimaryClip();
					if(null!=clipData){
						String new_text_candid = null;
						
						// if you need text data only, use:
						if (clipData.getDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))
							// WARNING: The item could cantain URI that points to the text data.
							// In this case the getText() returns null and this code fails!
							new_text_candid = clipData.getItemAt(0).getText().toString();
						// or you may coerce the data to the text representation:
						new_text_candid = clipData.getItemAt(0).coerceToText(getApplicationContext()).toString();
					
						//check renew. do something only when new clipboard data comes
						if(null!=new_text_candid && !new_text_candid.equals(clipboard_text)){
							Log.i(this.getClass().getName(),"new data - [" + new_text_candid + "]");
							
							//invoke message
							if(null!=clipboard_text){//do not prompt at first time service launches..
								
								Message msg = mServiceHandler.obtainMessage();
								msg.obj = new String(new_text_candid);
								mServiceHandler.sendMessage(msg);
							}
							clipboard_text = new_text_candid;
						}
						
						if(null!=clipboard_text)
							Log.i(this.getClass().getName(),"data - [" + clipboard_text + "]");
						
//						if(null != clipboard_text){
//							//new token found!
//							//do something with local var. not global one.
//							String new_text =  clipboard_text;
//							clipboard_text = null;
//							
//							Message msg = mServiceHandler.obtainMessage();
//							msg.obj = new_text;
//							mServiceHandler.sendMessage(msg);
//						}
					}
					
					try {
						Thread.sleep(sleep_time);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						
						//invoke interrupt
						Thread.currentThread().interrupt();
					}
				}
			}
		});
		thread.setDaemon(true);
		thread.start();
		
		// For each start request, send a message to start a job and deliver the
		// start ID so we know which request we're stopping when we finish the job
//		Message msg = mServiceHandler.obtainMessage();
//		msg.arg1 = startId;
//		mServiceHandler.sendMessage(msg);

		// If we get killed, after returning from here, restart
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// We don't provide binding, so return null
		return null;
	}

	@Override
	public void onDestroy() {
		Log.i(this.getClass().getName(),"Service onDestroy called");
		thread.interrupt();
//		Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
	}

}
