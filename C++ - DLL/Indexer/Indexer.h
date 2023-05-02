#include "jni_md.h"
#include "jni.h"

#ifndef _Included_Indexer
#define _Included_Indexer
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_Indexer_collect(JNIEnv *, jclass, jobject);

#ifdef __cplusplus
}
#endif
#endif