import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Trie {

	private Vertex root;

	public Trie() {
		root = new Vertex();
	}

	public List<String> listAllWords() {

		List<String> words = new ArrayList<String>();
		Vertex[] edges = root.edges;

		for (int i = 0; i < edges.length; i++) {
			if (edges[i] != null) {
				String word = "" + (char) ('a' + i);
				depthFirstSearchWords(words, edges[i], word);
			}
		}
		return words;
	}

	private void depthFirstSearchWords(List words, Vertex vertex,
			String wordSegment) {
		Vertex[] edges = vertex.edges;
		boolean hasChildren = false;
		for (int i = 0; i < edges.length; i++) {
			if (edges[i] != null) {
				hasChildren = true;
				String newWord = wordSegment + (char) ('a' + i);
				depthFirstSearchWords(words, edges[i], newWord);
			}
		}
		if (!hasChildren) {
			words.add(wordSegment);
		}
	}

	public int countPrefixes(String prefix) {
		return countPrefixes(root, prefix);
	}

	private int countPrefixes(Vertex vertex, String prefixSegment) {
		if (prefixSegment.length() == 0) { // reach the last character of the
											// word
			return vertex.prefixes;
		}

		char c = prefixSegment.charAt(0);
		int index = c - 'a';
		if (vertex.edges[index] == null) { // the word does NOT exist
			return 0;
		} else {

			return countPrefixes(vertex.edges[index],
					prefixSegment.substring(1));

		}

	}

	public int countWords(String word) {
		return countWords(root, word);
	}

	private int countWords(Vertex vertex, String wordSegment) {
		if (wordSegment.length() == 0) { // reach the last character of the word
			return vertex.words;
		}

		char c = wordSegment.charAt(0);
		int index = c - 'a';
		if (vertex.edges[index] == null) { // the word does NOT exist
			return 0;
		} else {
			return countWords(vertex.edges[index], wordSegment.substring(1));

		}

	}

	public void addWord(String word) {
		addWord(root, word);
	}

	private void addWord(Vertex vertex, String word) {
		if (word.length() == 0) { // if all characters of the word has been
									// added
			vertex.words++;
		} else {
			vertex.prefixes++;
			char c = word.charAt(0);
			c = Character.toLowerCase(c);
			int index = c - 'a';
			if (vertex.edges[index] == null) { // if the edge does NOT exist
				vertex.edges[index] = new Vertex();
			}

			addWord(vertex.edges[index], word.substring(1)); // go the the next
																// character
		}
	}

	public static void main(String args[]) // Just used for test
	{
		Trie trie = new Trie();
		trie.addWord("China");
		trie.addWord("China");
		trie.addWord("China");

		trie.addWord("crawl");
		trie.addWord("crime");
		trie.addWord("ban");
		trie.addWord("China");

		trie.addWord("english");
		trie.addWord("establish");
		trie.addWord("eat");
		System.out.println(trie.root.prefixes);
		System.out.println(trie.root.words);

		List<String> list = trie.listAllWords();
		Iterator listiterator = list.listIterator();

		while (listiterator.hasNext()) {
			String s = (String) listiterator.next();
			System.out.println(s);
		}

		int count = trie.countPrefixes("ch");
		int count1 = trie.countWords("china");
		System.out.println("the count of c prefixes:" + count);
		System.out.println("the count of china countWords:" + count1);
	}
}
