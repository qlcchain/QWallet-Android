package com.stratagile.qlink.utils;

import android.content.Context;

import com.stratagile.qlink.R;
import com.stratagile.qlink.constant.ConstantValue;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by huzhipeng on 2018/4/2.
 */

public class NickUtil {
    public static void initUserNickName(Context context) {
        List<String> nameList = Arrays.asList(context.getResources().getString(R.string.name).split(","));
        Random random = new Random();
        int fanwei = random.nextInt(nameList.size() - 1);
        ConstantValue.nickName = nameList.get(fanwei).trim();
    }
}
