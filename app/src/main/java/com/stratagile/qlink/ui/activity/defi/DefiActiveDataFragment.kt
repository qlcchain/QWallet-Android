package com.stratagile.qlink.ui.activity.defi

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import butterknife.ButterKnife
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseFragment
import com.stratagile.qlink.entity.defi.ActiveDataBean
import com.stratagile.qlink.entity.defi.DefiDetail
import com.stratagile.qlink.entity.defi.DefiStateList
import com.stratagile.qlink.ui.activity.defi.component.DaggerDefiActiveDataComponent
import com.stratagile.qlink.ui.activity.defi.contract.DefiActiveDataContract
import com.stratagile.qlink.ui.activity.defi.module.DefiActiveDataModule
import com.stratagile.qlink.ui.activity.defi.presenter.DefiActiveDataPresenter
import com.stratagile.qlink.utils.ActiveDataYValueFormatter
import com.stratagile.qlink.utils.TimeUtil
import com.stratagile.qlink.utils.TimeValueFormatter
import kotlinx.android.synthetic.main.fragment_defi_active_state.*
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: $description
 * @date 2020/06/02 10:25:59
 */

class DefiActiveDataFragment : BaseFragment(), DefiActiveDataContract.View {

    @Inject
    lateinit internal var mPresenter: DefiActiveDataPresenter
    lateinit var viewModel: DefiViewModel
    lateinit var mDefiDetail : DefiDetail
    lateinit var valueMap : HashMap<String, MutableList<ActiveDataBean>>
    lateinit var defiStateList: DefiStateList

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_defi_active_state, null)
        ButterKnife.bind(this, view)
        val mBundle = arguments
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(DefiViewModel::class.java)
        viewModel.defiDetailLiveData.observe(this, Observer {
            mDefiDetail = it!!
        })
        viewModel.defiStateListLiveData.observe(this, Observer {
            defiStateList = it!!
        })
        valueMap = hashMapOf()
        valueMap["TVL USD"] = mutableListOf()
        valueMap["ETH"] = mutableListOf()
        valueMap["DAI"] = mutableListOf()
        valueMap["BTC"] = mutableListOf()

        segmentControlView.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.active_data_tvl_usd -> {
                    setToTvl()
                    KLog.i("active_data_tvl_usd")
                }
                R.id.active_data_eth-> {
                    setEth()
                    KLog.i("active_data_eth")
                }
                R.id.active_data_dai -> {
                    setDai()
                    KLog.i("active_data_dai")
                }
                R.id.active_data_btc -> {
                    setBtc()
                    KLog.i("active_data_btc")
                }
            }

        }

//        chart.setViewPortOffsets(10f, 0f, 10f, resources.getDimension(R.dimen.x30))
        chart.setBackgroundColor(resources.getColor(R.color.white))

        // no description text

        // no description text
        chart.description.isEnabled = false
        chart.legend.isEnabled = false

        // enable touch gestures

        // enable touch gestures
        chart.setTouchEnabled(false)

        // enable scaling and dragging

        // enable scaling and dragging
        chart.isDragEnabled = true
        chart.setScaleEnabled(false)

        // if disabled, scaling can be done on x- and y-axis separately

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false)

        chart.setDrawGridBackground(false)
        chart.maxHighlightDistance = 300f
        val x = chart.xAxis
        x.isEnabled = true
        x.setLabelCount(5, false)
        x.position = XAxis.XAxisPosition.BOTTOM
        x.setDrawAxisLine(true)
        x.setDrawGridLines(true)
        x.granularity = 1f
        x.setDrawLabels(true)
        x.textColor = resources.getColor(R.color.color_29282a)
        x.setValueFormatter(TimeValueFormatter())

        val y = chart.axisLeft
        y.setLabelCount(6, false)
        y.textColor = resources.getColor(R.color.color_29282a)
        y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        y.setDrawLabels(true)
        y.setDrawGridLines(true)
        y.setDrawAxisLine(true)
        y.setValueFormatter(ActiveDataYValueFormatter())
        chart.axisLeft.isEnabled = true
