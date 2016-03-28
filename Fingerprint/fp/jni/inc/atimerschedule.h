/******************************************************************************/
/** @file
 *  @brief   This is a set of macro definition about tailQ and time.
 *  @author  Zang Xiaohua
 *  @date    20090423
 *  @version 1.0.0
 *  @details 
 */
 /*****************************************************************************/
#ifndef ___VTIMERSCHEDULE_H___
#define ___VTIMERSCHEDULE_H___

#ifdef __cplusplus
extern "C"
{
#endif

#include <sys/times.h>
#include <unistd.h>

/** @brief The time struct we used for our timer.*/
typedef struct 
{
    long tv_sec;
    long tv_usec;
}vtimeval_t;

/***************************************
 * Global Macros
***************************************/
#define USE_CLOCK_MONOTONIC

/* Operations on timevals. */
#define timeval_clear(tvp)     (tvp)->tv_sec = (tvp)->tv_usec = 0
#define timeval_isset(tvp)     ((tvp)->tv_sec || (tvp)->tv_usec)

#define timeval_cmp(tvp, uvp, cmp)                                      \
    (((tvp)->tv_sec == (uvp)->tv_sec) ?                                 \
    ((tvp)->tv_usec cmp (uvp)->tv_usec) :                               \
    ((tvp)->tv_sec cmp (uvp)->tv_sec))

#define timeval_add(tvp, uvp, vvp)                                      \
    do {                                                                \
        (vvp)->tv_sec = (tvp)->tv_sec + (uvp)->tv_sec;                  \
        (vvp)->tv_usec = (tvp)->tv_usec + (uvp)->tv_usec;               \
        if ((vvp)->tv_usec >= 1000000) {                                \
            (vvp)->tv_sec++;                                            \
            (vvp)->tv_usec -= 1000000;                                  \
        }                                                               \
    } while (0)

#define timeval_sub(tvp, uvp, vvp)                                      \
    do {                                                                \
        (vvp)->tv_sec = (tvp)->tv_sec - (uvp)->tv_sec;                  \
        (vvp)->tv_usec = (tvp)->tv_usec - (uvp)->tv_usec;               \
        if ((vvp)->tv_usec < 0) {                                       \
            (vvp)->tv_sec--;                                            \
            (vvp)->tv_usec += 1000000;                                  \
        }                                                               \
    } while (0)

/*
 *  Please add your comment here
 *  TAILQ macro defination
 */
#define TAILQ_HEAD(name, type)                                          \
struct name{                                                            \
    struct type *tqh_first;     /* first element */                     \
    struct type **tqh_last;     /* addr of last next element */         \
}

#define TAILQ_ENTRY(type)                                               \
struct {                                                                \
    struct type *tqe_next;      /* next element */                      \
    struct type **tqe_prev;     /* addr of previous next element*/      \
}

#define TAILQ_INIT(head) do {                                           \
    (head)->tqh_first = NULL;                                           \
    (head)->tqh_last = &(head)->tqh_first;                              \
} while (0)

#define TAILQ_INSERT_TAIL(head, elm, field) do {                        \
    (elm)->field.tqe_next = NULL;                                       \
    (elm)->field.tqe_prev = (head)->tqh_last;                           \
    *(head)->tqh_last = (elm);                                          \
    (head)->tqh_last = &(elm)->field.tqe_next;                          \
} while (0)

#define TAILQ_INSERT_BEFORE(listelm, elm, field) do {                   \
    (elm)->field.tqe_prev = (listelm)->field.tqe_prev;                  \
    (elm)->field.tqe_next = (listelm);                                  \
    *(listelm)->field.tqe_prev = (elm);                                 \
    (listelm)->field.tqe_prev = &(elm)->field.tqe_next;                 \
} while (0)

#define TAILQ_REMOVE(head, elm, field) do {                             \
    if (((elm)->field.tqe_next) != NULL)                                \
        (elm)->field.tqe_next->field.tqe_prev = (elm)->field.tqe_prev;  \
    else                                                                \
        (head)->tqh_last = (elm)->field.tqe_prev;                       \
    *(elm)->field.tqe_prev = (elm)->field.tqe_next;                     \
} while (0)

