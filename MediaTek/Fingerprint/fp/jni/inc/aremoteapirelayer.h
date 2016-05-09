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

#ifndef _AREMOTEAPI_RELAYER_H_
#define _AREMOTEAPI_RELAYER_H_

#include "aremoteapi.h"
#include "arapi.h"
#include "aarray.h"

class AInterface;

class ARemoteApiRelayer : public ARemoteApi
{
protected:

public:
	virtual								~ARemoteApiRelayer();

	//static ARemoteApiRelayer*			Create();
	//static ARemoteApiRelayer*			Create(const char* inName);
	//static const ARemoteApiRelayer*	GetInstance();

	static int							CB_FwdCall_Func(
		char*							inf_name,
		char*							func_name,
		char*							func_args
		);

    virtual int							Dump();

            ARemoteApi*					Relayer();

protected:
	ARemoteApiRelayer(
		const char*						inName,
		AInterface*						inAttachedInf
		);

	virtual int							onPingRelayer();

	virtual int							OnClientConnect(
		AInfIPCNameStr_t*				inRApiClientSideRelayerName,
		AInfClientInfo_t*				inInfClientInfo
		);
	virtual int							OnClientDisConnect();

	int									FwdCall(
		char*							func_name,
		char*							func_args
		);
	virtual int							onFwdCall(
		char*							func_name,
		char*							func_args
		);

private:
	void relayer_register();

	void create_relayer(
		const char* in_relayer_name
		);

	void destroy_relayer();


private:
};

//inline const ARemoteApiRelayer* ARemoteApiRelayer::GetInstance()     { return (ARemoteApiRelayer*)s_pRemoteApiRelayer; }

#endif 
