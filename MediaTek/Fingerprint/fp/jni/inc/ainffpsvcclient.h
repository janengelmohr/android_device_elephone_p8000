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

#ifndef _AREMOTEAPI_FPSVC_CLIENT_H_
#define _AREMOTEAPI_FPSVC_CLIENT_H_

#include "aremoteapiclient.h"
#include "ainffpsvc.h"

//do not change the order of parents
class AInfFpsvcClient : public AInfFpsvc, public ARemoteApiClient
{
public:
	virtual							~AInfFpsvcClient();

	static AInfFpsvcClient*			Create(
		AInterface*					inInfCallBackRelayer
		);
	static AInfFpsvcClient*			Create(
		const char*					inName,
		AInterface*					inInfCallBackRelayer
		);

	virtual int				Dump();

protected:
							AInfFpsvcClient(
								const char* inName,
								AInterface* inInfCallBackRelayer
								);

	//if caller args have out-pointer, need to cp the *pointer back to out-pointer
	virtual int				onReturn(
								char* func_name,
								char* func_args
								);

private:

public:
			/*
			 * Class:     com_android_server_fpservice
			 * Method:    ResetFPService
			 * Signature: ()I
			 */
			virtual int ResetFPService();
			
			/*
			 * Class:     com_android_server_fpservice
			 * Method:    InitFPService
			 * Signature: ()I
			 */
//			virtual int InitFPService();

			/*
			 * Class:     com_android_server_fpservice
			 * Method:    DeinitFPService
			 * Signature: ()V
			 */
//			virtual int DeinitFPService();

			/*
			 * Class:     com_android_server_fpservice
			 * Method:    EnrollCredential
			 * Signature: (I)I
			 */
			virtual int EnrollCredential(int);

			/*
			 * Class:     com_android_server_fpservice
			 * Method:    IdentifyCredential
			 * Signature: (I)I
			 */
			virtual int IdentifyCredential(int);

			/*
			 * Class:     com_android_server_fpservice
			 * Method:    RemoveCredential
			 * Signature: (I)I
			 */
			virtual int RemoveCredential(int);

			/*
			 * Class:     com_android_server_fpservice
			 * Method:    EnalbeCredential
			 * Signature: (IZ)I
			 */
			virtual int EnalbeCredential(int, int);

			/*
			 * Class:     com_android_server_fpservice
			 * Method:    FpCancelOperation
			 * Signature: ()I
			 */
			virtual int FpCancelOperation();
			/*
			 * Class:     com_android_server_fpservice
			 * Method:    GetEnableCredential
			 * Signature: (I)Z
			 */
			virtual int GetEnableCredential(int);

			virtual int GetFPInfo(SLFpsvcIndex_t* opFpInfo);

			virtual int SetFPInfo(SLFpsvcIndex_t* opFpInfo);

};

#endif 
