package main.java.Models.ModelObject;

import java.util.List;

public class ResultListView {
    private int cnt;
    private List<SetViews> setViews;

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public int getCnt() {
        return cnt;
    }

    public void setSetViews(List<SetViews> setViews) {
        this.setViews = setViews;
    }

    public List<SetViews> getSetViews() {
        return setViews;
    }
}

