
#ifndef _SLFPAPI_H_
#define _SLFPAPI_H_

#include "fpsvc_types.h"

#ifdef __cplusplus
extern "C" {
#endif

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

void slfpapi_FpCancelFinished(void);

int slfpapi_FpSetOperation(int operation);


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
 * Method:    FpCancelOperation
 * Signature: ()I
 */
int SLFPAPI_FpCancelOperation(void);
int SLFPAPI_IsBeingCancelled(void);
/*
 * Class:     com_android_server_fpservice
 * Method:    GetEnableCredential
 * Signature: (I)Z
 */
int SLFPAPI_GetEnableCredential(int);

int SLFPAPI_GetFPInfo(SLFpsvcIndex_t *opFpInfo);

int SLFPAPI_SetFPInfo(SLFpsvcIndex_t *opFpInfo);



#ifdef __cplusplus
}
#endif
#endif
