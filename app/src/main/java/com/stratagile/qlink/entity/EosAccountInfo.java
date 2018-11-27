package com.stratagile.qlink.entity;

import java.util.List;

public class EosAccountInfo extends BaseBack {

    /**
     * data : {"errno":0,"data":{"creator":"yfhuangeos2g","create_timestamp":"2018-10-30T14:12:38.000","permissions":[{"parent":"owner","required_auth":{"waits":[],"keys":[{"weight":1,"key":"EOS5h4gKMhwHKjZ7f4XuZVdRVvAfYYqqbiLaav2zpLcJsqS2wzyGw"}],"threshold":1,"accounts":[]},"perm_name":"active"},{"parent":"","required_auth":{"waits":[],"keys":[{"weight":1,"key":"EOS5h4gKMhwHKjZ7f4XuZVdRVvAfYYqqbiLaav2zpLcJsqS2wzyGw"}],"threshold":1,"accounts":[]},"perm_name":"owner"}]},"errmsg":"Success"}
     */

    private DataBeanX data;

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * errno : 0
         * data : {"creator":"yfhuangeos2g","create_timestamp":"2018-10-30T14:12:38.000","permissions":[{"parent":"owner","required_auth":{"waits":[],"keys":[{"weight":1,"key":"EOS5h4gKMhwHKjZ7f4XuZVdRVvAfYYqqbiLaav2zpLcJsqS2wzyGw"}],"threshold":1,"accounts":[]},"perm_name":"active"},{"parent":"","required_auth":{"waits":[],"keys":[{"weight":1,"key":"EOS5h4gKMhwHKjZ7f4XuZVdRVvAfYYqqbiLaav2zpLcJsqS2wzyGw"}],"threshold":1,"accounts":[]},"perm_name":"owner"}]}
         * errmsg : Success
         */

        private int errno;
        private DataBean data;
        private String errmsg;

        public int getErrno() {
            return errno;
        }

        public void setErrno(int errno) {
            this.errno = errno;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public String getErrmsg() {
            return errmsg;
        }

        public void setErrmsg(String errmsg) {
            this.errmsg = errmsg;
        }

        public static class DataBean {
            /**
             * creator : yfhuangeos2g
             * create_timestamp : 2018-10-30T14:12:38.000
             * permissions : [{"parent":"owner","required_auth":{"waits":[],"keys":[{"weight":1,"key":"EOS5h4gKMhwHKjZ7f4XuZVdRVvAfYYqqbiLaav2zpLcJsqS2wzyGw"}],"threshold":1,"accounts":[]},"perm_name":"active"},{"parent":"","required_auth":{"waits":[],"keys":[{"weight":1,"key":"EOS5h4gKMhwHKjZ7f4XuZVdRVvAfYYqqbiLaav2zpLcJsqS2wzyGw"}],"threshold":1,"accounts":[]},"perm_name":"owner"}]
             */

            private String creator;
            private String create_timestamp;
            private List<PermissionsBean> permissions;

            public String getCreator() {
                return creator;
            }

            public void setCreator(String creator) {
                this.creator = creator;
            }

            public String getCreate_timestamp() {
                return create_timestamp;
            }

            public void setCreate_timestamp(String create_timestamp) {
                this.create_timestamp = create_timestamp;
            }

            public List<PermissionsBean> getPermissions() {
                return permissions;
            }

            public void setPermissions(List<PermissionsBean> permissions) {
                this.permissions = permissions;
            }

            public static class PermissionsBean {
                /**
                 * parent : owner
                 * required_auth : {"waits":[],"keys":[{"weight":1,"key":"EOS5h4gKMhwHKjZ7f4XuZVdRVvAfYYqqbiLaav2zpLcJsqS2wzyGw"}],"threshold":1,"accounts":[]}
                 * perm_name : active
                 */

                private String parent;
                private RequiredAuthBean required_auth;
                private String perm_name;

                public String getParent() {
                    return parent;
                }

                public void setParent(String parent) {
                    this.parent = parent;
                }

                public RequiredAuthBean getRequired_auth() {
                    return required_auth;
                }

                public void setRequired_auth(RequiredAuthBean required_auth) {
                    this.required_auth = required_auth;
                }

                public String getPerm_name() {
                    return perm_name;
                }

                public void setPerm_name(String perm_name) {
                    this.perm_name = perm_name;
                }

                public static class RequiredAuthBean {
                    /**
                     * waits : []
                     * keys : [{"weight":1,"key":"EOS5h4gKMhwHKjZ7f4XuZVdRVvAfYYqqbiLaav2zpLcJsqS2wzyGw"}]
                     * threshold : 1
                     * accounts : []
                     */

                    private int threshold;
                    private List<?> waits;
                    private List<KeysBean> keys;
                    private List<?> accounts;

                    public int getThreshold() {
                        return threshold;
                    }

                    public void setThreshold(int threshold) {
                        this.threshold = threshold;
                    }

                    public List<?> getWaits() {
                        return waits;
                    }

                    public void setWaits(List<?> waits) {
                        this.waits = waits;
                    }

                    public List<KeysBean> getKeys() {
                        return keys;
                    }

                    public void setKeys(List<KeysBean> keys) {
                        this.keys = keys;
                    }

                    public List<?> getAccounts() {
                        return accounts;
                    }

                    public void setAccounts(List<?> accounts) {
                        this.accounts = accounts;
                    }

                    public static class KeysBean {
                        /**
                         * weight : 1
                         * key : EOS5h4gKMhwHKjZ7f4XuZVdRVvAfYYqqbiLaav2zpLcJsqS2wzyGw
                         */

                        private int weight;
                        private String key;

                        public int getWeight() {
                            return weight;
                        }

                        public void setWeight(int weight) {
                            this.weight = weight;
                        }

                        public String getKey() {
                            return key;
                        }

                        public void setKey(String key) {
                            this.key = key;
                        }
                    }
                }
            }
        }
    }
}
