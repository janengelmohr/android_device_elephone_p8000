/*  ====================================================================================================
**
**  ---------------------------------------------------------------------------------------------------
**
**    File Name:      awakelock
**    
**    Description:    This file contains the implementation of OS wrapper.
**
**                    this is kernal code of SW framework.
**                    It contributes one of functionalities of SW Platform. 
**                    If the checkin is CR not PR, to add change History to this file head part 
**                    will be appreciated.
**
**  ---------------------------------------------------------------------------------------------------
**
**    Author:	      Warren Zhao
**
** -------------------------------------------------------------------------
**
**    Change History:
**    
**    Initial revision: Warren Zhao
**
**====================================================================================================*/

#ifndef __AWAKELOCK_HEADER__
#define __AWAKELOCK_HEADER__

#ifdef __cplusplus
extern "C" {
#endif

int AWAKELOCK_AcqWakeLock(const char* id);

int AWAKELOCK_RelWakeLock(const char* id);

#ifdef __cplusplus
}
#endif

#endif