????status???嶨???TOX_CONNECTION??

typedef enum TOX_CONNECTION {

    /**
     * There is no connection. This instance, or the friend the state change is
     * about, is now offline.
     */
    TOX_CONNECTION_NONE,

    /**
     * A TCP connection has been established. For the own instance, this means it
     * is connected through a TCP relay, only. For a friend, this means that the
     * connection to that particular friend goes through a TCP relay.
     */
    TOX_CONNECTION_TCP,

    /**
     * A UDP connection has been established. For the own instance, this means it
     * is able to send UDP packets to DHT nodes, but may still be connected to
     * a TCP relay. For a friend, this means that the connection to that
     * particular friend was built using direct UDP packets.
     */
    TOX_CONNECTION_UDP,

} TOX_CONNECTION;


/* Call_SelfStatusChange_To_Java
** when self status change ,call java func 
** 0 success call java func
** -1 can't find  class
** -2 can't find  mid_construct
** -3 can't find mid_instance ID
** -4 can't Create an instance 
*/


int Call_SelfStatusChange_To_Java(uint32_t status)
{   jclass clazz = NULL;  
    jobject jobj = NULL;  
    jmethodID mid_construct = NULL;  
    jmethodID mid_instance = NULL;  
    JNIEnv *env = Env;
    // 1????classpath·????????ClassMethod???????????????Class????  
    clazz = (*env)->FindClass(env, "com/stratagile/qlink/qlinkcom");  
    if (clazz == NULL) {  
        printf("?????'com/stratagile/qlink/qlinkcom'?????");  
        return -1;  
    }  
    // 2????????????????ID  
    mid_construct = (*env)->GetMethodID(env,clazz, "<init>","()V");  
    if (mid_construct == NULL) {  
        printf("??????????????");  
        return -2;  
    }  
    // 3???????????????ID  
    mid_instance = (*env)->GetMethodID(env, clazz, "CallSelfChange", "(I)V");  
    if (mid_instance == NULL) {  
        return -3;  
    }  

    // 4??????????????  
    jobj = (*env)->NewObject(env,clazz,mid_construct);  
    if (jobj == NULL) {  
        printf("??com.stratagile.qlink.qlinkcom?????????CallSelfChange????");  
        return -4;  
    }  

    // 5?????????????????  
    (*env)->CallVoidMethod(env,jobj,mid_instance,status);  

    // ??????????  
    (*env)->DeleteLocalRef(env,clazz);  
    (*env)->DeleteLocalRef(env,jobj);  
	return 0;
}

/* Call_FriendStatusChange_To_Java
** when friend status change ,call java func 
** 0 success call java func
** -1,-2 parameter not valid
** -3 can't find  class
** -4 can't find  mid_construct
** -5 can't find mid_instance ID
** -6 can't Create an instance 
*/
int Call_FriendStatusChange_To_Java(uint32_t friendnumber,uint32_t status)
{
		if(qlinkNode != NULL)
		{ 		
			if (friend_not_valid(qlinkNode, friendnumber))
			{
        	return -2;
    	}   
			jclass clazz = NULL;  
    	jobject jobj = NULL;  
    	jmethodID mid_construct = NULL;  
    	jmethodID mid_instance = NULL;  
   	 	JNIEnv *env = Env;
    	// 1????classpath·????????ClassMethod???????????????Class????  
    	clazz = (*env)->FindClass(env, "com/stratagile/qlink/qlinkcom");  
    	if (clazz == NULL) {  
        	printf("?????'com/stratagile/qlink/qlinkcom'?????");  
        	return -3;  
    	}  

    	// 2????????????????ID  
    	mid_construct = (*env)->GetMethodID(env,clazz, "<init>","()V");  
    	if (mid_construct == NULL) {  
        	printf("??????????????");  
        	return -4;  
    	}  

    	// 3???????????????ID  
   		mid_instance = (*env)->GetMethodID(env, clazz, "CallFriendChange", "(II)V");  
    	if (mid_instance == NULL) {  

        	return -5;  
    	}  

    	// 4??????????????  
    	jobj = (*env)->NewObject(env,clazz,mid_construct);  
   		if (jobj == NULL) {  
        	printf("??com.stratagile.qlink.qlinkcom?????????CallFriendChange????");  
        	return -6;  
    	}  

    	// 5?????????????????  
    	(*env)->CallVoidMethod(env,jobj,mid_instance,friendnumber,status);  

    	// ??????????  
    	(*env)->DeleteLocalRef(env,clazz);  
    	(*env)->DeleteLocalRef(env,jobj);  

		return 0;
	}
	else
		return -1;
}

