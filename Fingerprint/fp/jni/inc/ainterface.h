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


#ifndef __AINTERFACE_H__
#define __AINTERFACE_H__

#include "metaconsts.h"
#include "metatypes.h"
#include "aostypes.h"
#include "arapi.h"
#include "aarray.h"
#include "aremoteapi.h"
#include "agc.h"
#include "alog.h"

typedef struct{
	UInt8			mStamp[16];
} AINTERFACE_t;

class AInterface : AINTERFACE_t 
{
protected:
	//interface id data: must use these 6 combo to present the interface channel.
	char			m_RApiClientSideRelayerName[OSTAGName_Len+1]; //this is client name == relayer name of callback relayer if any or default name
	char			m_InfName[OSTAGName_Len+1];
	long			m_RApiClientInfID; //Inf client Pointer Value;
	int				m_isRelayer;//decide if the interface endpoint is service side or client side;
	int				m_isCallBackInf;//decide if the interface channel is callback or forward call service channel.

public:
	//interface id methods;
	const char*				GetSvcServerSideRelayerName(); //same as attachedRapi Relayer Name;
	const char*				GetSvcClientSideRelayerName();
	const char*				GetRApiServerSideRelayerName(); //same as attachedRapi Relayer Name;
	const char*				GetRApiClientSideRelayerName();
	const char*				GetInfName();
	long					GetRApiClientInfID();
	int						IsRelayer();
	int						IsCallBackInf();

protected:

	ARemoteApi*				m_AttachedRApi;
	AInterface*				m_InfCallBackOriginalRelayer;//only non-callback client has this.
	AInterface*				m_InfCallBackClient;//only non-callback relayer has this.
	AInterface*				m_InfCallBackRelayer;//only non-callback client has this.
	AInterface*				m_InfSvcRelayer;//only callback client has this.
	AInterface*				m_InfSvcClient;//only callback relayer has this.
	static AArray*			s_RelayerInterfaces;
	static AArray*			s_RelayerInfCallBackClientList;
	void*					m_pContext;
	int						m_SvcClientInfID;	//for callback to bind svc client ID;

public:
    virtual					~AInterface();
	const ARemoteApi*		GetAttachedRApi();
	const AInterface*		GetAttachedCallBackOriginalRelayer();
	const AInterface*		GetAttachedCallBackClient();
	const AInterface*		GetAttachedCallBackRelayer();
	const AInterface*		GetAttachedSvcRelayer();
	const AInterface*		GetAttachedSvcClient();
	void					SetAttachedCallBackClient(AInterface* inInf);
	void					SetAttachedCallBackRelayer(AInterface* inInf);
	void					SetAttachedSvcRelayer(AInterface* inInf);
	void					SetAttachedSvcClient(AInterface* inInf);

	static const AArray*	GetRelayerInterfacesList();
	static AInterface*		GetRelayerInterface(
		char*				inInterfaceName,
		char*				inRApiClientSideRelayerName,
		long				in_RApiClientInfID
		);
	static const AArray*	GetInfCallBackClientListOnSvcServerSide();
	static AInterface*		GetInfCallBackClientOnSvcServerSide(
		char*				inRelayerNameOfCallBackClient
		);

	static int				InfClientConnect(
		AInterface*			inInf,
		AInfIPCNameStr_t*	inRApiClientSideRelayerName,
		AInfClientInfo_t*	inInfClientInfo,
		AInterface**		oppInf
		);
	static int				InfClientDisConnect(
		AInterface*			inInf
		);

	virtual const void*		GetContext();
	virtual void			SetContext(const void* inContext);

	AInterface*				InfDuplicate();

	static int				IsValid(
		AInterface			*inInf
		);

	void					SetInvalid();

	static AInterface*		HoldInf(AInterface* inInf);
	static void				UnHoldInf(AInterface* inInf);

protected:
					AInterface(
						const char*			inName,
						int					isRelayer,
						int					isCallBackInf,
						ARemoteApi*			rApi,
						AInterface*			inInfCallBackRelayer
						);

