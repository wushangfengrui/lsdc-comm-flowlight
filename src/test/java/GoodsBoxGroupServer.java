import com.lishengzn.comm.util.ByteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteOrder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GoodsBoxGroupServer {

    private static final Logger LOG = LoggerFactory.getLogger(GoodsBoxGroupServer.class);
    private ServerSocket serverSocket = null;
    private static int port;
    public static GoodsBoxGroupServer getInstance() {
        return ServerInstance.serverInstance;
    }

    private ExecutorService threadPool = Executors.newCachedThreadPool();

    private static class ServerInstance {
        final static GoodsBoxGroupServer serverInstance = getServer();

        private static GoodsBoxGroupServer getServer() {
            try {
                return new GoodsBoxGroupServer(port);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * @param port
     * @throws IOException
     */
    private GoodsBoxGroupServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        LOG.info("在端口" + port + "开启服务");
    }

    public Socket getSocket() throws IOException {
        return serverSocket.accept();
    }

    public void doSocket() throws Exception {
        while (true) {
            GoodsBoxGroupClientHandler clientHandler = null;
            Socket socket = getSocket();
            LOG.info("客户端连接：" + socket.getInetAddress().getHostAddress());
            clientHandler = new GoodsBoxGroupClientHandler(socket);
            threadPool.execute(clientHandler);
        }
    }


    public static void main(String[] args) throws Exception {
        port = args.length>0?Integer.valueOf(args[0]):5003;
        GoodsBoxGroupServer socketServer = GoodsBoxGroupServer.getInstance();
        socketServer.doSocket();
//        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
//        System.out.println(path);
    }
}
