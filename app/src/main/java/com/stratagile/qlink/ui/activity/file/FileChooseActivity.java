package com.stratagile.qlink.ui.activity.file;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.data.fileInfo.FileInfo;
import com.stratagile.qlink.ui.activity.file.component.DaggerFileChooseComponent;
import com.stratagile.qlink.ui.activity.file.component.DaggerFileInfosComponent;
import com.stratagile.qlink.ui.activity.file.contract.FileChooseContract;
import com.stratagile.qlink.ui.activity.file.module.FileChooseModule;
import com.stratagile.qlink.ui.activity.file.presenter.FileChoosePresenter;
import com.stratagile.qlink.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.julian.common.Preconditions;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.file
 * @Description: $description
 * @date 2018/05/18 14:15:35
 */

public class FileChooseActivity extends BaseActivity implements FileChooseContract.View {

    private static final int OPERATION_NONE = 0;
    private static final int OPERATION_BACK_PRESSED = 1;
    private static final int OPERATION_SELECTED_TAB = 2;
    private static final int OPERATION_CLICK_ITEM = 3;

    private static final int REQUEST_CODE_OPEN_FILE = 10;

    @IntDef(value = {OPERATION_NONE, OPERATION_BACK_PRESSED, OPERATION_SELECTED_TAB,
            OPERATION_CLICK_ITEM})
    @Retention(RetentionPolicy.SOURCE)
    private @interface Operation {
    }

    private ArrayList<FileInfo> mDirectories = new ArrayList<>();
    private FileInfo mSelectedDirectory;

    private FragmentManager mFragmentManager;

    private MyHandler mMyHandler;

