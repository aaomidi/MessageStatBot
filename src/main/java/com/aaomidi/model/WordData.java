package com.aaomidi.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Created by amir on 2015-11-27.
 */
@RequiredArgsConstructor
public class WordData {
    @Getter
    private final String word;
    @Getter
    @Setter
    private int count = 0;
}
