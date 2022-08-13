package nerd.utopian.javastructurevalidator.util;

import java.io.InputStream;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonFileParserUtil {

  public static String getJsonStringFromFile(String fileLocation) {

    try (InputStream inputStream = JsonFileParserUtil.class.getClassLoader()
        .getResourceAsStream(fileLocation)) {

      JSONTokener jsonTokener = new JSONTokener(inputStream);
      JSONObject jsonObject = new JSONObject(jsonTokener);
      return jsonObject.toString();

    } catch (Exception ex) {

      ex.printStackTrace();
      return null;
    }
  }
}
