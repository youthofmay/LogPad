#include "pch.h"
#include "Indexer.h"

#define _WIN32_DCOM
#include <Windows.h>
#include <comdef.h>
#include <objbase.h>
#include <atlbase.h>
#include <iostream>
#include <WbemCli.h>
#include <comutil.h>
#include <WbemIdl.h>
#include <string>

#pragma comment(lib, "wbemuuid.lib")


BOOL APIENTRY DllMain(HMODULE hModule,
                      DWORD  ul_reason_for_call,
                      LPVOID lpReserved) {
    switch (ul_reason_for_call) {
    case DLL_PROCESS_ATTACH:
    case DLL_THREAD_ATTACH:
    case DLL_THREAD_DETACH:
    case DLL_PROCESS_DETACH:
        break;
    }
    return TRUE;
}

JNIEXPORT void JNICALL Java_Indexer_collect(JNIEnv* env, jclass cls, jobject obj) {
	HRESULT hr;
	hr = CoInitializeEx(NULL, COINIT_MULTITHREADED);
	if (FAILED(hr)) {
		exit(0);
	}
	hr = CoInitializeSecurity(NULL, -1, NULL, NULL, RPC_C_AUTHN_LEVEL_DEFAULT, RPC_C_IMP_LEVEL_IMPERSONATE, NULL, EOAC_NONE, NULL);
	if (FAILED(hr)) {
		exit(0);
	}
	CComPtr <IWbemLocator> pLoc;
	hr = CoCreateInstance(CLSID_WbemAdministrativeLocator, NULL, CLSCTX_INPROC_SERVER, IID_IWbemLocator, reinterpret_cast<void**>(&pLoc));
	if (FAILED(hr)) {
		exit(0);
	}
	CComPtr <IWbemServices> pSvc;
	hr = pLoc->ConnectServer((BSTR)L"ROOT\\CIMV2", NULL, NULL, NULL, WBEM_FLAG_CONNECT_USE_MAX_WAIT, NULL, NULL, &pSvc);
	if (SUCCEEDED(hr)) {
		CComPtr<IEnumWbemClassObject> pEnum;
		hr = pSvc->ExecNotificationQuery((BSTR)L"WQL", (BSTR)L"SELECT * FROM __InstanceCreationEvent WITHIN 1 WHERE TargetInstance ISA \'Win32_NTLogEvent\' ", WBEM_FLAG_FORWARD_ONLY | WBEM_FLAG_RETURN_IMMEDIATELY, NULL, &pEnum);
		if (SUCCEEDED(hr)) {
			while (env->CallIntMethod(obj, env->GetMethodID(env->GetObjectClass(obj), "isInterrupted", "()I"), NULL) == 0) {
				CComPtr<IWbemClassObject> pFolder = NULL;
				ULONG retcnt = 0L;
				while ((hr = pEnum->Next(WBEM_INFINITE, 1L, &pFolder, &retcnt)) == WBEM_S_TIMEDOUT);
				if (SUCCEEDED(hr) && (hr != WBEM_S_FALSE)) {
					if (retcnt > 0) {
						_variant_t var_val;
						VariantInit(&var_val);
						hr = pFolder->Get(L"TargetInstance", 0, &var_val, NULL, NULL);
						if (SUCCEEDED(hr)) {
							IUnknown* str = var_val;
							CComPtr<IWbemClassObject> pObj;
							hr = str->QueryInterface(IID_IWbemClassObject, reinterpret_cast<void**>(&pObj));
							if (SUCCEEDED(hr)) {
								_variant_t cn;
								VariantInit(&cn);
								hr = pObj->Get(L"__Class", 0, &cn, NULL, NULL);
								if (SUCCEEDED(hr)) {
									_bstr_t name = _bstr_t(cn);
									if (name == _bstr_t(L"Win32_NTLogEvent")) {
										const char prop[14][16] = { "CategoryString", "ComputerName", "Logfile", "Message", "SourceName", "Type", "User", "TimeGenerated", "TimeWritten", "Category", "EventCode", "EventIdentifier", "RecordNumber", "EventType" };
										_variant_t label;
										for (int i = 0; i < 14; i++) {
											std::string func = "set";
											func.append(prop[i]);
											VariantInit(&label);
											pObj->Get(_bstr_t(prop[i]), 0, &label, NULL, NULL);
											if ((label.vt == VT_NULL) || (label.vt == VT_EMPTY) || (label.vt & VT_ARRAY)) {
												if (i < 9)
													env->CallVoidMethod(obj,
														env->GetMethodID(env->GetObjectClass(obj), func.c_str(), "(Ljava/lang/String;)V"),
														env->NewStringUTF("N/A"));
												else
													env->CallVoidMethod(obj,
														env->GetMethodID(env->GetObjectClass(obj), func.c_str(), "(I)V"),
														NULL);
											}
											else {
												if (i < 9)
													env->CallVoidMethod(obj,
														env->GetMethodID(env->GetObjectClass(obj), func.c_str(), "(Ljava/lang/String;)V"),
														env->NewStringUTF(((std::string)_bstr_t(label)).c_str()));
												else if (i < 13)
													env->CallVoidMethod(obj,
														env->GetMethodID(env->GetObjectClass(obj), func.c_str(), "(I)V"),
														(jint)label.uintVal);
												else
													env->CallVoidMethod(obj,
														env->GetMethodID(env->GetObjectClass(obj), func.c_str(), "(I)V"),
														(jint)std::stoi(((std::string)_bstr_t(label))));
											}
											VariantClear(&label);
										}
										env->CallVoidMethod(obj,
											env->GetMethodID(env->GetObjectClass(obj), "ingest", "()V"),
											cls, obj);
									}
								}
								VariantClear(&cn);
							}
						}
						VariantClear(&var_val);
					}
				}
			}
		}
	}
	CoUninitialize();
}