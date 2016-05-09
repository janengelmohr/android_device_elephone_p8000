/*******************************************************************************
**
******************************************************************************/

#ifndef ___AVTINTERRUPT_H___
#define ___AVTINTERRUPT_H___

#include "aostypes.h"

#ifdef __cplusplus
extern "C"
{
#endif

/***************************************
 * Global Macros
***************************************/
#define VTINTR_Ttl   32
#define VTINTRNO_Min 0
#define VTINTRNO_Max (VTINTR_Ttl - 1)

/***************************************
 * Global Typedefs
***************************************/
typedef unsigned long AVTIntr_t;
typedef void (*VTINTREntry_t)(void);

/***************************************
 * Global Function Prototypes
***************************************/

/** avtintr_init
 * @brief  to initialize virtual soft interrupt vectors table.
 * @exception NULL
 * @param     NULL
 * @return    A return value of OSSTATUSFailure represents the <avtintr_init> operation failed, OSSTATUSSuccess sucessful.
 */
OSStatus_t avtintr_init();

/** avtintr_reg
 * @brief  to register an access assigned to interrupt no in virtual soft interrupt vectors table.
 * @exception NULL
 * @param [IN] _id is the assigned interrupt no.
 * @param [IN] _entry is the access assigned to the _id.
 * @return    A return value of OSSTATUSFailure represents the <avtintr_init> operation failed, OSSTATUSSuccess sucessful.
 */
OSStatus_t avtintr_reg(AVTIntr_t _id, VTINTREntry_t _entry);

/** avtintr_reg
 * @brief  to unregister an access assigned to interrupt no in virtual soft interrupt vectors table.
 * @exception NULL
 * @param [IN] _id is the assigned interrupt no.
 * @return    A return value of OSSTATUSFailure represents the <avtintr_init> operation failed, OSSTATUSSuccess sucessful.
 */
OSStatus_t avtintr_unreg(AVTIntr_t _id);

/** avtintr_enable
 * @brief  to enable interrupt no in virtual soft interrupt vectors table.
 * @exception NULL
 * @param [IN] _id is the assigned interrupt no.
 * @return    A return value of OSSTATUSFailure represents the <avtintr_init> operation failed, OSSTATUSSuccess sucessful.
 */
OSStatus_t avtintr_enable(AVTIntr_t _id);

/** avtintr_disable
 * @brief  to disable interrupt no in virtual soft interrupt vectors table.
 * @exception NULL
 * @param [IN] _id is the assigned interrupt no.
 * @return    A return value of OSSTATUSFailure represents the <avtintr_init> operation failed, OSSTATUSSuccess sucessful.
 */
OSStatus_t avtintr_disable(AVTIntr_t _id);

/** avtintr_trigger
 * @brief  to trigger interrupt no in virtual soft interrupt vectors table.
 * @exception NULL
 * @param [IN] _id is the assigned interrupt no.
 * @param [IN] _pid is the dest process id you want to trigger a interrupt.
 * @return    A return value of OSSTATUSFailure represents the <avtintr_init> operation failed, OSSTATUSSuccess sucessful.
 */
OSStatus_t avtintr_trigger(AVTIntr_t _id, int _pid);

/** avtintr_enableall
 * @brief  to enable all interrupt no in virtual soft interrupt vectors table.
 * @exception NULL
 * @param     NULL
 * @return    A return value of OSSTATUSFailure represents the <avtintr_init> operation failed, OSSTATUSSuccess sucessful.
 */
OSStatus_t avtintr_enableall();

/** avtintr_disableall
 * @brief  to disableall all interrupt no in virtual soft interrupt vectors table.
 * @exception NULL
 * @param     NULL
 * @return    A return value of OSSTATUSFailure represents the <avtintr_init> operation failed, OSSTATUSSuccess sucessful.
 */
OSStatus_t avtintr_disableall();

#ifdef __cplusplus
}
#endif

#endif /// ___AVTINTERRUPT_H___
