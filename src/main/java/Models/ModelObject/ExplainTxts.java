package main.java.Models.ModelObject;

public class ExplainTxts {
    private String string;
    private String explainTxt;

    public ExplainTxts(String string, String explainTxt) {
        this.string = string;
        this.explainTxt = explainTxt;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public void setExplainTxt(String explainTxt) {
        this.explainTxt = explainTxt;
    }

    public String getExplainTxt() {
        return explainTxt;
    }
}
