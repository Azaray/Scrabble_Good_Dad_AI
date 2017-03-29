
import java.util.List; 
import java.util.ArrayList; 
import java.util.Set; 
import java.util.Iterator; 
import java.util.Vector;

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

class KillerMoveStrategy extends MoveStrategy {
	
	DeserialDAWG dawgs = new DeserialDAWG();
	Game game;
	Board b;
	Rack r;
	Vector<AnchorSquare> anchSquares;
	Vector<AnchorSquare> tempSquares;
	Vector<AnchorSquare> dontUseSquares;
	
	int tempWordTotal;
	int maxScore; 
    List maxLetters;
	
    int INFINITY = 10;
	int preventInfinity = 0;
	int preventChkbrdInfinity = 0;
	int totalScore = 0;		
	
	@Override
	public Move generateMove(Game g) {
		
		game = g;
		r = game.getRack(); 
	    b = game.getBoard();
	    maxScore = 0; 
	    maxLetters = null;
		determineAnchorSquares(b);
		tempSquares = new Vector<AnchorSquare>(anchSquares);
		
		int size = anchSquares.size();
		String myString = new String();
		
		String tempLargWord = new String();
		int tempLargScore = -1;
		AnchorSquare tempAnch = new AnchorSquare();
		AnchorSquare tempSquare = new AnchorSquare();
		
		String tempLargWord2 = new String();
		int tempLargScore2;
		AnchorSquare tempAnch2 = new AnchorSquare();
		
//		if(dontUseSquares.size() > 0)
//			for(int q = 0; q < dontUseSquares.size(); q++)
//				anchSquares.remove(dontUseSquares.get(q));
		
		int limit;
		AnchorSquare temp_7 = new AnchorSquare();
		
		for(int i = 0; i < size; i++) {			
			limit = findLimit(anchSquares.get(i), b);
			temp_7.setX(anchSquares.get(i).getX());
			temp_7.setY(anchSquares.get(i).getY() + 1);
			tempWordTotal = 0;
			for(int j = 0; j < r.uniqueLetters().size(); j++){
				Character dawgKey = r.uniqueLetters().get(j).getChar();
				dawgKey = Character.toUpperCase(dawgKey);
				LeftPart(myString, ((DAWG) dawgs.getDawgs().get(dawgKey)).getRoot(), limit, temp_7, temp_7, dawgKey, false);
			}
		}
		
		for(int n = 0; n < size; n++) {
			
			limit = findLimit(anchSquares.get(n), b);
			temp_7.setX(anchSquares.get(n).getX());
			temp_7.setY(anchSquares.get(n).getY() + 1);
			tempWordTotal = 0;
			if(temp_7.getY() != 0 || temp_7.getY() != 14){
				if(b.isEmpty(temp_7.getX(), temp_7.getY() + 1) && b.isEmpty(temp_7.getX(), temp_7.getY() - 1)){
					for(int j = 0; j < r.uniqueLetters().size(); j++){
						Character dawgKey = r.uniqueLetters().get(j).getChar();
						Character.toUpperCase(dawgKey);
						LeftPart(myString, ((DAWG) dawgs.getDawgs().get(dawgKey)).getRoot(), limit, temp_7, temp_7, dawgKey, false);
					}
				}
			}
		}
		
//		b.transposeBoard();
//		
//		determineAnchorSquares(b);
//		size = anchSquares.size();
//		myString = new String();
//		
////		if(dontUseSquares.size() > 0)
////			for(int q = 0; q < dontUseSquares.size(); q++)
////				anchSquares.remove(dontUseSquares.get(q));
//		
//		
//		for(int m = 0; m < size; m++) {
//		
//			limit = findLimit(anchSquares.get(m), b);
//			temp_7.setX(anchSquares.get(m).getX());
//			temp_7.setY(anchSquares.get(m).getY());
//			tempWordTotal = 0;
//		
//			for(int j = 0; j < r.uniqueLetters().size(); j++){
//				Character dawgKey = r.uniqueLetters().get(j).getChar();
//				Character.toUpperCase(dawgKey);
//				LeftPart(myString, ((DAWG) dawgs.getDawgs().get(dawgKey)).getRoot(), limit, temp_7, temp_7, dawgKey, true);
//			}
//		}
//		
//		for(int n = 0; n < size; n++) {
//		
//			limit = findLimit(anchSquares.get(n), b);
//			temp_7.setX(anchSquares.get(n).getX());
//			temp_7.setY(anchSquares.get(n).getY() + 1);
//			tempWordTotal = 0;
//				
//			if(temp_7.getY() != 0 || temp_7.getY() != 14){
//				if(b.isEmpty(temp_7.getX(), temp_7.getY() + 1) && b.isEmpty(temp_7.getX(), temp_7.getY() - 1)){
//					for(int j = 0; j < r.uniqueLetters().size(); j++){
//						Character dawgKey = r.uniqueLetters().get(j).getChar();
//						Character.toUpperCase(dawgKey);
//						LeftPart(myString, ((DAWG) dawgs.getDawgs().get(dawgKey)).getRoot(), limit, temp_7, temp_7, dawgKey, true);
//					}
//				}
//			}
//		}
		
		myString = new String();
		return new Move(g.getPlayer(),maxLetters);
		
	}
	
