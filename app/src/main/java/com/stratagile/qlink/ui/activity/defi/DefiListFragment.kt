package com.stratagile.qlink.ui.activity.defi

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pawegio.kandroid.runDelayed
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseFragment
import com.stratagile.qlink.entity.DefiPrice
import com.stratagile.qlink.entity.defi.DefiCategory
import com.stratagile.qlink.entity.defi.DefiList
import com.stratagile.qlink.entity.defi.DefiStatsCache
import com.stratagile.qlink.entity.eventbus.DefiLoadData
import com.stratagile.qlink.ui.activity.defi.component.DaggerDefiListComponent
import com.stratagile.qlink.ui.activity.defi.contract.DefiListContract
import com.stratagile.qlink.ui.activity.defi.module.DefiListModule
import com.stratagile.qlink.ui.activity.defi.presenter.DefiListPresenter
import com.stratagile.qlink.ui.adapter.LeftSpaceItemDecoration
import com.stratagile.qlink.ui.adapter.defi.DefiCategoryAdapter
import com.stratagile.qlink.ui.adapter.defi.DefiCategoryEntity
import com.stratagile.qlink.ui.adapter.defi.DefiHistoryListAdapter
import com.stratagile.qlink.ui.adapter.defi.DefiListAdapter
import com.stratagile.qlink.utils.FileUtil
import com.stratagile.qlink.utils.FireBaseUtils
import com.stratagile.qlink.utils.GsonUtils
import com.stratagile.qlink.utils.UIUtils
import kotlinx.android.synthetic.main.fragment_defi_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: $description
 * @date 2020/05/25 17:10:05
 */

class DefiListFragment : BaseFragment(), DefiListContract.View {

    @Inject
    lateinit internal var mPresenter: DefiListPresenter

    lateinit var selectCategory: DefiCategoryEntity

    var currentPage = 0

    var asc = false

