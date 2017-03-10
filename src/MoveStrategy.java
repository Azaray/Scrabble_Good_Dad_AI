import java.util.List; 
import java.util.ArrayList; 
import java.util.Set; 
import java.util.Iterator; 

public abstract class MoveStrategy
{ public abstract Move generateMove(Game g); 

  public boolean checkValid(String wd, List cc)
  { // That every letter in wd is in cc, with same multiplicity, or its a space
    for (int i = 0; i < wd.length(); i++) 
    { String lett = "" + wd.charAt(i);
      int ind = cc.indexOf(lett); 
      if (ind >= 0)
      { cc.remove(ind); } 
      else 
      { int ind2 = cc.indexOf(" "); 
        if (ind2 >= 0)
        { cc.remove(ind2); } 
        else 
        { return false; }
      } 
    }
    return true; 
  } 

  public String isValid(String wd, List cc)
  { // That every letter in wd is in cc, with same multiplicity, or its a space
    // Returns null if invalid, otherwise wd with space into upper case

    String res = ""; 

    for (int i = 0; i < wd.length(); i++) 
    { String lett = "" + wd.charAt(i);
      int ind = cc.indexOf(lett); 
      if (ind >= 0)
      { cc.remove(ind);
        res = res + lett;
      } 
      else 
      { int ind2 = cc.indexOf(" "); 
        if (ind2 >= 0)
        { cc.remove(ind2);
          res = res + lett.toUpperCase(); 
        } 
        else 
        { return null; }
      } 
    }
    return res; 
  } 
} 


