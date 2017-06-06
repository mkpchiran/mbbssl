package process;

import java.io.Serializable;

/**
 * Created by chiranz on 5/15/17.
 */

public class Model implements Serializable{

    String reg_No,reg_Date,full_Name,address,qualifications;

    public String getReg_No() {
        return reg_No;
    }

    public void setReg_No(String reg_No) {
        this.reg_No = reg_No;
    }

    public String getReg_Date() {
        return reg_Date;
    }

    public void setReg_Date(String reg_Date) {
        this.reg_Date = reg_Date;
    }

    public String getFull_Name() {
        return full_Name;
    }

    public void setFull_Name(String full_Name) {
        this.full_Name = full_Name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getQualifications() {
        return qualifications;
    }

    @Override
    public String toString() {
        return "Model{" +
                "reg_No='" + reg_No + '\'' +
                ", reg_Date='" + reg_Date + '\'' +
                ", full_Name='" + full_Name + '\'' +
                ", address='" + address + '\'' +
                ", qualifications='" + qualifications + '\'' +
                '}';
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

}
