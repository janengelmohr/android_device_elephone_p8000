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

#ifndef _AREMOTEAPI_FPSVC_RELAYER_CB_H_
#define _AREMOTEAPI_FPSVC_RELAYER_CB_H_

#include "aremoteapirelayer.h"
#include "ainffpsvcCB.h"

//do not change the order of parents
class AInfFpsvcRelayerCB : public AInfFpsvcCB, public ARemoteApiRelayer
{
protected:
	static AInfFpsvcRelayerCB* s_pAInfFpsvcRelayerCB;
public:
	virtual					~AInfFpsvcRelayerCB();

	static AInfFpsvcRelayerCB*        Create();
	static const AInfFpsvcRelayerCB*  GetInstance();

	virtual int				Dump();

protected:
							AInfFpsvcRelayerCB();

	virtual AInterface*		OnInfDuplicate();

	virtual int				onFwdCall(
								char* func_name,
								char* func_args
								);

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

inline const AInfFpsvcRelayerCB* AInfFpsvcRelayerCB::GetInstance()     { return (AInfFpsvcRelayerCB*)s_pAInfFpsvcRelayerCB; }


#endif

