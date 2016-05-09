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

#ifndef _AGC_H_
#define _AGC_H_

#include <unistd.h>
#include "metaconsts.h"
#include "metatypes.h"
#include "aostypes.h"
#include "alog.h"

class AGc
{
public:

protected:
	char						m_Name[OSTAGName_Len+1];

public:
	virtual						~AGc();

	virtual		int				Dump();
				int				Recycle();

				int				IsProcessDead(pid_t in_pid);

protected:
								AGc(
									const char* inName
									);
	virtual		int				OnRecycle();


private:
};

#endif 
