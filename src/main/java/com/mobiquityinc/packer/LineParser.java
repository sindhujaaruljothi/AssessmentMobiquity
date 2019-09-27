package com.mobiquityinc.packer;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class LineParser {

    private static final String INPUT_LINE_FORMAT_REGEX =
            "^\\s*\\d+(\\.\\d+)? *: *(\\( *\\d+ *, *\\d+(\\.\\d+)? *, *€\\d+(\\.\\d+)? *\\) *)+\\s*$";
    private static final String INPUT_LINE_SEPARATORS = " :()";
    private static final String ITEM_VALUES_SEPARATORS = " ,€";

    PackerCase parse(String line) throws APIException {
        validateInputLine(line);
        List<String> tokens = Splitter.on(CharMatcher.anyOf(INPUT_LINE_SEPARATORS)).omitEmptyStrings().splitToList(line);
        float capacity = Float.parseFloat(tokens.get(0));
        List<Item> items = parseTokenItems(tokens);
        checkForIndexesDuplication(items);
        return new PackerCase(items, capacity);
    }

    private static void validateInputLine(String line) throws APIException {
        if (line == null) {
            throw new APIException("Input line is null");
        }
        if (line.isEmpty()) {
            throw new APIException("Input line is empty");
        }
        if (!line.matches(INPUT_LINE_FORMAT_REGEX)) {
            throw new APIException("Input line has wrong format \"" + line + "\"");
        }
    }

    private static List<Item> parseTokenItems(List<String> tokens) {
        Splitter splitter = Splitter.on(CharMatcher.anyOf(ITEM_VALUES_SEPARATORS)).omitEmptyStrings();
        return tokens.stream()
                .skip(1)
                .map(splitter::splitToList)
                .map(LineParser::convertItemValuesToItem)
                .sorted(Comparator.comparingInt(Item::getIndex))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private static Item convertItemValuesToItem(List<String> itemValues) {
        return new Item(Integer.parseInt(itemValues.get(0)), Float.parseFloat(itemValues.get(1)),
                Float.parseFloat(itemValues.get(2)));
    }

    private static void checkForIndexesDuplication(List<Item> items) throws APIException {
        Item prevItem = null;
        for (Item item : items) {
            if (prevItem != null && prevItem.getIndex() == item.getIndex()) {
                throw new APIException("There are items indexes duplication: 1" + item.getIndex());
            }
            prevItem = item;
        }
    }
}