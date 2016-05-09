/*******************************************************************************
* @autility.h
* @description: universal utility
* @author     : zangxiaohua
* @version    : 1.0
* @history
* 1. created on 20090826
* 2. 
*******************************************************************************/
#ifndef ___AUTILITY_H___
#define ___AUTILITY_H___

#ifdef __cplusplus
extern "C"
{
#endif

//******************************************************************************
// Global Macros
//******************************************************************************

//******************************************************************************
// Global Typedefs
//******************************************************************************

//******************************************************************************
// Global Function Prototypes
//******************************************************************************

/** augusta_env_get
 * @brief  to get environment variable.
 * @exception NULL
 * @param     [IN] _name is the name of the environment variable you want.
 * @return    the value of the environment variable.
 */
char* augusta_env_get(const char* _name);

/** string_split
 * @brief  to split a string by the specified sign.
 * @exception NULL
 * @param     [IN] _src the string you want to split.
 * @param     [IN] _sign the sign to separate the string.
 * @param     [IN] _update the callback invoked each sub-string.
 * @param     [IN] _arg the parameter of the callback.
 * @return    0.
 */
typedef unsigned char (*STRINGSPLIT_Update)(char*, void*);
unsigned char string_split(char* _src, char _sign, STRINGSPLIT_Update _update, void* _arg);

typedef enum {
    MMI_PTHREAD_IDLE            = 0,
    MMI_PTHREAD_LOWEST          = 1,
    MMI_PTHREAD_BELOW_NORMAL    = 2,
    MMI_PTHREAD_NORMAL          = 3,
    MMI_PTHREAD_ABOVE_NORMAL    = 4,
    MMI_PTHREAD_HIGHEST         = 5,
} MMI_PTHREAD_PRIORITY;

void MMI_setPthreadPrio( void * threadIdPtr, MMI_PTHREAD_PRIORITY prio );

#ifdef __cplusplus
}
#endif

#endif /// ___VUTILITY_H___
