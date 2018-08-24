package com.stratagile.qlink.ui.activity.file.presenter;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.data.fileInfo.FileInfo;
import com.stratagile.qlink.data.fileInfo.FileInfosRepository;
import com.stratagile.qlink.ui.activity.file.contract.FileInfosContract;
import com.stratagile.qlink.ui.activity.file.FileInfosFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import rx.functions.Action1;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.file
 * @Description: presenter of FileInfosFragment
 * @date 2018/05/18 16:46:15
 */
public class FileInfosPresenter implements FileInfosContract.FileInfosContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final FileInfosContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private FileInfosFragment mFragment;

    private FileInfosRepository mFileInfosRepository;

    private FileInfo mDirectory;

    @Inject
    public FileInfosPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull FileInfosContract.View view, FileInfosFragment fragment) {
        mView = view;
        this.httpAPIWrapper = httpAPIWrapper;
        mCompositeDisposable = new CompositeDisposable();
        this.mFragment = fragment;
    }
    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
    }

    @Override
    public void loadFileInfos() {
        mFileInfosRepository
                .listFileInfos(mDirectory)
                .map(fileInfos -> {
                    List<FileInfo> retrnList = new ArrayList<>();
                    for (FileInfo fileInfo : fileInfos) {
                        if (!fileInfo.getmFile().isHidden() && !".".equals(fileInfo.getName().charAt(0))) {
                            retrnList.add(fileInfo);
                        }
                    }
                    return retrnList;
                })
                .observeOn(Schedulers.trampoline())
                .subscribe(new Observer<List<FileInfo>>() {
                    Disposable disposable;
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(List<FileInfo> fileInfos) {
                        KLog.i(fileInfos.size() + " ");
                        if (fileInfos.isEmpty()) {
                            mView.showNoFileInfos();
                        } else {
                            if (fileInfos.size() == 1) {
                                if (fileInfos.get(0).getName().equals("")) {
                                    mView.showNoFileInfos();
                                    return;
                                }
                            }
                            Collections.sort(fileInfos);
                            mView.showFileInfos(fileInfos);
                        }
                        disposable.dispose();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showNoFileInfos();
                        disposable.dispose();
                    }

                    @Override
                    public void onComplete() {
                        disposable.dispose();
                    }
                });
//                .subscribe(new Consumer<List<FileInfo>>() {
//                    @Override
//                    public void accept(List<FileInfo> fileInfos) throws Exception {
//                        KLog.i(fileInfos.size() + " ");
//                        if (fileInfos.isEmpty()) {
//                            mView.showNoFileInfos();
//                        } else {
//                            if (fileInfos.size() == 1) {
//                                if (fileInfos.get(0).getName().equals("")) {
//                                    mView.showNoFileInfos();
//                                    return;
//                                }
//                            }
//                            mView.showFileInfos(fileInfos);
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        //onError
//                        mView.showNoFileInfos();
//                        KLog.i("onError");
//                        throwable.printStackTrace();
//                    }
//                }, new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        //onComplete
//                        KLog.i("onComplete");
//                    }
//                });
//        mCompositeDisposable.add(disposable);
    }

    @Override
    public void init(FileInfosRepository fileInfosRepository, FileInfo fileInfo) {
        mFileInfosRepository = fileInfosRepository;
        mDirectory = fileInfo;
    }
}