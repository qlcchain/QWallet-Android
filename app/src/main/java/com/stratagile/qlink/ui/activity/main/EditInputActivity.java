package com.stratagile.qlink.ui.activity.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.ui.activity.main.component.DaggerEditInputComponent;
import com.stratagile.qlink.ui.activity.main.contract.EditInputContract;
import com.stratagile.qlink.ui.activity.main.module.EditInputModule;
import com.stratagile.qlink.ui.activity.main.presenter.EditInputPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: $description
 * @date 2019/04/25 14:13:26
 */

public class EditInputActivity extends BaseActivity implements EditInputContract.View {

    @Inject
    EditInputPresenter mPresenter;
    @BindView(R.id.etContent)
    EditText etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        LayoutInflaterCompat.setFactory(LayoutInflater.from(this), new LayoutInflaterFactory() {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                if (name.equals("com.android.internal.view.menu.IconMenuItemView") || name.equals("com.android.internal.view.menu.ActionMenuItemView") || name.equals("android.support.v7.view.menu.ActionMenuItemView")) {
                    try {
                        View view = getLayoutInflater().createView(name, null, attrs);
                        if (view instanceof TextView) {
                            ((TextView) view).setTextColor(getResources().getColor(R.color.mainColor));
                            ((TextView) view).setAllCaps(false);
                        }
                        return view;
                    } catch (InflateException e) {
                        e.printStackTrace();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }

                }
                return null;
            }
        });
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_edit_input);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        setTitle(getIntent().getStringExtra("title"));
        etContent.setHint(getIntent().getStringExtra("hint"));
        etContent.setText(getIntent().getStringExtra("content"));
        etContent.setSelection(getIntent().getStringExtra("content").length());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_input, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.editInput) {
            setBack();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setBack() {
        if (etContent.getText().toString().trim().equals(getIntent().getStringExtra("content")) || "".equals(etContent.getText().toString().trim())) {

        } else {
            Intent intent = new Intent();
            intent.putExtra("result", etContent.getText().toString().trim());
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    @Override
    protected void setupActivityComponent() {
        DaggerEditInputComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .editInputModule(new EditInputModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(EditInputContract.EditInputContractPresenter presenter) {
        mPresenter = (EditInputPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

}