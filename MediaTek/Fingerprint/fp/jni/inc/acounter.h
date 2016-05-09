/*  ====================================================================================================
**
**  ---------------------------------------------------------------------------------------------------
**
**	File Name:  	acounter.h
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

#ifndef _ACOUNTER_H_
#define _ACOUNTER_H_

#include "aostypes.h"

#ifdef __cplusplus
extern "C"
{
#endif

/*  ====================================================================================================
**
**  ---------------------------------------------------------------------------------------------------
**
**	File Name:  	acounter.cpp
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

void ACNT_Init( void );

void ACNT_Enable( void );

void ACNT_Disable( void );

UInt32 ACNT_GetTicks( void );

void ACNT_Shutdown( void );

UInt32 ACNT_GetDownCountDelta( UInt32 counter );

UInt32 ACNT_GetDownCountValue( void );

#ifdef __cplusplus
}
#endif


#endif

