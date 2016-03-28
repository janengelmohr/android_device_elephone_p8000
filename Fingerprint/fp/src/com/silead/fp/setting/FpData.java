package com.silead.fp.setting;

import android.content.Context;
import android.os.Bundle;

public class FpData

{

	final static String TAG = "FpData";
	Context cn;
	String fpName;
	boolean bEnable;
	boolean bEnroll;
	int fpIndex;// fp index

	public FpData(Context context) {
		cn = context;
		fpName = "fprint";
		bEnable = true;// false; change default value to true
		bEnroll = false;
		fpIndex = -1;
	}

	public FpData(Context context, Bundle mbundle) {

	}
	
	public void setFpName(String title) {
		this.fpName = title;
	}

	public String getFpName() {

		return this.fpName;
	}

	public void setFpEnable(boolean enable) {
		this.bEnable = enable;
	}

	public boolean getFpEnable() {
		return this.bEnable;
	}

	public void setFpEnroll(boolean fpEnroll) {
		this.bEnroll = fpEnroll;
	}

	public boolean getFpEnroll() {
		return this.bEnroll;
	}

	public void setFpIndex(int index) {
		this.fpIndex = index;
	}

	public int getFpIndex() {
		return this.fpIndex;
	}
}
