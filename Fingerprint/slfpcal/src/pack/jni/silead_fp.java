package pack.jni;

public class silead_fp {
	
	static public native boolean silead_fp_hwInit();
	
	static public native int silead_fp_DhwInit();
	
	static public native byte[] GetOneFrame();

	static public native int writeAddr(String addr_u32,String reg_data);
	 
	static public native String readAddr(String TotalAddr);
	 
	static public native void clearIntFlag();

	static public native String testString();
	 
	static public native int ReadReg(int reg_);
	 
	static {
		 try {
			System.loadLibrary("sileadinc_dev");
		} catch (Exception e) {

		}
	 }
}
