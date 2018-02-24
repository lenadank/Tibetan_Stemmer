package syllRep;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SyllableAsVec {
	public enum stemType {main_stem, main_stem_norm};
	public static List<String> keys = Arrays.asList("", "k", "g", "g.", "c", "j", "t", "d", "n", "p", 
													"b", "m", "w", "z", "'", "y", "r", "l", "s", "h", 
													"a", "kh", "ng", "ch", "ny", "th", "ph", "ts", "dz", "zh", "sh", "tsh", "i",
			"u", "e", "o", "'i", "'is", "'ang", "'am", "'o" );

//	private final static Set<String> prescriptsStrings = new HashSet<>(Arrays.asList("g", "g.", "d", "b", "m", "'"));
//	private final static Set<String> superscriptsStrings = new HashSet<>(Arrays.asList("r", "l", "s"));
//	private final static Set<String> subscriptsStrings = new HashSet<>(Arrays.asList("y", "r", "l", "w"));
	private final static Set<String> coreLettersStrings = new HashSet<>(Arrays.asList("k", "kh", "g", "ng", "c", "ch", "j", "ny",
		    "t", "th", "d", "n", "p", "ph", "b", "m",
		    "ts", "tsh", "dz", "zh", "z", "'", "y", "r", "l", "sh", "s", "h", "w"));

	private final static Set<String> codaStrings = new HashSet<>(Arrays.asList("g", "ng", "d", "n", "b", "m", "'", "r", "l", "s"));
	private final static Set<String> postscriptsStrings = new HashSet<>(Arrays.asList("s", "d"));
	private final static Set<String> appendedPrefixStrings = new HashSet<>(Arrays.asList("'i", "'is", "'ang", "'am", "'o", "'u"));
	private final static Set<Character> vowels = new HashSet<>(Arrays.asList('a', 'e', 'i', 'o', 'u'));
	private final static Set<Character> firstCharInAMultipleCharLetter = new HashSet<>(Arrays.asList('k', 'g', 'n', 'c', 't', 'd', 'p', 'z', 's'));
	
	
	private final static Map<String, Set<String>> superscriptByCoreLetter = new HashMap<>();
	
	static{
		addCombinationToMap(superscriptByCoreLetter, "r", new String[]{"k", "g", "ng", "j", "ny", "t", "d", "n", "b", "m", "ts", "dz"});
		addCombinationToMap(superscriptByCoreLetter, "l", new String[]{"k", "g", "ng", "c", "j", "t", "d", "p", "b", "h"});
		addCombinationToMap(superscriptByCoreLetter, "s", new String[]{"k", "g", "ng", "ny", "t", "d", "n", "p", "b", "m", "ts"});
	}
	
	private final static Map<String, Set<String>> subscriptsByCoreLetter = new HashMap<>();
	static{
		addCombinationToMap(subscriptsByCoreLetter, "y", new String[]{"k", "kh", "g", "p", "ph", "b", "m"});
		addCombinationToMap(subscriptsByCoreLetter, "r", new String[]{"k", "kh", "g", "t", "th", "d", "p", "ph", "b", "m", "sh", "s", "h"});
		addCombinationToMap(subscriptsByCoreLetter, "l", new String[]{"k", "g", "b", "z", "r", "s"});
		addCombinationToMap(subscriptsByCoreLetter, "w", new String[]{"k", "kh", "g", "c", "ny", "t", "d", "ts", "tsh", "zh", "z", "r", "l", "sh", "s", "h"});
	}
	
	//the values in the map are not core letters but sequences of several letters (starting with the core).
	private final static Map<String, Set<String>> prescriptByCoreLetter = new HashMap<>();
	static{
		addCombinationToMap(prescriptByCoreLetter, "g", new String[]{"c", "ny", "t", "d", "n", "zh", "z", "sh", "s", "ts"});
		addCombinationToMap(prescriptByCoreLetter, "g.", new String[]{"y"});
		addCombinationToMap(prescriptByCoreLetter, "d", new String[]{"k", "g", "ng", "p", "b", "m", "ky", "gy", "py", "by", "my", "kr", "gr", "pr", "br"});
		addCombinationToMap(prescriptByCoreLetter, "b", new String[]{"k", "g", "c", "t", "d", "zh", "z", "sh", "s", "ky", "gy", "kr", "gr", "kl", "zl", "rl", "sl", 
				"rk", "rg", "rng", "rj", "rny", "rt", "rd", "rn", "rts", "rdz", "lt", "sk", "sg", "sng", "sny", "st", "sd", "sn", "sts", "rky", "rgy", "sky", 
				"sgy", "skr", "sgr"});
		addCombinationToMap(prescriptByCoreLetter, "m", new String[]{"kh", "g", "ng", "ch", "j", "ny", "th", "d", "n", "tsh", "dz", "ky", "gy", "khr", "gr"});
		addCombinationToMap(prescriptByCoreLetter, "'", new String[]{"kh", "g", "ch", "j", "th", "d", "ph", "b", "tsh", "dz", "khy", "gy", "phy", "by", "khr", 
				"gr", "thr", "dr", "phr", "br"});
	}
	
	private static void addCombinationToMap(Map<String, Set<String>> map, String slotValue, String[] coreLetters){
		final Set<String> rule = new HashSet<>();
		for (String coreLetter : coreLetters){
			rule.add(coreLetter);
		}
		map.put(slotValue, rule);
	}
	private final static Map<String, Set<String>> specialCasesSubscripts = new HashMap<>();
	static{
		//{"y", "r", "l", "w"};
		final Set<String> subscriptOfG = new HashSet<>();
		subscriptOfG.add("y");
		subscriptOfG.add("r");
		subscriptOfG.add("l");
		subscriptOfG.add("w");
		specialCasesSubscripts.put("g", subscriptOfG);
		
		final Set<String> subscriptOfD = new HashSet<>();
		// {"r", "w"};
		subscriptOfD.add("r");
		subscriptOfD.add("w");
		specialCasesSubscripts.put("d", subscriptOfD);
		
		final Set<String> subscriptOfB = new HashSet<>();
		//  {"y", "r", "l"}
		subscriptOfB.add("y");
		subscriptOfB.add("r");
		subscriptOfB.add("l");
		specialCasesSubscripts.put("b", subscriptOfB);

		final Set<String> subscriptOfM = new HashSet<>();
		//  {"y", "r"}
		subscriptOfM.add("y");
		subscriptOfM.add("r");
		specialCasesSubscripts.put("m", subscriptOfM);
	}

	
	private final static int preScriptIndex = 0;
	private final static int superScript = 1;
	private final static int coreIndex = 2;
	private final static int subScriptIndex =3;
	private final static int vowelIndex = 4;
	private final static int finalIndex = 5;
	private final static int postScriptIndex = 6;
	private final static int appendedParticleIndex = 7;
	
	
	//Data:
	private final String[] vector;
	private final String[] normalizedVector;
	private final String word;
	private final String stem;
	private final String normalizedStem;
	private final boolean isStemmed;

	public String getWord() {
		return word;
	}
	
	private static boolean isSpecialCase(String s1, String s2){
		return specialCasesSubscripts.containsKey(s1) && specialCasesSubscripts.get(s1).contains(s2);
	}
	
	private static boolean isPreScript(String s, String prefix){
		return prescriptByCoreLetter.containsKey(s) && prescriptByCoreLetter.get(s).contains(prefix);
	}
	
	private static boolean isPostScripts(String s){
		return postscriptsStrings.contains(s);
	}
	
	private static boolean isCore(String s){
		return coreLettersStrings.contains(s);
	}
	
	private static boolean isSuperScript(String s, String coreLetter){
		return superscriptByCoreLetter.containsKey(s) && superscriptByCoreLetter.get(s).contains(coreLetter);
	}
	
	private static boolean isSubScript(String s, String coreLetter){
		return subscriptsByCoreLetter.containsKey(s) && subscriptsByCoreLetter.get(s).contains(coreLetter);
	}

	
	private static boolean isFirstLetterInMultiple(char c){
		return(firstCharInAMultipleCharLetter.contains(c));
	}
	
	private static boolean isVowel(char c){
		return vowels.contains(c);
	}
	
	private static boolean isAppendedPrefix(String s){
		return appendedPrefixStrings.contains(s);
	}
	
	private static boolean isCoda(String s){
		//return isFinalLetter[index];
		return codaStrings.contains(s);
	}
	
	
	public String getPreScript(stemType normalized){
		return getVector(normalized)[preScriptIndex];
	}
	
	public String getSuperScript(stemType normalized){
		return getVector(normalized)[superScript];
	}
	
	public String getCoreLetter(stemType normalized){
		return getVector(normalized)[coreIndex];
	}
	
	public String getSubScript(stemType normalized){
		return getVector(normalized)[subScriptIndex];
	}
	
	public String getVowel(stemType normalized){
		return getVector(normalized)[vowelIndex];
	}
	
	public String getFinal(stemType normalized){
		return getVector(normalized)[finalIndex];
	}
	
	public String getPostScript(stemType normalized){
		return getVector(normalized)[postScriptIndex];
	}
	
	public String getExtra(stemType normalized){
		return getVector(normalized)[appendedParticleIndex];
	}
	
	public String getStem(stemType normalized){
		switch (normalized){
		case main_stem: return stem;
		case main_stem_norm : return normalizedStem; 
		}
		return null;
	}

	public boolean isStemmed(){
		return isStemmed;
	}
	public String toString(){
		return word + " : " + (isStemmed? stem : "-no stemming-");
	}
	
	public String[] getStemVector(stemType stemType, int vecSize){
		String[] v = null;
		switch(stemType){
		case main_stem:
			v = vector;
			break;
		case main_stem_norm:
			v = normalizedVector;
			break;
		}
		
		if (vecSize == 5){
			return Arrays.copyOfRange(v, 1, 6);
		}
		else{
			v = Arrays.copyOf(v, 8);
			v[0] = "";
			v[6] = "";
			v[7] = "";
			return v;	
		}
	}
	
	public SyllableAsVec(String word){
		this.word = word;
		String[] v = new String[]{"", "","","","","","",""};
		boolean lIsStem = false;
		try{
			v = syllableToVector(word, false);
			lIsStem = true;
		}
		catch(Exception e){
		}
		this.vector = v; 
		this.normalizedVector = normalizeVector(v);
		this.stem = createStem(stemType.main_stem);
		this.normalizedStem = createStem(stemType.main_stem_norm);
		this.isStemmed=lIsStem;		
	}
	
	public String[] getVector(stemType normalized){
		switch(normalized){
		case main_stem: return vector;
		case main_stem_norm: return normalizedVector;
		}
		return null;
	}
	private String createStem(stemType noramlized){
		StringBuilder sb = new StringBuilder();
		sb.append(getSuperScript(noramlized)).append(getCoreLetter(noramlized)).append(getSubScript(noramlized)).append(getVowel(noramlized)).append(getFinal(noramlized));
		String stem = sb.toString();
		if (stem.isEmpty()){
			stem = this.word;
		}
		return stem;
	}
	
	private static String[] stringLettersToVec(List<String> letters, int vowelLetterInd , String extra ) throws Exception{
		String[] stringVector = new String[]{"", "","","","","","",""};
		stringVector[vowelIndex] = letters.get(vowelLetterInd);
		//prefixes:
		if (vowelLetterInd == 0){
			stringVector[coreIndex] = "a"; //a as a core letter
		}
		else if (vowelLetterInd == 1){
			//this means that the first letter is a core letter
			if (!isCore(letters.get(0))){
				throw new Exception("cannot convert: " + letters);
			}
			stringVector[coreIndex] = letters.get(0);
		}
		else if (vowelLetterInd == 2){
			//exception:
			if (isSpecialCase(letters.get(0), letters.get(1))){
				stringVector[coreIndex] = letters.get(0);
				stringVector[subScriptIndex] = letters.get(1);
			}
			else if (isPreScript(letters.get(0), letters.get(1))){
				if (isCore(letters.get(1))){
					stringVector[preScriptIndex] = letters.get(0);
					stringVector[coreIndex] = letters.get(1);
				}
				else{
					throw new Exception("cannot convert: " + letters);
				}
			}
			else if (isCore(letters.get(1)) && isSuperScript(letters.get(0), letters.get(1))){
				stringVector[superScript] = letters.get(0);
				stringVector[coreIndex] = letters.get(1);
			}
			else if (isCore(letters.get(1)) && isSubScript(letters.get(1), letters.get(0))){		
					stringVector[coreIndex] = letters.get(0);
					stringVector[subScriptIndex] = letters.get(1);
			}
			else{
				throw new Exception("cannot convert: " + letters);
			}
		}
		else if (vowelLetterInd == 3){
			if (isPreScript(letters.get(0), letters.get(1) + letters.get(2))){
				if (isCore(letters.get(2)) && isSuperScript(letters.get(1), letters.get(2))){
						stringVector[preScriptIndex] = letters.get(0);
						stringVector[superScript] = letters.get(1);
						stringVector[coreIndex] = letters.get(2);
				}
				else{
					if(isCore(letters.get(1)) && isSubScript(letters.get(2), letters.get(1))){
							stringVector[preScriptIndex] = letters.get(0);
							stringVector[coreIndex] = letters.get(1);
							stringVector[subScriptIndex] = letters.get(2);
					}
					else{
						throw new Exception("cannot convert: " + letters);
					}

				}
			}
			else{
				if (isCore(letters.get(1)) && isSuperScript(letters.get(0), letters.get(1)) && isSubScript(letters.get(2), letters.get(1))){
					stringVector[superScript] = letters.get(0);
					stringVector[coreIndex] = letters.get(1);
					stringVector[subScriptIndex] = letters.get(2);
				}
				else{
					throw new Exception("cannot convert: " + letters);
				}
			}
		}
		else if (vowelLetterInd == 4){
			if (isPreScript(letters.get(0), letters.get(1) + letters.get(2) + letters.get(3)) && 
					isCore(letters.get(2)) && isSuperScript(letters.get(1), letters.get(2)) && isSubScript(letters.get(3), letters.get(2))){
				stringVector[preScriptIndex] = letters.get(0);
				stringVector[superScript] = letters.get(1);
				stringVector[coreIndex] = letters.get(2);
				stringVector[subScriptIndex] = letters.get(3);
			}
			else{
				throw new Exception("cannot convert: " + letters);
			}
		}
		else{
			throw new Exception("cannot convert: " + letters);
		}
	
		//suffixes:
		int numOfLetters = letters.size();
		if (numOfLetters - vowelLetterInd >= 4){
			throw new Exception("cannot convert: " + letters);
		}
		if (numOfLetters - vowelLetterInd >= 2){ //one letter after 
			if (isCoda(letters.get(vowelLetterInd+1))){
				stringVector[finalIndex] = letters.get(vowelLetterInd+1);
				if (numOfLetters - vowelLetterInd == 3){
					if (isPostScripts(letters.get(vowelLetterInd+2))){
						stringVector[postScriptIndex] = letters.get(vowelLetterInd+2);
					}
					else{
						throw new Exception("cannot convert: " + letters);
					}
				}
			}
			else{
				throw new Exception("cannot convert: " + letters);
			}
		}
		stringVector[appendedParticleIndex] = extra;
		/*add another apostrophe for the case of: prescript + core letter + vowel "a" (only in the case of vowel "a") + particle -> final apostrophe*/
		if (extra.startsWith("'a") && stringVector[vowelIndex].equals("a") && !stringVector[preScriptIndex].isEmpty() && !stringVector[coreIndex].isEmpty()){
			stringVector[finalIndex] = "'";
		}
		
		
		//change g. to g
		if(!stringVector[preScriptIndex].isEmpty() && letters.get(0).equals("g.")){ //check if "g." is the prescript
			stringVector[preScriptIndex] = "g";
		}
		return stringVector;
	}
	
	private static String[] normalizeVector(String[] vector){
		String[] normalized = Arrays.copyOf(vector, 8);
		if (normalized[vowelIndex].equals("o")){
			normalized[vowelIndex] = ("a");
		}
		Set<String> coreEquivalents = new HashSet<>();
		coreEquivalents.add("c"); coreEquivalents.add("ch");coreEquivalents.add("j");
		coreEquivalents.add("zh"); coreEquivalents.add("sh");
		if (coreEquivalents.contains(normalized[coreIndex])){
			normalized[coreIndex] = "c";
		}
		coreEquivalents.clear();
		coreEquivalents.add("ts"); coreEquivalents.add("tsh");coreEquivalents.add("dz");
		coreEquivalents.add("z");
		if (coreEquivalents.contains(normalized[coreIndex])){
			normalized[coreIndex] = "ts";
		}
		if (normalized[finalIndex].equals("s")){
			normalized[finalIndex] = "";
		}
		return normalized;
	}
	
//	private static Object[] syllableToIntArray(String syllable, boolean ignoreFirstA) throws Exception{
//		int vowelIndex = -1;
//		List<Integer> letters = new ArrayList<>();
//		int extra = 0;
//		char[] syllableCharArr = syllable.toCharArray();
//		int i= 0;
//		while(i < syllable.length()){
//			boolean isMultiple = false;
//			if (isFirstLetterInMultiple(syllableCharArr[i]) && i+1 < syllable.length()){
//				String twoLetters = "" + syllableCharArr[i] + syllableCharArr[i+1]; //StringBuilder?
//				if (i+2 < syllable.length()){
//					String threeLetters = twoLetters + syllableCharArr[i+2];
//					Integer intRepresentation = letterToInt.get(threeLetters);
//					if (intRepresentation != null){
//						letters.add(intRepresentation);
//						i += 3;
//						isMultiple = true;
//					}
//				}
//				if (!isMultiple){
//					Integer intRepresentation = letterToInt.get(twoLetters);
//					if (intRepresentation != null){
//						letters.add(intRepresentation);
//						i +=2;
//						isMultiple = true;
//					}
//
//				}
//			}
//			if (!isMultiple){
//				letters.add(letterToInt.get(syllableCharArr[i] +""));
//				if (isVowel(syllableCharArr[i])){
//					if (vowelIndex == -1){
//						vowelIndex = letters.size()-1;
//					}
//					else{
//						String suffixWithWovel = syllable.substring(i-1);
//						if (!isExtra(letterToInt.get(suffixWithWovel))){
//							throw new Exception("cannot parse: " + syllable);
//						}
//						Integer intRepresentation = letterToInt.get(suffixWithWovel);
//						if (intRepresentation == null){
//							throw new Exception("cannot parse: " + syllable);
//						}
//						extra = intRepresentation;
//						letters.remove(letters.size()-1);
//						letters.remove(letters.size()-1);  //remote the ' + vowel
//						if(suffixWithWovel.startsWith("'a")){
//							letters.add(letterToInt.get("'"));
//						}
//						break;
//					}
//				}
//				i++;
//			}
//		}
//		return new Object[]{letters, vowelIndex, extra};
//	}
	
	private static Object[] syllablesToLettersArray(String syllable, boolean ignoreFirstVowelIfA) throws Exception{
		int vowelIndex = -1;
		boolean alreadySawOneWovel = false;
		List<String> letters = new ArrayList<>();
		String extra = "";
		char[] syllableCharArr = syllable.toCharArray();
		int i= 0;
		while(i < syllable.length()){
			boolean isMultiple = false;
			if (isFirstLetterInMultiple(syllableCharArr[i]) && i+1 < syllable.length()){
				String twoLetters = "" + syllableCharArr[i] + syllableCharArr[i+1]; //StringBuilder?
				if (i+2 < syllable.length()){
					String threeLetters = twoLetters + syllableCharArr[i+2];
					if (keys.contains(threeLetters)){
						letters.add(threeLetters);
						i += 3;
						isMultiple = true;
					}
				}
				if (!isMultiple){
					if (keys.contains(twoLetters)){
						letters.add(twoLetters);
						i +=2;
						isMultiple = true;
					}

				}
			}
			if (!isMultiple){
				letters.add(syllableCharArr[i] +"");
				if (isVowel(syllableCharArr[i]) ){	
					if (vowelIndex == -1){
						if (!ignoreFirstVowelIfA || alreadySawOneWovel){
							vowelIndex = letters.size()-1;
						}
						else{  //this mean we ignore a first time and so far didn't see any vowel
							alreadySawOneWovel = true;
							if (syllableCharArr[i] != 'a'){
								vowelIndex = letters.size()-1;
							}
						}
					}
					else{
						String suffixWithWovel = syllable.substring(i-1);
						if (!isAppendedPrefix(suffixWithWovel)){
							throw new Exception("cannot parse: " + syllable);
						}
						if (!keys.contains(suffixWithWovel)){
							throw new Exception("cannot parse: " + syllable);
						}
//						Integer intRepresentation = letterToInt.get(suffixWithWovel);
//						if (intRepresentation == null){
//							throw new Exception("cannot parse: " + syllable);
//						}
						extra = suffixWithWovel;
						letters.remove(letters.size()-1);
						letters.remove(letters.size()-1);  //remote the ' + vowel
						break;
					}
				}
				i++;
			}
		}
		return new Object[]{letters, vowelIndex, extra};
	}
	
	@SuppressWarnings("unchecked")
	private static String[] syllableToVector(String syllable, boolean ignoreFirstA) throws Exception{
		Object[] lettersAndVowelIndex = syllablesToLettersArray(syllable, ignoreFirstA);
		List<String> letters = (List<String>)lettersAndVowelIndex[0];
		int vowelLetterInd = ((Integer)lettersAndVowelIndex[1]).intValue();
		String extra = (String)(lettersAndVowelIndex[2]);
		return stringLettersToVec(letters, vowelLetterInd, extra);	
	}

	
	private byte[] getEncodedVector(String[] v, ISyllableEncoder encoder){
		byte[] encoding = new byte[v.length];
		for (int i = 0; i < encoding.length; i++){
			encoding[i] = encoder.encodeSlot(v[i], i+(v.length == 8 ? 0 : 1));
		}
		return encoding;

	}
	
	public byte[] getEncodedVector(stemType sType, ISyllableEncoder encoder){
		String[] v = getVector(sType);
		return getEncodedVector(v, encoder);
	}
	
	public byte[] getEncodedStemVector(stemType sType, ISyllableEncoder encoder){
		String[] v = getStemVector(stemType.main_stem_norm, 5);
		return getEncodedVector(v, encoder);
	}

	public static String getVectorStr(String[] vector){
		String[] newVec = Arrays.copyOf(vector, vector.length);
		for (int i = 0; i < newVec.length; i++){
			if (newVec[i].isEmpty()){
				newVec[i] = "-";
			}
		}
		return Arrays.toString(newVec);
	}
	
	public static void main(String[] args) throws Exception{
//		System.out.println(letterToInt);
		String input = /*"sgra'ang"*/ "pa'ang";
		SyllableAsVec v = new SyllableAsVec(input);
		System.out.println(v.isStemmed());
		System.out.println(v.getStem(stemType.main_stem_norm));
		System.out.println(getVectorStr(v.getVector(stemType.main_stem)));
		System.out.println(Arrays.toString(v.getVector(stemType.main_stem_norm)));
	//	printGroups();
		//System.out.println(byteToLetter);
	}
	
	public static void printGroups(){
		List<String[]> groups = new ArrayList<String[]>();
		groups.add(new String[] {"k", "kh", "g"});
		groups.add(new String[] {"ng"});
		groups.add(new String[] {"c", "ch", "j", "zh", "sh"});
		groups.add(new String[] {"n"});
		groups.add(new String[] {"t", "th", "d"});
		groups.add(new String[] {"ny"});
		groups.add(new String[] {"p", "ph", "b"});
		groups.add(new String[] {"m"});
		groups.add(new String[] {"ts", "tsh", "dz", "z"});
		for (int i = 0; i < groups.size(); i++){
			String groupStr = "";
			for (String s : groups.get(i)){
				groupStr = "," + s + groupStr;
			}
			groupStr= groupStr.substring(1);
			System.out.println(i + ":" + groupStr);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((stem == null) ? 0 : stem.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SyllableAsVec other = (SyllableAsVec) obj;
		if (stem == null) {
			if (other.stem != null)
				return false;
		} else if (!stem.equals(other.stem))
			return false;
		return true;
	}
	
}
