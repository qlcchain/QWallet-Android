package com.stratagile.qlink.ui.activity.finance.contract;

import com.stratagile.qlink.api.transaction.SendBackWithTxId;
import com.stratagile.qlink.api.transaction.SendCallBack;
import com.stratagile.qlink.entity.NeoWalletInfo;
import com.stratagile.qlink.entity.newwinq.ProductDetail;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for ProductDetailActivity
 * @Description: $description
 * @date 2019/04/11 11:16:32
 */
public interface ProductDetailContract {
    interface View extends BaseView<ProductDetailContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void showProductDetail(ProductDetail productDetail);

        void getNeoTokensInfo(NeoWalletInfo baseBack);

        void buyQLCProductBack();
    }

    interface ProductDetailContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);

        void getProductDetail(Map map);

        void getNeoWalletDetail(String address, Map map, String toAddress, String amount, SendBackWithTxId sendCallBack);

        void getUtxo(String address, SendCallBack sendCallBack);

        void buyQLCProduct(Map map);

        void getQLCCount(Map map);
    }
}