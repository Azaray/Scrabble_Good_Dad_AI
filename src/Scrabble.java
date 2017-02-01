/*
 * Classname : Scrabble
 * 
 * Version information : 1
 *
 * Date :
 * 
 * Description : This describes the GUI interface of 
 * the Scrabble game,
 * which includes a simple window, 
 * the menu options and their action 
 * listeners.
 */

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector; 
import java.util.List; 
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap; 
import java.util.StringTokenizer;
import java.io.*; 

public class Scrabble extends 
             JFrame implements ActionListener 
{ JPanel boardPanel = new JPanel(); 
  JPanel rackPanel = new JPanel(); 
  JPanel resultsPanel = new JPanel(); 
  JButton playButton = new JButton("Move"); 
  JButton cancelButton = new JButton("Cancel"); 
  JButton endButton = new JButton("End move");
  JButton addPlayer = new JButton("Add player"); 

  JButton[][] boardButtons = new JButton[15][15]; 
  JButton[] rackButtons = new JButton[7]; 
  Map xcoord = new HashMap();  // JButton --> Integer
  Map ycoord = new HashMap();  // JButton --> Integer
  JLabel[] resultLabels = new JLabel[5]; 
  JLabel bagLabel; 
  JLabel playerLabel = new JLabel("Player"); 

  ImageIcon osIcon = new ImageIcon("os.bmp","Ordinary Square");
  ImageIcon dwsIcon = new ImageIcon("dws.gif","double word square"); 
  ImageIcon aIcon = new ImageIcon("a.gif",""); 

  Game g; 
  int lastRack = -1; // current != null  =>  lastRack: 0..7 
  Letter current = null; 
  Move currentMove; 
  int numberOfPlayers = 0;
  List history = new ArrayList(); // of Move
  static ScrabbleBuilder builder; 

  public Scrabble(String lang) {
	
	builder = new EnglishScrabbleBuilder();

    g = new Game(builder);

    numberOfPlayers = 1; 
    boardPanel.setLayout(new GridLayout(15,15)); 
    resultsPanel.setLayout(new GridLayout(6,1)); 
    addSquares(); 
    getContentPane().add(boardPanel, BorderLayout.CENTER); 
    addRack(); 
    getContentPane().add(rackPanel, BorderLayout.SOUTH); 
    addResults(); 
    getContentPane().add(resultsPanel, BorderLayout.EAST); 
    updateDisplay(); 
    Dictionary.loadFromFile(lang); 
//    System.out.println(dictionary.toString()); 

    addWindowListener(new WindowAdapter() 
      { public void windowClosing(WindowEvent e) 
        { saveDataToFile(); 
          System.exit(0);
        }
      });    
  }
    

  private void addSquares()
  { for (int j = 0; j < 15; j++) 
    { for (int i = 0; i < 15; i++)
      { JButton jb = new JButton();
        xcoord.put(jb,new Integer(i)); 
        ycoord.put(jb,new Integer(j));  
        jb.addActionListener(this); 
        boardButtons[i][j] = jb;
        jb.setBackground(g.getSquare(i,j).getColor()); 
        boardPanel.add(jb);  
      }
    }
  }

  private void addRack()
  { JPanel rPanel1 = new JPanel(); 
    JPanel rPanel2 = new JPanel(); 
    rPanel2.setLayout(new GridLayout(1,7)); 
    rPanel1.add(playButton);
    rPanel1.add(cancelButton); 
    rPanel1.add(endButton); 
    rPanel1.add(playerLabel); 
    playButton.addActionListener(this);
    cancelButton.addActionListener(this);
    endButton.addActionListener(this); 
 
    for (int i = 0; i < 7; i++) 
    { // CoordinateButton jb = new CoordinateButton(i,-1); 
      JButton jb = new JButton("-"); 
      xcoord.put(jb,new Integer(i)); 
      jb.addActionListener(this);  
      rackButtons[i] = jb; 
      rPanel2.add(jb);
      jb.setEnabled(false); 
    }
    rackPanel.add(rPanel1); 
    rackPanel.add(rPanel2); 
    // rackPanel.setForegroundColour(Color.green); 
  }

  private void addResults()
  { JPanel buttPanel = new JPanel();
    buttPanel.add(addPlayer);  
    addPlayer.addActionListener(this); 
    resultsPanel.add(buttPanel); 

    for (int i = 0; i < 4; i++) 
    { JLabel jb = new JLabel("Player " + (i+1) + " score");
      resultLabels[i] = jb; 
      resultsPanel.add(jb); 
    } 
    bagLabel = new JLabel("Size of bag = " + g.getBagSize()); 
    resultLabels[4] = bagLabel; 
    resultsPanel.add(bagLabel); 
  }  

  private void updateDisplay()
  { bagLabel.setText("Size of bag = " + g.getBagSize());
    for (int i = 0; i < 15; i++) 
    { for (int j = 0; j < 15; j++) 
      { Square sq = g.getSquare(i,j); 
        JButton jb = boardButtons[i][j]; 
        jb.setText(sq.getText());
        if (sq.isOccupied())
        { jb.setEnabled(false); 
          jb.setBackground(Color.white); 
        }
        else 
        { jb.setText(null);
          jb.setBackground(sq.getColor());
          jb.setEnabled(true);
        }
      } 
    }
    Rack r = g.getRack();
    updateRack(r); 
    updatePlayers(); 
    playerLabel.setText("Player " + g.getPlayer().getName()); 
  }

  private void updateRack(Rack r)
  { int i;
    for (i = 0; i < r.rackSize(); i++)
    { Letter lt = r.getLetter(i); 
      rackButtons[i].setText("" + lt);
      rackButtons[i].setToolTipText("score = " + lt.getScore()); 
      rackButtons[i].setEnabled(true);
    }
    for (; i < 7; i++)
    { Character c = new Character('\u0644'); 
	    System.out.println(Character.isDefined('\u0644')); 
	    rackButtons[i].setText("" + c);
      rackButtons[i].setEnabled(false);
    }
  }

  private void updatePlayers()
  { List players = g.getPlayers(); 
    int numPlayers = players.size();
    if (numPlayers > 4)
    { numPlayers = 4; }  
    for (int i = 0; i < numPlayers; i++)
    { Player p = (Player) players.get(i); 
      resultLabels[i].setText("Player " + p.getName() + " score: " + 
                              p.getScore()); 
    }
  }

  private void clearRack()
  { for (int i = 0; i < 7; i++)
    { rackButtons[i].setText("-");
      rackButtons[i].setEnabled(false);
    }
  }

  public void actionPerformed(ActionEvent e) 
  { Object eventSource = e.getSource(); 
    if (eventSource instanceof JButton)
    { JButton cb = (JButton) eventSource;
      String command = cb.getText();
      if (command != null)
      {
        if (command.equals("Add player"))
        { String name = 
            JOptionPane.showInputDialog("Enter new player name"); 
          String corh = 
            JOptionPane.showInputDialog("Computer or Human (C or H)"); 
          g.addPlayer(name,corh);
          numberOfPlayers++;  
          if (numberOfPlayers >= 4)
          { cb.setEnabled(false); } 
          updatePlayers(); 
          return; 
        }
        if (command.equals("Move"))
        { g.startMove();
          Player pl = g.getPlayer(); 
          currentMove = new Move(pl);  
          // g.display();
          updateDisplay();
          addPlayer.setEnabled(false); // really when startGame is pressed
          if (pl instanceof ComputerPlayer) 
          { Move mv = null; 
            if (history.size() == 0)
            { FirstMoveStrategy ss = new FirstMoveStrategy(); 
              mv = ss.generateMove(g); 
            }
            else 
            { CompositeMoveStrategy pms = new CompositeMoveStrategy(); 
              mv = pms.generateMove(g); 
            }
            if (mv != null)
            { g.endMove(mv);
              history.add(mv);  
              updateDisplay(); 
              currentMove = null;
              clearRack();
            }
          }
          return; 
        }
        if (command.equals("Cancel"))
        { updateDisplay();
          currentMove = new Move(g.getPlayer());
          return;
        }
        if (command.equals("End move"))
        { g.endMove(currentMove);
          history.add(currentMove); 
          updateDisplay(); 
          currentMove = null;
          clearRack();
          // System.out.println(toXml()); 
          return;
        }
      }
      if (ycoord.get(cb) != null) // board
      { if (current == null || currentMove == null) // nothing to place, ignore
        { System.err.println("No selection from rack"); } 
        else 
        { int x = ((Integer) xcoord.get(cb)).intValue(); 
          int y = ((Integer) ycoord.get(cb)).intValue(); 
          // g.placeLetter(current,x,y);
          if (current.isBlank())
          { String resp = 
              JOptionPane.showInputDialog("Enter letter for blank"); 
          
            if (resp != null && resp.length() > 0)
            { current.setSymbol(resp.charAt(0)); } 
          }
          LetterMove lm = new LetterMove(x,y,current); 
          currentMove.addLetterMove(lm);  
          boardButtons[x][y].setText(current + ""); 
          boardButtons[x][y].setEnabled(false); 
          rackButtons[lastRack].setEnabled(false); 
          current = null; 
          lastRack = -1; 
        } 
      }
      else if (xcoord.get(cb) != null)  // rack
      { Integer coord = (Integer) xcoord.get(cb); 
        int x = coord.intValue(); 
        current = g.getRackLetter(x); 
        lastRack = x; 
      }
    }
  }

  public String toXml()
  { String res = "<history>\n";
    for (int i = 0; i < history.size(); i++)
    { Move m = (Move) history.get(i);
      res = res + m.toXml();
    }
    return res + "</history>\n";
  }

  public void saveDataToFile()
  { File startingpoint = new File("output");
    JFileChooser fc = new JFileChooser();
    fc.setCurrentDirectory(startingpoint);
    fc.setDialogTitle("Save History Data");
    int returnVal = fc.showSaveDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION)
    { File file = fc.getSelectedFile();
      try
      { PrintWriter out =
          new PrintWriter(
            new BufferedWriter(new FileWriter(file)));
        out.println(toXml()); 
        out.close(); 
      }
      catch (Exception e) { System.err.println("Data not saved!"); } 
    }
  }
  
  public static void main(String[] args) 
  { Scrabble window; 
    if (args == null || args.length == 0)
    { window = new Scrabble("eng"); }
    else 
    { window = new Scrabble(args[0]); } 
  
    window.setTitle("Scrabble Game"); 
    window.setSize(1000, 800);
    window.setVisible(true);   
  }
}