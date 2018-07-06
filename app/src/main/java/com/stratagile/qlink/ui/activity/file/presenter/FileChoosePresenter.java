package com.stratagile.qlink.ui.activity.file.presenter;
import android.support.annotation.NonNull;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.file.contract.FileChooseContract;
import com.stratagile.qlink.ui.activity.file.FileChooseActivity;
import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.file
 * @Description: presenter of FileChooseActivity
 * @date 2018/05/18 14:15:35
 */
public class FileChoosePresenter implements FileChooseContract.FileInfosContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final FileChooseContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private FileChooseActivity mActivity;

    @Inject
    public FileChoosePresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull FileChooseContract.View view, FileChooseActivity activity) {
        mView = view;
        this.httpAPIWrapper = httpAPIWrapper;
        mCompositeDisposable = new CompositeDisposable();
        this.mActivity = activity;
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
}