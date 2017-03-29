
import java.util.*; 

public abstract class ScrabbleBuilder
{ private static List premiumSquares = new ArrayList(); 
  protected List alphabet = new ArrayList(); // of String
  protected List letters = new ArrayList();  // of Letter
  protected Map letterMap = new HashMap();   // String --> Letter

  public List getLetters()
  { return letters; } 

  public Letter getLetter(char ascii)
  { String asc = "" + ascii; 
    return (Letter) letterMap.get(asc); 
  }  

  public List getAlphabet()
  { return alphabet; } 

  public static Square[][] getBoard()
  { Square[][] res = new Square[15][15];
    premiumSquares = new ArrayList(); 

    for (int i = 0; i < 15; i++)
    { for (int j = 0; j < 15; j++)
      { setOrdinarySquare(i,j,res); }
    }
    
    // set diagonals to dws:
    for (int i = 0; i < 15; i++)
    { setDoubleWS(i,i,res); 
      setDoubleWS(i,14-i,res);
    }
    
    setDoubleLS(3,7,res); 
    setDoubleLS(11,7,res); 
    setDoubleLS(3,0,res); 
    setDoubleLS(11,0,res); 
    setDoubleLS(6,2,res);
    setDoubleLS(8,2,res);
    setDoubleLS(0,3,res);
    setDoubleLS(7,3,res);
    setDoubleLS(14,3,res);
    setDoubleLS(2,6,res); 
    setDoubleLS(6,6,res); 
    setDoubleLS(8,6,res); 
    setDoubleLS(12,6,res); 
    setDoubleLS(2,8,res); 
    setDoubleLS(6,8,res); 
    setDoubleLS(8,8,res); 
    setDoubleLS(12,8,res); 
    setDoubleLS(3,14,res); 
    setDoubleLS(11,14,res); 
    setDoubleLS(6,12,res);
    setDoubleLS(8,12,res);
    setDoubleLS(0,11,res);
    setDoubleLS(7,11,res);
    setDoubleLS(14,11,res);

    for (int x = 1; x <= 13; x = x + 4)
    { setTripleLS(x,5,res); 
      setTripleLS(x,9,res); 
    } 
    for (int x = 5; x <= 9; x = x + 4)
    { setTripleLS(x,1,res); 
      setTripleLS(x,13,res); 
    }  

    setTripleWS(0,0,res);
    setTripleWS(7,0,res);
    setTripleWS(14,0,res);
    setTripleWS(0,7,res);
    setTripleWS(14,7,res);
    setTripleWS(0,14,res);
    setTripleWS(7,14,res);
    setTripleWS(14,14,res);
    return res; 
  }    

  private static void setTripleWS(int x, int y, Square[][] res)
  { TripleWordSquare tws = new TripleWordSquare(x,y);
    premiumSquares.add(0,tws); 
    res[x][y] = tws;
  }

  private static void setDoubleWS(int x, int y, Square[][] res)
  { DoubleWordSquare dws = new DoubleWordSquare(x,y);
    premiumSquares.add(0,dws); 
    res[x][y] = dws;
  }

  private static void setTripleLS(int x, int y, Square[][] res)
  { TripleLetterSquare tls = new TripleLetterSquare(x,y);
    premiumSquares.add(0,tls); 
    res[x][y] = tls;
  }

  private static void setDoubleLS(int x, int y, Square[][] res)
  { DoubleLetterSquare dls = new DoubleLetterSquare(x,y);
    premiumSquares.add(0,dls); 
    res[x][y] = dls;
  }

  private static void setOrdinarySquare(int x, int y, Square[][] res)
  { OrdinarySquare os = new OrdinarySquare(x,y);
    res[x][y] = os;
  }

  public static List getPremiumSquares()
  { return (List) ((ArrayList) premiumSquares).clone(); } 

  protected void addLetter(char sym, int s, int n)
  { // adds n copies of letter sym with score s
    alphabet.add("" + sym); 
    letterMap.put("" + sym,new Letter(sym,s)); 
    for (int i = 0; i < n; i++)
    { Letter l = new Letter(sym,s);
      letters.add(l); 
    }
  }

  protected void addLetter(char sym, char asc, int s, int n)
  { // adds n copies of letter sym with score s
    alphabet.add("" + sym); 
    letterMap.put("" + asc,new Letter(sym,s,asc)); 
    for (int i = 0; i < n; i++)
    { Letter l = new Letter(sym,s,asc);
      letters.add(l); 
    }
  }
}


class EnglishScrabbleBuilder 
    extends ScrabbleBuilder
{ private static int[] scores = {1, 3, 3, 2, 1, 4, 2, 4, 1,
                                 8, 5, 1, 3, 1, 1, 3, 10, 1,
                                 1, 1, 1, 4, 4, 8, 4, 10, 0}; 

  public EnglishScrabbleBuilder()
  { super();
    addLetter('a',1,9);
    addLetter('b',3,2);
    addLetter('c',3,2);
    addLetter('d',2,4);
    addLetter('e',1,12);
    addLetter('f',4,2);
    addLetter('g',2,3);
    addLetter('h',4,2);
    addLetter('i',1,9);
    addLetter('j',8,1);
    addLetter('k',5,1);
    addLetter('l',1,4);
    addLetter('m',3,2);
    addLetter('n',1,6);
    addLetter('o',1,8);
    addLetter('p',3,2);
    addLetter('q',10,1);
    addLetter('r',1,6);
    addLetter('s',1,4);
    addLetter('t',1,6);
    addLetter('u',1,4);
    addLetter('v',4,2);
    addLetter('w',4,2);
    addLetter('x',8,1);
    addLetter('y',4,2);
    addLetter('z',10,1);
    //addLetter(' ',0,2);
  }

  public static int getScore(char c)
  { if ((int) c < (int) 'a') 
    { return 0; }
    if ((int) c > (int) 'z')
    { return 0; } 
    return scores[(int) c - (int) 'a']; 
  } 
}