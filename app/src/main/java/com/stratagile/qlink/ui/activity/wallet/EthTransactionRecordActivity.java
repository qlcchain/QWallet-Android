package com.stratagile.qlink.ui.activity.wallet;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.entity.AllWallet;
import com.stratagile.qlink.entity.KLine;
import com.stratagile.qlink.entity.QrEntity;
import com.stratagile.qlink.entity.TokenInfo;
import com.stratagile.qlink.entity.TransactionInfo;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerEthTransactionRecordComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.EthTransactionRecordContract;
import com.stratagile.qlink.ui.activity.wallet.module.EthTransactionRecordModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.EthTransactionRecordPresenter;
import com.stratagile.qlink.ui.adapter.wallet.TransacationHistoryAdapter;
import com.stratagile.qlink.utils.TimeUtil;
import com.stratagile.qlink.utils.ToastUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description
 * @date 2018/10/29 16:12:21
 */

public class EthTransactionRecordActivity extends BaseActivity implements EthTransactionRecordContract.View {

    @Inject
    EthTransactionRecordPresenter mPresenter;
    @BindView(R.id.tvTokenValue)
    TextView tvTokenValue;
    @BindView(R.id.tvTokenMoney)
    TextView tvTokenMoney;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.llSend)
    LinearLayout llSend;
    @BindView(R.id.llRecive)
    LinearLayout llRecive;
    @BindView(R.id.chart)
    LineChart chart;

    private TokenInfo tokenInfo;

    TransacationHistoryAdapter transacationHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_eth_transaction_record);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        chart.setViewPortOffsets(10, 0, 10, getResources().getDimension(R.dimen.x30));
        chart.setBackgroundColor(getResources().getColor(R.color.white));

        // no description text
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawGridBackground(false);
        chart.setMaxHighlightDistance(300);
        XAxis x = chart.getXAxis();
        x.setEnabled(true);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setDrawAxisLine(true);
        x.setDrawGridLines(false);
        x.setGranularity(1);
        x.setDrawLabels(true);
        x.setTextColor(getResources().getColor(R.color.color_29282a));
//        x.setPosition(XAxis.XAxisPosition.BOTTOM);
//        x.setAxisLineColor(getResources().getColor(R.color.mainColor));
//        x.setAxisMaximum(12);
//        x.setAxisMinimum(4);
        x.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
//                KLog.i((long)value + 25000000);
                long millis = TimeUnit.MINUTES.toMillis((long) value + 25000000);
//                KLog.i(millis);
                return TimeUtil.getTransactionHistoryTime(millis);
//                return value + "";
            }
        });

        YAxis y = chart.getAxisRight();
        y.setLabelCount(6, false);
        y.setTextColor(getResources().getColor(R.color.color_29282a));
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y.setDrawGridLines(false);
        y.setDrawAxisLine(false);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisLeft().setEnabled(false);
        y.setAxisLineColor(getResources().getColor(R.color.mainColor));
        chart.getAxisRight().setEnabled(true);
        chart.getLegend().setEnabled(false);


        chart.animateXY(2000, 2000);

        // don't forget to refresh the drawing
        chart.invalidate();
    }

    @Override
    public void setChartData(KLine data) {
        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < data.getData().size(); i++) {
            long now = TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(data.getData().get(i).get(0)));
            values.add(new Entry(now - 25000000, Float.parseFloat(data.getData().get(i).get(1))));
