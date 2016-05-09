/*  ====================================================================================================
**
**  ---------------------------------------------------------------------------------------------------
**
**	File Name:  	
**	
**	Description:	This file contains the implementation of interface
**
**					this is kernal code of SW framework.
**					It contributes one of functionalities of SW Platform. 
**					If the checkin is CR not PR, to add change History to this file head part 
**					will be appreciated.
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


#ifndef __AINTERFACE_FPSVC_H__
#define __AINTERFACE_FPSVC_H__

#include "ainterfaceNCB.h"
#include "fpsvc_types.h"

class AInfFpsvc : public AInterfaceNCB
{
public:
			/*
			 * Class:     com_android_server_fpservice
			 * Method:    ResetFPService
			 * Signature: ()I
			 */
			virtual int ResetFPService() = 0;

			/*
			 * Class:     com_android_server_fpservice
			 * Method:    InitFPService
			 * Signature: ()I
			 */
//			virtual int InitFPService() = 0;

			/*
			 * Class:     com_android_server_fpservice
			 * Method:    DeinitFPService
			 * Signature: ()V
			 */
//			virtual int DeinitFPService() = 0;

			/*
			 * Class:     com_android_server_fpservice
			 * Method:    EnrollCredential
			 * Signature: (I)I
			 */
			virtual int EnrollCredential(int) = 0;

			/*
			 * Class:     com_android_server_fpservice
			 * Method:    IdentifyCredential
			 * Signature: (I)I
			 */
			virtual int IdentifyCredential(int) = 0;

			/*
			 * Class:     com_android_server_fpservice
			 * Method:    RemoveCredential
			 * Signature: (I)I
			 */
			virtual int RemoveCredential(int) = 0;

			/*
			 * Class:     com_android_server_fpservice
			 * Method:    EnalbeCredential
			 * Signature: (IZ)I
			 */
			virtual int EnalbeCredential(int, int) = 0;

			/*
			 * Class:     com_android_server_fpservice
			 * Method:    FpCancelOperation
			 * Signature: ()I
			 */
			virtual int FpCancelOperation() = 0;
			/*
			 * Class:     com_android_server_fpservice
			 * Method:    GetEnableCredential
			 * Signature: (I)Z
			 */
			virtual int GetEnableCredential(int) = 0;

			virtual int GetFPInfo(SLFpsvcIndex_t* opFpInfo) = 0;

			virtual int SetFPInfo(SLFpsvcIndex_t* opFpInfo) = 0;


public:
			virtual				~AInfFpsvc();

protected:
			AInfFpsvc(
				int				isRelayer,
				ARemoteApi*		rApi,
				AInterface*		inInfCallBackRelayer
				);

			virtual AInterface*	OnInfCallBackClientCreate(
				const char	*inRelayerNameOfCallBackClient
				);

};



#endif 