class PremiumMoveStrategy extends MoveStrategy
{ public Move generateMove(Game g)
  { Rack r = g.getRack(); 
    Board b = g.getBoard(); 
    int rsize = r.rackSize(); 
    List premiums = new ArrayList(); 
    // premiums.addAll(ScrabbleBuilder.getPremiumSquares());
    // premiums.addAll(b.getRow(5));
    // premiums.addAll(b.getRow(9));
    premiums.addAll(b.getSquares());    
    // ArrayList deleted = new ArrayList(); 

    /* for (int i = 0; i < premiums.size(); i++) 
    { Square sq = (Square) premiums.get(i); 
      if (sq.isOccupied())
      { deleted.add(sq); } 
    } 
    premiums.removeAll(deleted); */  
    // for each premium square, find out range of squares
    // where rack letters could be placed. Expressed as a Word

    // System.out.println(premiums); 
    int psquares = premiums.size(); 

    Word[] vertranges = new Word[psquares]; 
    Word[] horizranges = new Word[psquares]; 

    for (int i = 0; i < premiums.size(); i++) 
    { Square prem = (Square) premiums.get(i); 
      vertranges[i] = findVerticalRange(prem,rsize,b);
      horizranges[i] = findHorizRange(prem,rsize,b);
      // if (vertranges[i] != null)
      // { System.out.println(vertranges[i] + " " + vertranges[i].getStartY() + 
      //                      " " + vertranges[i].getEndY() + " " + 
      //                      vertranges[i].getPremium());
      // }  
    } 
    

    List letters = new ArrayList(); 
    for (int i = 0; i < rsize; i++) 
    { letters.add("" + r.getLetter(i).toAscii()); }

    System.out.println("Rack letters are: " + letters); 
    if (letters.size() == 7)
    { System.out.println("All 7-letter words from these are: " + 
                         Dictionary.lookup(letters,7)); 
    }  


    letters.add(null); // dummy
    SublistIterator si = new SublistIterator(letters); 

    
    int maxscore = 0; 
    Word maxword = null; 
    // Move maxmove = null; 
    List maxletts = null; 

    do
    { ArrayList curr = (ArrayList) si.getCurrent(); 
      // System.out.println(curr);
      int x = curr.size(); // only interested if x > 2
      // if (x <= 2)
      // { si.advance();
      //   continue;
      // }

      ArrayList cc = (ArrayList) curr.clone(); 
      cc.remove(x-1); 
      for (int i = 0; i < premiums.size(); i++) 
      { Square p = (Square) premiums.get(i); 
        if (vertranges[i] != null)
        { Word range = vertranges[i]; 
          int prem = range.getPremium(); 
          // for (int disp = 7 - (x-1); 
          //         disp <= prem; disp++)
          { int disp = 0; 
            List allletts = range.allLetters(cc,disp);
            if (allletts != null && 
                allletts.size() > x)
            { // System.out.println("Range for " + range + " and " + cc + 
              //                    " is: " + allletts + " " + allletts.get(0) + 
              //                    " " + allletts.get(1));
              int newstarty = ((Integer) allletts.get(0)).intValue(); 
              allletts.remove(0); 
              int newendy = ((Integer) allletts.get(0)).intValue(); 
              allletts.remove(0); 
              int y = allletts.size(); 
              Set possible = Dictionary.lookup(allletts,y);
              // System.out.println("Possible: " + possible); 
              for (Iterator j = possible.iterator(); j.hasNext(); )
              { String wd = (String) j.next();
                ArrayList alllettsc = (ArrayList) ((ArrayList) allletts).clone(); 
                String wd2 = isValid(wd,alllettsc);
                if (wd2 != null) 
                { List newletts = range.fitsY(wd2,newstarty); 
                  if (newletts.size() > 0)
                  { Move mm = new Move(g.getPlayer(),newletts);
                    Board bb = (Board) b.clone(); 
                    if (bb.placeMove(mm,b))
                    { mm.findWords(bb); 
                      if (mm.inDictionary())
                      { int score = mm.getScore(b); 
                        if (score > maxscore)
                        { maxscore = score; 
                        // maxmove = mm;
                          maxletts = newletts; 
                          System.out.println(allletts + " " + possible + 
                                       " Word " + wd2 + 
                                       " fits into " + range +
                                       " from " + newstarty +  
                                       " With new: " + newletts + 
                                       " Score = " + maxscore);
                        }
                      } 
                    }
                  } 
                } 
              } 
            }
          }
        }
        if (horizranges[i] != null)
        { Word hrange = horizranges[i]; 
          int prem = hrange.getPremium(); 
          // for (int disp = 7 - (x-1); 
          //         disp <= prem; disp++)
          { int disp = 0; 
            List allletts = hrange.allLetters(cc,disp);
            if (allletts != null && 
                allletts.size() > x)
            { // System.out.println("Range for " + range + " and " + cc + 
              //                    " is: " + allletts + " " + allletts.get(0) + 
              //                    " " + allletts.get(1));
              int newstartx = ((Integer) allletts.get(0)).intValue(); 
              allletts.remove(0); 
              int newendx = ((Integer) allletts.get(0)).intValue(); 
              allletts.remove(0); 
              int y = allletts.size(); 
              Set possible = Dictionary.lookup(allletts,y);
              // System.out.println("Possible: " + possible); 
              for (Iterator j = possible.iterator(); j.hasNext(); )
              { String wd = (String) j.next();
                ArrayList alllettsc = (ArrayList) ((ArrayList) allletts).clone(); 
                String wd2 = isValid(wd,alllettsc); 
                if (wd2 != null)
                { List newletts = hrange.fitsX(wd2,newstartx); 
                  if (newletts.size() > 0)
                  { Move mm = new Move(g.getPlayer(),newletts);
                    Board bb = (Board) b.clone(); 
                    if (bb.placeMove(mm,b))
                    { mm.findWords(bb); 
                      if (mm.inDictionary())
                      { int score = mm.getScore(b); 
                        if (score > maxscore)
                        { maxscore = score; 
                        // maxmove = mm;
                          maxletts = newletts; 
                          System.out.println(allletts + " " + possible + 
                                       " Word " + wd + 
                                       " fits into " + hrange +
                                       " from " + newstartx +  
                                       " With new: " + newletts + 
                                       " maxscore = " + maxscore);
                        }
                      } 
                    }
                  } 
                } 
              } 
            }
          }        

        }
      }
      si.advance();
    } while (!si.atEnd());

    if (maxletts == null) 
    { return null; } 
    return new Move(g.getPlayer(),maxletts);
  }

