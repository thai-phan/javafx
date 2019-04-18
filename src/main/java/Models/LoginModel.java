package main.java.Models;

import main.java.Models.ModelObject.PersonalInfo;
import main.java.Models.ModelObject.Resultinfo;

public class LoginModel {
    private Resultinfo resultinfo;
    private PersonalInfo personalInfo;

    public void setResultinfo(Resultinfo resultinfo) {
        this.resultinfo = resultinfo;
    }

    public Resultinfo getResultinfo() {
        return resultinfo;
    }

    public void setPersonalInfo(PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }
}
