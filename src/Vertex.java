
// 内部类
class Vertex {// 节点类
	protected int words;
	protected int prefixes;
	protected Vertex[] edges;// 每个节点包含26个子节点(类型为自身)

	Vertex() {
		words = 0;
		prefixes = 0;
		edges = new Vertex[26];
		for (int i = 0; i < edges.length; i++) {
			edges[i] = null;
		}
	}
}