package main.java.Models.ModelObject;

import java.util.List;

public class CmList {
    private String cnt;
    private List<CmDatas> cmDatas;

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCmDatas(List<CmDatas> cmDatas) {
        this.cmDatas = cmDatas;
    }

    public List<CmDatas> getCmDatas() {
        return cmDatas;
    }
}

