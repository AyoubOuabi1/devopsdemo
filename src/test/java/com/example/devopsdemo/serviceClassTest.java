package com.example.devopsdemo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class serviceClassTest {

    @Test
    void testService() {
        assertFalse(serviceClass.isService());
    }
}