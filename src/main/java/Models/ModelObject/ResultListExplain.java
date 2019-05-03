package main.java.Models.ModelObject;

import java.util.List;

public class ResultListExplain {
    private int cnt;
    private List<ExplainTxts> explainTxts;

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public int getCnt() {
        return cnt;
    }

    public void setExplainTxts(List<ExplainTxts> explainTxts) {
        this.explainTxts = explainTxts;
    }

    public List<ExplainTxts> getExplainTxts() {
        return explainTxts;
    }
}
