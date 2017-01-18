/*
  Copyright (C) 2013 George Opiyo 

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.	See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program.	If not, see <http://www.gnu.org/licenses/>.
 */

package ke.co.eaglesafari;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import ke.co.eaglesafari.auth.LoginDb;


public class SplashActivity extends Activity implements AnimationListener {
    public static final boolean DEBUG = false;

    static String VersionText = "error";
    static String VersionCode = "error";
    static String responseString = null;
    ImageView logo;
    Animation animRotate, animLogo, animMotto, animFinish, animLogin;
    TextView txtMotto;
    RelativeLayout lin;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.MULTIPLY);
        progressBar.setVisibility(View.VISIBLE);


        LoginDb ldb = LoginDb.getInstance(this);
        ldb.getWritableDatabase();
        if (ldb.getRowCount() > 0) {
            startActivity(new Intent(SplashActivity.this,
                    MainActivity.class));
            finish();
            return;

        }


        lin = (RelativeLayout) findViewById(R.id.main_layout);
        logo = (ImageView) findViewById(R.id.logo);


        txtMotto = (TextView) findViewById(R.id.textVersion);
        //	txtMotto.setTextSize(23);

        animLogin = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_out);
        animLogin.setAnimationListener(this);
        animFinish = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_out);
        animFinish.setAnimationListener(this);
        animLogo = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        animLogo.setAnimationListener(this);

        animMotto = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        animMotto.setAnimationListener(this);

        animRotate = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate);
        animRotate.setAnimationListener(this);

        logo.startAnimation(animLogo);

        /** create a thread to show splash up to splash time */
            /*	Thread welcomeThread = new Thread()
					{

						// 2 seconds, enough time to read version number
						long	endit	= System.currentTimeMillis() + (9 * 500);

						@Override
						public void run()
							{
								try
									{
										super.run();
										while (System.currentTimeMillis() < endit)
											{
											}
									} catch (Exception e)
									{
										System.out.println("EXc=" + e);
									} finally
									{
										// start login
										startActivity(new Intent(SplashActivity.this,
												LoginActivity.class));

										finish();
									}
							}
					};
				welcomeThread.start();*/


    }

    @Override
    public void onAnimationEnd(Animation animation) {
        // Take any action after completing the animation

        // check for fade in animation
        if (animation == animRotate) {
        } else if (animation == animLogo) {

            Intent y = new Intent(SplashActivity.this,
                    LoginActivity.class);

            startActivity(y);

            finish();
        } else if (animation == animFinish) {

        } else if (animation == animLogin) {

        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }


}
