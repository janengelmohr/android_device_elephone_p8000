/*  ====================================================================================================
**
**  ---------------------------------------------------------------------------------------------------
**
**	File Name:  	aos.h
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


#ifndef _AOS_H_
#define	_AOS_H_

#include "aostypes.h"

typedef enum {
	PREEMPTION,
	NO_PREEMPTION
} OSPreemption_t;

OSPreemption_t AOS_ChangePreemption(		// change the OS Preemption setting
	OSPreemption_t preemption			// new preemption value
);

#endif
