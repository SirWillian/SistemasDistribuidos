import java.nio.ByteBuffer;
import java.util.UUID;

public class UUIDUtils {

    /**
     * Transforma um UUID em um array de bytes.
     * 
     * @param uuid UUID a ser convertido
     * @return Array de bytes referente ao UUID
     */
    public static byte[] getBytesFromUUID(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());

        return bb.array();
    }

    /**
     * Transforma um array de bytes em um UUID
     * 
     * @param bytes Array de bytes referente a um UUID
     * @return UUID convertido
     */
    public static UUID getUUIDFromBytes(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        Long high = byteBuffer.getLong();
        Long low = byteBuffer.getLong();

        return new UUID(high, low);
    }
}
