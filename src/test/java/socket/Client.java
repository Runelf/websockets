package socket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

public class Client extends WebSocketClient {
    private final SocketContext context;
    private Date openedTime;

    public Client(SocketContext context) throws URISyntaxException {
        super(new URI(context.getURI()));
        this.context = context;
    }


    @Override
    public void onOpen(ServerHandshake handshakedata) {
        openedTime = new Date();
        System.out.println("Opened Connection " + context.getURI());
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received new message " + message);
        context.getMessageList().add(message);
        if(message.equals(context.getExpectedMessage())){
            closeConnection(1000, "Received expected message");
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Close socket with code "+ code + ", reason is " + reason );
        context.setStatusCode(code);
    }

    @Override
    public void onError(Exception ex) {

    }

    public int getAliveTime(){
        Date closedDate = new Date();
        int timeIneSeconds = (int) (closedDate.getTime() - openedTime.getTime()) / 1000;
        context.setTimeTaken(timeIneSeconds);
        return timeIneSeconds;
    }
}
