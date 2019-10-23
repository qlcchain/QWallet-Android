package com.stratagile.qlink.entity.stake;

import java.util.List;

public class MultSign {

    /**
     * extra : {}
     * label : AQ4SCrvgeU5AzfhL4QVvy2FC2TPqUgJLBd
     * isDefault : false
     * lock : false
     * contract : {"script":"5221030A21D4F076F8098A7AD738FC60BB3EDD8FA069E3EA3421CDC4BECA739A1B4E5F2102c6e68c61480003ed163f72b41cbb50ded29d79e513fd299d2cb844318b1b8ad552ae","parameters":[null,null],"deployed":false}
     * _address : AQ4SCrvgeU5AzfhL4QVvy2FC2TPqUgJLBd
     * _scriptHash : 87183904652e62ec60d0ed36a3a60975f6d6e25a
     */

    private ExtraBean extra;
    private String label;
    private boolean isDefault;
    private boolean lock;
    private ContractBean contract;
    private String _address;
    private String _scriptHash;

    public ExtraBean getExtra() {
        return extra;
    }

    public void setExtra(ExtraBean extra) {
        this.extra = extra;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public ContractBean getContract() {
        return contract;
    }

    public void setContract(ContractBean contract) {
        this.contract = contract;
    }

    public String get_address() {
        return _address;
    }

    public void set_address(String _address) {
        this._address = _address;
    }

    public String get_scriptHash() {
        return _scriptHash;
    }

    public void set_scriptHash(String _scriptHash) {
        this._scriptHash = _scriptHash;
    }

    public static class ExtraBean {
    }

    public static class ContractBean {
        /**
         * script : 5221030A21D4F076F8098A7AD738FC60BB3EDD8FA069E3EA3421CDC4BECA739A1B4E5F2102c6e68c61480003ed163f72b41cbb50ded29d79e513fd299d2cb844318b1b8ad552ae
         * parameters : [null,null]
         * deployed : false
         */

        private String script;
        private boolean deployed;
//        private List<Null> parameters;

        public String getScript() {
            return script;
        }

        public void setScript(String script) {
            this.script = script;
        }

        public boolean isDeployed() {
            return deployed;
        }

        public void setDeployed(boolean deployed) {
            this.deployed = deployed;
        }

//        public List<Null> getParameters() {
//            return parameters;
//        }
//
//        public void setParameters(List<Null> parameters) {
//            this.parameters = parameters;
//        }
    }
}
