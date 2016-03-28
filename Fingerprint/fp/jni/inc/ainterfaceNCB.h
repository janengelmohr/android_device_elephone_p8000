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


#ifndef __AINTERFACE_NCB_H__
#define __AINTERFACE_NCB_H__

#include "metaconsts.h"
#include "metatypes.h"
#include "aostypes.h"
#include "arapi.h"
#include "aarray.h"
#include "ainterface.h"

class AInterfaceNCB : public AInterface
{
protected:

public:
    virtual         ~AInterfaceNCB();

protected:
                    AInterfaceNCB(
						const char* inName,
						int			isRelayer,
						ARemoteApi* rApi,
						AInterface*	inInfCallBackRelayer
						);

	virtual AInterface*	OnInfCallBackClientCreate(
		const char	*inRelayerNameOfCallBackClient
		);

};

#endif 
