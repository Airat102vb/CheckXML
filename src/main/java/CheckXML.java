import model.BICDirectoryEntry;
import model.PacketEPD;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CheckXML {

  private static final String EDAUTHOR = "EDAuthor";
  private static final String EDDATE = "EDDate";
  private static final String EDNO = "EDNo";
  private static final String EDQUANTITY = "EDQuantity";
  private static final String SYSTEMCODE = "SystemCode";
  private static final String SUM = "Sum";

  public static void main(String[] args) {

    //TODO подключить зависимость assertj, улучшить описание ошибок
    File file = new File("PacketEPD.xml");
    File fileEd807 = new File("20220530_ED807_full.xml");
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    Document document = null;
    Document document807 = null;
    List<BICDirectoryEntry> bicDirectoryEntries = new ArrayList<>();
    List<PacketEPD> packetEPDS = new ArrayList<>();

    try {
      document = documentBuilderFactory.newDocumentBuilder().parse(file);
      document807 = documentBuilderFactory.newDocumentBuilder().parse(fileEd807);
    } catch (Exception e) {
      e.printStackTrace();
    }

    PacketEPD packetEPD = getFieldsValues(document);
    PacketEPD eD807 = getFieldsValues(document807);
    NodeList list = document.getDocumentElement().getElementsByTagName("ED101");
    NodeList list801 = document807.getDocumentElement().getElementsByTagName("BICDirectoryEntry");

    for(int i = 0; i < list801.getLength(); i++) {
      BICDirectoryEntry entry = new BICDirectoryEntry();
      entry.setBic(list801.item(i).getAttributes().getNamedItem("BIC").getNodeValue());
      entry.setNameP(list801.item(i).getChildNodes().item(0).getAttributes().getNamedItem("NameP").getNodeValue());
      entry.setRgn(Long.parseLong(list801.item(i).getChildNodes().item(0).getAttributes().getNamedItem("Rgn").getNodeValue()));
      entry.setDateIn(list801.item(i).getChildNodes().item(0).getAttributes().getNamedItem("DateIn").getNodeValue());
      entry.setPtType(list801.item(i).getChildNodes().item(0).getAttributes().getNamedItem("PtType").getNodeValue());
      try {
        entry.setAccount(list801.item(i).getChildNodes().item(1).getAttributes().getNamedItem("Account").getNodeValue());
      } catch (NullPointerException e) {
        try {
          entry.setAccount(list801.item(i).getChildNodes().item(2).getAttributes().getNamedItem("Account").getNodeValue());
        } catch (NullPointerException ex) {}
      }
      bicDirectoryEntries.add(entry);
    }

    for(int i = 0; i < list.getLength(); i++) {
      PacketEPD entryPayer = new PacketEPD();
      entryPayer.setBic(list.item(i).getChildNodes().item(1).getChildNodes().item(1).getAttributes().getNamedItem("BIC").getNodeValue());
      entryPayer.setAccDocNo(list.item(i).getChildNodes().item(0).getAttributes().getNamedItem("AccDocNo").getNodeValue());
      entryPayer.setAccDocDate(list.item(i).getChildNodes().item(0).getAttributes().getNamedItem("AccDocDate").getNodeValue());
      entryPayer.setSum(Long.parseLong(list.item(i).getAttributes().getNamedItem(SUM).getNodeValue()));
      entryPayer.setName(list.item(i).getChildNodes().item(1).getChildNodes().item(0).getTextContent());
      entryPayer.setCorrespAcc(Optional.ofNullable(list.item(i).getChildNodes().item(1).getChildNodes().item(1)
          .getAttributes().getNamedItem("CorrespAcc").getNodeValue()));
      entryPayer.setPayer(list.item(i).getChildNodes().item(1).getNodeName().equals("Payer") ? true : false);
      packetEPDS.add(entryPayer);

      PacketEPD entryPayee = new PacketEPD();
      entryPayee.setBic(list.item(i).getChildNodes().item(2).getChildNodes().item(1).getAttributes().getNamedItem("BIC").getNodeValue());
      entryPayee.setAccDocNo(list.item(i).getChildNodes().item(0).getAttributes().getNamedItem("AccDocNo").getNodeValue());
      entryPayee.setAccDocDate(list.item(i).getChildNodes().item(0).getAttributes().getNamedItem("AccDocDate").getNodeValue());
      entryPayee.setSum(Long.parseLong(list.item(i).getAttributes().getNamedItem(SUM).getNodeValue()));
      entryPayee.setName(list.item(i).getChildNodes().item(2).getChildNodes().item(0).getTextContent());
      entryPayee.setCorrespAcc(Optional.ofNullable(list.item(i).getChildNodes().item(2).getChildNodes().item(1)
          .getAttributes().getNamedItem("CorrespAcc").getNodeValue()));
      packetEPDS.add(entryPayee);
    }

    //Test mandatory fields
    if(packetEPD.getEdAuthor() != eD807.getEdAuthor()) {
      System.out.println("BUG! :" + EDAUTHOR + " не совпадает с источником");
    }
    if(!packetEPD.getEdDate().equals(eD807.getEdDate())) {
      System.out.println("BUG! :" + EDDATE + " не совпадает с источником");
    }
    if(packetEPD.getEdNo() != eD807.getEdNo()) {
      System.out.println("BUG! :" + EDNO + " не совпадает с источником");
    }

    //EDQuantity - должно соответствовать общему количеству платежных поручений ED101
    int docCount = document.getDocumentElement().getElementsByTagName("ED101").getLength();
    if(docCount != packetEPD.getEdQuantity()) {
      System.out.println("BUG! :" + EDQUANTITY + " не совпадает с общим количеством платежных поручений");
    }

    long sum = 0;
    for (int i = 0; i < list.getLength(); i++) {
      NamedNodeMap node = list.item(i).getAttributes();
      sum += Long.parseLong(node.getNamedItem("Sum").getNodeValue());

      //Проверка атрибутов платежных поручений ED101
      if (!node.getNamedItem(EDDATE).getNodeValue().equals(packetEPD.getEdDate())) {
        System.out.println("BUG! :" + EDDATE + " не совпадает c заголовком пакета PacketEPD");
      }
      if (Long.parseLong(node.getNamedItem(EDAUTHOR).getNodeValue()) != (packetEPD.getEdAuthor())) {
        System.out.println("BUG! :" + EDDATE + " не совпадает c заголовком пакета PacketEPD");
      }
      if (Integer.parseInt(node.getNamedItem(EDNO).getNodeValue()) != i + 1) {
        System.out.println("BUG! :" + EDNO + " не совпадает c ожидаемым порядковым номером");
      }
      if(!"01".equals(node.getNamedItem(SYSTEMCODE).getNodeValue())) {
        System.out.println("BUG! :" + SYSTEMCODE + " не совпадает с ожидаемым значением");
      }
    }

    if(sum != packetEPD.getSum()) {
      System.out.println("BUG! :" + SUM + " не совпадает с суммой Sum всех платежных поручений");
    }

    bicDirectoryEntries.stream().forEach(x -> {
      Optional<PacketEPD> p = packetEPDS.stream().filter(a -> a.getBic().equals(x.getBic())).findFirst();
      if(p.isPresent()) {
        if(p.get().isPayer()) {
          if (!x.getPtType().equals(p.get().getAccDocNo())) {
            System.out.println("BUG!: ED101 AccDocNo не совпадает c BICDirectoryEntry PtType");
          }
          if (!x.getDateIn().equals(p.get().getAccDocDate())) {
            System.out.println("BUG!: ED101 AccDocDate не совпадает c BICDirectoryEntry DateIn");
          }
          if (x.getRgn() != (p.get().getSum())) {
            System.out.println("BUG!: ED101 Sum не совпадает c BICDirectoryEntry Rgn");
          }
        }
        if (!x.getNameP().equals(p.get().getName())) {
          System.out.println("BUG!: ED101 Payer Name не совпадает c BICDirectoryEntry Payer NameP");
        }
        try {
          if (!x.getAccount().equals(p.get().getCorrespAcc().get())) {
            System.out.println("BUG!: ED101 Payer CorrespAcc не совпадает c BICDirectoryEntry Payer Account");
          }
        }
        catch (NullPointerException e) {}
      }
    });
  }

  static PacketEPD getFieldsValues(Document document) {
    PacketEPD packet = new PacketEPD();
    NamedNodeMap namedNodeMap = document.getDocumentElement().getAttributes();
    packet.setEdAuthor(Long.parseLong(namedNodeMap.getNamedItem(EDAUTHOR).getNodeValue()));
    packet.setEdDate(namedNodeMap.getNamedItem(EDDATE).getNodeValue());
    packet.setEdNo(Long.parseLong(namedNodeMap.getNamedItem(EDNO).getNodeValue()));

    if(!document.getDocumentElement().getNodeName().contains("807")) {
      packet.setEdQuantity(Long.parseLong(namedNodeMap.getNamedItem(EDQUANTITY).getNodeValue()));
      packet.setSystemCode(namedNodeMap.getNamedItem(SYSTEMCODE).getNodeValue());
      packet.setSum(Long.parseLong(namedNodeMap.getNamedItem(SUM).getNodeValue()));
    }
    return packet;
  }
}
