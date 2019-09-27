package com.mobiquityinc.packer;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Packer {
    private static final char ITEM_VALUES_SEPARATOR = ',';
    private static final int ITEMS_LIMIT = 15;
    private static final int WEIGHT_AND_COST_LIMIT = 100;


    public static String pack(String filePath) throws APIException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), Charsets.UTF_8);
             CharArrayWriter stringWriter = new CharArrayWriter();
             PrintWriter writer = new PrintWriter(new BufferedWriter(stringWriter))
        ) {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                writer.println(parseLineAndFindSolution(line));
            }
            writer.flush();
            return stringWriter.toString();
        } catch (IOException e) {
            throw new APIException(e.getMessage());
        }
    }

    private static String parseLineAndFindSolution(String line) throws APIException {
        PackerCase packerCase = new LineParser().parse(line);
        validateCaseForTaskConstraints(packerCase);
        // add your own logic here
        int itemLength = packerCase.getItems().size();
        float capacity = packerCase.getCapacity();
        StringBuilder index1 = new StringBuilder();

        List<Item> items = new ArrayList<>();
        items = packerCase.getItems();
        Comparator<Item> itemComparator = Comparator.comparing((Item::getCost)).reversed().thenComparing(Item::getWeight);

        List<Item> sortedItemBelowCapacity = items.stream().filter((item) -> item.getWeight() < capacity).sorted(itemComparator).
                collect(Collectors.toList());


        List<Integer> index = new ArrayList<>();
        float freeCapacity = capacity;
        for (int i = 0; i < sortedItemBelowCapacity.size(); i++) {
            if (sortedItemBelowCapacity.get(i).getWeight() < freeCapacity) {
                freeCapacity = freeCapacity - sortedItemBelowCapacity.get(i).getWeight();
                index.add(sortedItemBelowCapacity.get(i).getIndex());
            }
        }
        System.out.println(index);

        return Joiner.on(ITEM_VALUES_SEPARATOR).

                join(items);

    }

    private static void validateCaseForTaskConstraints(PackerCase pack) throws APIException {
        if (pack.getCapacity() > WEIGHT_AND_COST_LIMIT) {
            throw new APIException("The capacity of the package is too high: " + pack.getCapacity());
        }
        if (pack.getItems().size() > ITEMS_LIMIT) {
            throw new APIException("Too much items in the case, should be not more than 15");
        }

        for (Item item : pack.getItems()) {
            if (item.getWeight() > WEIGHT_AND_COST_LIMIT) {
                throw new APIException("The capacity of the item is too high: " + item.getWeight());
            } else if (item.getCost() > WEIGHT_AND_COST_LIMIT) {
                throw new APIException("The cost of the item is too high: " + item.getCost());
            }
        }
    }
}
