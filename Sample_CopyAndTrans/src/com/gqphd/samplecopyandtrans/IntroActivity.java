package com.gqphd.samplecopyandtrans;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class IntroActivity extends Activity {

	ImageView vImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//make it fullscreen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_intro);
		
		vImage = (ImageView)findViewById(R.id.imgLogo);
		vImage.setVisibility(View.INVISIBLE);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if(null != vImage){
			
			Animation anim_fadein = new AlphaAnimation(0, 1);
			anim_fadein.setDuration(2500);
			
			Animation anim_up = new TranslateAnimation(0, 0, -150, 0);
			anim_up.setInterpolator(new AccelerateDecelerateInterpolator());
			anim_up.setDuration(2000);
			
			AnimationSet animation = new AnimationSet(true);
			animation.addAnimation(anim_fadein);
			animation.addAnimation(anim_up);
			animation.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationRepeat(Animation arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation arg0) {
					
					Log.i(this.getClass().getName(),"fn called - onAnimationEnd");
					Handler h = new Handler();
					h.postDelayed(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Log.i(this.getClass().getName(),"fn called - delated task!");
							
							Intent intent = new Intent(getApplicationContext(),MainActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
							startActivity(intent);
							finish();
						}
					}, 1000);
				}
			});

			vImage.setVisibility(View.VISIBLE);
			vImage.startAnimation(animation);
		}
	}
}
