package QueryDB.getData;

public class Comparation {
	public String compare() {
		Api api = new Api();
		String string1="car";
		String string2="bus";
		String temp = new StringBuffer("").append("string=").append(string1).append("&").append("string=").append(string2).toString();
		
		String result = api.comparationApi(temp);
	return result;
	}
}
