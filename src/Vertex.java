import java.util.HashMap;

class Vertex {
	protected int words;
	protected int prefixes;
	protected HashMap<Character, Vertex> edges;

	Vertex() {
		words = 0;
		prefixes = 0;
		edges = new HashMap<Character, Vertex>();
	}
}
