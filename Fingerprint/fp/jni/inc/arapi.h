/*  ====================================================================================================
**
**  ---------------------------------------------------------------------------------------------------
**
**	File Name:  	aremoteapi.h
**	
**	Description:	This file contains the implementation of OS wrapper.
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
#ifndef __AREMOTE_API_H__
#define __AREMOTE_API_H__

#include <sys/types.h>
#include <stdio.h>
#include <string.h>

#ifdef __cplusplus
extern "C"
{
#endif

//////////////////////////////////////
//Client
//////////////////////////////////////
typedef void* ARemoteApiClient_t;

typedef int (*CB_AOSRemoteApi_Client_HandleReturn_Func_t)(
	void* rapi_client,
	char* func_name,
	char* func_args
	);

void aremoteapi_destroy_crashed_relayer(
	const char* in_relayer_name
	);

void aremoteapi_client_register( 
	ARemoteApiClient_t  in_client,
	void*				in_clientClass,
	CB_AOSRemoteApi_Client_HandleReturn_Func_t  in_AOSRemoteApi_Client_HandleReturn_Func
	);

ARemoteApiClient_t aremoteapi_create_client(
	const char* in_relayer_name,
	const char* in_inf_name
	);

int aremoteapi_client_set_attachedinfname(
	ARemoteApiClient_t  in_client,
	const char*			in_inf_name
	);

void aremoteapi_destroy_client(
	ARemoteApiClient_t in_client
	);

char* aremoteapi_client_regargsbuff(
	ARemoteApiClient_t in_client,
	const char*        in_func_name
	);

int aremoteapi_client_call(
	ARemoteApiClient_t in_client
	);

//////////////////////////////////////
//Relayer
//////////////////////////////////////
typedef int (*CB_AOSRemoteApi_Relayer_FwdCall_Func_t)(
	char* inf_name,
	char* func_name,
	char* func_args
	);

void aremoteapi_relayer_register( 
	CB_AOSRemoteApi_Relayer_FwdCall_Func_t  in_AOSRemoteApi_Relayer_FwdCall_Func
	);

void aremoteapi_create_relayer(
	const char* in_relayer_name
	);

void aremoteapi_destroy_relayer();
void aremoteapi_destroy_named_relayer(
	const char* in_relayer_name
	);

#ifdef __cplusplus
}
#endif

#endif
