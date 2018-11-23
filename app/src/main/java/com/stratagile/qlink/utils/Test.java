package com.stratagile.qlink.utils;

import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        Map<Integer, String> vpnFileTemp = new HashMap<>();
        vpnFileTemp.put(1000, "1");
        vpnFileTemp.put(2000, "2");
        vpnFileTemp.put(3000, "3");
        vpnFileTemp.put(4000, "4");
        vpnFileTemp.put(1000, "10");
        vpnFileTemp.put(2000, "20");
        vpnFileTemp.put(3000, "30");
    }
}
