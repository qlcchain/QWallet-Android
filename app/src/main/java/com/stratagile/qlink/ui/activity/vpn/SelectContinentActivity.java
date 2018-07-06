package com.stratagile.qlink.ui.activity.vpn;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.entity.ContinentAndCountry;
import com.stratagile.qlink.ui.activity.vpn.component.DaggerSelectContinentComponent;
import com.stratagile.qlink.ui.activity.vpn.contract.SelectContinentContract;
import com.stratagile.qlink.ui.activity.vpn.module.SelectContinentModule;
import com.stratagile.qlink.ui.activity.vpn.presenter.SelectContinentPresenter;
import com.stratagile.qlink.utils.FileUtil;
import com.stratagile.qlink.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: $description
 * @date 2018/03/12 16:57:42
 * 选择是在哪个洲
 */

public class SelectContinentActivity extends BaseActivity implements SelectContinentContract.View {

    @Inject
    SelectContinentPresenter mPresenter;
    @BindView(R.id.tabLayout)
    TextView tabLayout;
    @BindView(R.id.map_null)
    ImageView mapNull;
    @BindView(R.id.map_asia)
    ImageView mapAsia;
    @BindView(R.id.map_africa)
    ImageView mapAfrica;
    @BindView(R.id.map_australia)
    ImageView mapAustralia;
    @BindView(R.id.map_northamerica)
    ImageView mapNorthamerica;
    @BindView(R.id.map_southamerica)
    ImageView mapSouthamerica;
    @BindView(R.id.map_europe)
    ImageView mapEurope;
    @BindView(R.id.tv_continent)
    TextView tvContinent;
    @BindView(R.id.bt_cancel)
    Button btCancel;
    @BindView(R.id.bt_continue)
    Button btContinue;
    private Bitmap mapNullBitmap;
    private Bitmap mapAsiaBitmap;
    private Bitmap mapAustraliaBitmap;
    private Bitmap mapNorthamericaBitmap;
    private Bitmap mapSouthamericaBitmap;
    private Bitmap mapEuropeBitmap;
    private Bitmap mapAfricaBitmap;

