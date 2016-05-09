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

#ifndef _AREMOTEAPI_H_
#define _AREMOTEAPI_H_

#include "metaconsts.h"
#include "metatypes.h"
#include "aostypes.h"
#include "arapi.h"
#include "alog.h"

/*for pointer, you must input arg as follows:
int ARemoteApiRelayer::onFwdCall(
	char* func_name,
	char* func_args
	)
{
	char* t_pargs = func_args;

	if( strcmp(func_name,"Foo") == 0 )
	{
		int index;
		Foo_t foo, *pfoo;

		ARAPI_MEM_TO_ARGS(t_pargs,int,index);
		ARAPI_MEM_TO_ARGS(t_pargs,Foo_t,foo); or ARAPI_MEM_TO_PTR_ARGS(t_pargs,Foo_t,pfoo) //later one is faster

		Foo(index,&foo); or Foo(index,pfoo); //later one is faster
	}
}
int AInfFpsvcClient::IdentifyCredential(
    int     index,
	Foo_t*  pfoo
    )
{	
	char* t_pargs = RegArgsBuff("IdentifyCredential");

	ARAPI_ARGS_TO_MEM(t_pargs,int,index);
	ARAPI_ARGS_TO_MEM(t_pargs,Foo_t,(*pfoo)); or ARAPI_PTR_ARGS_TO_MEM(t_pargs,Foo_t,pfoo)//both are same preformance

	return Call();
}

*/

#define WZ_TPAD(x)	(((sizeof(x)+7)/8)*8)
#define WZ_SCHK(x)	(((sizeof(x)+7)/8)*8)

#define ARAPI_MEM_TO_ARGS(p,type,arg)			\
	{											\
		volatile void* volatile mem = (p);		\
		(arg) = *((type*)mem);					\
		(p) = ((char*)mem) + WZ_TPAD(type);		\
	}

#define ARAPI_ARGS_TO_MEM(p,type,arg)			\
	{											\
		volatile void* volatile mem = (p);		\
		*((type*)mem) = (arg);					\
		(p) = ((char*)mem) + WZ_TPAD(type);		\
	}

#define ARAPI_MEM_TO_PTR_ARGS(p,type,ptr)		\
	{										    \
		volatile void* volatile mem = (p);		\
		(ptr) = (type*)mem;						\
		(p) = ((char*)mem) + WZ_TPAD(type);		\
	}

#define ARAPI_PTR_ARGS_TO_MEM(p,type,ptr)		\
	{										    \
		volatile void* volatile mem = (p);		\
		*((type*)mem) = *((type*)(ptr));	    \
		(p) = ((char*)mem) + WZ_TPAD(type);		\
	}

#define ARAPI_SKIP_ARGS_INMEM(p,type)			\
	{											\
		volatile void* volatile mem = (p);		\
		(p) = ((char*)mem) + WZ_TPAD(type);		\
	}

typedef struct{
	char m_name[OSTAGName_Len+1];
} AInfIPCNameStr_t;

typedef struct{
	long  m_clientid;
} AInfClientInfo_t;

typedef struct{
	char m_RApiClientSideRelayerName[OSTAGName_Len+1];
	long m_clientid;
	long m_context;
} AInfCallContext_t;

class AInterface;

class ARemoteApi
{
public:

protected:
	char						m_sRelayerName[OSTAGName_Len+1];
	ARemoteApiClient_t			m_ClientHandle;
	AInterface*					m_pAttachedInf;
	//static ARemoteApi*		s_pRemoteApiRelayer;

private:

public:
	virtual						~ARemoteApi();

	//static const ARemoteApi*  GetRelayer();
	const char*					GetRelayerName();
	//return 0 == succ
	virtual int					PingRelayer();
	virtual int					Dump();

	virtual const ARemoteApi*   Client();
	virtual const ARemoteApi*   Relayer();

	Boolean						IsInstanceClient();

protected:
								ARemoteApi(
									const char* inName,
									AInterface* inAttachedInf
									);

	//return 0 == succ
	virtual int					onPingRelayer();

	virtual int					ClientConnect(
		AInfIPCNameStr_t*		inRApiClientSideRelayerName,
		AInfClientInfo_t*		inInfClientInfo
		);
	virtual int					ClientDisConnect();

	virtual int					OnClientConnect(
		AInfIPCNameStr_t*		inRApiClientSideRelayerName,
		AInfClientInfo_t*		inInfClientInfo
		);
	virtual int					OnClientDisConnect();

private:
};

inline const char*       ARemoteApi::GetRelayerName()  { return m_sRelayerName; }
//inline const ARemoteApi* ARemoteApi::GetRelayer()     { return s_pRemoteApiRelayer; }
inline Boolean           ARemoteApi::IsInstanceClient(){ return m_ClientHandle? TRUE:FALSE; }

#endif 
