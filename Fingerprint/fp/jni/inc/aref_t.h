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


#ifndef __AREF_H__
#define __AREF_H__

#include "metaconsts.h"
#include "metatypes.h"
#include "alog.h"

class AREF_t {
public:
	char			mStamp[16];
	int				mSeqNo;

	static int		IsValid(
		AREF_t		*inAREF,
		int			inSeqNo
		);
	void			SetInvalid();

protected:
					AREF_t();
	virtual			~AREF_t();
};

inline void	AREF_t::SetInvalid()
{
	memset((char*)mStamp,0,16);
}

inline int AREF_t::IsValid(
	AREF_t *inAREF,
	int		inSeqNo
	)
{
	if(inAREF)
	{
		if( strcmp( (char*)( ((AREF_t*)(inAREF))->mStamp ), "INTERFACE") == 0 )
		{
			if(inSeqNo == inAREF->mSeqNo)
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
	else
	{
		return 0;
	}
}

#endif 
