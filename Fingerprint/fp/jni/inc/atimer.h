/*  ====================================================================================================
**
**  ---------------------------------------------------------------------------------------------------
**
**	File Name:  	atimer.h
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

#ifndef _ATIMER_H_
#define _ATIMER_H_

#ifdef __cplusplus
extern "C"
{
#endif

#include "aostypes.h"

//******************************************************************************
// Global Macros
//******************************************************************************
#define NO_REPEAT_TIME					0	// do not repeat timer task, after
											// first timeout

//******************************************************************************
// Global Typedefs
//******************************************************************************
typedef void*	Timer_t;
typedef unsigned long	TimerID_t;

#define ATIMER_Create(entry,arg,init_time,repeat_time)  atimer_create(entry,arg,init_time,repeat_time,NULL)
#define ATIMER_Start(timer)                             atimer_start(timer)
#define ATIMER_Stop(timer)                              atimer_stop(timer)
#define ATIMER_Reset(timer)                             atimer_reset(timer)
#define ATIMER_Destroy(timer)                           atimer_destroy(timer)
//******************************************************************************
// Global Function Prototypes
//******************************************************************************
typedef Timer_t   atimer_t;
typedef TimerID_t atimerid_t;
typedef void (*TIMEREntry_t)(atimer_t _t, void* para);

/** vcnt_init
 * @brief  to 
 * @exception NULL
 * @param     [IN] _entry is the callback function launched when the associated timer is timeout.
 * @param     [IN/OUT] _id_ is a indentifer to the associated timer.
 * @param     [IN] _interval is the time interval after which the associated timer is timeout.
 * @param     [IN] _repeate_times is the times the associated timer is enabled.
 * @return    the handle you newly-created timer.
 */
atimer_t atimer_create(
    TIMEREntry_t _entry, 
    void*        _arg,
    Ticks_t      _interval,
    Ticks_t      _repeat_times,
    atimerid_t*  _id_);

/** atimer_start
 * @brief This function is to start the timer you newly-created.
 * @exception NULL
 * @param     [IN] _timer is the timer you want to start.
 * @return    NULL
 */
void atimer_start(atimer_t _timer);

/** atimer_stop
 * @brief This function is to stop the timer you specified.
 * @exception NULL
 * @param     [IN] _timer is the timer you want to stop.
 * @return    NULL
 */
void atimer_stop(atimer_t _timer);

/** atimer_reset
 * @brief This function is to re-start the timer you specified.
 * @exception NULL
 * @param     [IN] _timer is the timer you want to re-start.
 * @return    NULL
 */
void atimer_reset(atimer_t _timer);

/** atimer_destroy 
 * @brief This function is to destroy the timer you specified.
 * @exception NULL
 * @param     [IN] _timer is the timer you want to destroy.
 * @return    NULL
 */
void atimer_destroy(atimer_t _timer);

#ifdef __cplusplus
}
#endif

#endif

