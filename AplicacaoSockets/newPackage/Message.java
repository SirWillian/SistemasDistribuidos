import enums.*;
import java.io.*;
import java.security.Key;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;

/**
 * Representa a mensagem que é enviada entre peers num grupo multicast.
 * 
 * @author WILLIAN
 */
public class Message implements Serializable{
    private UUID id;
    private byte[] encryptedUUID;
    private EnumMessageType messageType;
    private EnumRecursoId recursoId;
    //Preenche quando messageType for JOIN ou PUBLIC_KEY
    private Key key;
    //Preenche quando messageType for REQUEST ou REPLY
    private Long requestTimestamp;
    //Tempo de envio da mensagem
    private Long timestamp;
    private boolean started;
    
    /**
     *Construtor da mensagem.
     * 
     * @param uuid Identificador do remetente
     * @param mT Tipo da mensagem sendo enviada
     * @param rI Id do recurso sendo requisitado (no caso de um requisição)
     * @param k Chave pública do remetente (quando um novo peer entra no grupo)
     * @param time Timestamp da requisição de um recurso
     * @param encryptKey Chave a ser usada para criptografia (parte do processo da assinatura digital)
     * @param s Flag que representa se recursos já podem ser requisitadods ou não
     */
    public Message(UUID uuid, EnumMessageType mT, EnumRecursoId rI, Key k, Long time, Key encryptKey, boolean s){
        id=uuid;
        messageType=mT;
        recursoId=rI;
        key=k;
        requestTimestamp=time;
        timestamp=System.currentTimeMillis();
        started=s;
        
        //Se não quiser encriptar (por exemplo, ao mandar mensagem para um peer que ainda não possui a chave para decriptar)
        if(encryptKey!=null){
            try {
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.ENCRYPT_MODE, encryptKey);
                encryptedUUID = cipher.doFinal(id.toString().getBytes());
            } catch (Exception ex) {
                Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }
    
    /**
     * Transforma a mensagem em um array de bytes
     * 
     * @return Array de bytes da mensagem
     * @throws IOException
     */
    public byte[] serialize() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(this);
        return out.toByteArray();
    }
    
    /**
     * Converte um array de bytes em um objeto Message
     * 
     * @param data Mensagem serializada
     * @return Mensagem deserializada
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Message deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return (Message) is.readObject();
    }
    
    /**
     * Valida a mensagem usando a chave recebida
     * 
     * @param decryptKey
     * @return
     */
    public boolean validate(Key decryptKey){
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, decryptKey);
            String decryptedUUID = new String(cipher.doFinal(encryptedUUID));
            if(id.toString().equals(decryptedUUID))
                return true;
        } catch (Exception ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Mensagem recebida não pode ser validada");
        return false;
    }
    
    public UUID getUUID(){
        return this.id;
    }
    
    public EnumMessageType getMessageType(){
        return this.messageType;
    }
    
    public EnumRecursoId getRecursoId(){
        return this.recursoId;
    }
    
    public Key getKey(){
        return this.key;
    }
    
    public Long getTimestamp(){
        return this.timestamp;
    }
    
    public Long getRequestTimestamp(){
        return this.requestTimestamp;
    }
    
    public boolean getStarted(){
        return this.started;
    }
}
