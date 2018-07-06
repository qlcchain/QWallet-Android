package com.stratagile.qlink.guideview.compnonet;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.guideview.Component;

/**
 * Created by binIoter on 16/6/17.
 */
public class EnterWalletComponent implements Component {

  @Override
  public View getView(LayoutInflater inflater) {

    LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.layout_componoent_enter_wallet, null);
    return ll;
  }

  @Override
  public int getAnchor() {
    return Component.ANCHOR_TOP;
  }

  @Override
  public int getFitPosition() {
    return Component.FIT_END;
  }

  @Override
  public int getXOffset() {
    return (int) AppConfig.getInstance().getResources().getDimension(R.dimen.guide_enter_wallet_x);
  }

  @Override
  public int getYOffset() {
    return (int) AppConfig.getInstance().getResources().getDimension(R.dimen.guide_enter_wallet_y);
  }
}
