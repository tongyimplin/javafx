import org.junit.Test;
import top.jafar.fx.oracle.transfer.util.DateHelper;
import top.jafar.fx.oracle.transfer.util.FileHelper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class FileHelperTest {

    @Test
    public void writableFileTest(){
        try(FileHelper writableFile = FileHelper.getWritableFile("sql-log");) {
            writableFile.append("ok");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void dateStrTest() {
        String s = DateHelper.nowTimeStampPureNumber();
        System.out.println(s);
    }

    @Test
    public void testStr() {
        String str = "迁移";
        try {
            System.out.println(new String(str.getBytes("utf-8"), "gbk"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
