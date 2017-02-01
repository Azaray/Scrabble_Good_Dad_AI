
public class Letter // implements Comparable
{ private char symbol;
  private char ascii; 
  private int score;

  public Letter(char sym, int scre)
  { symbol = sym;
    ascii = sym; 
    score = scre;
  }

  public Letter(char sym, int scre, char asc)
  { this(sym,scre); 
    ascii = asc; 
  } 

  public boolean isBlank()
  { return symbol == ' '; }

  public int getScore()
  { return score; }

  public String toString()
  { return "" + symbol; }

  public String toAscii()
  { return "" + ascii; } 

  public void setSymbol(char sym) // for blanks
  { if (symbol == ' ')
    { symbol = sym; }
  } 

  public String getHtml()
  { return "<bold>" + symbol + "</bold><sub>" + score + "</sub>"; } 

  public Object clone()
  { return new Letter(symbol,score,ascii); } 

  public String toXml()
  { return "    <letter>\n" + 
      "      <symbol>" + symbol + "</symbol>\n" + 
      "      <score>" + score + "</score>\n" + 
      "    </letter>\n"; 
  }

  public boolean equals(Object obj) 
  { if (obj instanceof Letter)
    { Letter ll = (Letter) obj; 
      if (ll.symbol == symbol && 
          ll.getScore() == score) 
      { return true; }
      if (ll.getScore() == 0 && score == 0)
      { return true; } // But if there are 2 blanks on the rack ... 
    } 
    return false; 
  } 
}