/*
 * Tail queue access methods.
 */
#define TAILQ_EMPTY(head)            ((head)->tqh_first == NULL)
#define TAILQ_FIRST(head)            ((head)->tqh_first)
#define TAILQ_NEXT(elm, field)       ((elm)->field.tqe_next)
#define TAILQ_FOREACH(var, head, field)                                 \
    for ((var) = TAILQ_FIRST(head);                                     \
        (var);                                                          \
        (var) = TAILQ_NEXT(var, field))


/** 
 * Names of the interval timers, and structure
 * defining a timer setting.
 */

#define TIMERKIND_LIMITED       0
#define TIMERKIND_UNLIMITED     1

#define TIMERSTAT_IDLE          0
#define TIMERSTAT_READY         1
#define TIMERSTAT_ACTIVE        2

#define TIMERSWITCH_OFF         0
#define TIMERSWITCH_ON          1

#define TIMEOUT_DEFAULT         1

#define USE_CLOCK_MONOTONIC
/***************************************
 * Global Typedefs
***************************************/

/** @brief the attribute of the timer */
typedef struct _timerattr
{
    unsigned long timer_kind   : 1; /**< 0: repeat limited times 1: unlimited times */
    unsigned long status       : 2; /**< 0: 1: 2: 3: */
    unsigned long onoff        : 1; /**< 0: 1:       */
    unsigned long reserve      : 4;
    unsigned long times        : 24;
} timerattr_t;

typedef void* evtimer_t;

/***************************************
 * Global Function Prototypes
***************************************/

/** evtimer_init
 * @brief  to initialize timer sub-system.
 * @exception NULL
 * @param     NULL
 * @return    NULL
 */
void evtimer_init();

/** time_relative
 * @brief  The time have elapsed since the moment the system was booted.
 * @exception NULL
 * @param [IN/OUT] _tv is theplace to store the result.
 * @return NULL.
 */
/// void time_relative(vtimeval_t* _tv);

/** evtimer_new
 * @brief  to create a timer.
 * @exception NULL
 * @param     NULL
 * @return    NULL
 */
evtimer_t evtimer_new();
void evtimer_free(evtimer_t);

/** timer_set
 * @brief  to create a new timer according to the arguments.
 * @exception NULL
 * @param [IN]  _t    is the timer interval.
 * @param [IN]  _attr is the timer attribute, please refer to ETIMERKind_t.
 * @param [IN]  _obj  is the object releated to this timer.
 * @param [In]  _cb   is the call back function launched when timeout.
 * @param [IN]  _arg  is the argument on which the _cb function will be launched.
 * @return the newly-create timer event.
 */
void  
evtimer_set(evtimer_t   _ev,
            vtimeval_t  _t, 
            timerattr_t _attr, 
            void*       _obj,
            void (*_cb)(void* _arg), 
            void* _arg);

/** timer_add
 * @brief  to add the specified timer according to the arguments to the timer list.
 * @exception NULL
 * @param [IN] _ev  is the specified timer you want to add to timer list.
 * @return NULL.
 */
void evtimer_add(evtimer_t _ev);

/** timer_del
 * @brief  to delete the specified timer according to the arguments from the timer list.
 * @exception NULL
 * @param [IN] _ev  is the specified timer you want to delete from timer list.
 * @return NULL.
 */
void evtimer_del(evtimer_t _ev);

/** evtimer_post
 * @brief  to post the temp timer event to the timer list.
 * @exception NULL
 * @param     NULL
 * @return    NULL
 */
void evtimer_post();

/** timeout_next
 * @brief  returns the time to wait for the next timer
 * @exception NULL
 * @param [IN/OUT] _tv is theplace to store the result.
 * @return A return value of -1 represents the <timeout_next> operation failed, 0 sucessful.
 */
void timeout_next(vtimeval_t*  _tv);

/** timeout_process
 * @brief  Activate pending timers.
 * @exception NULL
 * @param     NULL
 * @return    NULL
 */
void timeout_process(void);

#ifdef __cplusplus
}
#endif

#endif /// ___VTIMERSCHEDULE_H___

