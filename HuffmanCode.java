import java.io.FileInputStream;
import java.lang.*;
import java.io.FileNotFoundException;

public class HuffmanCodes {
	private static BitInputStream in;
	//private static HashMap<Byte, Integer> ordering;
 
 abstract class Node implements Comparable<Node>{
        final protected int count;
        static final int ZERO = 0;
        static final int ONE = 1;

        protected Node(int count) {
            this.count = count;
        }

        public int compareTo(Node n) {
            return Integer.compare(this.count, n.count);
        }

        protected abstract void create(Map<Byte, String> table, String bits);

        public final Map<Byte, String> extractCodes() {
            Map<Byte, String> codes = new HashMap<>();
            this.create(codes, "");
            return codes;
        }

        protected abstract void makeD(Map<String, Byte> table, String bits);

        public final Map<String, Byte> extractDecodes() {
            Map<String, Byte> codes = new HashMap<>();
            this.makeD(codes, "");
            return codes;
        }

        public abstract void writeTo(BitOutputStream out) throws IOException;
    }

    class ValueNode extends Node {
        final byte value;

        ValueNode(byte value, int count) {
            super(count);
            this.value = value;
        }

        @Override
        protected void create(Map<Byte, String> table, String bits) {table.put(this.value, bits);
        }

        @Override
        protected void makeD(Map<String, Byte> table, String bits) {table.put(bits, this.value);
        }

        public void writeTo(BitOutputStream out) throws IOException {
            out.writeBit(ONE);
            out.writeByte(value);
        }
    }

    class DecisionNode extends Node {
        Node left;
        Node right;

        DecisionNode(Node left, Node right) {
            super(left.count + right.count);
            this.left = left;
            this.right = right;
        }

        @Override
        protected void create(Map<Byte, String> table, String bits) {
            this.left.create(table, bits + "0");
            this.right.create(table, bits + "1");
        }

        @Override
        protected void makeD(Map<String, Byte> table, String bits) {
            this.left.makeD(table, bits + "0");
            this.right.makeD(table, bits + "1");
        }

        public void writeTo(BitOutputStream out) throws IOException {
            out.writeBit(ZERO);
            left.writeTo(out);
            right.writeTo(out);
        }
    }

        //end of node stuff




   //end of node class

