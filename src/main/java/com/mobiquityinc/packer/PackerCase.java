package com.mobiquityinc.packer;

import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class PackerCase {

    private float capacity;
    private List<Item> items;

    PackerCase(List<Item> items, float capacity) {
        this.capacity = capacity;
        this.items = Collections.unmodifiableList(requireNonNull(items));
    }

    float getCapacity() {
        return capacity;
    }

    List<Item> getItems() {
        return items;
    }
}
