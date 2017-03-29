

public abstract class Player
{ private String name;
  private Rack rack;
  private int score = 0;

  public Player(String nme)
  { name = nme;
    rack = new Rack(); 
  }

  public String getName()
  { return name; }

  public Rack getRack()
  { return rack; }

  public int getScore()
  { return score; }

  public void addScore(int x)
  { score += x; }

  public abstract Move doMove(Board b); 

  public String toString()
  { return name; } 

  public String toXml()
  { String res = "  <player>\n    <name>" + name + "</name>\n" + 
      "    <score>" + score + "</score>\n  </player>\n"; 
    return res; 
  }
}

class HumanPlayer extends Player
{ // to make a move, user enters each letter move
  public HumanPlayer(String nme)
  { super(nme); }

  public Move doMove(Board b)
  { return Move.makeMove(this,b); } 
}

class ComputerPlayer extends Player
{ // calculates optimal move
  public ComputerPlayer(String nme)
  { super(nme); }

  public Move doMove(Board b)  // compute best move in fact
  { return Move.makeMove(this,b); } 
}
