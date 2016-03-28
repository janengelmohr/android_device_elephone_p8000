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

#ifndef _AREMOTEAPI_FPSVC_CLIENT_CB_H_
#define _AREMOTEAPI_FPSVC_CLIENT_CB_H_

#include "aremoteapiclient.h"
#include "ainffpsvcCB.h"

//do not change the order of parents
class AInfFpsvcClientCB : public AInfFpsvcCB, public ARemoteApiClient
{
public:
    virtual               ~AInfFpsvcClientCB();

    static AInfFpsvcClientCB* Create(const char* inName);

    virtual int           Dump();

protected:

                          AInfFpsvcClientCB(
                              const char* inName
							  );

	//if caller args have out-pointer, need to cp the *pointer back to out-pointer
	virtual int           onReturn(
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

#endif 
