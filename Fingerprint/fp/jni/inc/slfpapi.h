
#ifndef _SLFPAPI_H_
#define _SLFPAPI_H_

#include "fpsvc_types.h"

#ifdef __cplusplus
extern "C" {
#endif


int slfpapi_IsFingersEnabled();

int slfpapi_GetFpSvcState();
void slfpapi_SetFpSvcState(int inState);
void slfpapi_WakeUpScreen(void);
void SLFPAPI_RunFunctionKey(int finger_id, int state);


extern void SLFP_PrintLogAdapter(unsigned char*);
#define SLFP_LOG(s)	      	\
	{						\
	    unsigned char tempStr[201];			\
		snprintf(tempStr,200,"\nF=%s:L=%s:M=%s:%s\n" ,__FILE__,__LINE__,__FUNCTION__,s);	\
		SLFP_PrintLogAdapter(tempStr);			\
	}

/*
 * Class:     com_android_server_fpservice
 * Method:    InitFPService
 * Signature: ()I
 */
int slfpapi_InitFPService();

/*
 * Class:     com_android_server_fpservice
 * Method:    DeinitFPService
 * Signature: ()V
 */
int slfpapi_DeinitFPService(void);

/*
 * Class:     com_android_server_fpservice
 * Method:    EnrollCredential
 * Signature: (I)I
 */
int slfpapi_EnrollCredential(int,int*,int*);

/*
 * Class:     com_android_server_fpservice
 * Method:    IdentifyCredential
 * Signature: (I)I
 */
int slfpapi_IdentifyCredential(int,int*);


/*
 * Class:     com_android_server_fpservice
 * Method:    RemoveCredential
 * Signature: (I)I
 */
int SLFPAPI_RemoveCredential(int);

/*
 * Class:     com_android_server_fpservice
 * Method:    EnalbeCredential
 * Signature: (IZ)I
 */
int SLFPAPI_EnalbeCredential(int, int);

/*
 * Class:     com_android_server_fpservice
 * Method:    GetEnableCredential
 * Signature: (I)Z
 */
int SLFPAPI_GetEnableCredential(int);

int SLFPAPI_GetFPInfo(SLFpsvcIndex_t *opFpInfo);

int SLFPAPI_SetFPInfo(SLFpsvcIndex_t *opFpInfo);

void SLFPAPI_SwitchUser(int userid);

int SLFPAPI_GetWakeUpState();

int SLFPAPI_GetVirtualKeyState();

int SLFPAPI_GetFingerPrintState();

int SLFPAPI_GetVirtualKeyCode();

int SLFPAPI_SetWakeUpState(int WakeUpState);

int SLFPAPI_SetVirtualKeyState(int VirtualKeyState);

int SLFPAPI_SetFingerPrintState(int FingerPrintState);

int SLFPAPI_SetVirtualKeyCode(int VirtualKeyCode);

void slfpapi_IdleDetectFinger();

int SLFPAPI_SetFPScreenStatus(int);

#ifdef __cplusplus
}
#endif
#endif
