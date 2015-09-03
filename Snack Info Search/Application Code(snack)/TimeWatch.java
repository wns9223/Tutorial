package prosess.time.pkg;

import search.pkg.TotalSearchActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

public class TimeWatch extends Thread {
	private Handler mHandler;
	private ProgressDialog mProgressDialog;

	public void run(Activity thisAc, int second) {
		// TODO Auto-generated method stub
		super.run();
		
		mHandler = new Handler();

		mProgressDialog = ProgressDialog.show(thisAc, "", "please wait.",
				true);
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				try {
					if (mProgressDialog != null && mProgressDialog.isShowing()) {
						mProgressDialog.dismiss();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, second);
	}

}
