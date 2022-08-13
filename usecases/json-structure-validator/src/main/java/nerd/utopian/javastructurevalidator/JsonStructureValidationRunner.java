package nerd.utopian.javastructurevalidator;

import java.util.List;
import nerd.utopian.javastructurevalidator.util.JsonFileParserUtil;
import nerd.utopian.javastructurevalidator.util.JsonStructureValidationUtil;

public class JsonStructureValidationRunner {

  private static final String EXPECTED_JSON_FILE_PATH = "expected/expected.json";
  private static final String ACTUAL_JSON_FILE_PATH = "actual/actual.json";

  public static void main(String[] args) {

    String actualJsonString = JsonFileParserUtil.getJsonStringFromFile(ACTUAL_JSON_FILE_PATH);
    String expectedJsonString = JsonFileParserUtil.getJsonStringFromFile(EXPECTED_JSON_FILE_PATH);

    List<String> jsonStructureValidationResultList = JsonStructureValidationUtil.validateJsonStructure(
        actualJsonString, expectedJsonString);

    JsonStructureValidationUtil.displayResults(jsonStructureValidationResultList);
  }
}
