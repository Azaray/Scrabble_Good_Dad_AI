
import java.util.List; 
import java.util.ArrayList; 
import java.util.Set; 
import java.util.Iterator; 

public class FirstMoveStrategy extends MoveStrategy
{ public Move generateMove(Game g)
  { Board b = g.getBoard(); 
    Rack r = g.getRack(); 
    int rsize = r.rackSize(); 
    List letters = new ArrayList(); 
    for (int i = 0; i < rsize; i++) 
    { letters.add("" + r.getLetter(i).toAscii()); }
    letters.add(null); // dummy
    SublistIterator si = new SublistIterator(letters); 
    
    int maxscore = 0; 
    Word maxword = null; 

    do
    { ArrayList curr = (ArrayList) si.getCurrent(); 
      System.out.println(curr);
      int x = curr.size(); // only interested if x > 2
      // if (x <= 2)
      // { si.advance();
      //   continue;
      // }

      ArrayList cc = (ArrayList) curr.clone(); 
      cc.remove(x-1); 
      Set possible = Dictionary.lookup(cc,x-1); // perms of cc

      System.out.println(possible); 
      for (Iterator j = possible.iterator(); j.hasNext(); )
      { String wd = (String) j.next(); // it has length x-1

        ArrayList cc1 = (ArrayList) cc.clone(); 
        String wd2 = isValid(wd,cc1); 
        if (wd2 != null)
        { Word word = new Word(7,7,7 + x - 2,7,wd2);
          int score = word.getScore(b); 
          if (score > maxscore)
          { maxscore = score; 
            maxword = word;
          }
        } 
      } 
      si.advance();
    } while (!si.atEnd());

    if (maxword != null) 
    { Move mv = new Move(g.getPlayer(),maxword,b);
      return mv; 
    }          
    return null; 
  }             


  public static void main(String[] args)
  { String wd = "idealiie"; 
    ArrayList cc = new ArrayList(); 
    cc.add("i"); 
    cc.add("d"); 
    cc.add("i"); 
    cc.add("i"); 
    cc.add("e"); 
    cc.add("a");
    cc.add("e"); 
    cc.add("l");  
    FirstMoveStrategy fms = new FirstMoveStrategy(); 
    System.out.println(fms.checkValid(wd,cc));
  }  
       
  
}