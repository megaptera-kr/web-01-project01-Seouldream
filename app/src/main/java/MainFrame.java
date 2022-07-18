import models.*;
import panels.*;
import utils.*;

import javax.swing.*;
import java.awt.*;

import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class MainFrame extends JFrame {
  private Account account;
  private JPanel contentPanel;
  private List<Writing> writings = new ArrayList<>();

  MainFrame(Account account) throws FileNotFoundException {

   this.account = account;

    Fileloader fileloader = new Fileloader(writings);

    writings = fileloader.loadWritings();

    setMainFrameSetting();

    setMenuPanel(writings);

    contentPanel = new JPanel();
    this.add(contentPanel,BorderLayout.CENTER);
    this.setVisible(true);
   }

  private void setMainFrameSetting() {
    this.setSize(500,550);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.setLayout(new BorderLayout());
  }

  private void setMenuPanel(List<Writing> writings) {
    JPanel menuPanel = new JPanel();
    this.add(menuPanel,BorderLayout.PAGE_START);
    menuPanel.setLayout(new GridLayout(1,3));
    menuPanel.add(createDiaryBoardButton(writings));
    menuPanel.add(createPrivateDiaryBoard());
    menuPanel.add(createMessengerPanel());
  }

  private JButton createDiaryBoardButton(List<Writing> writings) {
    JButton button = new JButton("너의 일기가 보고싶어!");
    button.addActionListener(event -> {
      JPanel diaryBoardPanel = new DiaryBoardPanel(account);
      showContentPanel(diaryBoardPanel);
    });
    return button;}

  private JButton createPrivateDiaryBoard() {
    JButton button = new JButton("나만의 일기장");
    button.addActionListener(event -> {
      JPanel privateDiaryBoardPanel = new PivateDiaryBoardPanel();
      showContentPanel(privateDiaryBoardPanel);
    });
    return button;
  }

  private JButton createMessengerPanel() {
    JButton button = new JButton("메신저함");
    button.addActionListener(event -> {
      JPanel messengerPanel = new MessengerPanel();
    });
    return button;
  }

  public void showContentPanel(JPanel panel) {
    contentPanel.removeAll();
    contentPanel.add(panel);
    contentPanel.setVisible(false);
    contentPanel.setVisible(true);
    this.setVisible(true);
  }
  private void saveDiary() {

    this.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent event) {
        Fileloader fileloader = new Fileloader(writings);
        try{
          fileloader.diaryWriter(writings);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    });
  }
}
