







import java.util.*;
import java.lang.*;
import java.io.*;




public class DeserialDAWG{

	Map<Character, DAWG> dawgs = new HashMap<Character, DAWG>();
	

	// DeserializeDAWG constructor - deserialize a DAWG serialized file.
	public DeserialDAWG() {
	
	Character[] alphabet = { 'A', 'B', 'C', 'D', 'E',
             'F', 'G', 'H', 'I', 'J',
             'K', 'L', 'M', 'N', 'O',
             'P', 'Q', 'R', 'S', 'T',
             'U', 'V', 'W', 'X', 'Y'};	
	
	
		try {
			for(int i = 0; i<alphabet.length; i++){
				DAWG dawg = new DAWG(new Vector<Vector <Node>>());
	            ObjectInputStream my_in = new ObjectInputStream(new FileInputStream("Dict_DAWG_" + alphabet[i] + ".out"));
	            dawg = (DAWG) my_in.readObject();
	            dawgs.put(alphabet[i],dawg);
			}
            
        } catch (FileNotFoundException e) {
           	System.out.println("File not found, and this doesn't mean the Trie.class file.");
        } catch (IOException f) {
           	System.out.println("Must catch this IOException to make Java happy.");
           	System.out.println(f);
        } catch (ClassNotFoundException g) {
           	System.out.println("Must catch this ClassNotFoundException to make Java happy.");
        }
	
	}
	
	
	
	
	
	// getDAWG - returns the DAWG data member object "dawgs".
	public Map getDawgs() {
		return dawgs;
	}
	
	// used for testing
	public static void main(String[] args) {
	
		DeserialDAWG myTest = new DeserialDAWG();
		System.out.println(((DAWG) myTest.getDawgs().get('I')).containsWord("inside"));
	}

}