package com.gqphd.samplecopyandtrans;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

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
			Log.i(this.getClass().getName(),"Service task started");
			
			String txt = (String)msg.obj;
			txt = txt.replace(" ","");//rem blank
			
			//simple - simply open naver dic.
			//open web @ref http://caliou.tistory.com/2
			String sUri = String.format("http://m.endic.naver.com/search.nhn?searchOption=all&query=%s&=",txt);
			Uri uri = Uri.parse(sUri);   
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			
			Log.i(this.getClass().getName(),"Service task end");
			// Stop the service using the startId, so that we don't stop
			// the service in the middle of handling another job
			stopSelf(msg.arg1);
		}
	}

	@Override
	public void onCreate() {
		Log.i(this.getClass().getName(),"Service onCreate called");
		HandlerThread thread = new HandlerThread("ServiceStartArguments");
		thread.start();

		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);
		
		clipboard_mgr = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(this.getClass().getName(),"Service onStartCommand called");

		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(!Thread.interrupted()){
					long sleep_time = 2000;//2 sec
					
					//http://stackoverflow.com/questions/8617404/paste-from-clipboard-in-android
					ClipData clipData = clipboard_mgr.getPrimaryClip();
					if(null!=clipData){
						String new_text_candid = null;
						
						if (clipData.getDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))
							// In this case the getText() returns null and this code fails!
							new_text_candid = clipData.getItemAt(0).getText().toString();
						else
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
	}

}