    var isShowHistory = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_defi_list, null)
        val mBundle = arguments
        EventBus.getDefault().register(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectCategory = DefiCategoryEntity("All", true)
        categoryAdapter = DefiCategoryAdapter(arrayListOf())
        recyclerViewCatogry.layoutManager = LinearLayoutManager(activity!!, 0, false)
        recyclerViewCatogry.adapter = categoryAdapter
        recyclerViewCatogry.addItemDecoration(LeftSpaceItemDecoration(UIUtils.dip2px(15f, activity!!)))
        categoryAdapter.setOnItemChildClickListener { adapter, view, position ->
            categoryAdapter.data.forEachIndexed { index, defiCategoryEntity ->
                FireBaseUtils.logEvent(activity, FireBaseUtils.Defi_Home_Top_Category)
                defiCategoryEntity.isSelect = (index == position)
                if (defiCategoryEntity.isSelect) {
                    selectCategory = defiCategoryEntity
                    currentPage = 0
                    getDefis()
                    if (isShowHistory) {
                        llHistoryBt.performClick()
                    }
                }
            }
            categoryAdapter.notifyDataSetChanged()
        }
        defiListAdapter = DefiListAdapter(arrayListOf())
        recyclerView.adapter = defiListAdapter
        defiListAdapter.setEmptyView(R.layout.empty_layout, refreshLayout)
        defiListAdapter.setEnableLoadMore(true)
        defiListAdapter.setOnItemClickListener { adapter, view, position ->
            startActivity(Intent(activity!!, DefiDetailActivity::class.java).putExtra("defiEntity", defiListAdapter.data[position]))
            addDefiDetailHistory(defiListAdapter.data[position])
        }
        UIUtils.configSwipeRefreshLayoutColors(refreshLayout)
        refreshLayout.setOnRefreshListener {
            refreshLayout.isRefreshing = false
            currentPage = 0
            getDefis()
        }
        defiListAdapter.setOnLoadMoreListener({ getDefis() }, recyclerView)

        ivSort.setOnClickListener {
            asc = !asc
            currentPage = 0
            getDefis()
            if (asc) {
                ivSort.setImageResource(R.mipmap.arrow_up)
            } else {
                ivSort.setImageResource(R.mipmap.arrow_lower)
            }
        }
        tvLocked.setOnClickListener {
            ivSort.performClick()
        }

        llHistoryBt.setOnClickListener {
            if (isShowHistory) {
                llHistory.visibility = View.GONE
            } else {
                FireBaseUtils.logEvent(activity, FireBaseUtils.Defi_Home_Record)
                llHistory.visibility = View.VISIBLE
                showHistory()

            }
            llHistoryBt.isSelected = !isShowHistory
            ivHsitory.isSelected = !isShowHistory
            isShowHistory = !isShowHistory
        }
        getCategoryList()
        var saveDefiStr = FileUtil.readData("/Qwallet/saveDefiStr.txt")
        if (!"".equals(saveDefiStr)) {
            var list = Gson().fromJson<ArrayList<DefiList.ProjectListBean>>(saveDefiStr, object : TypeToken<ArrayList<DefiList.ProjectListBean>>() {}.type)
            defiListAdapter.setNewData(list)
        }
        var saveDefiPriceStr = FileUtil.readData("/Qwallet/saveDefiPriceStr.txt")
        if (!"".equals(saveDefiPriceStr)) {
            var list = Gson().fromJson<ArrayList<DefiPrice.DefiTokenListBean>>(saveDefiPriceStr, object : TypeToken<ArrayList<DefiPrice.DefiTokenListBean>>() {}.type)
            tvTokenPrice.removeAllViews()
            var views: ArrayList<View> = ArrayList()
            list.forEach {
                val moreView = LayoutInflater.from(activity).inflate(R.layout.defi_price_item, null) as LinearLayout
                var tvTokenName = moreView.findViewById<TextView>(R.id.tvTokenName)
                var tvTokenPrice1 = moreView.findViewById<TextView>(R.id.tvTokenPrice1)
                var percentChange24h = moreView.findViewById<TextView>(R.id.percentChange24h)
                var ivArrow = moreView.findViewById<ImageView>(R.id.ivArrow)
                tvTokenName.text = it.symbol
                tvTokenPrice1.text = "$ " + it.price.toBigDecimal().setScale(4, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()
                percentChange24h.text = it.percentChange24h + " (24H)"
                if (it.percentChange24h.toFloat() > 0) {
                    percentChange24h.setTextColor(resources.getColor(R.color.color_7ED321))
                    ivArrow.setImageResource(R.mipmap.icon_arrow_up)
                } else {
                    percentChange24h.setTextColor(resources.getColor(R.color.color_f50f60))
                    ivArrow.setImageResource(R.mipmap.icon_arrow_down)
                }
                views.add(moreView)
            }
            tvTokenPrice.setViews(views)
        }

        var saveDefiCategoryStr = FileUtil.readData("/Qwallet/saveDefiCategoryStr.txt")
        if (!"".equals(saveDefiCategoryStr)) {
            var list = Gson().fromJson<ArrayList<String>>(saveDefiCategoryStr, object : TypeToken<ArrayList<String>>() {}.type)
            var cateList = arrayListOf<DefiCategoryEntity>()
            selectCategory = DefiCategoryEntity("All", true)
            cateList.add(selectCategory)
            list.forEach {
                cateList.add(DefiCategoryEntity(it, false))
            }
            categoryAdapter.setNewData(cateList)
        }

    }

    lateinit var defiHistoryListAdapter : DefiHistoryListAdapter
    fun showHistory() {
        var defiHistoryStr = FileUtil.readData("/Qwallet/defiHisotry.txt")
        if ("".equals(defiHistoryStr)) {
            return
        }
        var list = Gson().fromJson<ArrayList<DefiList.ProjectListBean>>(defiHistoryStr, object : TypeToken<ArrayList<DefiList.ProjectListBean>>() {}.type)
        list.reverse()
        defiHistoryListAdapter = DefiHistoryListAdapter(list)
        recyclerViewHistory.adapter = defiHistoryListAdapter
        defiHistoryListAdapter.setOnItemClickListener { adapter, view, position ->
            startActivityForResult(Intent(activity!!, DefiDetailActivity::class.java).putExtra("defiEntity", defiHistoryListAdapter.data[position]).putExtra("position", position), 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == 10) {
            KLog.i("删除")
            defiHistoryListAdapter.remove(data!!.getIntExtra("position", 0))
            defiHistoryListAdapter.notifyDataSetChanged()
            var saveHisotryStr = GsonUtils.objToJson(defiHistoryListAdapter.data)
            KLog.i(saveHisotryStr)
            FileUtil.savaData("/Qwallet/defiHisotry.txt", saveHisotryStr)
        }
    }

    fun addDefiDetailHistory(projectListBean: DefiList.ProjectListBean) {
        var defiHistoryStr = FileUtil.readData("/Qwallet/defiHisotry.txt")
        var list = Gson().fromJson<ArrayList<DefiList.ProjectListBean>>(defiHistoryStr, object : TypeToken<ArrayList<DefiList.ProjectListBean>>() {}.type)
        var hisotryMap = LinkedHashMap<String, DefiList.ProjectListBean>()
        if (list != null) {
            list.forEach {
                hisotryMap[it.id] = it
            }
        }
        hisotryMap.remove(projectListBean.id)
        hisotryMap[projectListBean.id] = projectListBean
        var saveList = mutableListOf<DefiList.ProjectListBean>()
        hisotryMap.toList().forEach {
            saveList.add(it.second)
        }
        var saveHisotryStr = GsonUtils.objToJson(saveList)
        KLog.i(saveHisotryStr)
        FileUtil.savaData("/Qwallet/defiHisotry.txt", saveHisotryStr)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun defiLoadData(defiLoadData: DefiLoadData) {
        KLog.i("加载数据")
        getCategoryList()
    }

    fun getDefis() {
        currentPage++
        refreshLayout.setRefreshing(false)
        var infoMap = hashMapOf<String, String>()
        infoMap["category"] = if (selectCategory.categoryName.equals("All")) {
            ""
        } else {
            selectCategory.categoryName
        }
        infoMap["order"] = if (asc) {
            "asc"
        } else {
            "desc"
        }
        infoMap["page"] = currentPage.toString()
        infoMap["size"] = "20"
        mPresenter.getDefis(infoMap, currentPage)
        runDelayed(400) {
            mPresenter.defiPriceList(hashMapOf())
        }
    }


    override fun setupFragmentComponent() {
        DaggerDefiListComponent
                .builder()
                .appComponent((activity!!.application as AppConfig).applicationComponent)
                .defiListModule(DefiListModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: DefiListContract.DefiListContractPresenter) {
        mPresenter = presenter as DefiListPresenter
    }

    override fun initDataFromLocal() {

    }

    override fun initDataFromNet() {
        getCategoryList()
    }

    fun getCategoryList() {
        KLog.i("延时加载。。。")
        mPresenter.getCategoryList()
    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

    lateinit var categoryAdapter: DefiCategoryAdapter
    override fun setCategoryList(category: DefiCategory) {
        var cateList = arrayListOf<DefiCategoryEntity>()
        selectCategory = DefiCategoryEntity("All", true)
        cateList.add(selectCategory)
        category.categoryList.forEach {
            cateList.add(DefiCategoryEntity(it, false))
        }
        getDefis()
        categoryAdapter.setNewData(cateList)

        var saveDefiCategoryStr = GsonUtils.objToJson(category.categoryList)
        KLog.i(saveDefiCategoryStr)
        FileUtil.savaData("/Qwallet/saveDefiCategoryStr.txt", saveDefiCategoryStr)

    }

    lateinit var defiListAdapter: DefiListAdapter
    override fun setDefiList(defiList: DefiList, currentPage: Int) {
        if (defiList.projectList.size > 0) {
            var defiListIds = mutableListOf<String>()
            defiList.projectList.forEach {
                defiListIds.add(it.id)
            }
            getDefiStatsCache(defiListIds)
        }
        if (currentPage == 1) {
            defiListAdapter.setNewData(ArrayList())
            var saveDefiStr = GsonUtils.objToJson(defiList.projectList)
            KLog.i(saveDefiStr)
            FileUtil.savaData("/Qwallet/saveDefiStr.txt", saveDefiStr)
        }
        defiListAdapter.addData(defiList.projectList)
        if (currentPage != 1) {
            defiListAdapter.loadMoreComplete()
        }
        if (defiList.projectList.size == 0) {
            defiListAdapter.loadMoreEnd(true)
        }
    }

    fun getDefiStatsCache(list: MutableList<String>) {
        var ids = ""
        list.forEachIndexed { index, s ->
            ids += s
            if (index < list.size - 1) {
                ids += ","
            }
        }
        var infoMap = hashMapOf<String, String>()
        infoMap["projectIds"] = ids
        mPresenter.getDefiStats(infoMap)
    }

    override fun setDefiStats(defiStateCache: DefiStatsCache) {
        defiListAdapter.data.forEach {project ->
            defiStateCache.statsCache.forEach {
                if (it.id.equals(project.id)) {
                    project.statsCache = it
                }
            }
        }
        defiListAdapter.notifyDataSetChanged()
    }

    override fun setDefiPrice(defiPrice: DefiPrice) {
        tvTokenPrice.removeAllViews()
        var views: ArrayList<View> = ArrayList()
        defiPrice.priceList.forEach {
            val moreView = LayoutInflater.from(activity).inflate(R.layout.defi_price_item, null) as LinearLayout
            var tvTokenName = moreView.findViewById<TextView>(R.id.tvTokenName)
            var tvTokenPrice1 = moreView.findViewById<TextView>(R.id.tvTokenPrice1)
            var percentChange24h = moreView.findViewById<TextView>(R.id.percentChange24h)
            var ivArrow = moreView.findViewById<ImageView>(R.id.ivArrow)
            tvTokenName.text = it.symbol
            tvTokenPrice1.text = "$ " + it.price.toBigDecimal().setScale(4, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()
            percentChange24h.text = it.percentChange24h + " (24H)"
            if (it.percentChange24h.toFloat() > 0) {
                percentChange24h.setTextColor(resources.getColor(R.color.color_7ED321))
                ivArrow.setImageResource(R.mipmap.icon_arrow_up)
            } else {
                percentChange24h.setTextColor(resources.getColor(R.color.color_f50f60))
                ivArrow.setImageResource(R.mipmap.icon_arrow_down)
            }
            views.add(moreView)
        }
        tvTokenPrice.setViews(views)
        var saveDefiPriceStr = GsonUtils.objToJson(defiPrice.priceList)
        KLog.i(saveDefiPriceStr)
        FileUtil.savaData("/Qwallet/saveDefiPriceStr.txt", saveDefiPriceStr)
    }
}