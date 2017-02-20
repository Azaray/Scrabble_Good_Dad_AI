import java.io.*;
import javax.swing.JFileChooser; 
import java.util.List;
import java.util.Vector;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class HistoryParser 
{ private DocumentBuilder builder;

  public HistoryParser()
  throws ParserConfigurationException
  { DocumentBuilderFactory factory =
      DocumentBuilderFactory.newInstance();
    builder = factory.newDocumentBuilder();
  }

  public List parse(// String file)
                    File ff)
  throws SAXException, IOException
  { // File ff = new File(file);
    Document doc = builder.parse(ff);
    Element root = doc.getDocumentElement();
    // System.out.println("Root is: " + root); 
    return getHistory(root);
  }

  public List parse(String file)
  throws SAXException, IOException
  { File ff = new File(file);
    Document doc = builder.parse(ff);
    Element root = doc.getDocumentElement();
    // System.out.println("Root is: " + root); 
    return getHistory(root);
  }

  private static List getHistory(Element e)
  { Vector moves = new Vector();
    NodeList children = e.getChildNodes();
    for (int i = 0; i < children.getLength(); i++)
    { Node cn = children.item(i);
      System.out.println("Node name: " + cn.getNodeName()); 
      if (cn instanceof Element) // skip white space
      { Element ce = (Element) cn;
        System.out.println("Tag: " + ce.getTagName()); 
        if (ce.getTagName().equals("move"))
        { System.out.println("Move found."); 
          Move mv = getMove(ce);
          System.out.println(mv); 
          moves.add(mv);
        }
      }
    }
    return moves;
  }

  private static Move getMove(Element e)
  { NodeList children = e.getChildNodes();
    Player player = null;
    String score = null;
    Vector letterMoves = new Vector();
    for (int i = 0; i < children.getLength(); i++)
    { Node cn = children.item(i);
      if (cn instanceof Element)
      { Element ce = (Element) cn;
        String tag = ce.getTagName();
        System.out.println("Found element: " + tag); 
        if (tag.equals("player"))
        { player = getPlayer(ce); }
        else if (tag.equals("score"))
        { Text tn = (Text) ce.getFirstChild();
          score = tn.getData();
          System.out.println("Player score: " + score); 
        }
        else if (tag.equals("lettermove"))
        { letterMoves.add(getLetterMove(ce)); }
      }
    }
    return new Move(player,letterMoves);
  }

  private static Player getPlayer(Element e)
  { NodeList children = e.getChildNodes();
    String att = e.getAttribute("playerFlag"); 
    System.out.println("player flag = " + att); 

    String name = "";
    int score = 0;
    for (int i = 0; i < children.getLength(); i++)
    { Node cn = children.item(i);
      if (cn instanceof Element)
      { Element ce = (Element) cn;
        String tag = ce.getTagName();
        Text tn = (Text) ce.getFirstChild();
        String data = tn.getData();
        if (tag.equals("score"))
        { score = Integer.parseInt(data); }
        else if (tag.equals("name"))
        { name = data; }
      }
    }
    if (att != null && att.equals("HumanPlayer"))
    { return new HumanPlayer(name); } 
    else 
    { return new ComputerPlayer(name); } 
  }

  private static LetterMove getLetterMove(Element e)
  { NodeList children = e.getChildNodes();
    int x = 0;
    int y = 0;
    Letter letter = null;
    for (int i = 0; i < children.getLength(); i++)
    { Node cn = children.item(i);
      if (cn instanceof Element)
      { Element ce = (Element) cn;
        String tag = ce.getTagName();
        Text tn = (Text) ce.getFirstChild();
        String data = tn.getData();
        if (tag.equals("x"))
        { x = Integer.parseInt(data); }
        else if (tag.equals("y"))
        { y = Integer.parseInt(data); }
        else if (tag.equals("letter"))
        { letter = getLetter(ce); }
      }
    }
    return new LetterMove(x,y,letter);
  }

  private static Letter getLetter(Element e)
  { NodeList children = e.getChildNodes();
    String symbol = " ";
    int score = 0;
    for (int i = 0; i < children.getLength(); i++)
    { Node cn = children.item(i);
      if (cn instanceof Element)
      { Element ce = (Element) cn;
        String tag = ce.getTagName();
        Text tn = (Text) ce.getFirstChild();
        String data = tn.getData();
        if (tag.equals("symbol"))
        { symbol = data; }
        else if (tag.equals("score"))
        { score = Integer.parseInt(data); }
      }
    }
    return new Letter(symbol.charAt(0),score);
  }

  public static void main(String[] args)
  { File file = new File("out.dat");  /* default */ 
    try 
    { HistoryParser hp = new HistoryParser(); 

      File startingpoint = new File("output");
      JFileChooser fc = new JFileChooser();
      fc.setCurrentDirectory(startingpoint);
      fc.setDialogTitle("Load data");
      int returnVal = fc.showOpenDialog(null);
      if (returnVal == JFileChooser.APPROVE_OPTION)
      { file = fc.getSelectedFile(); 
        System.out.println(hp.parse(file)); 
      }
      else
      { System.err.println("Load aborted");
        return; 
      }  
      System.out.println(hp.parse("output/out.dat")); 
    } catch (Exception e) { System.err.println("Load aborted"); } 
  } 
}