    @Inject
    FileChoosePresenter mPresenter;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_file_infos);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.SELECT_PROFILE).toUpperCase());
    }

    @Override
    protected void initData() {

        mMyHandler = new MyHandler(this);
        mFragmentManager = getSupportFragmentManager();
        showDirectory(null, OPERATION_NONE);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showDirectory((FileInfo) tab.getTag(), OPERATION_SELECTED_TAB);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void setupActivityComponent() {
        DaggerFileChooseComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .fileChooseModule(new FileChooseModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(FileChooseContract.FileInfosContractPresenter presenter) {
        mPresenter = (FileChoosePresenter) presenter;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFileInfo(FileInfo fileInfo) {
        if (fileInfo.isFile()) {
            Intent intent = new Intent();
            intent.putExtra("path", fileInfo.getAbsolutePath());
            setResult(RESULT_OK, intent);
            finish();
        } else {
            if (fileInfo == null) {

            } else {
                showDirectory(fileInfo, OPERATION_CLICK_ITEM);
            }
        }
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    private void showDirectory(FileInfo fileInfo, @Operation int operation) {
        KLog.d( "showDirectory: " + operation);
        switch (operation) {
            case OPERATION_NONE:
                showDirectoryWithNone();
                break;
            case OPERATION_BACK_PRESSED:
                showDirectoryWithBackPressed();
                break;
            case OPERATION_SELECTED_TAB:
                showDirectoryWithSelectedTab(fileInfo);
                break;
            case OPERATION_CLICK_ITEM:
                showDirectoryWithClickItem(fileInfo);
                break;
            default:
                throw new IllegalArgumentException(operation + " is invalid");
        }
    }

    private void showDirectoryWithNone() {
        KLog.d("showDirectoryWithNone11111: " + (mSelectedDirectory == null ? "null" : mSelectedDirectory.getName()) + ", size: " + mDirectories.size());
        if (mSelectedDirectory == null) {
            mSelectedDirectory = new FileInfo(Environment.getExternalStorageDirectory());
            mDirectories.add(mSelectedDirectory);
        }

        mTabLayout.removeAllTabs();
        final int size = mDirectories.size();
        for (int i = 0; i < size; i++) {
            FileInfo directory = mDirectories.get(i);
            if (i == size - 1) {
                mTabLayout.addTab(mTabLayout.newTab().setCustomView(R.layout.directory_tab_view_without_arrow)
                        .setText(directory.getName()).setTag(directory), false);
            } else {
                mTabLayout.addTab(mTabLayout.newTab().setCustomView(R.layout.directory_tab_view)
                        .setText(directory.getName()).setTag(directory), false);
            }
        }

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        for (FileInfo directory : mDirectories) {
            Fragment fragment = mFragmentManager.findFragmentByTag(directory.getAbsolutePath());
            if (fragment != null && !fragment.isDetached()) {
                if (!mSelectedDirectory.equals(directory)) {
                    ft.detach(fragment);
                }
            }
        }
        Fragment selected = mFragmentManager.findFragmentByTag(mSelectedDirectory.getAbsolutePath());
        if (selected == null) {
            selected = FileInfosFragment.newInstance(mSelectedDirectory.getAbsolutePath());
            ft.add(R.id.contentFrame, selected, mSelectedDirectory.getAbsolutePath());
        } else {
            ft.attach(selected);
        }
        ft.commit();

        Message msg = mMyHandler.obtainMessage();
        msg.arg1 = mDirectories.indexOf(mSelectedDirectory);
        KLog.d("showDirectoryWithNone22222: " + (mSelectedDirectory == null ? "null" : mSelectedDirectory.getName()) + ", size: " + mDirectories.size());
        mMyHandler.sendMessageDelayed(msg, 100L);
    }

    private void showDirectoryWithBackPressed() {
        Preconditions.checkNotNull(mSelectedDirectory, "mSelectedDirectory == null");
        Preconditions.checkArgument(mDirectories.contains(mSelectedDirectory),
                "mDirectories not contain:" + mSelectedDirectory.getAbsolutePath());
        final int selectedPosition = mDirectories.indexOf(mSelectedDirectory);
        if (selectedPosition == 0) {
            finish();
        } else {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            Fragment f = mFragmentManager.findFragmentByTag(mSelectedDirectory.getAbsolutePath());
            if (f != null && !f.isDetached()) {
                ft.detach(f);
            }
            int previousPosition = selectedPosition - 1;
            FileInfo previous = mSelectedDirectory = mDirectories.get(previousPosition);
            f = mFragmentManager.findFragmentByTag(previous.getAbsolutePath());
            if (f == null) {
                f = FileInfosFragment.newInstance(previous.getAbsolutePath());
                ft.add(R.id.contentFrame, f, previous.getAbsolutePath());
            } else {
                ft.attach(f);
            }
            ft.commit();

            Message msg = mMyHandler.obtainMessage();
            msg.arg1 = previousPosition;
            mMyHandler.sendMessageDelayed(msg, 100L);
        }
    }

    private void showDirectoryWithSelectedTab(FileInfo fileInfo) {
        Preconditions.checkNotNull(fileInfo, "fileInfo == null");
        Preconditions.checkNotNull(mSelectedDirectory, "mSelectedDirectory == null");
        KLog.i( "showDirectoryWithSelectedTab: " + fileInfo.getName());
        if (mSelectedDirectory != fileInfo) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            Fragment f = mFragmentManager.findFragmentByTag(mSelectedDirectory.getAbsolutePath());
            if (f != null && !f.isDetached()) {
                ft.detach(f);
            }

            mSelectedDirectory = fileInfo;

            f = mFragmentManager.findFragmentByTag(fileInfo.getAbsolutePath());
            if (f == null) {
                f = FileInfosFragment.newInstance(fileInfo.getAbsolutePath());
                ft.add(R.id.contentFrame, f, fileInfo.getAbsolutePath());
            } else {
                ft.attach(f);
            }
            ft.commit();
        }
    }

    private void showDirectoryWithClickItem(@NonNull FileInfo fileInfo) {
        Preconditions.checkNotNull(fileInfo, "fileInfo == null");
        // 如果用户当前选中的文件夹已经添加到 mDirectories 中
        String selectedPath = fileInfo.getAbsolutePath();
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Iterator<FileInfo> iterator = mDirectories.iterator();
        while (iterator.hasNext()) {
            FileInfo child = iterator.next();
            String childPath = child.getAbsolutePath();
            if (!selectedPath.contains(childPath)) {
                Fragment f = mFragmentManager.findFragmentByTag(childPath);
                if (f != null) {
                    ft.remove(f);
                }
                iterator.remove();
                int tabPosition = getPositionForTab(child);
                if (tabPosition != -1) {
                    mTabLayout.removeTabAt(tabPosition);
                }
            }
        }
        if (mDirectories.contains(mSelectedDirectory)) {
            Fragment f = mFragmentManager.findFragmentByTag(mSelectedDirectory.getAbsolutePath());
            if (f != null && !f.isDetached()) {
                ft.detach(f);
            }
        } else {
            Fragment f = mFragmentManager.findFragmentByTag(mSelectedDirectory.getAbsolutePath());
            if (f != null) {
                ft.remove(f);
            }
        }

        if (!mDirectories.contains(fileInfo)) {
            int insertedPosition = mDirectories.size();
            if (insertedPosition > 0) {
                int lastPosition = insertedPosition - 1;
                TabLayout.Tab lastTab = mTabLayout.getTabAt(lastPosition);
                if (lastTab == null) {
                    throw new NullPointerException("lastTab == null");
                }
                lastTab.setCustomView(null);
                lastTab.setCustomView(R.layout.directory_tab_view);
            }
            mDirectories.add(insertedPosition, fileInfo);
            mTabLayout.addTab(mTabLayout.newTab().setCustomView(R.layout.directory_tab_view_without_arrow)
                    .setText(fileInfo.getName()).setTag(fileInfo), insertedPosition);
        } else {
            int position = mDirectories.indexOf(fileInfo);
            if (position != mDirectories.size() - 1) {
                throw new IllegalStateException(position + " is not last one");
            }
            TabLayout.Tab lastTab = mTabLayout.getTabAt(position);
            if (lastTab == null) {
                throw new NullPointerException("lastTab == null");
            }
            lastTab.setCustomView(null);
            lastTab.setCustomView(R.layout.directory_tab_view_without_arrow);
        }

        Fragment f = mFragmentManager.findFragmentByTag(fileInfo.getAbsolutePath());
        if (f == null) {
            f = FileInfosFragment.newInstance(fileInfo.getAbsolutePath());
            ft.add(R.id.contentFrame, f, fileInfo.getAbsolutePath());
        } else {
            ft.attach(f);
        }
        mSelectedDirectory = fileInfo;
        ft.commit();

        Message msg = mMyHandler.obtainMessage();
        msg.arg1 = mDirectories.indexOf(fileInfo);
        mMyHandler.sendMessageDelayed(msg, 100L);
    }

    private int getPositionForTab(FileInfo directory) {
        int tabCount = mTabLayout.getTabCount();
        for (int i = 0; i < tabCount; i++) {
            FileInfo tag = getTabTag(mTabLayout.getTabAt(i));
            if (tag.equals(directory)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onBackPressed() {
        showDirectory(null, OPERATION_BACK_PRESSED);
    }


    @NonNull
    private FileInfo getTabTag(TabLayout.Tab tab) {
        FileInfo tabTag = (FileInfo) tab.getTag();
        if (tabTag == null) {
            throw new NullPointerException("tabTag == null");
        }
        return tabTag;
    }

    private void selectTab(int position) {
        if (position < 0) {
            return;
        }
        TabLayout.Tab tab = position < mTabLayout.getTabCount()
                ? mTabLayout.getTabAt(position)
                : null;
        if (tab == null) {
            return;
        }
        try {
            Method method = TabLayout.class.getDeclaredMethod("selectTab", TabLayout.Tab.class);
            method.setAccessible(true);
            method.invoke(mTabLayout, tab);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class MyHandler extends Handler {

        private WeakReference<FileChooseActivity> mReference;

        private MyHandler(FileChooseActivity fileInfosActivity) {
            mReference = new WeakReference<>(fileInfosActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            FileChooseActivity fileInfosActivity = mReference.get();
            if (fileInfosActivity != null) {
                int selectedTabPosition = msg.arg1;
                fileInfosActivity.selectTab(selectedTabPosition);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}