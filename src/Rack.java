import java.util.*;

public class Rack
{ private List letters = new ArrayList(); // of Letter
      
  public void addLetters(List l)
  { letters.addAll(l); }  // highest scorers first

  public void removeLetters(List l)
  { for (int i = 0; i < l.size(); i++) 
    { Letter lett = (Letter) l.get(i);
      int ind = letters.indexOf(lett); // using Equals on Letter
      if (ind >= 0)
      { letters.remove(ind); } 
    }
  }

  public int rackSize()
  { return letters.size(); }

  public Letter getLetter(int i)
  { return (Letter) letters.get(i); }

  public String toString()
  { return letters.toString(); }

  public List getSuffixes()
  { List res = new ArrayList(); 
    boolean e = false; 
    boolean d = false; 
    for (int i = 0; i < letters.size(); i++)
    { Letter lett = (Letter) letters.get(i); 
      if ("e".equals("" + lett))
      { e = true; } 
      else if ("d".equals("" + lett))
      { d = true; }
      else if ("s".equals("" + lett))
      { res.add("s"); } 
    } 
    if (e && d) 
    { res.add("ed"); } 
    return res; 
  } 
}