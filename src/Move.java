import java.util.*;
import javax.swing.*; 
      
public class Move
{ private List words = new ArrayList(); // of Word
  // the new words formed by this move
  private List letterMoves = new ArrayList(); 
  // of LetterMove, the individual new tiles on board
  private int score = 0;  // total score of this move
  private Player p;       // player who moved

  public Move(Player pl)
  { p = pl; }

  public Move(Player pl, List moves)
  { letterMoves = moves;
    p = pl; 
  }

  public Move(Player pl, int scre, List moves)
  { this(pl,moves); 
    setScore(scre); 
  }

  public Move(Player pl, Word wd, Board b)
  { p = pl; 
    int x1 = wd.getStartX(); 
    int y1 = wd.getStartY(); 
    int x2 = wd.getEndX(); 
    int y2 = wd.getEndY(); 
    int len = 0; 
    if (x1 < x2)
    { len = x2 - x1 + 1; 
      for (int i = 0; i < len; i++) 
      { letterMoves.add(new LetterMove(x1+i,y1,wd.getLetter(i))); } 
    } 
    else if (y1 < y2)  
    { len = y2 - y1 + 1; 
      for (int i = 0; i < len; i++) 
      { letterMoves.add(new LetterMove(x1,y1+i,wd.getLetter(i))); } 
    } 
    // findWords(b); 
  }
    
  public void clearWords()
  { words = new ArrayList(); } 

  public void addLetterMove(LetterMove lm)
  { letterMoves.add(lm); }

  public void setScore(int x)
  { score = x; } 

  public List getLetterMoves()
  { return letterMoves; }

  public List getWords()
  { return words; }

  public List getLetters()
  { List res = new ArrayList(); 
    for (int i = 0; i < letterMoves.size(); i++) 
    { LetterMove lm = (LetterMove) letterMoves.get(i); 
      res.add(lm.getLetter()); 
    } 
    return res; 
  }   

  public static Move makeMove(Player pl, Board b)
  { Rack r = pl.getRack();
    System.out.println("Your rack is: " + r);
    System.out.println("Select locations on the board");
    System.out.println("to place letters from your rack.");
    System.out.println("All letters placed must be in");
    System.out.println("a single vertical/horizontal line.");
    List moves = new ArrayList();
    String finish = 
        JOptionPane.showInputDialog(
                      "Enter quit to finish move");
    while (finish != null && !finish.equals("quit"))
    { LetterMove lm = LetterMove.makeLetterMove(b,r);
      if (lm != null)
      { moves.add(lm); }
      finish = 
        JOptionPane.showInputDialog(
                      "Enter quit to finish move");
    }
    return new Move(pl,moves);
  }

  public boolean validateMove(int numb) // numb of moves
  { // all must be in line
    boolean res = true; 
    Set xcoords = new TreeSet();
    Set ycoords = new TreeSet();
    for (int i = 0; i < letterMoves.size(); i++)
    { LetterMove lm = (LetterMove) letterMoves.get(i);
      xcoords.add(new Integer(lm.getX()));
      ycoords.add(new Integer(lm.getY()));
      if (xcoords.size() > 1 &&
          ycoords.size() > 1)  // error
      { return false; }
    }
    if (numb == 1) // must contain 7,7 location
    { Integer seven = new Integer(7);
      if (xcoords.contains(seven) &&
          ycoords.contains(seven))
      { res = true; }
      else 
      { res = false; }
    }                                     
    for (int i = 0; i < words.size(); i++)
    { Word wd = (Word) words.get(i); 
      boolean res1 = Dictionary.lookup(wd); 
      System.out.println("Word " + wd + " in dictionary: " + res1);
      // res = res && res1;  
    } 
    return res;
  }

  public boolean inDictionary()
  { boolean res = true; 
    for (int i = 0; i < words.size(); i++)
    { Word wd = (Word) words.get(i); 
      boolean res1 = Dictionary.lookup(wd); 
      // System.out.println("Word " + wd + " in dictionary: " + res1);
      res = res && res1;  
    } 
    return res;
  }  

  public boolean inDictionary0()
  { boolean res = true; 
    for (int i = 0; i < words.size(); i++)
    { Word wd = (Word) words.get(i); 
      boolean res1 = Dictionary.lookup(wd); 
      System.out.println("Word " + wd + " in dictionary: " + res1);
      res = res && res1;  
    } 
    return res;
  }  

  public boolean findWords(Board b)
  { int lmsize = letterMoves.size();
    if (lmsize == 0)
    { words = new ArrayList();
      return false;
    } 
    LetterMove lm1 = (LetterMove) letterMoves.get(0);
    LetterMove lm2 = (LetterMove) letterMoves.get(lmsize-1);

    int x1 = lm1.getX();
    int x2 = lm2.getX();
    int y1 = lm1.getY();
    int y2 = lm2.getY();

    if (x1 == x2 && y1 == y2) // single letter move
    { words = lm1.findVerticalWords(b); 
      words.addAll(lm1.findHorizontalWords(b)); 
      return true; 
    } 

    if (x1 == x2) // vertical move
    { List allvert = lm1.findVerticalWords(b); 
      if (allvert.size() == 1)
      { words.addAll(allvert); }  // only one
      else 
      { return false; }
      Word vertword = (Word) allvert.get(0); 
  
      for (int i = 0; i < lmsize; i++)
      { LetterMove lm = (LetterMove) letterMoves.get(i);
        if (vertword.inWord(lm))
        { words.addAll(lm.findHorizontalWords(b)); }
        else 
        { System.out.println("Disconnected lettermove: " + lm); 
          return false; 
        } 
      }
      return true; 
    }

    if (y1 == y2)  // horizontal move
    { List allhoriz = lm1.findHorizontalWords(b);
      if (allhoriz.size() == 1)
      { words.addAll(allhoriz); }
      else 
      { return false; } 
      Word horizword = (Word) allhoriz.get(0); 

      for (int j = 0; j < lmsize; j++)
      { LetterMove lm = (LetterMove) letterMoves.get(j);
        if (horizword.inWord(lm))
        { words.addAll(lm.findVerticalWords(b)); }
        else 
        { System.out.println("Disconnected lettermove: " + lm); 
          return false; 
        }         
      }
    }
    return true; 
  }

  public int getScore(Board b)
  { int total = 0;
    for (int i = 0; i < words.size(); i++)
    { Word wd = (Word) words.get(i);
      total += wd.getScore(b);
    }
    if (letterMoves.size() == 7) // bonus
    { total += 50; }
    score = total;
    return total;
  }

  public String toString()
  { String res = "Move: player " + p + " score = " + score + "\n"; 
    for (int i = 0; i < letterMoves.size(); i++)
    { LetterMove lm = (LetterMove) letterMoves.get(i); 
      res = res + " letter move: " + lm + "\n"; 
    }
    return res;
  }

  public String toXml()
  { String res = "<move>\n  <score>" + score + "</score>\n"; 
    String playerXml = p.toXml(); 
    res = res + playerXml; 
    for (int i = 0; i < letterMoves.size(); i++)
    { LetterMove lm = (LetterMove) letterMoves.get(i); 
      res = res + lm.toXml(); 
    }
    return res + "</move>\n"; 
  }
}