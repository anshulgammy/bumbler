package nerd.utopian.javastructurevalidator.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonStructureValidationUtil {

  private static final String JSON_STRUCTURE_EQUAL = "Actual and Expected JSON Structures are Equal!";
  private static final String JSON_STRUCTURE_MISSING_ELEMENTS = "Actual JSON Structure has Missing Elements:";
  private static final String JSON_STRUCTURE_ADDITIONAL_ELEMENTS = "Actual JSON Structure has Additional Elements:";

  public static List<String> validateJsonStructure(String actualJsonString,
      String expectedJsonString) {

    Preconditions.checkArgument(StringUtils.isNotEmpty(actualJsonString),
        "actualJsonString cannot be empty");
    Preconditions.checkArgument(StringUtils.isNotEmpty(expectedJsonString),
        "expectedJsonString cannot be empty");

    List<String> jsonStructureValidationResultList = new ArrayList<>();

    List<String> jsonPathListActual = new ImmutableList.Builder<String>().addAll(
        getJsonPathList(actualJsonString)).build();

    List<String> jsonPathListExpected = new ImmutableList.Builder<String>().addAll(
        getJsonPathList(expectedJsonString)).build();

    boolean structureEqual = validateEquality(jsonPathListExpected, jsonPathListActual);

    if (structureEqual) {
      jsonStructureValidationResultList.add(JSON_STRUCTURE_EQUAL);

    } else {
      populateMissingElements(jsonPathListExpected, jsonPathListActual,
          jsonStructureValidationResultList);

      populateAdditionalElements(jsonPathListExpected, jsonPathListActual,
          jsonStructureValidationResultList);
    }

    return jsonStructureValidationResultList;
  }

  private static void populateAdditionalElements(List<String> jsonPathListExpected,
      List<String> jsonPathListActual, List<String> jsonStructureValidationResultList) {

    List<String> jsonPathListAct = new ArrayList<>(jsonPathListActual);

    List<String> jsonPathListExp = new ArrayList<>(jsonPathListExpected);

    // In case of additional elements, JsonPath for actual will always contain more.
    jsonPathListAct.removeAll(jsonPathListExp);

    if (!jsonPathListAct.isEmpty()) {
      jsonStructureValidationResultList.add(JSON_STRUCTURE_ADDITIONAL_ELEMENTS);
      jsonStructureValidationResultList.addAll(jsonPathListAct);
    }
  }

  private static void populateMissingElements(List<String> jsonPathListExpected,
      List<String> jsonPathListActual, List<String> jsonStructureValidationResultList) {

    List<String> jsonPathListAct = new ArrayList<>(jsonPathListActual);

    List<String> jsonPathListExp = new ArrayList<>(jsonPathListExpected);

    // In case of missing elements, JsonPath for expected will always contain more.
    jsonPathListExp.removeAll(jsonPathListAct);

    if (!jsonPathListExp.isEmpty()) {
      jsonStructureValidationResultList.add(JSON_STRUCTURE_MISSING_ELEMENTS);
      jsonStructureValidationResultList.addAll(jsonPathListExp);
    }
  }

  private static boolean validateEquality(List<String> jsonPathListActual,
      List<String> jsonPathListExpected) {

    // JsonPath should be equal.
    return jsonPathListActual.containsAll(jsonPathListExpected);
  }

  private static List<String> getJsonPathList(String jsonString) {

    Set<String> jsonKeySet = new LinkedHashSet<>();

    JSONObject jsonObject = new JSONObject(jsonString);

    populateKeys(jsonObject, jsonKeySet);

    List<String> jsonPathList = new ArrayList<>();

    for (String key : jsonKeySet) {

      if (key != null) {
        Configuration configuration = Configuration.builder().options(Option.AS_PATH_LIST).build();
        List<String> currentPathList = JsonPath.using(configuration).parse(jsonString)
            .read("$.." + key + "");

        // flattening List<List<String>>
        jsonPathList.addAll(currentPathList.stream().flatMap(Stream::of)
            .collect(Collectors.toList()));
      }
    }

    return jsonPathList;
  }

  private static String populateKeys(JSONObject jsonObject, Set<String> jsonKeySet) {

    JSONArray keys = jsonObject.names();

    for (int i = 0; Objects.nonNull(keys) && i < keys.length(); i++) {

      String currentKey = keys.get(i).toString();

      // Checking if the current key has a JSONObject or a JSONArray.
      // In case of JSONObject, will generate keys recursively, till are keys are obtained.
      if (jsonObject.get(currentKey).getClass().getName().equals(JSONObject.class.getName())) {
        jsonKeySet.add(currentKey);
        jsonKeySet.add(populateKeys((JSONObject) jsonObject.get(currentKey), jsonKeySet));

      } else if (jsonObject.get(currentKey).getClass().getName()
          .equals(JSONArray.class.getName())) {

        for (int j = 0; j < ((JSONArray) jsonObject.get(currentKey)).length(); j++) {

          // Checking if the current key has a JSONObject or a JSONArray.
          // In case of JSONObject, will generate keys recursively, till are keys are obtained.
          if (((JSONArray) jsonObject.get(currentKey)).get(j).getClass().getName()
              .equals(JSONObject.class.getName())) {
            jsonKeySet.add(currentKey);
            jsonKeySet.add(
                populateKeys((JSONObject) ((JSONArray) jsonObject.get(currentKey)).get(j),
                    jsonKeySet));

          } else {
            jsonKeySet.add(currentKey);
          }
        }

      } else {
        jsonKeySet.add(currentKey);
      }
    }
    return null;
  }

  public static void displayResults(List<String> jsonStructureValidationResultList) {

    for (String structure : jsonStructureValidationResultList) {
      System.out.println(structure);
    }
  }
}
