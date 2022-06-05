package model;

public class BICDirectoryEntry {

  private String Bic;
  private String NameP;
  private long Rgn;
  private String DateIn;
  private String PtType;
  private String Account;

  public String getBic() {
    return Bic;
  }

  public void setBic(String bic) {
    Bic = bic;
  }

  public String getNameP() {
    return NameP;
  }

  public void setNameP(String nameP) {
    NameP = nameP;
  }

  public long getRgn() {
    return Rgn;
  }

  public void setRgn(long rgn) {
    Rgn = rgn;
  }

  public String getDateIn() {
    return DateIn;
  }

  public void setDateIn(String dateIn) {
    DateIn = dateIn;
  }

  public String getPtType() {
    return PtType;
  }

  public void setPtType(String ptType) {
    PtType = ptType;
  }

  public String getAccount() {
    return Account;
  }

  public void setAccount(String account) {
    Account = account;
  }
}
