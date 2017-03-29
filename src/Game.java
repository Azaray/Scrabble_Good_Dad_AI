
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
  	corh = corh.toUpperCase();
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
  
  public int getTurn(){
	  return turn;
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

  public void refreshRack()
  { // turn player selects letters from bag
    Player p = (Player) players.get(getTurn());
    Rack r = p.getRack();
    if(!bag.isEmpty()){
    	if(!(r.spaceLeft() >= bag.getSize())){ // check if there is more space in rack than letters in bag to avoid null pointer
    		List selection = bag.giveLetters(r.spaceLeft());
    		r.addLetters(selection);
    	}
    	else { // else add the rest of the bag contents into current players rack
    		List selection = bag.giveLetters(bag.getSize());
    		r.addLetters(selection);
    	}
    }
  }

  public void endMove(Move m)
  { Board b = (Board) board.clone();
    Board b2 = (Board) board.clone();
    Player p = (Player) players.get(turn);
    Rack r = p.getRack();

    if (moveNumber == 1 || b.placeMove(m,board)) {
       if (moveNumber == 1 || m.findWords(b)) {
         if(m.inDictionary0()){         	
      	  	m.clearWords();  
      	  	board.placeMove(m,board);
            m.findWords(board);
            System.out.println("Letter moves are: " + m.getLetterMoves()); 
            System.out.println("All words are: " + m.getWords()); 	
            int s = m.getScore(b2);
            System.out.println("Valid move, score is: " + s);
            p.addScore(s); 
            r.removeLetters(m.getLetters());
            }
            else {
            m.inDictionary0();
          	System.out.println("Invalid words, you lost a turn");
            }
        }
        else 
        { System.out.println("Invalid move, you lost a turn"); }
      }
      else 
      { System.out.println("Invalid move, you lost a turn"); }
    refreshRack();
    turn = (turn + 1) % players.size(); 
    moveNumber++; 
  }

  public boolean gameEnded()
  { return bag.isEmpty(); }

}
      