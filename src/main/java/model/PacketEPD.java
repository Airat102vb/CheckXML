package model;

import java.util.Optional;

public class PacketEPD {

  private long edAuthor;
  private String edDate;
  private long edNo;
  private long edQuantity;
  private String SystemCode;
  private String Bic;
  private String AccDocNo;
  private String AccDocDate;
  private long Sum;
  private String Name;
  private Optional<String> CorrespAcc;
  private boolean isPayer;

  public boolean isPayer() {
    return isPayer;
  }

  public void setPayer(boolean payer) {
    isPayer = payer;
  }

  public long getEdQuantity() {
    return edQuantity;
  }

  public void setEdQuantity(long edQuantity) {
    this.edQuantity = edQuantity;
  }

  public String getSystemCode() {
    return SystemCode;
  }

  public void setSystemCode(String systemCode) {
    SystemCode = systemCode;
  }

  public long getEdAuthor() {
    return edAuthor;
  }

  public void setEdAuthor(long edAuthor) {
    this.edAuthor = edAuthor;
  }

  public String getEdDate() {
    return edDate;
  }

  public void setEdDate(String edDate) {
    this.edDate = edDate;
  }

  public long getEdNo() {
    return edNo;
  }

  public void setEdNo(long edNo) {
    this.edNo = edNo;
  }

  public String getBic() {
    return Bic;
  }

  public void setBic(String bic) {
    Bic = bic;
  }

  public String getAccDocNo() {
    return AccDocNo;
  }

  public void setAccDocNo(String accDocNo) {
    AccDocNo = accDocNo;
  }

  public String getAccDocDate() {
    return AccDocDate;
  }

  public void setAccDocDate(String accDocDate) {
    AccDocDate = accDocDate;
  }

  public long getSum() {
    return Sum;
  }

  public void setSum(long sum) {
    Sum = sum;
  }

  public String getName() {
    return Name;
  }

  public void setName(String name) {
    Name = name;
  }

  public Optional<String> getCorrespAcc() {
    return CorrespAcc;
  }

  public void setCorrespAcc(Optional<String> correspAcc) {
    CorrespAcc = correspAcc;
  }
}