//            KLog.i(i);
//            KLog.i(now);
//            KLog.i(Long.parseLong(data.getData().get(i).get(0)));
        }
        LineDataSet set1;
        // create a dataset and give it a type
        set1 = new LineDataSet(values, "");

        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setCubicIntensity(0.2f);
        set1.setDrawFilled(true);
        set1.setDrawCircles(false);
        set1.setLineWidth(1.0f);
        set1.setCircleColor(getResources().getColor(R.color.main_color));
        set1.setHighLightColor(getResources().getColor(R.color.primary_dark));
        set1.setColor(getResources().getColor(R.color.main_color));
        set1.setFillColor(getResources().getColor(R.color.color_eaf1fe));
        set1.setFillAlpha(60);
        set1.setDrawHorizontalHighlightIndicator(true);
        set1.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return chart.getAxisLeft().getAxisMinimum();
            }
        });
        // create a data object with the data sets
        LineData data1 = new LineData(set1);
        data1.setValueTextSize(9f);
        data1.setDrawValues(false);
        chart.setData(data1);
    }

    @Override
    protected void initData() {
        HashMap infoMap = new HashMap<String, Object>();
        tokenInfo = getIntent().getParcelableExtra("tokenInfo");
        infoMap.put("address", tokenInfo.getWalletAddress());
        if (tokenInfo.getWalletType() == AllWallet.WalletType.EthWallet) {
            if (!tokenInfo.getTokenSymol().toLowerCase().equals("eth")) {
                infoMap.put("token", tokenInfo.getTokenAddress());
            }
            mPresenter.getEthWalletTransaction(infoMap, tokenInfo.getWalletAddress());
        } else {
            infoMap.put("page", 1);
            mPresenter.getNeoWalletTransaction(infoMap);
        }
        setTitle(tokenInfo.getTokenSymol());
        String value = tokenInfo.getTokenValue() / (Math.pow(10.0, tokenInfo.getTokenDecimals())) + "";
        tvTokenValue.setText(value);
        BigDecimal b = new BigDecimal(new Double((tokenInfo.getTokenValue() * tokenInfo.getTokenPrice())).toString());
        double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        tvTokenMoney.setText("≈" + ConstantValue.currencyBean.getCurrencyImg() + f1);
        transacationHistoryAdapter = new TransacationHistoryAdapter(new ArrayList<>());
        recyclerView.setAdapter(transacationHistoryAdapter);
        transacationHistoryAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setPrimaryClip(ClipData.newPlainText("", transacationHistoryAdapter.getData().get(position).getTransationHash()));
                ToastUtil.displayShortToast("copy success");
            }
        });

        Map<String, Object> infoMap1 = new HashMap<>();
        infoMap1.put("symbol", tokenInfo.getTokenSymol());
        infoMap1.put("interval", "1m");
        infoMap1.put("size", 500);
        mPresenter.getTokenKline(infoMap1, tokenInfo.getTokenPrice());
    }

    @Override
    protected void setupActivityComponent() {
        DaggerEthTransactionRecordComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .ethTransactionRecordModule(new EthTransactionRecordModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(EthTransactionRecordContract.EthTransactionRecordContractPresenter presenter) {
        mPresenter = (EthTransactionRecordPresenter) presenter;
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
    public void setEthTransactionHistory(ArrayList<TransactionInfo> transactionInfos) {
        transacationHistoryAdapter.setNewData(transactionInfos);
    }

    @OnClick({R.id.llSend, R.id.llRecive})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llSend:
                if (tokenInfo.getWalletType() == AllWallet.WalletType.EthWallet) {
                    Intent intent1 = new Intent(this, EthTransferActivity.class);
                    intent1.putExtra("tokenInfo", tokenInfo);
                    startActivity(intent1);
                } else if (tokenInfo.getWalletType() == AllWallet.WalletType.NeoWallet) {
                    Intent intent1 = new Intent(this, NeoTransferActivity.class);
                    intent1.putExtra("tokenInfo", tokenInfo);
                    startActivity(intent1);
                }
                break;
            case R.id.llRecive:
                QrEntity qrEntity = new QrEntity(tokenInfo.getWalletAddress(), tokenInfo.getTokenName() + " Address", tokenInfo.getTokenImgName());
                Intent intent = new Intent(this, WalletQRCodeActivity.class);
                intent.putExtra("qrentity", qrEntity);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}