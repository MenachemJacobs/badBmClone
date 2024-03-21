package edu.touro.mco152.bm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class DiskMarkTest {
    DiskMark myMark;

    @BeforeEach
    void prep() {
        myMark = new DiskMark(DiskMark.MarkType.READ);
    }

    @ParameterizedTest
    @CsvSource({
            "1",
            "-100",
            "-20",
            "11",
            "0"
    })
    void SetMarkNum(int markNum) {
        myMark.setMarkNum(markNum);
        assertEquals(markNum, myMark.getMarkNum(), "markNum is not being properly set and get in DiskMark");
    }
}
