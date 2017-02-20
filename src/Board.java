import java.util.*;

public class Board
{ private Square[][] squares = new Square[15][15];
  // Square[x][y] is square at this coordinate
  private List allSquares = new ArrayList(); 

  public Board(ScrabbleBuilder b)
  { // uses b to do layout -- mostly standard
    squares = b.getBoard();
    for (int i = 0; i < 15; i++)
    { for (int j = 0; j < 15; j++)
      { allSquares.add(squares[i][j]); }
    }
  }

  public Board(Square[][] arr) 
  { squares = arr;
    for (int i = 0; i < 15; i++)
    { for (int j = 0; j < 15; j++)
      { allSquares.add(squares[i][j]); }
    }
  } 

  public Object clone()
  { Square[][] newsquares = new Square[15][15]; 
    for (int i = 0; i < 15; i++) 
    { for (int j = 0; j < 15; j++) 
      { Square sq = (Square) squares[i][j].clone(); 
        newsquares[i][j] = sq; 
      } 
    } 
    return new Board(newsquares); 
  } 

  public Square getSquare(int i, int j)
  { return squares[i][j]; }

  public ArrayList getRow(int y)
  { ArrayList res = new ArrayList(); 
    for (int i = 0; i < 15; i++) 
    { res.add(squares[i][y]); } 
    return res; 
  } 

  public List getSquares()
  { return allSquares; }

  public boolean isOccupied(int i, int j)
  { return squares[i][j].isOccupied(); }
 
  public boolean occupiedAdjacent(int x, int y)
  { int x0 = x;
    int y0 = y;
    int x1 = x;
    int y1 = y;
    if (x > 0)
    { x0 = x-1; }
    if (x < 14)
    { x1 = x+1; }
    if (y > 0)
    { y0 = y-1; }
    if (y < 14)
    { y1 = y+1; }
    return squares[x][y].isOccupied() ||
           squares[x0][y].isOccupied() ||
           squares[x1][y].isOccupied() ||
           squares[x][y0].isOccupied() ||
           squares[x][y1].isOccupied();
  }

  public boolean placeMove(Move m, Board oldboard)
  { if (m == null) { return false; }
    List lms = m.getLetterMoves();
    List connected = new ArrayList(); 
    List disconnected = new ArrayList(); 

    for (int i = 0; i < lms.size(); i++)
    { LetterMove lm = (LetterMove) lms.get(i); 
      Letter l = lm.getLetter(); 
      int x = lm.getX(); 
      int y = lm.getY(); 
      if (oldboard.occupiedAdjacent(x,y))
      { connected.add(lm); } 
      else 
      { disconnected.add(lm); } 
      placeLetter(l,x,y); 
    }

    if (connected.size() == 0)
    { System.out.println("Invalid move: disconnected moves " + disconnected); 
      return false; 
    } 
    return true; 
  }

  public void placeLetter(Letter l, int x, int y)
  { squares[x][y].setLetter(l); }

  public String toString()
  { String res = "";
    for (int j = 0; j < 15; j++)
    { for (int i = 0; i < 15; i++)
      { res = res + squares[i][j]; }
      res = res + "\n";
    }
    return res;
  }
}