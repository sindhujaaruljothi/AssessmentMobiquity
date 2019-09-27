package com.mobiquityinc.packer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PackerTest {


    @Test
    public void checkNoFileThrowsException() throws IOException, APIException {
        String filePath = "testfile1.txt";
        assertThrows(APIException.class, () -> Packer.pack(filePath));
    }

    @Test
    public void checkRightThingsArePacked() throws APIException {
        String filepath = "src/test/resources/testfile.txt";
        String lines = Packer.pack(filepath);

    }


}