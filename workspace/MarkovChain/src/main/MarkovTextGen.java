package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MarkovTextGen {

	public static void main(String a[]) throws IOException {

		int prefix = 6;
		Path source = Paths.get("files/green_eggs_and_ham.txt");

		transformTxt(source, prefix);

	}

	public static void transformTxt(Path file, int prefix) {

		Map<String, List<String>> map = new HashMap<String, List<String>>();
		StringBuffer txt = new StringBuffer();
		Charset ch = Charset.forName("US-ASCII");

		try {
			txt = readFile(file, ch);

			map = getMarkovChain(txt, prefix);
			
			markovIt(map, prefix, ch, txt);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		
	}

	private static void markovIt(Map<String, List<String>> map, int prefix, Charset ch, StringBuffer txt) {
		Random rd = new Random();
		try (BufferedWriter wr = Files.newBufferedWriter(Paths.get("files/out"), ch)) {

			StringBuffer sb = new StringBuffer();
			String currentPrefix = txt.substring(0, prefix);
			sb.append(currentPrefix);

			for (int i = 0; i <= txt.length() - prefix; i++) {
				List<String> possibilities = map.get(currentPrefix);
				if (!possibilities.isEmpty() && possibilities.size() > 0) {
					int index = possibilities.size();
					String next = possibilities.get(rd.nextInt(index));
					sb.append(next);
					currentPrefix = sb.substring(sb.length() - prefix, sb.length());
				}
			}

			System.out.println(sb.toString());

		} catch (Exception ex) {
			System.out.println("here");
		}

	}

	public static StringBuffer readFile(Path file, Charset ch) {

		StringBuffer sb = new StringBuffer();

		try (BufferedReader br = Files.newBufferedReader(file, ch)) {

			String line = null;
			while ((line = br.readLine()) != null) {

				sb.append(line);
			}
		} catch (Exception e) {
			System.out.println("File does not exist. " + e.getMessage());
		}
		return sb;
	}

	private static Map<String, List<String>> getMarkovChain(StringBuffer txt, int prefix) {

		Map<String, List<String>> map = new HashMap<>();

		try {
			for (int i = 0; i < txt.length() - prefix; i++) {

				String gram = txt.substring(i, i + prefix);

				String suffix = String.valueOf(txt.charAt(i + prefix));

				if (!map.containsKey(gram)) {
					List<String> tmp = new ArrayList<>();
					map.put(gram, tmp);
				}
				map.get(gram).add(suffix);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return map;
	}
}
