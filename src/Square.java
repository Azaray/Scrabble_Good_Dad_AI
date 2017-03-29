
import javax.swing.*;

import java.awt.*;

public abstract class Square
{ protected Letter letter = null;
  protected int x; 
  protected int y; 

  public Square() { }

  public Square(int xx, int yy)
  { x = xx; 
    y = yy; 
  } 

  public Square(Letter l)
  { letter = l; } 

  public void setLetter(Letter l)
  { letter = l; }

  public void setX(int xx) 
  { x = xx; } 

  public void setY(int yy) 
  { y = yy; } 

  public int getX()
  { return x; } 

  public int getY()
  { return y; }

  public void clear()
  { letter = null; } 

  public Letter getLetter()
  { return letter; }

  public boolean isOccupied()
  { return letter != null; } 

  // hook method:
  public abstract int getLetterScore(Letter l);
  
  // template method:
  public int getLetterScore()
  { if (letter == null)
    { return 0; }
    return getLetterScore(letter);
  }

  public abstract int getWordScore(); 

  // hook method:
  public abstract String squareSymbol();
  
  // template method:
  public String toString()
  { String res = "[";
    if (letter != null)
    { res = res + letter + "]"; }
    else 
    { res = res + squareSymbol() + "]"; }
    return res;
  }

  public String getText()
  { if (letter != null)
    { return letter + ""; }
    return squareSymbol();
  }

  public abstract Color getColor();

  public abstract Object clone(); 
}
class AnchorSquare extends Square { 
	public AnchorSquare(Letter l) {
}
   

  public AnchorSquare()
  { super(); }

  public AnchorSquare(int xx, int yy) {
   super(xx,yy); 
   }

@Override
public int getLetterScore(Letter l) {
	// TODO Auto-generated method stub
	return 0;
}

@Override
public int getWordScore() {
	// TODO Auto-generated method stub
	return 0;
}

@Override
public String squareSymbol() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Color getColor() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Object clone() {
	// TODO Auto-generated method stub
	return null;
} 
  }
  
  
class OrdinarySquare extends Square {
  private Color darkGreen = new Color(0, 102, 0);  

  public OrdinarySquare(Letter l) {
   super(l); } 

  public OrdinarySquare()
  { super(); }

  public OrdinarySquare(int xx, int yy)
  { super(xx,yy); } 

  public Object clone()
  { Square res = new OrdinarySquare(letter); 
    res.setX(x); 
    res.setY(y); 
    return res; 
  } 

  public int getLetterScore(Letter l)
  { return l.getScore(); }

  public int getWordScore()
  { return 1; } 

  public String squareSymbol()
  { return "-"; }

  public Color getColor()
  { return darkGreen; } 
}


class DoubleLetterSquare extends Square
{ private Color lightblue = new Color(0.4f,0.6f,1.0f); 

  public DoubleLetterSquare()
  { super(); }

  public DoubleLetterSquare(int xx, int yy)
  { super(xx,yy); } 

  public DoubleLetterSquare(Letter l)
  { super(l); } 

  public Object clone()
  { Square res = new DoubleLetterSquare(letter); 
    res.setX(x); 
    res.setY(y); 
    return res; 
  } 

  public int getLetterScore(Letter l)
  { return 2*l.getScore(); }

  public int getWordScore()
  { return 1; } 

  public String squareSymbol()
  { return "~"; }

  public Color getColor()
  { return lightblue; } 
}


class TripleLetterSquare extends Square
{ public TripleLetterSquare()
  { super(); }

  public TripleLetterSquare(Letter l)
  { super(l); }

  public TripleLetterSquare(int xx, int yy)
  { super(xx,yy); } 

  public Object clone()
  { Square res = new TripleLetterSquare(letter); 
    res.setX(x); 
    res.setY(y); 
    return res; 
  } 

  public int getLetterScore(Letter l)
  { return 3*l.getScore(); }

  public int getWordScore()
  { return 1; } 

  public String squareSymbol()
  { return "="; }

  public Color getColor()
  { return Color.blue; } 
}


class DoubleWordSquare extends Square
{ public DoubleWordSquare(Letter l)
  { super(l); } 

  public DoubleWordSquare()
  { super(); }

  public DoubleWordSquare(int xx, int yy)
  { super(xx,yy); } 

  public Object clone()
  { return new DoubleWordSquare(letter); } 

  public int getLetterScore(Letter l)
  { return l.getScore(); }

  public int getWordScore()
  { return 2; } 

  public String squareSymbol()
  { return "*"; }

  public Color getColor()
  { return Color.pink; } 
}


class TripleWordSquare extends Square
{ public TripleWordSquare(Letter l)
  { super(l); } 

  public TripleWordSquare()
  { super(); }

  public TripleWordSquare(int xx, int yy)
  { super(xx,yy); } 

  public Object clone()
  { return new TripleWordSquare(letter); } 

  public int getLetterScore(Letter l)
  { return l.getScore(); }

  public int getWordScore()
  { return 3; } 

  public String squareSymbol()
  { return "#"; }

  public Color getColor()
  { return Color.red; } 
}
