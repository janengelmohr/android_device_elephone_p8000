/*  ====================================================================================================
**
**
**  ---------------------------------------------------------------------------------------------------
**
**	File Name:  	
**	
**	Description:	This file contains the interface for the Buffer Control.
**
**					this is kernal code of SW framework.
**					It contributes one of functionalities of SW Platform. 
**
**  ---------------------------------------------------------------------------------------------------
**
**  Author:			Warren Zhao
**
** -------------------------------------------------------------------------
**
**	Change History:
**	
**	Initial revision
**
**====================================================================================================*/

#include <unistd.h>
#include <android/log.h>
#include "ainffpsvcfpapkrelayerCB.h"
#include "com_silead_fp_utils_FpControllerNative.h"

AInfFpsvcFPApkRelayerCB* AInfFpsvcFPApkRelayerCB::s_pAInfFpsvcFPApkRelayerCB = NULL;

AInfFpsvcFPApkRelayerCB::AInfFpsvcFPApkRelayerCB()
	:
	AInfFpsvcRelayerCB()
{
}

AInfFpsvcFPApkRelayerCB::~AInfFpsvcFPApkRelayerCB()
{
	s_pAInfFpsvcFPApkRelayerCB = NULL;
}

AInterface*	AInfFpsvcFPApkRelayerCB::OnInfDuplicate()
{
	return new AInfFpsvcFPApkRelayerCB();
}

AInfFpsvcFPApkRelayerCB* AInfFpsvcFPApkRelayerCB::Create()
{
	if(s_pAInfFpsvcFPApkRelayerCB == NULL)
	{
		s_pAInfFpsvcFPApkRelayerCB = new AInfFpsvcFPApkRelayerCB();
	}

	return (AInfFpsvcFPApkRelayerCB*)s_pAInfFpsvcFPApkRelayerCB;
}

/////////////////////////////////////////////////////
///INTERFACE///////////
/////////////////////////////////////////////////////

/*
 * Class:     com_android_server_fpservice
 * Method:    EnrollCredential_CB
 * Signature: (I)I
 * if return != 0, pls play VIB;
 * -2: curren enroll failure;
 * -3: abort whole enroll transaction;
 */
int AInfFpsvcFPApkRelayerCB::EnrollCredential_CB(
	int index,
	int percent,
	int inret,
	int area
	)
{
//__android_log_print(4, "[SL_FP_CB]","\n--AInfFpsvcFPApkRelayerCB::EnrollCredential_CB-index=%d-inret=%d-pid=%d\n",index,inret,getpid());
	enrollCredentialRSP (index, percent, inret, area);
	return 0;
}

/*
 * Class:     com_android_server_fpservice
 * Method:    IdentifyCredential_CB
 * Signature: (I)I
 */
int AInfFpsvcFPApkRelayerCB::IdentifyCredential_CB(
    int index,
	int inret,
	int fingerid
    )
{
//__android_log_print(4, "[SL_FP_CB]","\n--AInfFpsvcFPApkRelayerCB::IdentifyCredential_CB-index=%d-inret=%d-pid=%d\n",index,inret,getpid());
	identifyCredentialRSP (index, inret,fingerid);
	return 0;
}
int AInfFpsvcFPApkRelayerCB::slfpkey_CB(
    int slfpkey
    )
{
//__android_log_print(4, "[SL_FP_CB]","\n--AInfFpsvcFPApkRelayerCB::IdentifyCredential_CB- slfpkey = %d-pid=%d\n",slfpkey,getpid());
	slfpkeyRSP (slfpkey);
	return 0;
}
