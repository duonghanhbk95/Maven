package QueryDB.getData;

public class Comparation {

    public Float compare(String str1, String str2) {
        
         
        String string1 = str1.replace(" ", "_").toLowerCase();
        String string2 = str2.replace(" ", "_").toLowerCase();

        if (str1.length() == 0 && str2.length() == 0) {
            float result = 0;
            return result;
        } else {
            String temp = new StringBuffer("").append("string=").append(string1).append("&").append("string=").append(string2).toString();

            String result = Api.comparationApi(temp);

            return Float.parseFloat(result);
        }
    }
}