//        y.axisLineColor = resources.getColor(R.color.mainColor)
        chart.axisRight.isEnabled = false
        chart.legend.isEnabled = true


        chart.animateX(2000)

        chart.invalidate()
    }

    fun setToTvl() {
        tvDesc.text = "Total Value Locked (USD)"
        val values = ArrayList<Entry>()
        valueMap["TVL USD"]!!.forEach {
            values.add(Entry(it.time.toFloat(), it.value.toFloat()))
        }
        val set1: LineDataSet
        // create a dataset and give it a type
        // create a dataset and give it a type
        set1 = LineDataSet(values, "")

        set1.mode = LineDataSet.Mode.LINEAR
        set1.cubicIntensity = 0.2f
        set1.setDrawFilled(false)
        set1.setDrawCircles(false)
        set1.lineWidth = 1.0f
        set1.color = resources.getColor(R.color.color_945BFF)
        set1.fillColor = resources.getColor(R.color.transparent)
        set1.fillAlpha = 0
        set1.setDrawHorizontalHighlightIndicator(false)
        set1.fillFormatter = IFillFormatter { dataSet, dataProvider -> chart.axisLeft.axisMinimum }
        // create a data object with the data sets
        // create a data object with the data sets
        val data1 = LineData(set1)
        chart.animateX(2000)
        data1.setValueTextSize(9f)
        data1.setDrawValues(false)
        chart.data = data1

        val sets = chart.data.dataSets
        for (iSet in sets) {
            val set = iSet as LineDataSet
            set.setDrawFilled(true)
            set.mode = LineDataSet.Mode.LINEAR
        }
        chart.invalidate()
    }

    fun setEth() {
        tvDesc.text = "ETH Locked in" + mDefiDetail.project.name
        val values = ArrayList<Entry>()
        valueMap["ETH"]!!.forEach {
            values.add(Entry(it.time.toFloat(), it.value.toFloat()))
        }
        val set1: LineDataSet
        // create a dataset and give it a type
        // create a dataset and give it a type
        set1 = LineDataSet(values, "")

        set1.mode = LineDataSet.Mode.LINEAR
        set1.cubicIntensity = 0.2f
        set1.setDrawFilled(true)
        set1.setDrawCircles(false)
        set1.lineWidth = 1.0f
        set1.color = resources.getColor(R.color.color_945BFF)
        set1.fillColor = resources.getColor(R.color.transparent)
        set1.fillAlpha = 0
        set1.setDrawHorizontalHighlightIndicator(false)
        set1.fillFormatter = IFillFormatter { dataSet, dataProvider -> chart.axisLeft.axisMinimum }
        // create a data object with the data sets
        // create a data object with the data sets
        val data1 = LineData(set1)
        chart.animateX(2000)
        data1.setValueTextSize(9f)
        data1.setDrawValues(false)
        chart.data = data1

        val sets = chart.data
                .dataSets

        for (iSet in sets) {
            val set = iSet as LineDataSet
            set.setDrawFilled(true)
            set.mode = LineDataSet.Mode.LINEAR
        }
        chart.invalidate()
    }
    fun setDai() {
        tvDesc.text = "DAI Locked in" + mDefiDetail.project.name
        val values = ArrayList<Entry>()
        valueMap["DAI"]!!.forEach {
            values.add(Entry(it.time.toFloat(), it.value.toFloat()))
        }
        val set1: LineDataSet
        // create a dataset and give it a type
        // create a dataset and give it a type
        set1 = LineDataSet(values, "")

        set1.mode = LineDataSet.Mode.LINEAR
        set1.cubicIntensity = 0.2f
        set1.setDrawFilled(true)
        set1.setDrawCircles(false)
        set1.lineWidth = 1.0f
        set1.color = resources.getColor(R.color.color_945BFF)
        set1.fillColor = resources.getColor(R.color.transparent)
        set1.fillAlpha = 0
        set1.setDrawHorizontalHighlightIndicator(false)
        set1.fillFormatter = IFillFormatter { dataSet, dataProvider -> chart.axisLeft.axisMinimum }
        // create a data object with the data sets
        // create a data object with the data sets
        val data1 = LineData(set1)
        chart.animateX(2000)
        data1.setValueTextSize(9f)
        data1.setDrawValues(false)
        chart.data = data1

        val sets = chart.data
                .dataSets

        for (iSet in sets) {
            val set = iSet as LineDataSet
            set.setDrawFilled(true)
            set.mode = LineDataSet.Mode.LINEAR
        }
        chart.invalidate()
    }
    fun setBtc() {
        tvDesc.text = "BTC Locked in" + mDefiDetail.project.name
        val values = ArrayList<Entry>()
        valueMap["BTC"]!!.forEach {
            values.add(Entry(it.time.toFloat(), it.value.toFloat()))
        }
        val set1: LineDataSet
        // create a dataset and give it a type
        // create a dataset and give it a type
        set1 = LineDataSet(values, "")

        set1.mode = LineDataSet.Mode.LINEAR
        set1.cubicIntensity = 0.2f
        set1.setDrawFilled(true)
        set1.setDrawCircles(false)
        set1.lineWidth = 1.0f
        set1.color = resources.getColor(R.color.color_945BFF)
        set1.fillColor = resources.getColor(R.color.transparent)
        set1.fillAlpha = 0
        set1.setDrawHorizontalHighlightIndicator(false)
        set1.fillFormatter = IFillFormatter { dataSet, dataProvider -> chart.axisLeft.axisMinimum }
        // create a data object with the data sets
        // create a data object with the data sets
        val data1 = LineData(set1)
        chart.animateX(2000)
        data1.setValueTextSize(9f)
        data1.setDrawValues(false)
        chart.data = data1

        val sets = chart.data
                .dataSets

        for (iSet in sets) {
            val set = iSet as LineDataSet
            set.setDrawFilled(true)
            set.mode = LineDataSet.Mode.LINEAR
        }
        chart.invalidate()
    }

    fun getDefiStateList() {
        var infoMap = hashMapOf<String, String>()
        infoMap["projectId"] =mDefiDetail.project.id
        infoMap["page"] = "1"
        infoMap["size"] = "30"
        mPresenter.getDefiStateList(infoMap)
    }

    override fun setDefiStateListData(defiStateList: DefiStateList) {
        this.defiStateList = defiStateList
        defiStateList.historicalStatsList.forEach {
            if (it.tvlUsd != 0.toDouble()) {
                valueMap["TVL USD"]!!.add(0, ActiveDataBean(TimeUtil.timeStamp(it.statsDate), it.tvlUsd))
            }
            if (it.eth != 0.toDouble()) {
                valueMap["ETH"]!!.add(0, ActiveDataBean(TimeUtil.timeStamp(it.statsDate), it.eth))
            }
            if (it.dai != 0.toDouble()) {
                valueMap["DAI"]!!.add(0, ActiveDataBean(TimeUtil.timeStamp(it.statsDate), it.dai))
            }
            if (it.btc != 0.toDouble()) {
                valueMap["BTC"]!!.add(0, ActiveDataBean(TimeUtil.timeStamp(it.statsDate), it.btc))
            }
        }

        if (valueMap["BTC"]!!.size != 0) {
            var tcbButton = layoutInflater.inflate(R.layout.defi_rado, null, false) as RadioButton
            tcbButton.text = "BTC"
            tcbButton.id = R.id.active_data_btc
            segmentControlView.addView(tcbButton, 0)
            KLog.i("添加BTC")
        }
        if (valueMap["DAI"]!!.size != 0) {
            var daiButton = layoutInflater.inflate(R.layout.defi_rado, null, false) as RadioButton
            daiButton.text = "DAI"
            daiButton.id = R.id.active_data_dai
            segmentControlView.addView(daiButton, 0)
            KLog.i("添加DAI")
        }
        if (valueMap["ETH"]!!.size != 0) {
            var ethButton = layoutInflater.inflate(R.layout.defi_rado, null, false) as RadioButton
            ethButton.text = "ETH"
            ethButton.id = R.id.active_data_eth
            segmentControlView.addView(ethButton, 0)
            KLog.i("添加ETH")
        }
        if (valueMap["TVL USD"]!!.size != 0) {
            var tvlUsdButton = layoutInflater.inflate(R.layout.defi_rado, null, false) as RadioButton
            tvlUsdButton.text = "TVL USD"
            tvlUsdButton.id = R.id.active_data_tvl_usd
            segmentControlView.addView(tvlUsdButton, 0)
            KLog.i("添加TVL USD")
        }
        segmentControlView.check(R.id.active_data_tvl_usd)
    }

    override fun setupFragmentComponent() {
        DaggerDefiActiveDataComponent
                .builder()
                .appComponent((activity!!.application as AppConfig).applicationComponent)
                .defiActiveDataModule(DefiActiveDataModule(this))
                .build()
                .inject(this)
    }

    override fun initDataFromNet() {
        if (this::defiStateList.isInitialized) {
            setDefiStateListData(defiStateList)
        } else {
            getDefiStateList()
        }
    }

    override fun setPresenter(presenter: DefiActiveDataContract.DefiActiveDataContractPresenter) {
        mPresenter = presenter as DefiActiveDataPresenter
    }

    override fun initDataFromLocal() {

    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }
}