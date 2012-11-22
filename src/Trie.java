import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Trie {

	private Vertex root;

	public Trie() {
		root = new Vertex();
	}

	public List<String> listAllWords() {

		List<String> words = new ArrayList<String>();
		Map<Character, Vertex> edges = root.edges;

		Iterator<Entry<Character, Vertex>> iter = edges.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Character key = (Character) entry.getKey();
			Vertex val = (Vertex) entry.getValue();
			String word = "" + key;
			depthFirstSearchWords(words, val, word);
		}
		return words;
	}

	private void depthFirstSearchWords(List words, Vertex vertex,
			String wordSegment) {
		HashMap<Character, Vertex> edges = vertex.edges;
		boolean hasChildren = false;
		Iterator<Entry<Character, Vertex>> iter = edges.entrySet().iterator();
		while (iter.hasNext()) {
			hasChildren = true;
			Map.Entry entry = (Map.Entry) iter.next();
			Character key = (Character) entry.getKey();
			Vertex val = (Vertex) entry.getValue();
			String newWord = wordSegment + key;
			depthFirstSearchWords(words, val, newWord);
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
		if (!vertex.edges.containsKey(c)) { // the word does NOT exist
			return 0;
		} else {

			return countPrefixes(vertex.edges.get(c),
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
		if (!vertex.edges.containsKey(c)) { // the word does NOT exist
			return 0;
		} else {
			return countWords(vertex.edges.get(c), wordSegment.substring(1));

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
			if (!vertex.edges.containsKey(c)) { // if the edge does NOT exist
				vertex.edges.put(c, new Vertex());
			}
			addWord(vertex.edges.get(c), word.substring(1)); // go the the next
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
