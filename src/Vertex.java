
class Vertex {
	protected int words;
	protected int prefixes;
	protected Vertex[] edges;

	Vertex() {
		words = 0;
		prefixes = 0;
		edges = new Vertex[26];
		for (int i = 0; i < edges.length; i++) {
			edges[i] = null;
		}
	}
}
