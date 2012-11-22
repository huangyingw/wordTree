import java.util.HashMap;

class Vertex {
	protected int words;
	protected int prefixes;
	public char myChar;
	protected HashMap<Character, Vertex> edges;

	Vertex(char c) {
		words = 0;
		prefixes = 0;
		edges = new HashMap<Character, Vertex>();
		myChar = c;
	}

	public Vertex() {
		words = 0;
		prefixes = 0;
		edges = new HashMap<Character, Vertex>();
	}
}