	virtual	AInterface*	OnInfDuplicate();

	virtual	void	AddInterface(
					AInterface*				inInterface
					);
	virtual	void	RemoveInterface(
					AInterface*				inInterface
					);

	virtual void	AddInfCallBackClient(
					AInterface*				inInterface
					);

	virtual void	RemoveInfCallBackClient(
					AInterface*				inInterface
					);

	virtual int			OnInfClientConnect(
		AInfIPCNameStr_t*					inRApiClientSideRelayerName,
		AInfClientInfo_t*					inInfClientInfo
		);
	virtual int			OnInfClientDisConnect();

	virtual AInterface*	OnInfCallBackClientCreate(
		const char	*inRelayerNameOfCallBackClient
		);

public:
	class AGcInf : public AGc
	{

	public:
									AGcInf();
		virtual						~AGcInf();

		virtual		int				Dump();
		virtual		int				GetPid(char* inStr);

	protected:
		virtual		int				OnRecycle();


	private:
	};

	const AGc*				GetGc();

protected:
	static AGcInf*			s_pGc;

};

inline const char*			AInterface::GetInfName()				{ return m_InfName; }
inline int					AInterface::IsRelayer()					{ return m_isRelayer; }
inline const ARemoteApi*	AInterface::GetAttachedRApi()			{ return m_AttachedRApi; }
inline const AInterface*	AInterface::GetAttachedCallBackOriginalRelayer(){ return m_InfCallBackOriginalRelayer; }//only non-callback client has this.
inline const AInterface*	AInterface::GetAttachedCallBackRelayer(){ return m_InfCallBackRelayer; }//only non-callback client has this.
inline const AInterface*	AInterface::GetAttachedSvcRelayer()		{ return m_InfSvcRelayer; }
inline const AInterface*	AInterface::GetAttachedSvcClient()		{ return m_InfSvcClient; }
inline void					AInterface::SetAttachedCallBackClient(AInterface* inInf)	{ m_InfCallBackClient = inInf; }
inline void					AInterface::SetAttachedCallBackRelayer(AInterface* inInf)	{ m_InfCallBackRelayer = inInf; }//only non-callback client has this.
inline void					AInterface::SetAttachedSvcRelayer(AInterface* inInf)		{ m_InfSvcRelayer = inInf; }
inline void					AInterface::SetAttachedSvcClient(AInterface* inInf)			{ m_InfSvcClient = inInf; }


inline int					AInterface::IsCallBackInf()				{ return m_isCallBackInf; }
inline long					AInterface::GetRApiClientInfID()		{ return m_RApiClientInfID; }
inline const char*			AInterface::GetRApiClientSideRelayerName() { return m_RApiClientSideRelayerName; }
inline const char*			AInterface::GetRApiServerSideRelayerName() { return m_AttachedRApi->GetRelayerName(); }

inline const AInterface*	AInterface::GetAttachedCallBackClient()	
{ 
	return m_InfCallBackClient; 
}

inline const char*			AInterface::GetSvcClientSideRelayerName() 
{ 
	if(!m_isCallBackInf)
	{
		return m_RApiClientSideRelayerName; 
	}
	else
	{
		return m_AttachedRApi->GetRelayerName();
	}
}
inline const char*			AInterface::GetSvcServerSideRelayerName() 
{
	if(!m_isCallBackInf)
	{
		return m_AttachedRApi->GetRelayerName();
	}
	else
	{
		return m_RApiClientSideRelayerName;
	}
}

inline int AInterface::IsValid(
	AInterface *inInterface
	)
{
	if(inInterface)
	{
		if( strcmp( (char*)( (AINTERFACE_t*)(inInterface)->mStamp ), "INTERFACE") == 0)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	else
	{
		return 0;
	}
}

inline void	AInterface::SetInvalid()
{
	memset((char*)mStamp,0,16);
}

inline const AGc* AInterface::GetGc() { return s_pGc; }

#endif 
