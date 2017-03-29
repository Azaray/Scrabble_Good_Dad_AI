
import java.util.*;
import java.io.*;
import javax.swing.*; 

public class Dictionary
{ private static String[] englishLetters = { "a", "b", "c", "d", "e",
                               "f", "g", "h", "i", "j",
                               "k", "l", "m", "n", "o",
                               "p", "q", "r", "s", "t",
                               "u", "v", "w", "x", "y",
                               "z" };
  private static String[] russianLetters =
                             { "a", "b", "d", "e", "-",
                               "f", "g", "h", "i", "j",
                               "k", "l", "m", "n", "o",
                               "p", "q", "r", "s", "t",
                               "u", "v", "w", "x", "y",
                               "z", "~", "#", "%", ";", ":", "<", ">" }; 
/*     { "\u0430", "\u0431", "\u0432", "\u0433", "\u0434",
       "\u0435", "\u0436", "\u0437", "\u0438", "\u0439",
       "\u043A", "\u043B", "\u043C", "\u043D", "\u043E",
       "\u043F", "\u0440", "\u0441", "\u0442", "\u0443",
       "\u0444", "\u0445", "\u0446", "\u0447", "\u0448",
       "\u0449", "\u044B", "\u044D", "\u044E", "\u044F",
       "\u0451" }; */

  private static String[] letters; 

  // dictionary is split into sets of words containing
  // each letter, and with length from 2 to 15
  
  private static Map dictionary = new HashMap();
  // maps "a" to hasA, etc. hasA[i-2] is set of 
  // words containing "a", with length i

  private static Set[] allwords = new TreeSet[14];

  private static void initialiseDictionary(String lang)
  { if (lang.equals("russian"))
    { letters = russianLetters; } 
    else 
    { letters = englishLetters; } 

    for (int i = 0; i < letters.length; i++)
    { // System.out.println("setting up dictionary: " + i); 
      Set[] hasChar = new TreeSet[14]; 
      for (int j = 0; j < 14; j++)
      { hasChar[j] = new TreeSet(); }
    
      dictionary.put(letters[i],hasChar);
    }
    for (int k = 0; k < 14; k++)
    { allwords[k] = new TreeSet(); }

    // addWords("ch da di do ea ee eh em en er es ex fa fy gi go gu"); 
    // addWords("lethal brave beer account abacus syllabus");
    // addWords("creep ax or career rabbit drool pram aa ad");
    // addWords("aura ef el ore peep true kale cringe nu mu");
    // addWords("jo zoo fox fax fraggle nonce carp ox qi xi");
  }

  public static void loadFromFile(String lang)
  { BufferedReader br = null;
    Vector res = new Vector();
    String line;
    boolean eof = false;
    File file;
      // = new File("out.dat");  /* default */ 

    File startingpoint = new File("input");
    JFileChooser fc = new JFileChooser();
    fc.setCurrentDirectory(startingpoint);
    fc.setDialogTitle("Load dictionary");
    int returnVal = fc.showOpenDialog(null);
    if (returnVal == JFileChooser.APPROVE_OPTION)
    { file = fc.getSelectedFile(); }
    else
    { System.err.println("Load aborted");
      return; 
    }

    try
    { br = new BufferedReader(new FileReader(file)); }
    catch (FileNotFoundException e)
    { System.out.println("File not found: " + file);
      return; 
    }

    initialiseDictionary(lang);

    while (!eof)
    { try { line = br.readLine(); }
      catch (IOException e)
      { eof = true;
        return;
      }
      System.out.println(line); 
      if (line == null) 
      { eof = true; } 
      addWords(line);    
    }
  }

  public String toString()
  { String result = "";
    for (int i = 0; i < letters.length; i++)
    { String lett = letters[i];
      Set[] lettwords = (Set[]) dictionary.get(lett);
      for (int j = 0; j < lettwords.length; j++)
      { result = result + lett + " " + j + "\n   " +
                          lettwords[j] + "\n";
      }
    }
    return result;
  }

  public static void addWords(String line)
  { // line is a series of words
    if (line == null) { return; }

    StringTokenizer st = new StringTokenizer(line,",. {}()?!");
    while (st.hasMoreTokens())
    { String word = st.nextToken();
      String wd2 = word.toLowerCase();
      int len = wd2.length();
      if (len <= 1 || len > 15) { continue; } // skip 
      for (int i = 0; i < len; i++)
      { String c = wd2.charAt(i) + "";
        Set[] wordsWithc = (Set[]) dictionary.get(c);
        if (wordsWithc != null)
        { wordsWithc[len-2].add(wd2);
          // dictionary.put(c,wordsWithc); 
          allwords[len-2].add(wd2);
        }
      }
    }
  }

  public static boolean lookup(Word word) 
  { // checks if word is in dictionary 
    String wd = "" + word;
    int len = wd.length(); 
    String lett1 = "" + wd.charAt(0);
    // System.out.print("Letter: " + lett1 + " length " + len); 
    Set[] words1 = (Set[]) dictionary.get(lett1);
    // System.out.println(" Dictionary: " + words1[len]); 
    if (words1 == null) 
    { return false; } 
    return words1[len-2].contains(wd);     
  } 

  public static Set lookup(List letts, int i)
  { // returns all words of length i that contain
    // all letters in letts -- letts.size() > 0 needed
    Set result = new TreeSet();
    if (i < 2) { return result; } 

    String lett1 = (String) letts.get(0);
    if (lett1.equals(" "))
    { result.addAll(allwords[i-2]); }
    else 
    { Set[] words1 = (Set[]) dictionary.get(lett1);
      if (words1 != null) 
      { result.addAll(words1[i-2]); }
    }

    for (int j = 1; j < letts.size(); j++)
    { String lett = (String) letts.get(j);
      if (lett.equals(" "))
      { continue; }
      Set[] words = (Set[]) dictionary.get(lett);
      if (words != null) 
      { Set wordslengthi = words[i-2];
        result.retainAll(wordslengthi); // intersection
      } 
      else 
      { result = new HashSet(); } 
    }
    return result;
  }
      
  public static Set getList(String ch, int i)
  { Set[] words = (Set[]) dictionary.get(ch);
    return words[i-2];
  } 
      
      
  public static void main(String[] args)
  { // initialiseDictionary();
    loadFromFile("russian");  
    System.out.println(getList("d",3)); 
    System.out.println(getList("d",2)); 
    Vector v1 = new Vector(); 
    v1.add("d"); 
    v1.add("o"); 
    v1.add("m"); 
    
    System.out.println(lookup(v1,3));
    // Vector v2 = new Vector(); 
    // v2.add("o"); 
    // System.out.println(d.lookup(v2,2));
    // System.out.println(lookup(new Word(0,0,0,4,"verve"))); 
    // System.out.println(lookup(new Word(0,0,0,1,"ar"))); 
  }
}