/* Call_Message_Process_Func_From_Java
** when friend status change ,call java func 
** 0 success call java func
** -1,-2,-3 parameter not valid
** -4 can't find  class
** -5 can't find  mid_construct
** -6 can't find mid_instance ID
** -7 can't Create an instance 
*/
int Call_Message_Process_Func_From_Java(char *message, int friendnum)
{
	if(qlinkNode != NULL)
	{ 		
			if(message == NULL)
					return -2;
			if (friend_not_valid(qlinkNode, friendnum))
			{
        	return -3;
    	}      
			jclass clazz = NULL;  
   	 	jobject jobj = NULL;  
    	jmethodID mid_construct = NULL;  
    	jmethodID mid_instance = NULL; 
			jstring str_arg = NULL; 
    	JNIEnv *env = Env;
    	// 1????classpath·????????ClassMethod???????????????Class????  
    	clazz = (*env)->FindClass(env, "com/stratagile/qlink/qlinkcom");  
    	if (clazz == NULL) {  
        	printf("?????'com/stratagile/qlink/qlinkcom'?????");  
        	return -4;  
    	}  

    	// 2????????????????ID  
    	mid_construct = (*env)->GetMethodID(env,clazz, "<init>","()V");  
    	if (mid_construct == NULL) {  
        	printf("??????????????");  
        	return -5;  
    	}  

    	// 3???????????????ID  
    	mid_instance = (*env)->GetMethodID(env, clazz, "CallFriendMessageProcess", "(Ljava/lang/String;I)V");  
    	if (mid_instance == NULL) {  

        	return -6;  
    	}  

    	// 4??????????????  
    	jobj = (*env)->NewObject(env,clazz,mid_construct);  
    	if (jobj == NULL) {  
        	printf("??com.stratagile.qlink.qlinkcom?????????CallFriendMessageProcess????");  
        	return -7;  
    	}  

    	// 5?????????????????  
    	str_arg = (*env)->NewStringUTF(env,message); 
    	(*env)->CallVoidMethod(env,jobj,mid_instance,str_arg,friendnum);  

    	// ??????????  
    	(*env)->DeleteLocalRef(env,clazz);  
    	(*env)->DeleteLocalRef(env,jobj); 
			(*env)->DeleteLocalRef(env,str_arg); 

			return 0;
	}
	else
		return -1;
}

/* Java_com_stratagile_qlink_qlinkcom_SendRequest
** 0 SendRequest ok
** -1 qlinkNode not valid
** -2 message not valid
** -3 friend_not_valid
*/
JNIEXPORT jint JNICALL Java_com_stratagile_qlink_qlinkcom_SendRequest
(JNIEnv *env,jobject obj,int friendNum,jstring message)
{
	if(qlinkNode != NULL)
	{ 		
		char* message_c = Jstring2CStr(env,message);	
		if(message_c == NULL)
			return -2;
		if (friend_not_valid(qlinkNode, friendNum))
		{
        	return -3;
    }
		tox_friend_send_message(qlinkNode, friendNum, TOX_MESSAGE_TYPE_NORMAL, message_c, strlen(message_c), NULL);
		free(message_c);
		return 0;        
  }
		
	else
		return -1;
}




