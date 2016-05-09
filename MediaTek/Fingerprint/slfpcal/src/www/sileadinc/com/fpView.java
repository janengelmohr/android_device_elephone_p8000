package www.sileadinc.com;

import pack.jni.silead_fp;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class fpView extends View {
	
	Paint paint;
	
	public fpView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		paint = new Paint(); 
        //paint.setColor(Color.YELLOW);    
        paint.setStrokeJoin(Paint.Join.ROUND);    
        paint.setStrokeCap(Paint.Cap.ROUND);    
        paint.setStrokeWidth(3);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		byte[] oneframe = silead_fp.GetOneFrame();
		RectF  rect = new RectF(10,10,13,13);
		for (int i = 0; i < oneframe.length; i++) {
			paint.setColor((oneframe[i]&0xff));
			canvas.drawColor(oneframe[i]);
			canvas.drawRect(rect, paint);
			rect.left += 3;
		}
		invalidate();
	}
}
