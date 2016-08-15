/*
 * =====================================================================================
 *
 *       Filename:  sl_log.h
 *
 *    Description:  
 *
 *        Version:  1.0
 *        Created:  
 *        Revision:  none
 *
 *         Author:  WY, 
 *        Company:  
 *
 * =====================================================================================
 */
#ifndef __SL_LOG_H__
#define __SL_LOG_H__

#include <unistd.h>

#ifdef __cplusplus
extern "C"
{
#endif

int sl_log_printf(int prio, const char *tag, const char *fmt, ...);

typedef enum sl_LogPriority {
    SL_LOG_VERBOSE = 1,
    SL_LOG_DEBUG,
    SL_LOG_INFO,
    SL_LOG_WARN,
    SL_LOG_ERROR,
    SL_LOG_FATAL,
    SL_LOG_SILENT, 
} sl_LogPriority; 
#ifndef SL_LOGV
#define SL_LOGV(f,...)	sl_log_printf(SL_LOG_VERBOSE, "SLCODE","%-15s:%04d:pid=%d:ctid=%d => "f, __FUNCTION__, __LINE__,getpid(),gettid(),##__VA_ARGS__);
#endif
#ifndef SL_LOGD
#define SL_LOGD(f,...)	sl_log_printf(SL_LOG_DEBUG, "SLCODE","%-15s:%04d:pid=%d:ctid=%d => "f, __FUNCTION__, __LINE__,getpid(),gettid(),##__VA_ARGS__);
#endif
#ifndef SL_LOGI
#define SL_LOGI(f,...)	sl_log_printf(SL_LOG_INFO, "SLCODE","%-15s:%04d:pid=%d:ctid=%d => "f, __FUNCTION__, __LINE__,getpid(),gettid(),##__VA_ARGS__);
#endif
#ifndef SL_LOGW
#define SL_LOGW(f,...)	sl_log_printf(SL_LOG_WARN, "SLCODE","%-15s:%04d:pid=%d:ctid=%d => "f, __FUNCTION__, __LINE__,getpid(),gettid(),##__VA_ARGS__);
#endif
#ifndef SL_LOGE
#define SL_LOGE(f,...)	sl_log_printf(SL_LOG_ERROR, "SLCODE","%-15s:%04d:pid=%d:ctid=%d => "f, __FUNCTION__, __LINE__,getpid(),gettid(),##__VA_ARGS__);
#endif
#ifndef SL_LOGF
#define SL_LOGF(f,...)	sl_log_printf(SL_LOG_FATAL, "SLCODE","%-15s:%04d:pid=%d:ctid=%d => "f, __FUNCTION__, __LINE__,getpid(),gettid(),##__VA_ARGS__);
#endif
#ifndef SL_LOGS
#define SL_LOGS(f,...)	sl_log_printf(SL_LOG_SILENT, "SLCODE","%-15s:%04d:pid=%d:ctid=%d => "f, __FUNCTION__, __LINE__,getpid(),gettid(),##__VA_ARGS__);
#endif

#ifdef __cplusplus
}
#endif

#endif
