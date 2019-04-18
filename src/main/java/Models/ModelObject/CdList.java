package main.java.Models.ModelObject;

import java.util.List;

public class CdList {
    private int cnt;
    private List<Cds> cds;

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCds(List<Cds> cds) {
        this.cds = cds;
    }

    public List<Cds> getCds() {
        return cds;
    }
}
