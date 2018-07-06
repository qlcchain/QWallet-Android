/**
 * Copyright (C) 2012 ToolkitForAndroid Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratagile.qlink.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Locale;

/**
 * @ClassName: Network 
 * @Description: 网络监测
 * @author wwx
 * @date 2015年7月24日 下午5:25:13 
 *
 */
@SuppressLint("DefaultLocale")
public class Network {

	/**
	 * 网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isAvailable(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		return !(networkinfo == null || !networkinfo.isAvailable());
	}


	/**
	 * 判断网络是否可用
	 * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
	 *
	 * @param context 上下文
	 * @return {@code true}: 可用<br>{@code false}: 不可用
	 */
	public static boolean isAvailableByPing(Context context) {
		ShellUtils.CommandResult result = ShellUtils.execCmd("ping -c 1 -w 1 123.125.114.144", false);
		boolean ret = result.result == 0;
		if (result.errorMsg != null) {
			Log.d("geek", "isAvailableByPing errorMsg"+result.errorMsg);
		}
		if (result.successMsg != null) {
			Log.d("geek","isAvailableByPing successMsg"+ result.successMsg);
		}
		return ret;
	}

	/**
	 * 获取当前的网络状态 -1： 没有网络 1： WIFI网络 2： wap网络 3： net网络
	 * 
	 * @param context
	 * @return
	 */
	public static int getAPNType(Context context) {
		int netType = -1;
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			Log.e("geek","networkInfo.getExtraInfo() is "+ networkInfo.getExtraInfo());
			if (networkInfo.getExtraInfo().toLowerCase(Locale.ENGLISH).equals("cmnet")) {
				netType = 3;
			} else {
				netType = 2;
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = 1;
		}
		return netType;
	}

	//-1： 没有网络 1： WIFI网络 2： wap网络 3： net网络
	public static boolean customIsAvailable(Context context){
		int type = getAPNType(context);
		Log.d("geek","type = "+ type);
		if(type == 1){
			if(isWIFIActivate(context)){
				Log.d("geek","isWIFIActivate = true");
				return  true;
			}else {
				Log.d("geek","isWIFIActivate = false");
				return  false;
			}
		}else if(type == 2 || type == 3){
			if(isMobileConnected(context)){
				Log.d("geek","isMobileConnected = true");
				return  true;
			}else {
				Log.d("geek","isMobileConnected = false");
				return  false;
			}
		}else{
			Log.d("geek","ww = false");
			return  false;
		}
	}

	/**
	 * Wifi是否可用
	 * 
	 * @return
	 */
	public static boolean isWIFIActivate(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 移动网是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 获取连接类型
	 * 
	 * @param context
	 * @return
	 */
	public static int getConnectedType(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}

	/**
	 * 获取设备IP地址
	 * 
	 * @return 设备IP地址
	 */
	public static String getDeviceIP() {
		String IP = null;
		StringBuilder IPStringBuilder = new StringBuilder();
		try {
			Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface
					.getNetworkInterfaces();
			while (networkInterfaceEnumeration.hasMoreElements()) {
				NetworkInterface networkInterface = networkInterfaceEnumeration
						.nextElement();
				Enumeration<InetAddress> inetAddressEnumeration = networkInterface
						.getInetAddresses();
				while (inetAddressEnumeration.hasMoreElements()) {
					InetAddress inetAddress = inetAddressEnumeration
							.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& !inetAddress.isLinkLocalAddress()
							&& inetAddress.isSiteLocalAddress()) {
						IPStringBuilder.append(inetAddress.getHostAddress()
								.toString() + "\n");
					}
				}
			}
		} catch (SocketException ex) {
			IP = null;
		}
		IP = IPStringBuilder.toString();
		return IP;
	}

	
	/**
	 * 获取wifi状态下 Ip地址
	 * 
	 * @param context
	 * @return
	 */
//	public static String getWifiIp(Context context) {
//		// 获取wifi服务
//		WifiManager wifiManager = (WifiManager) context
//				.getSystemService(Context.WIFI_SERVICE);
//		// 判断wifi是否开启
//		if (!wifiManager.isWifiEnabled()) {
//			wifiManager.setWifiEnabled(true);
//		}
//		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//		int ipAddress = wifiInfo.getIpAddress();
//		String ip = StringUitl.intToIp(ipAddress);
//		return ip;
//	}
	
