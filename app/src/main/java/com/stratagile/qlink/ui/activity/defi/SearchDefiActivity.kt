package com.stratagile.qlink.ui.activity.defi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import com.stratagile.qlink.R
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.db.DefiSearchHistory
import com.stratagile.qlink.ui.activity.defi.component.DaggerSearchDefiComponent
import com.stratagile.qlink.ui.activity.defi.contract.SearchDefiContract
import com.stratagile.qlink.ui.activity.defi.module.SearchDefiModule
import com.stratagile.qlink.ui.activity.defi.presenter.SearchDefiPresenter
import com.stratagile.qlink.ui.adapter.defi.DexSearchHistoryAdapter
import com.stratagile.qlink.utils.UIUtils
import kotlinx.android.synthetic.main.activity_search_defi.*
import javax.inject.Inject


/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: $description
 * @date 2020/10/22 15:09:25
 */

class SearchDefiActivity : BaseActivity(), SearchDefiContract.View {

    @Inject
    internal lateinit var mPresenter: SearchDefiPresenter
    lateinit var dexSearchHistoryAdapter: DexSearchHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        needFront = true
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_search_defi)
    }
    override fun initData() {
        tvSearch.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(tvSearch, InputMethodManager.SHOW_IMPLICIT)
        val llp = LinearLayout.LayoutParams(UIUtils.getDisplayWidth(this), UIUtils.getStatusBarHeight(this))
        statusBar.setLayoutParams(llp)
        tvSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if ("".equals(tvSearch.text.toString())){
                    true
                }
                val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                val v: android.view.View? = window.peekDecorView()
                if (null != v) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
                var dappIntent = Intent(this, DappBrowserActivity::class.java)
                var website = tvSearch.text.toString()
                website = website.replace("https://", "")
                website = website.replace("http://", "")
                website = "https://" + website
                dappIntent.putExtra("url", website)
                startActivity(dappIntent)
                var list = AppConfig.instance.daoSession.defiSearchHistoryDao.loadAll()
                list.forEach {
                    if (tvSearch.text.toString().equals(it.url)) {
                        finish()
                        true
                    }
                }
                var defiSearchHistory = DefiSearchHistory()
                defiSearchHistory.url = website
                AppConfig.instance.daoSession.defiSearchHistoryDao.insert(defiSearchHistory)
                finish()
                true
            }
            false
        }
        tvCancel.setOnClickListener {
            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val v: android.view.View? = window.peekDecorView()
            if (null != v) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
            }
            finish()
        }
        var list = AppConfig.instance.daoSession.defiSearchHistoryDao.loadAll()
        dexSearchHistoryAdapter = DexSearchHistoryAdapter(list)
        recyclerView.adapter = dexSearchHistoryAdapter
        dexSearchHistoryAdapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.ivDelete) {
                AppConfig.instance.daoSession.defiSearchHistoryDao.delete(list[position])
                list.removeAt(position)
                dexSearchHistoryAdapter.notifyDataSetChanged()
            }
        }
        dexSearchHistoryAdapter.setOnItemClickListener { adapter, view, position ->
            var dappIntent = Intent(this, DappBrowserActivity::class.java)
            dappIntent.putExtra("url", dexSearchHistoryAdapter.data[position].url)
            startActivity(dappIntent)
            finish()
        }
    }

    override fun setupActivityComponent() {
       DaggerSearchDefiComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .searchDefiModule(SearchDefiModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: SearchDefiContract.SearchDefiContractPresenter) {
            mPresenter = presenter as SearchDefiPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}