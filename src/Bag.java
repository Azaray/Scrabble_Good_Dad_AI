import java.util.*; 

public class Bag
{ private List letters = new ArrayList(); // of Letter

  public Bag(ScrabbleBuilder builder)
  { letters = builder.getLetters(); }

  public boolean isEmpty()
  { return letters.size() == 0; }

  public List giveLetters(int giving)
  { List res = new ArrayList();
    Random rand = new Random();
    int n = letters.size();
    for (int i = 0; i < giving; i++)
    { int index = rand.nextInt(n);
      Letter l = (Letter) letters.get(index);
      res.add(l);  // sort in decreasing score order
      letters.remove(index);
      n--;
    }
    return res;
  }

  public int getSize()
  { return letters.size(); }

  public String toString()
  { return letters.toString(); }
}