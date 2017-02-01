import java.util.*;

public class Word
{ private int startx;
  private int starty; 
  private int endx;
  private int endy; 
  // startx == endx || starty == endy
  private List letters = new ArrayList(); // of Letter
  private int score = 0;

  private int premium = -1;  // index of premium square if any
  
  public Word(int x1,int y1,int x2,int y2)
  { startx = x1;
    starty = y1;
    endx = x2;
    endy = y2;
  }

  public Word(int x1,int y1,int x2,int y2, String s)
  { this(x1,y1,x2,y2); 
    for (int i = 0; i < s.length(); i++)
    { char c = s.toLowerCase().charAt(i); 
      Letter lett = (Letter) Scrabble.builder.getLetter(c).clone(); 
      letters.add(lett); 
    }
  }

  public void setLetters(List letts) 
  { letters = letts; } 

  public void setPremium(int prem)
  { premium = prem; } 

  public int getPremium() 
  { return premium; } 

  public int getStartX() { return startx; }

  public int getStartY() { return starty; }

  public int getEndX() { return endx; }

  public int getEndY() { return endy; }

  public Letter getLetter(int i)
  { return (Letter) letters.get(i); } 

  public int getLength()
  { if (startx == endx) 
    { return endy - starty + 1; } 
    else 
    { return endx - startx + 1; } 
  } 

  public boolean isVertical()
  { return startx == endx; } 

  public boolean isHorizontal()
  { return starty == endy; } 

  private Square getSquare(int i, Board b)
  { if (isVertical())
    { return b.getSquare(startx,starty+i); } 
    return b.getSquare(startx+i,starty); 
  }

  public int getScore(Board b)
  { int factor = 1; 
    for (int i = 0; i < letters.size(); i++) 
    { Letter l = (Letter) letters.get(i); 
      Square sq = getSquare(i,b); // square under i'th letter
      if (sq.isOccupied()) // already covered, by l
      { score += l.getScore(); } 
      else 
      { score += sq.getLetterScore(l); 
        factor *= sq.getWordScore(); 
      } 
    } 
    score = score*factor; 
    // System.out.println("Score for word " + letters + " is " + score); 
    return score; 
  } 

  public boolean inWord(LetterMove lm)
  { int x = lm.getX(); 
    int y = lm.getY(); 
    return (startx <= x && x <= endx &&
            starty <= y && y <= endy); 
  } // and letters are the same?  
    // lm.getLetter().equals(letters.get(x - startx + y - starty))

      
  public void addFront(Letter l)
  { letters.add(0,l); }

  public void addEnd(Letter l)
  { letters.add(l); }

  public void setStartX(int x)
  { startx = x; }

  public void setStartY(int y)
  { starty = y; }

  public void setEndX(int x)
  { endx = x; }

  public void setEndY(int y)
  { endy = y; }

  public String toString()
  { String res = ""; 
    for (int i = 0; i < letters.size(); i++)
    { res = res + ((Letter) letters.get(i)).toAscii(); }
    return res;
  }

  public boolean equals(Object obj)
  { if (obj instanceof Word)
    { Word wd = (Word) obj; 
      if (wd.toString().equals(toString()) &&
          wd.getStartX() == startx &&
          wd.getStartY() == starty &&
          wd.getEndX() == endx &&
          wd.getEndY() == endy)
      { return true; }
    }
    return false;
  }

  public List allLetters(List rackletters, int disp)
  { if (startx == endx) 
    { return allVerticalLetters(rackletters,disp); } 
    else 
    { return allHorizLetters(rackletters,disp); } 
  } 

