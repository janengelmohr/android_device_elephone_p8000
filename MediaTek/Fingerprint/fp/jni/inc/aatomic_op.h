/*******************************************************************************
* @vbasicatomic.h
* @description: VBasicAtomic
* @author     : zangxiaohua
* @version    : 1.0
* @history
* 1. created on 20090611
* 2. 
*******************************************************************************/
#ifndef ___ATOMIC_OP_H___
#define ___ATOMIC_OP_H___

#include <sched.h>

#ifdef __cplusplus
extern "C"
{
#endif

//**************************************
// Global Macros
//**************************************
#define ARM_PROCESSOR
//**************************************
// Global Typedefs
//**************************************

//**************************************
// Global Function
//**************************************
#ifdef ARM_PROCESSOR
extern char v_atomic_lock;

inline char v_atomic_swp(volatile char *ptr, char newval)
{
    register int ret;
    asm volatile("swpb %0,%2,[%3]"
                 : "=&r"(ret), "=m" (*ptr)
                 : "r"(newval), "r"(ptr)
                 : "cc", "memory");
    return ret;
}

inline int v_atomic_test_and_set_int(volatile int *ptr, int expected, int newval)
{
    int ret = 0;
    while (v_atomic_swp(&v_atomic_lock, ~0) != 0) sched_yield();
    if (*ptr == expected) {
    *ptr = newval;
    ret = 1;
    }
    v_atomic_swp(&v_atomic_lock, 0);
    return ret;
}

inline int v_atomic_test_and_set_acquire_int(volatile int *ptr, int expected, int newval)
{
    return v_atomic_test_and_set_int(ptr, expected, newval);
}

inline int v_atomic_test_and_set_release_int(volatile int *ptr, int expected, int newval)
{
    return v_atomic_test_and_set_int(ptr, expected, newval);
}

inline int v_atomic_test_and_set_ptr(volatile void *ptr, void *expected, void *newval)
{
    int ret = 0;
    while (v_atomic_swp(&v_atomic_lock, ~0) != 0) sched_yield();
    if (*reinterpret_cast<void * volatile *>(ptr) == expected) {
    *reinterpret_cast<void * volatile *>(ptr) = newval;
    ret = 1;
    }
    v_atomic_swp(&v_atomic_lock, 0);
    return ret;
}

inline int v_atomic_increment(volatile int *ptr)
{
    while (v_atomic_swp(&v_atomic_lock, ~0) != 0) sched_yield();
    int originalValue = *ptr;
    *ptr = originalValue + 1;
    v_atomic_swp(&v_atomic_lock, 0);
    return originalValue != -1;
}

inline int v_atomic_decrement(volatile int *ptr)
{
    while (v_atomic_swp(&v_atomic_lock, ~0) != 0) sched_yield();
    int originalValue = *ptr;
    *ptr = originalValue - 1;
    v_atomic_swp(&v_atomic_lock, 0);
    return originalValue != 1;
}

inline int v_atomic_set_int(volatile int *ptr, int newval)
{
    while (v_atomic_swp(&v_atomic_lock, ~0) != 0) sched_yield();
    int originalValue = *ptr;
    *ptr = newval;
    v_atomic_swp(&v_atomic_lock, 0);
    return originalValue;
}

inline void *v_atomic_set_ptr(volatile void *ptr, void *newval)
{
    while (v_atomic_swp(&v_atomic_lock, ~0) != 0) sched_yield();
    void *originalValue = *reinterpret_cast<void * volatile *>(ptr);
    *reinterpret_cast<void * volatile *>(ptr) = newval;
    v_atomic_swp(&v_atomic_lock, 0);
    return originalValue;
}

inline int v_atomic_fetch_and_add_int(volatile int *ptr, int value)
{
    while (v_atomic_swp(&v_atomic_lock, ~0) != 0) sched_yield();
    int originalValue = *ptr;
    *ptr += value;
    v_atomic_swp(&v_atomic_lock, 0);
    return originalValue;
}

inline int v_atomic_fetch_and_add_acquire_int(volatile int *ptr, int value)
{
    return v_atomic_fetch_and_add_int(ptr, value);
}

inline int v_atomic_fetch_and_add_release_int(volatile int *ptr, int value)
{
    return v_atomic_fetch_and_add_int(ptr, value);
}
#else
inline int v_atomic_test_and_set_int(volatile int *ptr, int expected, int newval)
{
    if (*ptr == expected)
    {
        *ptr = newval;
        return 1;
    }
    return 0;
}

inline int v_atomic_test_and_set_acquire_int(volatile int *ptr, int expected, int newval)
{
    return v_atomic_test_and_set_int(ptr, expected, newval);
}

inline int v_atomic_test_and_set_release_int(volatile int *ptr, int expected, int newval)
{
    return v_atomic_test_and_set_int(ptr, expected, newval);
}

inline int v_atomic_test_and_set_ptr(volatile void *ptr, void *expected, void *newval)
{
    if (*reinterpret_cast<void * volatile *>(ptr) == expected)
    {
        *reinterpret_cast<void * volatile *>(ptr) = newval;
        return 1;
    }
    return 0;
}

inline int v_atomic_increment(volatile int *ptr)
{
    return ++(*ptr);
}

inline int v_atomic_decrement(volatile int *ptr)
{
    return --(*ptr);
}

inline int v_atomic_set_int(volatile int *ptr, int newval)
{
    register int ret = *ptr;
    *ptr = newval;
    return ret;
}

inline void *v_atomic_set_ptr(volatile void *ptr, void *newval)
{
    register void *ret = *reinterpret_cast<void * volatile *>(ptr);
    *reinterpret_cast<void * volatile *>(ptr) = newval;
    return ret;
}

inline int v_atomic_fetch_and_add_int(volatile int *ptr, int value)
{
    int originalValue = *ptr;
    *ptr += value;
    return originalValue;
}

inline int v_atomic_fetch_and_add_acquire_int(volatile int *ptr, int value)
{
    return v_atomic_fetch_and_add_int(ptr, value);
}

inline int v_atomic_fetch_and_add_release_int(volatile int *ptr, int value)
{
    return v_atomic_fetch_and_add_int(ptr, value);
}
#endif

#ifdef __cplusplus
}
#endif

#endif /// ___ATOMIC_OP_H___
