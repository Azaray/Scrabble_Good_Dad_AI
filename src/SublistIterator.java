import java.util.*;

public class SublistIterator
{ private List l; // list whose sublists are being
                  // iterated over
  private int index = 0; // position in l
  // private boolean atEnd = false;
  private List current = new ArrayList();
  private Object head;          // head of the list   
  private List sublist = null;  // tail of the list 
  private SublistIterator sublistIterator = null;

  public SublistIterator(List ll)
  { l = ll;
    index = 0;
    if (l.size() > 0)
    { current = (List) ((ArrayList) l).clone();
      sublist = (List) ((ArrayList) l).clone();
      head = sublist.get(0); 
      sublist.remove(0);
      // atEnd = false; 
      sublistIterator = new SublistIterator(sublist);
    }
  }

  public List getCurrent()
  { return current; }

  public boolean atEnd()
  { if (l.size() == 0)
    { return true; }
    // if (atEnd) { return true; } 
    return (index == l.size() - 1 &&
            sublistIterator.atEnd());
  }

  public void advance()
  { if (l.size() == 0) { return; }  // can't move
    /* if (l.size() == 1)
    { if (atEnd)
      { current = new ArrayList(); }
      else 
      { atEnd = true; 
        current = (List) ((ArrayList) l).clone();
      }
      return; 
    } */
    if (sublistIterator.atEnd())
    { if (index == l.size() - 1)
      { current = new ArrayList(); 
        return; 
      }  // can't move
      index++;
      head = sublist.get(0); 
      current = (List) ((ArrayList) sublist).clone();
      sublist.remove(0);
      sublistIterator = new SublistIterator(sublist);
      return;
    }
    sublistIterator.advance();
    List sb = sublistIterator.getCurrent();
    current = (List) ((ArrayList) sb).clone();
    current.add(0,l.get(index));
  }

  public static void main(String[] args)
  { List l = new ArrayList();
    l.add("A"); l.add("B"); 
    l.add("a"); l.add("b"); l.add("c"); l.add("d");
    SublistIterator si = new SublistIterator(l);
    do
    { System.out.println(si.getCurrent());
      si.advance();
    } while (!si.atEnd());
  }
}