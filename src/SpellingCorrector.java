import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Algorithm: - to set up the substrings-fullword mapping at reading disk file
 * phase - to search the full word based on given substring will be O(1). - to
 * setup the dictionary and mapping is O(n)+O(
 * 
 * Usage: - input any keyword - output the full words which contains the key
 * words
 * 
 * @author Peng Wang
 * 
 */

class SpellingCorrector {

	/**
	 * Global single dictionary for mapping each individual substring to it's
	 * potential full complete workd which is from disk file
	 */
	private final HashMap<String, List<String>> nwords = new HashMap<String, List<String>>();

	private final static String filepath = "big.txt";

	/**
	 * constructor
	 * 
	 * @param file
	 * @throws IOException
	 */
	public SpellingCorrector(String file) {
		try {
			initDict(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Load dictionary from disk and setup substring based index map
	 * 
	 * 
	 * @param file
	 * @throws IOException
	 */
	private void initDict(String file) throws IOException {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(file));

			/**
			 * set up the index-corrector in memory
			 * 
			 */
			String temp = null;
			while ((temp = in.readLine()) != null) {
				String[] wordlist = temp.split(" ");
				for (String word : wordlist) {
					List<String> indexList = getSubStrings(word);
					for (String oneIdx : indexList) {
						if (nwords.containsKey(oneIdx)) {
							List<String> fullWords = nwords.get(oneIdx);

							// to avoid duplicated words in returned full word
							// list
							if (fullWords.contains(word)) {
								continue;
							}
							fullWords.add(word);
							nwords.put(oneIdx, fullWords);
						} else {
							List<String> entries = new ArrayList<String>();
							entries.add(word);
							nwords.put(oneIdx, entries);
						}
					}
				}
			}
		} finally {
			if (in != null)
				in.close();
		}
	}

	/**
	 * 
	 * @param input
	 *            one single word and return all substring list
	 * @return e.g input word is the , and return index could be 't' 'h' 'e' ,
	 *         'th' and 'he'
	 */
	public List<String> getSubStrings(String inputWord) {
		List<String> ret = new ArrayList<String>();
		int wordLen = inputWord.length();

		for (int i = 0; i < wordLen; i++) {
			for (int j = 0; j < wordLen; j++) {
				if (wordLen - j - i + 1 > 0) {
					String subString = inputWord.substring(j, i + j);

					// put every sub strings into sub string list and
					// avoid duplicated index
					if (subString != null && subString.length() != 0
							&& !ret.contains(subString))
						ret.add(subString);
				}
			}
		}
		ret.add(inputWord);

		return ret;
	}

	/**
	 * To do retrival on existing map which is O(1)
	 * 
	 * @param word
	 * @return
	 */
	public final String correct(String word) {

		if (word == null || word.length() == 0)
			return "Invalid input keyword";

		StringBuffer ret = new StringBuffer();
		if (nwords.containsKey(word)) {
			List<String> retWords = nwords.get(word);
			for (String oneWord : retWords) {
				ret.append(oneWord).append(" ");
			}
		}

		return (ret.toString().length() != 0) ? ret.toString()
				: "Word Not Found Contains " + word;
	}

	/**
	 * Test
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException {
		System.out.println((new SpellingCorrector(filepath)).correct("p"));
	}

}
