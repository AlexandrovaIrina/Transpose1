import Transposer.Transposer;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;


import java.io.File;
import java.io.IOException;

import static org.apache.commons.io.FileUtils.contentEquals;
import static org.junit.jupiter.api.Assertions.*;

class TransposeTest {
    @Test

    void transposeTest() throws IOException {
        final File transposed = new File("files/outputFileName.txt");

        if (transposed.length() != 0) FileUtils.write(transposed, "");
        Transposer transposer = new Transposer(3, false, false);
        transposer.transpose("files/input1.txt", "files/" + transposed.getName());
        assertTrue(contentEquals(transposed, new File("files/output1.txt")));


        transposer = new Transposer(0, false, false);
        transposer.transpose("files/input2.txt", "files/" + transposed.getName());
        assertTrue(contentEquals(transposed, new File("files/output2_1.txt")));

        transposer = new Transposer(4, false, true);
        transposer.transpose("files/input2.txt", "files/" + transposed.getName());
        assertTrue(contentEquals(transposed, new File("files/output2_2.txt")));

        transposer = new Transposer(2, true, true);
        transposer.transpose("files/input3.txt", "files/" + transposed.getName());
        assertTrue(contentEquals(transposed, new File("files/output3.txt")));

        transposer = new Transposer(true, true);
        transposer.transpose("files/input4.txt", "files/" + transposed.getName());
        assertTrue(contentEquals(transposed, new File("files/output4.txt")));

        transposer = new Transposer(5,true, false);
        transposer.transpose("files/input4.txt", "files/" + transposed.getName());
        assertTrue(contentEquals(transposed, new File("files/output5.txt")));


    }


}