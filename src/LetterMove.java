
import java.util.*;
import javax.swing.*; 

public class LetterMove
{ private int x;
  private int y;
  private Letter letter;

  public LetterMove(int xx, int yy, Letter l)
  { x = xx;
    y = yy;
    letter = l;
  }

  public int getX()
  { return x; }

  public int getY()
  { return y; }

  public Letter getLetter()
  { return letter; }
 
  public List findWords(Board b)
  { List res = findHorizontalWords(b);
    res.addAll(findVerticalWords(b));
    return res;
  }

  public List findHorizontalWords(Board b)
  { List res = new ArrayList();
    // if no left-right adjacent letters:
    if (x == 0 || 
        !b.isOccupied(x-1,y))
    { if (x == 14 ||
          !b.isOccupied(x+1,y))
      { // System.out.println("Nothing to left or right of " + x +
        //                    " " + y);
        return res;
      }
      int i = x+1;
      Word w = new Word(x,y,0,y); // endx unknown
      w.addEnd(letter);
      // System.out.println("New word: " + w); 
      while (i <= 14 &&
             b.isOccupied(i,y))
      { w.addEnd(b.getSquare(i,y).getLetter());
        // System.out.println("Found word: " + w); 
        i++;
      }
      w.setEndX(i-1);
      res.add(w);
      return res;
    }
    int k = x-1;
    Word w1 = new Word(0,y,0,y); // xstart, xend?
    // System.out.println("New word: " + w1); 
    w1.addEnd(letter); 
    while (k >= 0 &&
           b.isOccupied(k,y))
    { w1.addFront(b.getSquare(k,y).getLetter());
      // System.out.println("Found word: " + w1); 
      k--;
    }
    w1.setStartX(k+1);
    int i = x+1;
    while (i <= 14 && 
           b.isOccupied(i,y))
    { w1.addEnd(b.getSquare(i,y).getLetter());
      // System.out.println("Found word: " + w1); 
      i++;
    }
    w1.setEndX(i-1);
    res.add(w1);
    return res;
  }

  public List findVerticalWords(Board b)
  { List res = new ArrayList();
    // if no up-down adjacent letters:
    if (y == 0 || 
        !b.isOccupied(x,y-1))
    { if (y == 14 ||
          !b.isOccupied(x,y+1))
      { // System.out.println("Nothing above, below " + x +
        //                    " " + y);
        return res;
      }
      int j = y+1;
      Word w = new Word(x,y,x,0); // endy unknown
      w.addEnd(letter);
      // System.out.println("New word: " + w); 
      while (j <= 14 &&
             b.isOccupied(x,j))
      { w.addEnd(b.getSquare(x,j).getLetter());
        // System.out.println("Found word: " + w); 
        j++;
      }
      w.setEndY(j-1);
      res.add(w);
      return res;
    }
    int k = y-1;
    Word w1 = new Word(x,0,x,0);
    // System.out.println("New word: " + w1); 
    w1.addEnd(letter); 
    while (k >= 0 &&
           b.isOccupied(x,k))
    { w1.addFront(b.getSquare(x,k).getLetter());
      // System.out.println("Found word: " + w1); 
      k--;
    }
    w1.setStartY(k+1);
    int j = y+1;
    while (j <= 14 && 
           b.isOccupied(x,j))
    { w1.addEnd(b.getSquare(x,j).getLetter());
      // System.out.println("Found word: " + w1); 
      j++;
    }
    w1.setEndY(j-1);
    res.add(w1);
    return res;
  }
          
  public static LetterMove makeLetterMove(Board b,
                                          Rack r)
  { String num = 
        JOptionPane.showInputDialog(
                      "Enter x-coord in range 1..15");
    int x = Integer.parseInt(num);
    if (x < 1 || x > 15)
    { System.out.println("Error, out of range");
      return null;
    }
    x--;
    num = 
        JOptionPane.showInputDialog(
                      "Enter y-coord in range 1..15");
    int y = Integer.parseInt(num);
    if (y < 1 || y > 15)
    { System.out.println("Error, out of range");
      return null;
    }
    y--;
    // if already occupied:
    Square sq = b.getSquare(x,y);
    Letter lt = sq.getLetter();
    if (lt != null)
    { System.out.println("Already occupied!");
      return null;
    }
    int rackSize = r.rackSize();
    num = 
        JOptionPane.showInputDialog(
                      "Select item from rack: 1.." +
                      rackSize);
    int n = Integer.parseInt(num);
    if (n < 1 || n > rackSize)
    { System.out.println("Error, out of range");
      return null;
    }
    n--;
    Letter l = r.getLetter(n);
    return new LetterMove(x,y,l);
  }

  public int getScore(Board b)
  { Square square = b.getSquare(x,y);
    return square.getLetterScore(letter);
  }

  public String toString()
  { return "letter move: " + letter + " At: " + x + " " + y; } 

  public String toXml()
  { String res = "  <lettermove>\n" + 
      "    <x>" + x + "</x>\n" + 
      "    <y>" + y + "</y>\n" + 
      letter.toXml() +
      "  </lettermove>\n"; 
    return res; 
  }
}  