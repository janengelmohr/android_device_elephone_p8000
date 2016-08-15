#include <assert.h>
#include <jni.h>
//#include "JNIHelp.h"
#include <android/log.h>
#include <unistd.h>
#include "sileadinc_dev.h"

#ifndef NULL
#define NULL (void*)0
#endif
#define FRAMELEN 2+128*103+128
unsigned char* frame = NULL;

JNIEXPORT jbyteArray JNICALL Java_pack_jni_silead_1fp_GetOneFrame
  (JNIEnv *env, jclass jcls)
{
	__android_log_print(4, "[SL_FP]", "Java_pack_jni_silead_1fp_GetOneFrame:start\n");
	if(frame)
		memset(frame,0x00,FRAMELEN);
	int tmp1 = FPCALAPI_read(0xff08000c);
	int tmp2 = FPCALAPI_read(0xff0000b8);
	__android_log_print(4, "[SL_FP]", "0xff08000c = %x 0xff0000b8 = %x",tmp1,tmp2);
	FPCALAPI_GetOneFrame(frame,FRAMELEN);
	jint jlen = 13186;
	jbyteArray jfp_data = (*env)->NewByteArray(env, jlen);
	(*env)->SetByteArrayRegion(env, jfp_data, 0, jlen, frame);
	FPCALAPI_Write(0xbf,0x00);
	return jfp_data;
}

JNIEXPORT jstring JNICALL Java_pack_jni_silead_1fp_readAddr
  (JNIEnv *env, jclass jclz, jstring jstr)
{
	__android_log_print(4, "[SL_FP]", "Java_pack_jni_silead_1fp_readAddr:start\n");
	unsigned int ret;
	unsigned char buf[9+1];
	const char *str = (*env)->GetStringUTFChars(env, jstr, 0);
	unsigned int  total_reg;
	total_reg = strtoul(str,0,16);
	ret = FPCALAPI_read(total_reg);
	snprintf(buf,9,"%08x",ret);
	return (*env)->NewStringUTF(env,buf);
}

JNIEXPORT jint JNICALL Java_pack_jni_silead_1fp_writeAddr
  (JNIEnv *env, jclass jclz, jstring addr_u32, jstring reg_data)
{
	__android_log_print(4, "[SL_FP]", "Java_pack_jni_silead_1fp_writeAddr:start\n");
	unsigned int addr,reg;
	const char *str_addr = (*env)->GetStringUTFChars(env, addr_u32, 0);
	const char *str_data = (*env)->GetStringUTFChars(env, reg_data, 0);
	addr = strtoul(str_addr,0,16);
	reg = strtoul(str_data,0,16);
	__android_log_print(4, "[SL_FP]", "reg = %x addr = %x\n",reg,addr);
	FPCALAPI_Write(reg,addr);
	return 1;
}

JNIEXPORT jboolean JNICALL Java_pack_jni_silead_1fp_silead_1fp_1hwInit
  (JNIEnv *env, jclass jcls)
{
	__android_log_print(4, "[SL_FP]", "Java_pack_jni_silead_1fp_silead_1fp_1hwInit:start\n");
	frame = (unsigned char *)malloc(2+128*103+128);
	FPCALAPI_HwInit();

	return JNI_TRUE;
}

JNIEXPORT jint JNICALL Java_pack_jni_silead_1fp_silead_1fp_1DhwInit
  (JNIEnv *env, jclass jcls)
{
	__android_log_print(4, "[SL_FP]", "Java_pack_jni_silead_1fp_silead_1fp_1DhwInit:start\n");
	if(!frame)
		free(frame);
	FPCALAPI_DeInit();
	return 1;
}

JNIEXPORT jstring JNICALL Java_pack_jni_silead_1fp_testString
  (JNIEnv *env, jclass jclz)
{
	__android_log_print(4, "[SL_FP]", "Java_pack_jni_silead_1fp_testString:start\n");
	jstring jstr;
	char str[] = "From JNI HELLLO test";
	jstr = (*env)->NewStringUTF(env,str);
	return jstr;
}

JNIEXPORT void JNICALL Java_pack_jni_silead_1fp_clearIntFlag
  (JNIEnv *env, jclass jclz)
{
	__android_log_print(4, "[SL_FP]", "Java_pack_jni_silead_1fp_clearIntFlag:start\n");
	FPCAL_ClearIntFlag();
}

JNIEXPORT jint JNICALL Java_pack_jni_silead_1fp_ReadReg
  (JNIEnv *env, jclass jcs, jint jnt)
{
	__android_log_print(4, "[SL_FP]", "Java_pack_jni_silead_1fp_ReadReg:start\n");
	return FPCALAPI_AdjOk();
}


static JNINativeMethod gMethods[] = {
  {"GetOneFrame",         	"()[B",          							(void *)Java_pack_jni_silead_1fp_GetOneFrame},
  {"readAddr",          	"(Ljava/lang/String;)Ljava/lang/String;",	(void *)Java_pack_jni_silead_1fp_readAddr},
  {"writeAddr",             "(Ljava/lang/String;Ljava/lang/String;)I",	(void *)Java_pack_jni_silead_1fp_writeAddr},
  {"silead_fp_hwInit",     	"()Z",         								(void *)Java_pack_jni_silead_1fp_silead_1fp_1hwInit},
  {"silead_fp_DhwInit",     "()I",         								(void *)Java_pack_jni_silead_1fp_silead_1fp_1DhwInit},
  {"testString",            "()Ljava/lang/String;",						(void *)Java_pack_jni_silead_1fp_testString},
  {"clearIntFlag",         	"()V",         								(void *)Java_pack_jni_silead_1fp_clearIntFlag},
  {"ReadReg",            	"(I)I",        								(void *)Java_pack_jni_silead_1fp_ReadReg},
};

int Java_pack_jni_silead_1fp(JNIEnv* env,
	const char* className,
	const JNINativeMethod* jniMethods,
	int numMethods) {
	jclass clazz = (*env)->FindClass(env,className);
    int res = (*env)->RegisterNatives(env, clazz, jniMethods, numMethods);
   __android_log_print(4, "[SL_FP]", "jniRegisterNativeMethods:1 numMethods = %d\n",numMethods);
    return 0;
}

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
	JNIEnv *env = NULL;
	int ret = -1;
	__android_log_print(4, "[SL_FP]", "[wq]:JNI_OnLoad:start\n");

	if ((*vm)->GetEnv(vm,(void **)&env, JNI_VERSION_1_4) != JNI_OK) {
		__android_log_print(4, "[SL_FP]", "[wq]:JNI_OnLoad:0\n");
		return -1;
	}
	__android_log_print(4, "[SL_FP]", "[wq]:JNI_OnLoad:1 env = %p\n",env);
	assert(env != NULL);
	__android_log_print(4, "[SL_FP]", "[wq]:JNI_OnLoad:2\n");
	ret = Java_pack_jni_silead_1fp(env, "pack/jni/silead_fp", gMethods, sizeof(gMethods)/sizeof(gMethods[0]));
	if ( ret < 0 ) {
		__android_log_print(4, "[SL_FP]", "[wq]:JNI_OnLoad:3\n");
		return -1;
	}
		__android_log_print(4, "[SL_FP]", "[wq]:JNI_OnLoad:end\n");
	return JNI_VERSION_1_4;
}
