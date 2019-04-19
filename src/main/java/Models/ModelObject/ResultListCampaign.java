package main.java.Models.ModelObject;

import java.util.List;

public class ResultListCampaign {
    private int cnt;
    private List<Cms> cms;

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCms(List<Cms> cms) {
        this.cms = cms;
    }

    public List<Cms> getCms() {
        return cms;
    }
}