	/**
	 * 修改WIFI状态
	 * 
	 * @param c
	 * @param status
	 */
	public static void changeWIFIStatus(Context c, boolean status) {
//		((WifiManager) c.getSystemService(Context.WIFI_SERVICE))
//				.setWifiEnabled(status);
	}

	/**
	 * 提示打开网络设置
	 */
	public static void showNetworkSetting(final Context context) {
		Builder builder = new Builder(context);
		builder.setTitle("网络设置提示")
				.setMessage("网络连接不可用,是否进行设置?")
				.setPositiveButton("设置", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						context.startActivity(new Intent(
								android.provider.Settings.ACTION_SETTINGS));
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}

	// cmwap和cmnet是GPRS网络的两种接入方式。其实上吧，是wap方式和net方式。cm是chinamobile。
	public static final int NETTYPE_WIFI = 0x01;
	public static final int NETTYPE_CMWAP = 0x02;
	public static final int NETTYPE_CMNET = 0x03;
	private static final int NET_3G_CU = 4;
	private static final int NET_2G_CM_OR_CU = 3;
	private static final int NET_2G_CT = 2;
	private static final int NET_3G_CT = 1;
	private static final int NET_UNKNOWN = 0;

	/**
	 * 获取当前网络类型
	 * 
	 * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
	 */
	public static int getNetworkType(Context c) {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) c
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();

		// int nSubType = networkInfo.getSubtype() ; //netSubtype ==
		// TelephonyManager.NETWORK_TYPE_UMTS
		// //新增
		// StringBuffer sb = new StringBuffer();
		// sb.append("nSubType="+ nSubType);
		// if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS || nSubType ==
		// TelephonyManager.NETWORK_TYPE_HSDPA) {
		// sb.append("联通3g") ;
		// } else if (nSubType == TelephonyManager.NETWORK_TYPE_GPRS || nSubType
		// == TelephonyManager.NETWORK_TYPE_EDGE) {
		// sb.append("移动或者联通2g") ;
		// }else if(nSubType==TelephonyManager.NETWORK_TYPE_CDMA){
		// sb.append("电信2g") ;
		// }else if(nSubType==TelephonyManager.NETWORK_TYPE_EVDO_0
		// ||nSubType==TelephonyManager.NETWORK_TYPE_EVDO_A){
		// sb.append("电信3g");
		// }else{
		// sb.append("非以上信号") ;
		// }
		// System.out.println(sb);

		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = networkInfo.getExtraInfo();
			if (StringUitl.isNoEmpty(extraInfo)) {
				if (extraInfo.toLowerCase(Locale.ENGLISH).equals("cmnet")) {
					netType = NETTYPE_CMNET;
				} else {
					netType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		return netType;
	}
	
	/**
	 * 获取当前网络类型
	 * 
	 * @return 0：没有网络 其他未响应的网络
	 */
	public static String getNetworkSubTypeStr(Context c) {
		String str = "信号类型未知";

		int subType = getNetworkType(c);
		switch (subType) {
		case NET_UNKNOWN:
			str = "信号类型未知";
			break;
		case NET_3G_CU:
			str = "联通3g";
			break;
		case NET_2G_CM_OR_CU:
			str = "移动或者联通2g";
			break;
		case NET_2G_CT:
			str = "电信2g";
			break;
		case NET_3G_CT:
			str = "电信3g";
			break;
		default:
			break;
		}

		return str;
	}
}
