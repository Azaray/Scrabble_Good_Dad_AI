/*
 * Classname : BuildDict
 * 
 * Version information : 1
 *
 * Date :
 * 
 * Description : An editor to add words to  
 * the dictionary of the Scrabble game.
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

public class BuildDict extends 
             JFrame implements ActionListener 
{ JPanel boardPanel = new JPanel(); 
  JPanel rackPanel = new JPanel(); 
  JPanel resultsPanel = new JPanel(); 
  JButton playButton = new JButton("Add"); 
  JButton cancelButton = new JButton("Cancel"); 

  JButton[][] boardButtons = new JButton[15][3]; 
  JLabel resultLabel = new JLabel("       "); 

  List alphabet = new ArrayList();   
  List words = new ArrayList(); 
  String current = ""; 
  String newword = ""; 
  String[] russianLetter = { "a", "b", "v", "g", "d", "e", "j",
                             "z", "i", "y", "k", "l", "m", "n", 
                             "o", "p", "r", "s", "t", "u", "f", 
                             "x", ";", "h", "#", "%", ">", "q", "<", "-",
                             "w", "~", " ", ":" }; 

 
  public BuildDict()
  { boardPanel.setLayout(new GridLayout(3,15));  
    alphabet = (new RussianScrabbleBuilder()).getAlphabet(); 
    addSquares(); 
    getContentPane().add(boardPanel, BorderLayout.CENTER); 
    playButton.addActionListener(this); 
    cancelButton.addActionListener(this); 
    rackPanel.add(playButton); 
    rackPanel.add(cancelButton); 
    rackPanel.add(resultLabel); 
    getContentPane().add(rackPanel, BorderLayout.SOUTH); 
    
    updateDisplay(); 

    addWindowListener(new WindowAdapter() 
      { public void windowClosing(WindowEvent e) 
        { saveDataToFile(); 
          System.exit(0);
        }
      });    
  }
    

  private void addSquares()
  { for (int j = 0; j < 3; j++) 
    { for (int i = 0; i < 15; i++)
      { JButton jb = new JButton();
        jb.addActionListener(this); 
        boardButtons[i][j] = jb;
        jb.setBackground(Color.white); 
        if (15*j + i < alphabet.size())
        { jb.setText((String) alphabet.get(15*j + i)); } 
        boardPanel.add(jb);  
      }
    }
  }

  private void updateDisplay()
  { repaint(); }

  public void actionPerformed(ActionEvent e) 
  { Object eventSource = e.getSource(); 
    if (eventSource instanceof JButton)
    { JButton cb = (JButton) eventSource;
      String command = cb.getText();
      if (command != null)
      { if (command.equals("Add"))
        { words.add(newword); 
          current = "";
          newword = ""; 
          resultLabel.setText("      "); 
          return; 
        }
        if (command.equals("Cancel"))
        { updateDisplay();
          current = "";
          newword = ""; 
          resultLabel.setText("      "); 
          return;
        }
        else 
        { current = current + command; 
          resultLabel.setText(current);
          // int code = Character.digit(command,16); 
          newword = newword + // "\\u0" + toHex((int) command.charAt(0)); 
                              russianLetter[(int) command.charAt(0) - 1072];
          System.out.println(newword); 
        }
      } 
    }
  }

  private String convert(String chars)
  { String res = ""; 
    for (int i = 0; i < chars.length(); i++) 
    { char c = chars.charAt(i); 
      int code = Character.digit(c,16); 
      res = res + "\\u0" + code; 
    } 
    return res; 
  }   

  private String toHex(int i) 
  { String res = ""; 
    int mod = 0; 
    do 
    { mod = i % 16; 
      if (mod < 10) 
      { res = mod + res; } 
      else if (mod == 10)
      { res = "A" + res; } 
      else if (mod == 11)
      { res = "B" + res; } 
      else if (mod == 12) 
      { res = "C" + res; } 
      else if (mod == 13)
      { res = "D" + res; } 
      else if (mod == 14) 
      { res = "E" + res; } 
      else if (mod == 15)
      { res = "F" + res; } 
      i = i / 16; 
    } while (i > 0); 
    return res; 
  } 

  public void saveDataToFile()
  { File startingpoint = new File("output");
    JFileChooser fc = new JFileChooser();
    fc.setCurrentDirectory(startingpoint);
    fc.setDialogTitle("Save Dictionary");
    int returnVal = fc.showSaveDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION)
    { File file = fc.getSelectedFile();
      try
      { PrintWriter out =
          new PrintWriter(
            new BufferedWriter(new FileWriter(file)),true);
        for (int i = 0; i < words.size(); i++)
        { out.println(words.get(i)); } 
        out.close(); 
      }
      catch (Exception e) { System.err.println("Data not saved!"); } 
    }
  }
  
  public static void main(String[] args) 
  { BuildDict window = new BuildDict(); 
  
    window.setTitle("Dictionary Creator"); 
    window.setSize(800, 300);
    window.setVisible(true);   
  }
}