关于Qlink App下Java和p2p C库之间的配合，随着代码的对接和App功能的增加和完善，我觉得有必要现在重新整理一下，让模块之间的功能更加清晰和易于扩展。
  首先从App功能需求上，有以下信息
    1，wifi sharing
      1）WiFi basic info
         a) SSID
         b) MAC
         c) price
         d) Time of the price
         e) num of allowed devices
         f)  num of connected devices
         g) allowed sharing time period
      2) WIFI Key info
          a) wifi pasword

    2, VPN sharing
       1) VPN basic info
         a) country of the VPN or a name
         b) bandwidth
        c) price
         d) Time of the price
         e) num of allowed devices
         f)  num of connected devices
         g) allowed sharing time period
       2) VPN key info
         a) VPN configuration and certificate file

3： 用户头像

其中
  Basic info内的红色字体部分是注册在区块链的
  Basic info内的蓝色字体部分是需要app记录在本地，通过p2p网络完成节点之间的相互传输。
  Key info部分是这个资产的关键信息，需要app加密后记录在本地, 在用户点击之后才通过p2p传输， 并且目标app不能记录这些信息
  用户头像是basic info 一种，传递方式类似蓝色字体，只是每个app只有一个，这一点不同于资产。

  其次在app逻辑上，app从Blockchain获取到当前可用的资产的p2pID之后，app需要自动完成从p2p节点下载basic info，显示，并等待用户发起对key info的获取。在现在已经完成的wifi分享的功能中， Basic info中wifi ssid, mac, 以及key info的password是通过C库的p2p功能模块记录的， 密码传递也是调用特定接口，这个明显不是好的做法， 从模块话的角度，需要改变的几件事是：
1，用户信息的存储通过app java，不再通过p2p功能模块实现。
2，P2P功能模块回归到纯粹的P2P网络功能， 提供通用的接口给app Java, 比如MessageSend, send的内容可以basic info，也可以是用户头像或者密码等，messageReceived的回调Java函数，及时处理p2p网络过来的信息。
3，节点之间的通信协议和回复逻辑在Java部分实现， p2p模块只作为网络通道。

BR
Zhijie Zhao

按照志杰邮件的思路，p2p模块增加了几个接口：
/*当friend状态变化时，p2p底层回调java中定义的方法通知app UI，从而达到app UI实时刷新friend状态*/
int Call_FriendStatusChange_To_Java(uint32_t friendnumber,uint32_t status);
        /*当self状态变化时，p2p底层回调java中定义的方法通知app UI，从而达到app UI实时刷新self状态*/
        int Call_SelfStatusChange_To_Java(uint32_t status);
/*当收到friend信息，回调java方法进行处理，p2p模块只作为网络通道，不做逻辑处理*/
int Call_Message_Process_Func_From_Java(char *message, int friendnum);

/*app统一调用以下函数接口进行数据传输(头像除外)*/
JNIEXPORT jint JNICALL Java_com_stratagile_qlink_qlinkcom_SendRequest(JNIEnv *, jobject, jint, jstring);

//app 消息传递的json格式定义(后续如有变化再做修改)：

//wifi basic info request   WiFi基本信息的请求
{"Type":"wifibasicinfoReq","Data": "{ "SSID": "YYM-5", "MAC":"00:0c:29:86:d9:94"}"}

//wifi sharer update this wifi basic info to friends   WiFi基本信息的返回
{"Type":"wifibasicinfoRsp","Data": { "SSID": "YYM-5", "MAC":"00:0c:29:86:d9:94",
"Pricing model": "Tipping mode","Price in QLC": 2.50,"Payment type": "Per Hour",
"Max connections":10,"Current connections":2,"Time limit per device":"02:00:00","Daily total time limit":"12:00:00"}}

//wifi password request  WiFi密码的请求
{"Type":"wifipasswordReq","Data": { "SSID": "YYM-5", "MAC":"00:0c:29:86:d9:94"}}

//wifi password response  WiFi密码的返回
{"Type":"wifipasswordRsp","Data": { "SSID": "YYM-5", "MAC":"00:0c:29:86:d9:94","Password":"123456"}}


几个接口函数的具体实现如附件，@小胡，请按照接口函数的实现去定义java方法，特别是函数名、传的参数及路径，java中要和C中一致，
如有问题，及时沟通！！