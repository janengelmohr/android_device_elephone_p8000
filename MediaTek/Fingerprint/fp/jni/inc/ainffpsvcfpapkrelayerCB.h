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

#ifndef _AREMOTEAPI_FPSVC_FPAPK_RELAYER_CB_H_
#define _AREMOTEAPI_FPSVC_FPAPK_RELAYER_CB_H_

#include "ainffpsvcrelayerCB.h"

class AInfFpsvcFPApkRelayerCB : public AInfFpsvcRelayerCB
{
protected:
	static AInfFpsvcFPApkRelayerCB* s_pAInfFpsvcFPApkRelayerCB;
public:
    virtual					~AInfFpsvcFPApkRelayerCB();
	
	static AInfFpsvcFPApkRelayerCB*        Create();
	static const AInfFpsvcFPApkRelayerCB*  GetInstance();

protected:
                          AInfFpsvcFPApkRelayerCB();
	virtual AInterface*		OnInfDuplicate();

private:

public:
			/*
			 * Class:     com_android_server_fpservice
			 * Method:    EnrollCredential_CB
			 * Signature: (I)I
			 */
			virtual int EnrollCredential_CB(int,int,int,int);

			/*
			 * Class:     com_android_server_fpservice
			 * Method:    IdentifyCredential_CB
			 * Signature: (I)I
			 */
			virtual int IdentifyCredential_CB(int,int,int);

			virtual int slfpkey_CB(int);
};

inline const AInfFpsvcFPApkRelayerCB* AInfFpsvcFPApkRelayerCB::GetInstance()     { return (AInfFpsvcFPApkRelayerCB*)s_pAInfFpsvcFPApkRelayerCB; }


#endif

