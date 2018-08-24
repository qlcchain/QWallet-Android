package com.stratagile.qlink.ui.activity.file;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.data.fileInfo.FileInfo;
import com.stratagile.qlink.data.fileInfo.FileInfosRepository;
import com.stratagile.qlink.ui.activity.file.component.DaggerFileInfosComponent;
import com.stratagile.qlink.ui.activity.file.contract.FileInfosContract;
import com.stratagile.qlink.ui.activity.file.module.FileInfosModule;
import com.stratagile.qlink.ui.activity.file.presenter.FileInfosPresenter;
import com.stratagile.qlink.ui.adapter.file.FileInfosAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.julian.appchooser.BuildConfig;
import io.julian.common.Preconditions;
import io.julian.common.widget.LoadingLayout;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.file
 * @Description: $description
 * @date 2018/05/18 16:46:15
 */

public class FileInfosFragment extends BaseFragment implements FileInfosContract.View {
    private static final String EXTRA_ABSOLUTE_PATH = BuildConfig.APPLICATION_ID + ".extra.ABSOLUTE_PATH";
    @Inject
    FileInfosPresenter mPresenter;

    FileInfosAdapter fileInfosAdapter;
    @BindView(R.id.recycler_view_file_infos)
    RecyclerView recyclerViewFileInfos;
    @BindView(R.id.loadingLayout)
    LoadingLayout loadingLayout;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            return view;
        }
        view = inflater.inflate(R.layout.fragment_file_infos, null);
        ButterKnife.bind(this, view);
        String absolutePath = getArguments().getString(EXTRA_ABSOLUTE_PATH);
        if (TextUtils.isEmpty(absolutePath)) {
            throw new IllegalStateException("Absolute path is null");
        }
        mPresenter.init(new FileInfosRepository(), new FileInfo(absolutePath));
        mPresenter.loadFileInfos();
        fileInfosAdapter = new FileInfosAdapter(new ArrayList<>());
        recyclerViewFileInfos.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewFileInfos.setAdapter(fileInfosAdapter);
        fileInfosAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                EventBus.getDefault().post(adapter.getItem(position));
            }
        });
        KLog.i("onCreateView一次。。");
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KLog.i("onCreate一次。。");
    }

    public static FileInfosFragment newInstance(@NonNull String absolutePath) {
        Preconditions.checkNotNull(absolutePath);
        Bundle args = new Bundle();
        args.putString(EXTRA_ABSOLUTE_PATH, absolutePath);

        FileInfosFragment fragment = new FileInfosFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void setupFragmentComponent() {
        DaggerFileInfosComponent
                .builder()
                .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
                .fileInfosModule(new FileInfosModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(FileInfosContract.FileInfosContractPresenter presenter) {
        mPresenter = (FileInfosPresenter) presenter;
    }

    @Override
    protected void initDataFromLocal() {

    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @Override
    public void showNoFileInfos() {
        if (getView() == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoadingLayout loadingLayout = (LoadingLayout) getView().findViewById(R.id.loadingLayout);
                loadingLayout.setStatus(LoadingLayout.EMPTY);
            }
        });
    }

    @Override
    public void showFileInfos(List<FileInfo> fileInfos) {
        if (getView() == null) {
            return;
        }
        KLog.i("数据获取成功: " + fileInfos.size());
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LoadingLayout loadingLayout = (LoadingLayout) getView().findViewById(R.id.loadingLayout);
                    loadingLayout.setStatus(LoadingLayout.SUCCESS);
                    fileInfosAdapter.setNewData(fileInfos);
                    fileInfosAdapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}