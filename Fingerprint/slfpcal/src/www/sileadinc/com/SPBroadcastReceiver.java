package www.sileadinc.com;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SPBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent==null){
			return;
		}
		String action = intent.getAction();
		if(action!=null&&action.equals("android.provider.Telephony.SECRET_CODE")){
			Intent fp_intent = new Intent(context,Silead_fpActivity.class);
			fp_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(fp_intent);
		}
	}

}