    private int selectedColor = -1400832;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_select_continent);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.VPN);
    }

    @Override
    protected void initData() {
        BitmapDrawable bd = (BitmapDrawable) mapNull.getDrawable();
        mapNullBitmap = bd.getBitmap();

        bd = (BitmapDrawable) mapAsia.getDrawable();
        mapAsiaBitmap = bd.getBitmap();

        bd = (BitmapDrawable) mapAfrica.getDrawable();
        mapAfricaBitmap = bd.getBitmap();

        bd = (BitmapDrawable) mapAustralia.getDrawable();
        mapAustraliaBitmap = bd.getBitmap();

        bd = (BitmapDrawable) mapEurope.getDrawable();
        mapEuropeBitmap = bd.getBitmap();

        bd = (BitmapDrawable) mapNorthamerica.getDrawable();
        mapNorthamericaBitmap = bd.getBitmap();

        bd = (BitmapDrawable) mapSouthamerica.getDrawable();
        mapSouthamericaBitmap = bd.getBitmap();

        mapNull.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    KLog.i(mapNullBitmap.getHeight());
                    KLog.i(mapNullBitmap.getWidth());
                    KLog.i("getX: " + event.getX());
                    KLog.i("getY: " + event.getY());
                    int bitmapX = (int) (event.getX() / mapNull.getWidth() * mapNullBitmap.getWidth());
                    int bitmapY = (int) (event.getY() / mapNull.getHeight() * mapNullBitmap.getHeight());
                    KLog.i(mapAsiaBitmap.getPixel(bitmapX, bitmapY));
                    KLog.i("selectedColor==" + selectedColor);
                    if (mapAsiaBitmap.getPixel(bitmapX, bitmapY) == selectedColor) {
                        setVisible(R.id.map_asia);
                        return true;
                    }
                    if (mapAfricaBitmap.getPixel(bitmapX, bitmapY) == selectedColor) {
                        setVisible(R.id.map_africa);
                        return true;
                    }
                    if (mapAustraliaBitmap.getPixel(bitmapX, bitmapY) == selectedColor) {
                        KLog.i(mapAustraliaBitmap.getWidth());
                        KLog.i(mapAustraliaBitmap.getHeight());
                        setVisible(R.id.map_australia);
                        return true;
                    }
                    if (mapEuropeBitmap.getPixel(bitmapX, bitmapY) == selectedColor) {
                        setVisible(R.id.map_europe);
                        return true;
                    }
                    if (mapNorthamericaBitmap.getPixel(bitmapX, bitmapY) == selectedColor) {
                        setVisible(R.id.map_northamerica);
                        return true;
                    }
                    if (mapSouthamericaBitmap.getPixel(bitmapX, bitmapY) == selectedColor) {
                        setVisible(R.id.map_southamerica);
                        return true;
                    }
                }
                return true;
            }
        });
        Gson gson = new Gson();
        ContinentAndCountry continentAndCountry = gson.fromJson(FileUtil.getJson(this, "ContinentAndCountryBean.json"), ContinentAndCountry.class);
        String userCountry = getIntent().getStringExtra("country").toLowerCase(Locale.ENGLISH);
        for (int i = 0; i < continentAndCountry.getContinent().size(); i++) {
            for (int j = 0; j < continentAndCountry.getContinent().get(i).getCountry().size(); j++) {
                if (continentAndCountry.getContinent().get(i).getCountry().get(j).getName().toLowerCase(Locale.ENGLISH).equals(userCountry)) {
                    if (continentAndCountry.getContinent().get(i).getContinent().equals("asia")) {
                        setVisible(R.id.map_asia);
                    }
                    if (continentAndCountry.getContinent().get(i).getContinent().equals("africa")) {
                        setVisible(R.id.map_africa);
                    }
                    if (continentAndCountry.getContinent().get(i).getContinent().equals("oceania")) {
                        setVisible(R.id.map_australia);
                    }
                    if (continentAndCountry.getContinent().get(i).getContinent().equals("europe")) {
                        setVisible(R.id.map_europe);
                    }
                    if (continentAndCountry.getContinent().get(i).getContinent().equals("northamerica")) {
                        setVisible(R.id.map_northamerica);
                    }
                    if (continentAndCountry.getContinent().get(i).getContinent().equals("southamerica")) {
                        setVisible(R.id.map_southamerica);
                    }
                    return;
                }
            }
        }
    }

    private void setVisible(int id) {
        ArrayList<View> ids = new ArrayList<>();
        ids.add(mapAsia);
        ids.add(mapAfrica);
        ids.add(mapAustralia);
        ids.add(mapNorthamerica);
        ids.add(mapSouthamerica);
        ids.add(mapEurope);
        switch (id) {
            case R.id.map_asia:
                tvContinent.setText(R.string.ASIA);
                break;
            case R.id.map_africa:
                tvContinent.setText(R.string.AFRICA);
                break;
            case R.id.map_australia:
                tvContinent.setText(R.string.OCEANIA);
                break;
            case R.id.map_europe:
                tvContinent.setText(R.string.EUROPE);
                break;
            case R.id.map_northamerica:
                tvContinent.setText(R.string.NORTHAMERICA);
                break;
            case R.id.map_southamerica:
                tvContinent.setText(R.string.SOUTHAMERICA);
                break;
            default:
                break;
        }
        for (int i = 0; i < ids.size(); i++) {
            if (ids.get(i).getId() == id) {
                ids.get(i).setVisibility(View.VISIBLE);
            } else {
                ids.get(i).setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void setupActivityComponent() {
        DaggerSelectContinentComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .selectContinentModule(new SelectContinentModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(SelectContinentContract.SelectContinentContractPresenter presenter) {
        mPresenter = (SelectContinentPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @OnClick({R.id.bt_cancel, R.id.bt_continue})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_cancel:
                finish();
                break;
            case R.id.bt_continue:
                if(tvContinent.getText().toString().equals(""))
                {
                    ToastUtil.displayShortToast(getResources().getString(R.string.please_choose_a_continent));
                    return;
                }
                Intent intent = new Intent(this, SelectCountryActivity.class);
                intent.putExtra("continent", tvContinent.getText().toString());
                startActivityForResult(intent, 0);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK, data);
            finish();
        }
    }
}