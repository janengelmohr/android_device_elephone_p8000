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

#ifndef _AREMOTEAPI_CLIENT_H_
#define _AREMOTEAPI_CLIENT_H_

#include "aremoteapi.h"
#include "arapi.h"

class AInterface;

class ARemoteApiClient : public ARemoteApi
{
protected:

public:
	virtual						~ARemoteApiClient();

	//static ARemoteApiClient* Create();
	//static ARemoteApiClient* Create(const char* inName);
	//static const ARemoteApiClient*  GetInstance();

	virtual void				SetAttachedInfName(const char* inName);

			int					PingRelayer();
	virtual int					Dump();

			ARemoteApi*			Client();

	static int					CB_Handle_Func(
		void*					client_class,
		char*					func_name,
		char*					func_args
		);

protected:

	//static ARemoteApiClient* s_pARemoteApiClient;

	ARemoteApiClient(
		const char*				inName,
		AInterface*				inAttachedInf
		);

	virtual int					CreateClientHandle();
	virtual int					Bind();


	virtual int					ClientConnect(
		AInfIPCNameStr_t*		inRApiClientSideRelayerName,
		AInfClientInfo_t*		inInfClientInfo
		);
	virtual int					ClientDisConnect();

	char*						RegArgsBuff(
		const char*				in_func_name
		);

	int							Call();

	//if caller args have out-pointer, need to cp the *pointer back to out-pointer
	virtual int					onReturn(
		char*					func_name,
		char*					func_args
		);

private:
	//following are wrapper
	void client_register( 
		ARemoteApiClient_t in_client,
		CB_AOSRemoteApi_Client_HandleReturn_Func_t  in_AOSRemoteApi_Client_HandleReturn_Func
		);

	ARemoteApiClient_t create_client(
		const char*				in_relayer_name,
		const char*				in_inf_name
		);

	void set_attachedinfname(
		ARemoteApiClient_t		in_client,
		const char*				in_inf_name
		);

	void destroy_client(
		ARemoteApiClient_t		in_client
		);

	char* client_regargsbuff(
		ARemoteApiClient_t		in_client,
		const char*				in_func_name
		);

	int client_call(
		ARemoteApiClient_t		in_client
		);


private:

};

//inline const ARemoteApiClient* ARemoteApiClient::GetInstance()     { return (ARemoteApiClient*)s_pARemoteApiClient; }

#endif 