  public static void main(String[] args) throws IOException {

  	if(args.length == 0) {

  	System.out.println("Usage: java HuffmanCodes MODE [OPTIONS...] IN OUT");
  	System.out.println("MODE is one of: ");
  	System.out.println("   -e   Encodes file IN to file OUT");
  	System.out.println("   -d   Decodes file IN to file OUT");
  	System.out.println("OPTIONS are zero or more of: ");
  	System.out.println("   -F   Show the frequencies of each byte");
  	System.out.println("   -C   Show the codes for each byte");
  	System.out.println("   -B   Show the encoded sequence in binary");

  }

     int arg1 = 0;
     boolean argF = false;
     boolean argC = false;
     boolean argB = false;
     String file1 = "";
     String file2 = "";
     HuffmanCodes huff = new HuffmanCodes();

    
      for (int i = 1; i < args.length; i++) {
         if (args[i].startsWith("-")) {
              if (args[i].equals("-F")) {                  
                       argF = true;
                  }                       
                    else if(args[i].equals("-C")) {
                    	argC = true;
                    }
                       else if (args[i].equals("-B")) {
                        argB = true;
                      }
                    else {
                        throw new IllegalArgumentException();
                    }
                }
            
            else {
                file1 = args[i];
                file2 = args[i + 1];
                break;
            }
        }
    
        File inputFile = new File(file1);
        File outputFile = new File(file2);
        BitInputStream in = new BitInputStream(inputFile);
        BitOutputStream out = new BitOutputStream(outputFile);

        if (in == null || out == null) 
            throw new IllegalArgumentException(); 
        if (args[0].equals("-e"))
            huff.encode(in, out, argF, argC, argB);
        else if (args[0].equals("-d"))
            huff.decode(in, out, argC);
        else{

          }


     /*if(args.length == 4) {

     	File inputFile = new File(args[2]);
     	File outputFile = new File(args[3]);

     if(!inputFile.exists()) {
     	throw new IOException();
     }
     else{
     	BitInputStream in = new BitInputStream(inputFile);
     }
     
     if(!outputFile.exists()) {
     	throw new IOException();
     }	
     else {
	    BitOutputStream out = new BitOutputStream(outputFile);
	 }
  

     if(args[0].equals("-e")) {
     	arg1 = 1;
     	huff.encode(in, out, argF,argC,argB);
     }    

     else if(args[0].equals("-d")) {
        arg1 = 2;
        huff.decode(in, out, argC);
     }

     else if(args[1].equals("-F")) {
     	argF = true;
     }

     else if(args[1].equals("-C")) {
     	argC = true;
     }

     else if(args[1].equals("-B")) {
     	argB = true;
     }

     //checking which combinations

     if(arg1 == 1 && argF == true) {

     }

    } //end of length if 4

    if(args.length == 3) {

    File inputFile = new File(args[2]);		

    if(!inputFile.exists()) {
     	throw new IOException();
     }
     else{
     	BitInputStream in = new BitInputStream(new FileInputStream(inputFile));
     }
     
    if(args[0].equals("-e")) {
     	arg1 = 1;
     	huff.decode(in,out,argF,argC,argB);
     }    

     else if(args[0].equals("-d")) {
        arg1 = 2;
        huff.decode(in,out,argF,argC,argB);
     }

  }*/ //end of if length 3

	
} //end of main


private static Map<Byte, Integer> getFreq(byte[] b) {
     Map<Byte, Integer> freq = new HashMap<>();
        for (int i = 0; i < b.length; i++){
            byte chara = b[i];
            if (freq.containsKey(chara))
                freq.put(chara, freq.get(chara) + 1);
            else 
                freq.put(chara, 1);
        }
        return freq;
}


    Node tree(Map<Byte, Integer> freq) {
        Queue<Node> t = buildTrees(freq);
        
        while  (t.size() > 1) {
        
            Node left = t.remove();
            Node right = t.remove();
            t.add(new DecisionNode(left, right));
        }

        return t.remove();
    }

    private Queue<Node> buildTrees(Map<Byte, Integer> freq) {
        Queue<Node> que = new PriorityQueue<>();
        for (Map.Entry<Byte, Integer> makeVN : freq.entrySet()) { 

            ValueNode vn = new ValueNode(makeVN.getKey(), makeVN.getValue());
            que.add(vn);
        }
        return que;
    }



    //encoding
        private void encode(BitInputStream in, BitOutputStream out, boolean freq, boolean argsC, boolean argsB) throws IOException {

        byte[] data = in.allBytes();

        Map<Byte, Integer> freqCount = getFreq(data);

        if (freq){
            for (Map.Entry<Byte, Integer> sort : freqCount.entrySet()) {
                if (!sort.getKey().equals("\\" + "n"))
                    System.out.println(asASCIILiteral(sort.getKey()) + " : " + sort.getValue());
            }
        }

        Node tree = tree(freqCount);
        Map<Byte, String> lines = tree.extractCodes();

        if (argsC) {
            for (Map.Entry<Byte, String> sort : lines.entrySet()) {
                System.out.println("\"" + sort.getValue() + "\" -> " + asASCIILiteral(sort.getKey()));
            }
        }

        StringBuilder build = new StringBuilder();

        for (int i = 0; i < in.size(); i++) {
            build.append(lines.get((byte)in.readByte()));
        }
        if (argsB) {
            System.out.println("ENCODED SEQUENCE\n" + build);
        }
        tree.writeTo(out);
        out.writeInt(in.size());
        int headerBits = out.tally();
        for (int i = 0; i < build.length(); i++) {
            out.writeBit((build.charAt(i) == '1') ? 1 : 0);
        }

        int encodeBits = out.tally() - headerBits, outputBytes = (int)Math.ceil(1.0*(headerBits + encodeBits)/8);
        out.close();
    }
	



    

