package syllRep;


import SyllableRepr.SyllableAsVec;

public class SimpleSillableTester {
	public static void main(String[] args){
		String input = "bzhin";
		SyllableAsVec v = new SyllableAsVec(input);
		System.out.println(v.isStemmed());
//		System.out.println(v.getStem());
//		System.out.println(v.getPreScript());
	}
}
