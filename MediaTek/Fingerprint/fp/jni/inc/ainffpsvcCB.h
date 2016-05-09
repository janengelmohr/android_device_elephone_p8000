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


#ifndef __AINTERFACE_FPSVC_CB_H__
#define __AINTERFACE_FPSVC_CB_H__

#include "ainterfaceCB.h"

class AInfFpsvcCB : public AInterfaceCB
{
public:


			/*
			 * Class:     com_android_server_fpservice
			 * Method:    EnrollCredential_CB
			 * Signature: (I)I
			 */
			virtual int EnrollCredential_CB(int,int,int,int) = 0;

			/*
			 * Class:     com_android_server_fpservice
			 * Method:    IdentifyCredential_CB
			 * Signature: (I)I
			 */
			virtual int IdentifyCredential_CB(int,int,int) = 0;

			virtual int slfpkey_CB(int) = 0;

public:
			virtual				~AInfFpsvcCB();

protected:
			AInfFpsvcCB(
				int				isRelayer,
				ARemoteApi*		rApi
				);
};


#endif 
