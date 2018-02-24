package syllableRep;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleVectorEncoder implements ISyllableEncoder{
	public static List<String> keys = Arrays.asList("", "k", "g", "g.", "c", "j", "t", "d", "n", "p", "b", "m", "w", "z",
			"'", "y", "r", "l", "s", "h", "a", "kh", "ng", "ch", "ny", "th", "ph", "ts", "dz", "zh", "sh", "tsh", "i",
			"u", "e", "o", "'i", "'is", "'ang", "'am", "'o" );
	private final static int values[] = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
			21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40 };
	public final static Map<String, Byte> letterToByte = new HashMap<>();
	public final static Map<Byte, String> byteToLetter = new HashMap<>();
	static{
		for (int i= 0; i < keys.size(); i++){
			letterToByte.put(keys.get(i), (byte)values[i]);
			byteToLetter.put((byte)values[i], keys.get(i));
		}
	}
	@Override
	public byte encodeSlot(String letter, int position) {
		return letterToByte.get(letter);
	}

}