    private void decode(BitInputStream in, BitOutputStream out, boolean lines) throws IOException {
        Node tree = newT(in);
        int count = in.readInt(), counted = 0;
        Map<String, Byte> decodes = tree.extractDecodes();
        if (lines) {
            for (Map.Entry<String, Byte> sort : decodes.entrySet()) {
                System.out.println("\"" + sort.getKey() + "\" -> " + asASCIILiteral(sort.getValue()));
            }
        }
        StringBuilder build = new StringBuilder();
        while (counted < count) {
            build.append(in.readBit());
            if (decodes.containsKey(build.toString())) {
                out.writeByte(decodes.get(build.toString()));
                build.delete(0, build.length());
                counted++;
            }
        }

        out.close();
    }


	private Node newT(BitInputStream in) throws IOException {
        if (in.readBit() != 0)
            return new ValueNode((byte)in.readByte(), 0);
        else
            return new DecisionNode(newT(in), newT(in));
    }


 /*public static void getFreq(BitInputStream f) {
  
 ArrayList<Integer> array = new ArrayList<Integer>();
 ordering = new  HashMap<Byte, Integer>();
 byte[] data = in.allBytes();
 HashMap<Byte, Integer> freq = new HashMap<Byte, Integer>();
 
 for(Byte b: data) {

 	if(!freq.containsKey(b)) {
 		freq.put(b, 0);
 	}

 	freq.put(b, 1 + freq.get(b));
 }

 for(Byte b : freq.keySet()) {
 	array.add(freq.get(b));
 }

 int[] intArr = new int[array.size()];

 for(int i = 0; i < array.size(); i++) {
 	intArr[i] = array.get(i);
 }

 Arrays.sort(intArr);

 for(int i = 0; i < array.size(); i++) {
 	for(Byte b : freq.keySet()) {
 		if(freq.get(b) == intArr[i]) {
 			ordering.put(b, intArr[i]);
 		}
 	}
 }

 for(Byte b : ordering.keySet()) {
 	System.out.printf("%s: %d%n", asASCIILiteral(b), ordering.get(b));
 }

} //end of getFreq

public static Node v;
public static Node x;
public static Node y;
public static Node d;
public static int x;

//adding in value nodes
public static void makeN(BitInputStream f) {
	
	PriorityQueue<Node> que = new PriorityQueue<Node>();
    int x = 0;
    

    for(Byte b : ordering.keySet()) {
    	x = ordering.get(b);
    	v = new ValueNode(b, x);
    	que.add(v);
    }



    for(int i = 0; i < que.size(); i += 2) {
      //x = que.geti);
      //v = que.get(i+1);
      d = new DecisionNode(que.add(i), que.add(i+1));
      que.add(d);
    }
} */



private static final Map<String, String> escapedLiterals = new HashMap<>();
static {
 escapedLiterals.put("\n", "\\n");
 escapedLiterals.put("\r", "\\r");
 escapedLiterals.put("\t", "\\t");
 escapedLiterals.put("\\", "\\\\");
 escapedLiterals.put("\'", "\\\'");
 escapedLiterals.put("\"", "\\\"");
} 

 private static String asASCIILiteral(byte asciiValue) {

 String escStr = new String(new byte[] {asciiValue});
 escStr = escapedLiterals.getOrDefault(escStr, escStr);

 return String.format("\'%s\'", escStr);
} //end of asciiliteral



//SOURCES OF HELP
//office hours with mac
//very detailed explanation from TA
//piazza slides
//https://www.siggraph.org/education/materials/HyperGraph/video/mpeg/mpegfaq/huffman_tutorial.html
//javadocs





} //end of class