  private List allVerticalLetters(List rletters, int disp)
  { if (disp + starty > endy) { return null; } 
    if (disp > premium) { return null; } 
    boolean found = false; 
    int rsize = rletters.size(); 
    // if (premium < 7 - rsize) { return null; } 
    // otherwise, start at 7 - rsize, end at prem. disp: (7 - rsize)..prem
    List res = new ArrayList(); 
    int p = 0; 
    int i = disp; 

    // add occupied squares before the start of line to be filled:  
    int k = i - 1; 
    for ( ; k >= 0; k--)
    { String st = "" + ((Letter) letters.get(k)).toAscii(); 
      if (st.equals("*")) { break; } 
      res.add(0,st); 
      found = true; 
    }
    while (i < endy - starty + 1 && p < rsize) 
    { String loc = "" + ((Letter) letters.get(i)).toAscii(); 
      if (loc.equals("*"))
      { res.add(rletters.get(p)); 
        p++; 
      } 
      else 
      { res.add(loc);
        found = true; 
      } 
      i++; 
    } 
    while (i < endy - starty + 1)  // p == rsize
    { String loc = "" + ((Letter) letters.get(i)).toAscii(); 
      if (loc.equals("*"))
      { break; } 
      else 
      { res.add(loc); 
        found = true; 
      } 
      i++; 
    }
    if (!found) { return null; } 

    res.add(0,new Integer(i-1)); 
    res.add(0,new Integer(k+1)); 
    return res; 
  }     

  private List allHorizLetters(List rletters, int disp)
  { if (disp + startx > endx) { return null; } 
    if (disp > premium) { return null; } 
    boolean found = false; 
    int rsize = rletters.size(); 
    // if (premium < 7 - rsize) { return null; } 
    // otherwise, start at 7 - rsize, end at prem. disp: (7 - rsize)..prem
    List res = new ArrayList(); 
    int p = 0; 
    int i = disp; 

    // add occupied squares before the start of line to be filled:  
    int k = i - 1; 
    for ( ; k >= 0; k--)
    { String st = "" + ((Letter) letters.get(k)).toAscii(); 
      if (st.equals("*")) { break; } 
      res.add(0,st); 
      found = true; 
    }
    while (i < endx - startx + 1 && p < rsize) 
    { String loc = "" + ((Letter) letters.get(i)).toAscii(); 
      if (loc.equals("*"))
      { res.add(rletters.get(p)); 
        p++; 
      } 
      else 
      { res.add(loc);
        found = true; 
      } 
      i++; 
    } 
    while (i < endx - startx + 1)  // p == rsize
    { String loc = "" + ((Letter) letters.get(i)).toAscii(); 
      if (loc.equals("*"))
      { break; } 
      else 
      { res.add(loc); 
        found = true; 
      } 
      i++; 
    }
    if (!found) { return null; } 

    res.add(0,new Integer(i-1)); 
    res.add(0,new Integer(k+1)); 
    return res; 
  }     

  public List fitsY(String letts, int disp) 
  { // checks if letts fits into range starting at disp. Returns new letters
    List res = new ArrayList(); 
    for (int i = disp; i < endy - starty + 1; i++) 
    { String loc = "" + ((Letter) letters.get(i)).toAscii(); 
      if (i < letts.length())
      { String lett = "" + letts.charAt(i); 
        if (loc.equals("*"))  // new letter to be placed in move
        { Letter ll = 
            (Letter) 
              Scrabble.builder.getLetter(lett.toLowerCase().charAt(0)).clone(); 
          res.add(new LetterMove(startx, i + starty, ll));
        } 
        else if (loc.equalsIgnoreCase(lett)) // existing letters, must be same
        { } 
        else 
        { return new ArrayList(); }  // doesn't fit
      }
    }
    return res; 
  } 

  public List fitsX(String letts, int disp) 
  { // checks if letts fits into range starting at disp. Returns new letters
    List res = new ArrayList(); 
    for (int i = disp; i < endx - startx + 1; i++) 
    { String loc = "" + ((Letter) letters.get(i)).toAscii(); 
      if (i < letts.length())
      { String lett = "" + letts.charAt(i); 
        if (loc.equals("*"))  // new letter to be placed in move
        { Letter ll = 
            (Letter) 
              Scrabble.builder.getLetter(lett.toLowerCase().charAt(0)).clone(); 
          res.add(new LetterMove(startx + i,starty, ll));
        } 
        else if (loc.equalsIgnoreCase(lett)) // existing letters, must be same
        { } 
        else 
        { return new ArrayList(); }  // doesn't fit
      }
    }
    return res; 
  } 

  public static void main(String[] args)
  { Word w = new Word(1,4,1,11,"***shco*");
    System.out.println(w.fitsY("slishcom",0)); 
  }  
       
}