  private Word findVerticalRange(Square prem, int rsize, Board b)
  { // includes rsize empty squares (prem is one) up and down
    // provided these reach to existing occupied square(s)
    int empty = 0; 
    int occupied = 0; 
    Letter star = new Letter('*',0); 

    int maxempty = 2*rsize + 1; 
    int x = prem.getX(); 
    int y = prem.getY(); 
    int j = y; 
    if (y == 0)
    { Word w = new Word(x,0,x,0); 
      while (j < 15 && empty < maxempty)
      { if (b.isOccupied(x,j))
        { occupied++; 
          w.addEnd(b.getSquare(x,j).getLetter()); 
        } 
        else 
        { empty++; 
          w.addEnd(star); 
        } 
        j++; 
      } 
      w.setEndY(j-1); 
      if (occupied == 0) // can't be connected to existing letters
      { return null; } 
      w.setPremium(0); 
      return w; 
    } 
    else 
    { Word w = new Word(x,y,x,y); 
      while (j >= 0 && empty < rsize) 
      { if (b.isOccupied(x,j))
        { w.addFront(b.getSquare(x,j).getLetter()); 
          occupied++; 
        } 
        else 
        { w.addFront(star); 
          empty++; 
        } 
        j--; 
      } 
      w.setStartY(j+1);
      int k = y+1;  
      while (k < 15 && empty < maxempty)
      { if (b.isOccupied(x,k))
        { occupied++; 
          w.addEnd(b.getSquare(x,k).getLetter()); 
        } 
        else 
        { empty++; 
          w.addEnd(star); 
        } 
        k++; 
      } 
      w.setEndY(k-1); 
      if (occupied == 0) // can't be connected to existing letters
      { return null; } 
      w.setPremium(y - (j+1));  
      return w; 
    }    
  }  

  private Word findHorizRange(Square prem, int rsize, Board b)
  { // includes rsize empty squares (prem is one) left and right
    // provided these reach to existing occupied square(s)
    int empty = 0; 
    int occupied = 0; 
    Letter star = new Letter('*',0); 

    int maxempty = 2*rsize + 1; 
    int x = prem.getX(); 
    int y = prem.getY(); 
    int j = x; 
    if (x == 0)
    { Word w = new Word(0,y,0,y); 
      while (j < 15 && empty < maxempty)
      { if (b.isOccupied(j,y))
        { occupied++; 
          w.addEnd(b.getSquare(j,y).getLetter()); 
        } 
        else 
        { empty++; 
          w.addEnd(star); 
        } 
        j++; 
      } 
      w.setEndX(j-1); 
      if (occupied == 0) // can't be connected to existing letters
      { return null; } 
      w.setPremium(0); 
      return w; 
    } 
    else 
    { Word w = new Word(x,y,x,y); 
      while (j >= 0 && empty < rsize) 
      { if (b.isOccupied(j,y))
        { w.addFront(b.getSquare(j,y).getLetter()); 
          occupied++; 
        } 
        else 
        { w.addFront(star); 
          empty++; 
        } 
        j--; 
      } 
      w.setStartX(j+1);
      int k = x+1;  
      while (k < 15 && empty < maxempty)
      { if (b.isOccupied(k,y))
        { occupied++; 
          w.addEnd(b.getSquare(k,y).getLetter()); 
        } 
        else 
        { empty++; 
          w.addEnd(star); 
        } 
        k++; 
      } 
      w.setEndX(k-1); 
      if (occupied == 0) // can't be connected to existing letters
      { return null; } 
      w.setPremium(x - (j+1));  
      return w; 
    }    
  }  
  
}

class CompositeMoveStrategy extends MoveStrategy
{ // try Premium first, then Suffix, etc
  public Move generateMove(Game g) {
    PremiumMoveStrategy pms = new PremiumMoveStrategy(); 
    Move res1 = pms.generateMove(g); 
    if (res1 == null)
    { Rack r = g.getRack(); 
      System.out.println(r.getSuffixes());
      System.out.println("Sorry, I give up, you try!"); 
    } 
    return res1; 
  } 
} 

/* also try to form 7-letter word and fit anywhere possible */ 