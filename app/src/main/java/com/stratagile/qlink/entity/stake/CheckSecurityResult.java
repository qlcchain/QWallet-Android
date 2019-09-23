package com.stratagile.qlink.entity.stake;

import java.util.List;

public class CheckSecurityResult {

    /**
     * valid : 1
     * mac_addresses : [{"mac_address":"00:0a:95:9d:68:16","index":1},{"mac_address":"01:0a:95:6d:68:24","index":2},{"mac_address":"01:0a:95:6d:68:34","index":3}]
     */

    private int valid;
    private List<MacAddressesBean> mac_addresses;

    public int getValid() {
        return valid;
    }

    public void setValid(int valid) {
        this.valid = valid;
    }

    public List<MacAddressesBean> getMac_addresses() {
        return mac_addresses;
    }

    public void setMac_addresses(List<MacAddressesBean> mac_addresses) {
        this.mac_addresses = mac_addresses;
    }

    public static class MacAddressesBean {
        /**
         * mac_address : 00:0a:95:9d:68:16
         * index : 1
         */

        private String mac_address;
        private int index;

        public String getMac_address() {
            return mac_address;
        }

        public void setMac_address(String mac_address) {
            this.mac_address = mac_address;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
}
