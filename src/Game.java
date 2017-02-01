import java.util.*;

public class Game
{ private Board board;
  private List players = new ArrayList();
  private int turn = 0;
  private Bag bag; 
  private int moveNumber = 1; 

  public Game(ScrabbleBuilder builder)
  { board = new Board(builder);
    bag = new Bag(builder); 
    players.add(new ComputerPlayer("Computer"));
    turn = 0;
  }

  public void startGame()
  { moveNumber = 1; 
    turn = 0; 
  } 

  public int getMoveNumber()
  { return moveNumber; }

  public void addPlayer(String nme, String corh)
  { Player pl; 
    if (corh != null && corh.equals("C"))
    { pl = new ComputerPlayer(nme); } 
    else 
    { pl = new HumanPlayer(nme); } // should be unique
    players.add(pl); 
  }

  public Board getBoard()
  { return board; } 

  public void display()
  { System.out.println("Bag = \n" + bag);
    System.out.println(); 
    System.out.println("Board = \n" + board); 
    System.out.println(); 
    for (int i = 0; i < players.size(); i++)
    { Player p = (Player) players.get(i);
      System.out.println("Player " + p +
                         " rack = \n" + p.getRack() +
                         " Score = " + p.getScore());
    }
  }

  public int getBagSize()
  { return bag.getSize(); } 

  public List getPlayers()
  { return players; } 

  public Player getPlayer()
  { return (Player) players.get(turn); }

  public Rack getRack()
  { return ((Player) players.get(turn)).getRack(); }

  public Letter getRackLetter(int i)
  { Rack r = getRack(); 
    return r.getLetter(i); 
  } 

  public Square getSquare(int i, int j)
  { return board.getSquare(i,j); }

  public void placeLetter(Letter l, int x, int y)
  { board.placeLetter(l,x,y); } 

  public void playMove()
  { // turn player selects letters from bag and moves
    Player p = (Player) players.get(turn);
    Rack r = p.getRack();
    List selection = bag.giveLetters(7 - r.rackSize());
    r.addLetters(selection);
    Move m = p.doMove(board);
    Board b = (Board) board.clone(); 
    b.placeMove(m,board); 
    m.findWords(b);
    System.out.println(m.getLetterMoves()); 
    System.out.println(m.getWords()); 
    if (board.validateMove(m,moveNumber))
    { int s = m.getScore(board); 
      board.placeMove(m,board);
      System.out.println("Valid move, score is: " + s);
      p.addScore(s); 
      r.removeLetters(m.getLetters());
    }
    else
    { System.out.println("Invalid move, you lost a turn"); }
    turn = (turn + 1) % players.size(); 
    moveNumber++; 
  }

  public void startMove()
  { // turn player selects letters from bag
    Player p = (Player) players.get(turn);
    Rack r = p.getRack();
    List selection = bag.giveLetters(7 - r.rackSize());
    r.addLetters(selection);
  }

  public void endMove(Move m)
  { Board b = (Board) board.clone();
    Board b2 = (Board) board.clone();
    Player p = (Player) players.get(turn);
    Rack r = p.getRack();

    if (board.validateMove(m,moveNumber))
    { if (moveNumber == 1 || b.placeMove(m,board))
      { if (moveNumber == 1 || m.findWords(b))
        { m.clearWords(); 
          board.placeMove(m,board); 
          m.findWords(board);
          System.out.println("Letter moves are: " + m.getLetterMoves()); 
          System.out.println("All words are: " + m.getWords()); 
          m.inDictionary0(); 
          int s = m.getScore(b2); 
          System.out.println("Valid move, score is: " + s);
          p.addScore(s); 
          r.removeLetters(m.getLetters());
        }
        else 
        { System.out.println("Invalid move, you lost a turn"); } 
      }
      else 
      { System.out.println("Invalid move, you lost a turn"); }
    }
    else 
    { System.out.println("Invalid move, you lost a turn"); }
    turn = (turn + 1) % players.size(); 
    moveNumber++; 
  }

  public boolean gameEnded()
  { return bag.isEmpty(); }
}
      