public class Test {
    private static final int POLYNOMIAL = 0xA001;
    @org.junit.Test
    public void test01(){
        byte[] data = {(byte)0xFA, (byte)0xFC, (byte)0x82, (byte)0x89, (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x0E, (byte)0x06, (byte)0x06};
        int crc = 0xFFFF;

        for (byte b : data) {
            crc ^= b & 0xFF;

            for (int i = 0; i < 8; i++) {
                if ((crc & 0x0001) != 0) {
                    crc = (crc >>> 1) ^ POLYNOMIAL;
                } else {
                    crc >>>= 1;
                }
            }
        }
        System.out.println(crc & 0xFFFF);

    }
}
