import enums.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.security.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class MulticastTransceiver extends Thread{
    private MulticastSocket s;
    private MulticastSocket sRequest;
    private InetAddress group;
    private UUID uniqueId;
    private PublicKey pubKey;
    private PrivateKey privKey;
    //Lista de peers conectados e suas chaves
    private Map<UUID, Key> idKeyDict;
    //Lista de espera dos recursos (caso haja a tentativa de acesso e o recurso já esteja sendo usado)
    private Map<EnumRecursoId,Set<UUID>> waitingLists;
    //Estado de acesso dos recursos
    private EnumRecursoState[] heldResources = {EnumRecursoState.RELEASED,EnumRecursoState.RELEASED};
    //Timestamps de quando um recurso foi requisitado. Se for 0, significa que não há requisição pendente nem posse do recurso
    private Long[] requestTimestamps = {Long.valueOf(0),Long.valueOf(0)};
    //Indica se o sistema já iniciou (se chegou em 3 peers conectados em algum momento)
    private boolean started = false;
    
    public MulticastTransceiver(MulticastSocket socket, MulticastSocket socketRequest, InetAddress g, UUID id){
        s = socket;
        sRequest = socketRequest;
        group = g;
        uniqueId = id;
        idKeyDict = new HashMap<>();
        waitingLists = new HashMap<>();
        try {
            //Gera chaves
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(512);
            KeyPair pair = keyGen.generateKeyPair();
            pubKey = pair.getPublic();
            privKey = pair.getPrivate();
        } 
        catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithm; " + e.getMessage());
        }
    }
    
    /**
     * Loop principal da thread.
     * 
     * Espera por mensagens do grupo multicast constantemente.
     */
    @Override
    public void run(){
        while(true) {
            try{
                byte[] buffer = new byte[1000];
                Message message;
                DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
                s.receive(messageIn);
                
                message=Message.deserialize(messageIn.getData());
                interpretMessage(message);
            }
            catch(Exception e){
                System.out.println("Exception: " + e.getMessage());
                break;
            }
        }
    }
    
    /**
     * Envia uma mensagem ao grupo multicast.
     * 
     * @param message
     */
    private void sendMessage(Message message){
        try{
            byte[] out = message.serialize();
            DatagramPacket messageOut = new DatagramPacket(out, out.length, group, 6789);
            s.send(messageOut);
        }
        catch(IOException e){
            System.out.println("IO; " + e.getMessage());
        }
    }
    
    /**
     * Notifica o grupo da entrada de um novo peer.
     */
    public void joinGroup(){
        sendMessage(new Message(this.uniqueId, EnumMessageType.JOIN, EnumRecursoId.NENHUM_RECURSO, pubKey, null, null,started));
    }
    
    /**
     * Notifica o grupo da saída de um peer.
     * Libera recursos que estejam sendo utilizados antes de sair.
     */
    public void leaveGroup(){
        for(int i=0; i<heldResources.length; i++){
            if(heldResources[i]==EnumRecursoState.HELD)
                releaseResource(EnumRecursoId.values()[i]);
        }
        sendMessage(new Message(this.uniqueId, EnumMessageType.LEAVE, EnumRecursoId.NENHUM_RECURSO, null, null, privKey,started));
    }
    
    /**
     * Requisita um recurso.
     * Caso um peer não responda à requisição a tempo, ele é removido da lista de peers conectados.
     *
     * @param recurso Recurso sendo requisitado
     */
    public void requestResource(EnumRecursoId recurso){
        if(started){
            if(heldResources[recurso.ordinal()]!=EnumRecursoState.HELD){
                heldResources[recurso.ordinal()]=EnumRecursoState.WANTED;
                //Preserva a timestamp no caso refazer o pedido no estado WANTED.
                if(requestTimestamps[recurso.ordinal()]==0)
                    requestTimestamps[recurso.ordinal()]=System.currentTimeMillis();

                sendMessage(new Message(this.uniqueId,EnumMessageType.REQUEST,recurso,null,requestTimestamps[recurso.ordinal()],privKey,started));
                Set<Message> respostas = new HashSet<Message>();

                //Enquanto não recebeu todas as respostas
                while(respostas.size()<idKeyDict.size()){
                    try{
                        respostas.add(receiveReply(requestTimestamps[recurso.ordinal()],recurso));
                    }
                    catch (SocketTimeoutException e){
                        //Tira da lista quem não respondeu
                        Set<UUID> peersRespostas = respostas.stream()
                                .map(resposta -> resposta.getUUID()).collect(Collectors.toSet());

                        Set<UUID> peersConectados = new HashSet<>(idKeyDict.keySet());
                        for(UUID peer : peersConectados){
                            if(!peersRespostas.contains(peer))
                                idKeyDict.remove(peer);
                        }
                    }
                }
                //Cria lista de espera com todos os peers e remove os que permitiram acesso
                Set<UUID> listaEspera = new HashSet<>(idKeyDict.keySet());
                for(Message r : respostas){
                    //Timestamp = 0 significa estado RELEASED
                    if(r.getRequestTimestamp()==0 || r.getRequestTimestamp() > requestTimestamps[recurso.ordinal()])
                        listaEspera.remove(r.getUUID());
                }
                //Se ninguem espera o recurso, então eu tenho acesso
                if(listaEspera.isEmpty()){
                    heldResources[recurso.ordinal()]=EnumRecursoState.HELD;
                    System.out.println("Acesso permitido ao recurso " + recurso.toString());
                }
                else{
                    waitingLists.put(recurso, listaEspera);
                    System.out.println("Colocado na fila de espera de acesso ao recurso " + recurso.toString());
                }
            }
            else
                System.out.println("Já possui o recurso requisitado!");
        }
        else
            System.out.println("O mínimo de peers conectados não foi atingido ainda.");
    }
    
    /**
     * Espera por mensagens do tipo REPLY.
     * Limpa o buffer de mensagens do socket e retorna a primeira REPLY válida.
     * 
     * @param startTimestamp Timestamp da requisição
     * @param recurso Recurso sendo requisitado
     * @return Mensagem de reply
     */
    private Message receiveReply(Long startTimestamp, EnumRecursoId recurso) throws SocketTimeoutException{
        try{
            while(true){
                byte[] buffer = new byte[1000];
                Message message;
                DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
                sRequest.receive(messageIn);

                message=Message.deserialize(messageIn.getData());
                //Limpa o buffer e retorna se receber um REPLY sobre o recurso desejado que não tenha vindo de mim mesmo
                if(message.getTimestamp()>=startTimestamp && message.getMessageType()==EnumMessageType.REPLY 
                        && message.getRecursoId()==recurso && uniqueId.compareTo(message.getUUID())!=0)
                    return message;
            }
                
        }
        catch(SocketTimeoutException e){
            throw new SocketTimeoutException();
        }
        catch(Exception e){
            System.out.println("Exception: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Libera um recurso.
     * 
     * @param recurso Recurso a ser liberado
     */
    public void releaseResource(EnumRecursoId recurso){
        if(heldResources[recurso.ordinal()]==EnumRecursoState.HELD){
            heldResources[recurso.ordinal()]=EnumRecursoState.RELEASED;
            requestTimestamps[recurso.ordinal()]=Long.valueOf(0);
            sendMessage(new Message(this.uniqueId,EnumMessageType.RELEASE,recurso,null,null,privKey,started));
            System.out.println("Recurso " + recurso.toString() + " liberado.");
        }
        else
            System.out.println("Não há como liberar o recurso sem ter acesso a ele!");
    }
    
    /**
     * Interpreta o significado de uma mensagem recebida.
     * Apenas mensagens válidas são interpretadas. Mensagens recebidas pelo próprio peer que a enviou também são filtradas.
     * 
     * @param message Mensagem a ser interpretada
     */
    private void interpretMessage(Message message){
        UUID incomingUUID = message.getUUID();
        
        //Se não for mensagem de si mesmo
        if(incomingUUID.compareTo(uniqueId)!=0){
            boolean validMessage=true;
            
            //Se a mensagem não é do tipo JOIN nem PUBLIC_KEY, tenta validar a mensagem
            if(message.getMessageType()!=EnumMessageType.JOIN && message.getMessageType()!=EnumMessageType.PUBLIC_KEY){
                //Não deve ser possível receber mensagens que não são do tipo JOIN ou PUBLIC_KEY sem ter a chave registrada
                if(!idKeyDict.containsKey(incomingUUID))
                    validMessage=false;
                validMessage=message.validate(idKeyDict.get(incomingUUID));
            }
            //Se não, se não possui a chave, a mensagem é inválida (não deve ser possível receber mensagens de um peer não registrado)
            

            if(validMessage){
                System.out.println("Recebeu " + message.getMessageType().toString() + " de " + incomingUUID.toString());
                switch(message.getMessageType()){
                    case JOIN:
                        idKeyDict.put(incomingUUID, message.getKey());
                        //Se houverem 2 peers conectados além de mim mesmo, a condição de acesso aos recursos foi cumprida
                        if(idKeyDict.size()==2)
                            started=true;
                        sendMessage(new Message(uniqueId,EnumMessageType.PUBLIC_KEY,EnumRecursoId.NENHUM_RECURSO,pubKey,null,null,started));
                        break;
                    case PUBLIC_KEY:
                        //
                        started=(started || message.getStarted());
                        //Registra apenas se não contém a chave
                        if(!idKeyDict.containsKey(incomingUUID))
                            idKeyDict.put(incomingUUID, message.getKey());               
                        break;
                    case REQUEST:
                        sendMessage(new Message(uniqueId,EnumMessageType.REPLY, message.getRecursoId(), null, requestTimestamps[message.getRecursoId().ordinal()], privKey,started));
                        break;
                    case RELEASE:
                        //Se eu quero acesso ao recurso, atualizo a lista de espera
                        if(heldResources[message.getRecursoId().ordinal()]==EnumRecursoState.WANTED){
                            Set<UUID> listaEspera=waitingLists.get(message.getRecursoId());
                            if(listaEspera.contains(message.getUUID())){
                                listaEspera.remove(message.getUUID());
                            }
                            
                            //Se a lista está vazia, eu tenho acesso ao recurso
                            if(listaEspera.isEmpty()){
                                heldResources[message.getRecursoId().ordinal()]=EnumRecursoState.HELD;
                                System.out.println("Acesso permitido ao recurso " + message.getRecursoId().toString());
                            }
                        }
                        break;
                    case LEAVE:
                        idKeyDict.remove(incomingUUID);
                        break;
                }
            }
            else
                System.out.println("Não foi possível validar a mensagem recebida");
        }
    }
}