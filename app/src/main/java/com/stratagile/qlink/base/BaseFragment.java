package com.stratagile.qlink.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.socks.library.KLog;
import com.stratagile.qlink.view.RxDialogLoading;
import com.stratagile.qlink.R;

/**
 * @ClassName: BaseFragment 
 * @Description: 基础Fragment�?
 * @author wwx
 * @date 2015�?1�?6�?下午1:38:32 
 */
@SuppressLint("ShowToast")
public abstract class BaseFragment extends Fragment {

	/**
	 * 进度条加载
	 */
	protected RxDialogLoading progressDialog;
	
	/**
	 * 根布局
	 */
	protected View rootView;


	private boolean viewCreated = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		//在自己的应用初始Activity中加入如下两行代码
		if (savedInstanceState != null) {
			//取出保存在savedInstanceState中
		}

		setupFragmentComponent();

	}

	public Activity getContainerActivity() {
		return getActivity();
	}
	
	protected abstract void setupFragmentComponent();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		initView(inflater, container);
		initDataFromLocal();
		return rootView;
	}

	/**
	 * 加载界面控件
	 */
	protected void initView(LayoutInflater inflater, ViewGroup container) {

	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		viewCreated = true;
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		progressDialog = new RxDialogLoading(activity);
		progressDialog.setmDialogColor(getResources().getColor(R.color.mainColor));
		progressDialog.setDialogText(getResources().getString(R.string.apploading));
//		progressDialog.setMyCancelListener(this);
	}

	/**
	 * 获取网络数据
	 * 
	 * @return
	 */
	protected abstract void initDataFromNet();

	/**
	 * 获取本地数据
	 * 
	 * @return
	 */
	protected abstract void initDataFromLocal();


	@Override
	public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
		try {
			//取出保存在savedInstanceState的值
			if (savedInstanceState != null) {
			}
			super.onViewStateRestored(savedInstanceState);
		} catch (Exception e) {
		}
	}

	/** 是否已经加载过初始数据, 在页面初始数据加载成功之后请置为false */
	protected boolean isLoaded = false;

	protected boolean isVisibleToUser = false;

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		KLog.i("isVisibleToUser：" + isVisibleToUser);
		KLog.i("isLoaded：" + isLoaded);
		KLog.i("viewCreated：" + viewCreated);
		this.isVisibleToUser = isVisibleToUser;
		if(this != null ){
			super.setUserVisibleHint(isVisibleToUser);
			if (isVisibleToUser) {
				if(!isLoaded && viewCreated){
					initDataFromNet();
					isLoaded = true;
				}
			}
		}
	}
}
