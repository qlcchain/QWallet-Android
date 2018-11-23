package com.stratagile.qlink.entity;

import java.util.List;

public class Test {

    /**
     * code : 0
     * msg : Request success
     * data : [[1541579040000,"0.00000843","0.00000843","0.00000843","0.00000843","3167.00000000",1541579099999,"0.02669781",2,"0.00000000","0.00000000","0"],[1541579100000,"0.00000843","0.00000843","0.00000840","0.00000840","36770.00000000",1541579159999,"0.30930505",15,"3893.00000000","0.03277906","0"],[1541579160000,"0.00000840","0.00000840","0.00000837","0.00000840","37878.00000000",1541579219999,"0.31734832",24,"6665.00000000","0.05596678","0"],[1541579220000,"0.00000840","0.00000840","0.00000840","0.00000840","0.00000000",1541579279999,"0.00000000",0,"0.00000000","0.00000000","0"],[1541579280000,"0.00000838","0.00000838","0.00000838","0.00000838","500.00000000",1541579339999,"0.00419000",1,"0.00000000","0.00000000","0"]]
     */

    private String code;
    private String msg;
    private List<List<Long>> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<List<Long>> getData() {
        return data;
    }

    public void setData(List<List<Long>> data) {
        this.data = data;
    }
}