	public void determineAnchorSquares(Board b) {
		
		anchSquares = new Vector<AnchorSquare>();
		
		if(anchSquares.size() > 0){
			System.out.println(anchSquares.size());
			anchSquares.clear();
		}
				
		for(int x = 0; x < 15; x++) {			
			for(int y = 0; y < 15; y++) {				
				if(!b.isEmpty(x, y) && y != 0) {
					anchSquares.addElement(new AnchorSquare(x, y-1));				
				}
			}
		}
		for(int x = 0; x < 15; x++) {				
			if(!b.isEmpty(x, 14)) {
				anchSquares.addElement(new AnchorSquare(x, 14));				
			}
		}
	}	


	public int findLimit(AnchorSquare aSquare, Board b) {
		int limit = 0;
		int x = aSquare.getX();
		int y = aSquare.getY();
		
		if(x == 0){
			return 0;
		}	
			while (b.isEmpty(x,y)){
				limit = limit + 1;
				x = x - 1;
				if(x == 0 && b.isEmpty(x,y)){
					return limit + 1;
					
				}
		
			}	
			return limit;
	}
	
	public void LeftPart(String argString, DAWG_Node dNode, int limit, 
			 AnchorSquare anchorSquare, AnchorSquare startWord, Character dawgKey, boolean trans) {
			 
		ExtendRight(argString, dNode, anchorSquare, anchorSquare, startWord, dawgKey, trans);
		DAWG_Node new_node = new DAWG_Node(new Vector<Node>());
		AnchorSquare next_square = new AnchorSquare();

		if(limit > 0 && startWord.getX() != 0) {

			Set<Character> childNodes = dNode.getChildren().keySet();

			for(Character one : childNodes) {
				for(int i=0; i < r.rackSize(); i++){
					if(one.equals(r.getLetter(i).getChar())) {		
						Letter current = r.getRack().remove(i);
						new_node = dNode.getChild(one);
						startWord.setX(startWord.getX() - 1);
						startWord.setY(startWord.getY());
						tempWordTotal = 0;
						LeftPart((one + argString), new_node, limit-1, anchorSquare, startWord, dawgKey, trans);
						r.getRack().add(current);
					}
				}
			}
		}
	}
	
	public void ExtendRight(String argString, DAWG_Node dNode, AnchorSquare anchorSquare, 
		    AnchorSquare newSquare, AnchorSquare startWord, Character dawgKey, boolean trans) {

		DAWG_Node new_node = new DAWG_Node(new Vector<Node>());
		AnchorSquare next_square = new AnchorSquare();
		System.out.println("X: " + newSquare.getX());
		System.out.println("Y: " + newSquare.getY());
		if(b.isEmpty(newSquare.getX(), newSquare.getY())) {
System.out.println("Contains word: " + ((DAWG) dawgs.getDawgs().get(dawgKey)).containsWord(argString));
System.out.println("Dawg is Word: " + dNode.isWord());
System.out.println(argString);
			if(((DAWG) dawgs.getDawgs().get(dawgKey)).containsWord(argString) && dNode.isWord()) {

				if( ((r.spaceLeft()) < argString.length()) 
						&& (r.rackSize() < 7) && (argString.length() != 1)) {
					
					Word currentWord = new Word(startWord.getX(), startWord.getY(), newSquare.getX(), newSquare.getY(), argString);
					List wordLetters = currentWord.fitsX(argString,startWord.getX());
					Move mm = new Move(game.getPlayer(),wordLetters);
                    Board bb = (Board) b.clone(); 
                    if (bb.placeMove(mm,b)){
                     mm.findWords(bb); 
                       int score = mm.getScore(b); 
                        if (score > maxScore){
                         maxScore = score; 
                        // maxmove = mm;
                          maxLetters = wordLetters;
                        }
                      
                    }
				}
			}

			Set<Character> childNodes = dNode.getChildren().keySet();
			if(newSquare.getX()!= 14){
				for(Character one : childNodes) {
					for(int i=0; i < r.rackSize(); i++){
						if(one.equals(r.getLetter(i).getChar())) {
	
						Letter current = r.getRack().remove(i);
						new_node = dNode.getChild(one);
						next_square = new AnchorSquare(newSquare.getX() + 1, 
									   newSquare.getY());
									   
						ExtendRight(argString + one, new_node, anchorSquare, 
								next_square, startWord, dawgKey, trans);
						r.getRack().add(current);
						}
					}
				}
			}
		}
		else {
			Character letter = b.getSquare(newSquare.getX(), newSquare.getY()).getLetter().getChar();

			if(dNode.getChild(letter) != null) {
				next_square = new AnchorSquare(newSquare.getX() + 1, newSquare.getY());
				ExtendRight(argString + letter, dNode.getChild(letter), anchorSquare, 
						next_square, startWord, dawgKey, trans);
			}		
		}
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

    letters.add(null); // dummy
    SublistIterator si = new SublistIterator(letters); 

    
    int maxscore = 0; 
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