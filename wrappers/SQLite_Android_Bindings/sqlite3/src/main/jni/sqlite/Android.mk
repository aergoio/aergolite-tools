LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := libbinn
LOCAL_SRC_FILES := ../binn/$(TARGET_ARCH_ABI)/libbinn.so
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/binn/include
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := libuv
LOCAL_SRC_FILES := ../libuv/$(TARGET_ARCH_ABI)/lib/libuv.so
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/libuv/$(TARGET_ARCH_ABI)/include
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := libsecp256k1-vrf
LOCAL_SRC_FILES := ../secp256k1-vrf/$(TARGET_ARCH_ABI)/lib/libsecp256k1-vrf.so
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/secp256k1-vrf/$(TARGET_ARCH_ABI)/include
include $(PREBUILT_SHARED_LIBRARY)


include $(CLEAR_VARS)

# Debug mode:
#LOCAL_CFLAGS += -g -DSQLITE_DEBUG=1 -DDEBUGPRINT -rdynamic -funwind-tables

LOCAL_CFLAGS += -DSQLITE_HAS_CODEC
#LOCAL_CFLAGS += -DSQLITE_USE_URI=1
#LOCAL_CFLAGS += -DSQLITE_ENABLE_JSON1
LOCAL_CFLAGS += -DSQLITE_THREADSAFE=1
LOCAL_CFLAGS += -DSQLITE_ENABLE_COLUMN_METADATA

LOCAL_CFLAGS += -DSQLITE_OMIT_BUILTIN_TEST
#LOCAL_CFLAGS += -DSQLITE_OMIT_LOAD_EXTENSION  -- used by the ledger interface

#Define HAVE_USLEEP, otherwise ALL sleep() calls take at least 1000ms
LOCAL_CFLAGS += -DHAVE_USLEEP=1

# Enable SQLite extensions.
LOCAL_CFLAGS += -DSQLITE_ENABLE_FTS5 
LOCAL_CFLAGS += -DSQLITE_ENABLE_RTREE
LOCAL_CFLAGS += -DSQLITE_ENABLE_JSON1
LOCAL_CFLAGS += -DSQLITE_ENABLE_FTS3
LOCAL_CFLAGS += -DSQLITE_ENABLE_BATCH_ATOMIC_WRITE

# This is important - it causes SQLite to use memory for temp files. Since 
# Android has no globally writable temp directory, if this is not defined the
# application throws an exception when it tries to create a temp file.
#
LOCAL_CFLAGS += -DSQLITE_TEMP_STORE=3

LOCAL_CFLAGS += -DHAVE_CONFIG_H -DKHTML_NO_EXCEPTIONS -DGKWQ_NO_JAVA
LOCAL_CFLAGS += -DNO_SUPPORT_JS_BINDING -DQT_NO_WHEELEVENT -DKHTML_NO_XBL
LOCAL_CFLAGS += -U__APPLE__
LOCAL_CFLAGS += -DHAVE_STRCHRNUL=0
LOCAL_CFLAGS += -DSQLITE_USE_URI=1
LOCAL_CFLAGS += -Wno-unused-parameter -Wno-int-to-pointer-cast
LOCAL_CFLAGS += -Wno-uninitialized -Wno-parentheses
LOCAL_CPPFLAGS += -Wno-conversion-null


ifeq ($(TARGET_ARCH), arm)
	LOCAL_CFLAGS += -DPACKED="__attribute__ ((packed))"
else
	LOCAL_CFLAGS += -DPACKED=""
endif

LOCAL_SRC_FILES:=                             \
	android_database_SQLiteCommon.cpp     \
	android_database_SQLiteConnection.cpp \
	android_database_SQLiteGlobal.cpp     \
	android_database_SQLiteDebug.cpp      \
	JNIHelp.cpp JniConstants.cpp

BASE = ../../../../../../../..

LOCAL_SRC_FILES += $(BASE)/core/sqlite3.c
LOCAL_SRC_FILES += $(BASE)/plugins/no-leader/no-leader.c

LOCAL_C_INCLUDES += $(LOCAL_PATH) $(LOCAL_PATH)/nativehelper/
LOCAL_C_INCLUDES += $(LOCAL_PATH)/../binn/include
LOCAL_C_INCLUDES += $(LOCAL_PATH)/../libuv/x86_64/include
LOCAL_C_INCLUDES += $(LOCAL_PATH)/../secp256k1-vrf/x86_64/include
LOCAL_C_INCLUDES += $(LOCAL_PATH)/$(BASE)/core

LOCAL_MODULE:= libsqliteX
LOCAL_LDLIBS += -ldl -llog 

LOCAL_SHARED_LIBRARIES := libbinn libuv libsecp256k1-vrf

include $(BUILD_SHARED_LIBRARY)
