import com.lishengzn.comm.util.ByteUtil;
import com.lishengzn.flowlight.packet.FlowPacketModel;
import com.lishengzn.flowlight.util.FlowSocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class GoodsBoxGroupClientHandler implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(GoodsBoxGroupClientHandler.class);
    private volatile boolean terminate;
    private Socket socket;

    public GoodsBoxGroupClientHandler(Socket socket) {
        super();
        this.socket = socket;
        terminate = false;
    }

    @Override
    public void run() {
        try {
            handleSocket();
        } catch (IOException e) {
            close();
            e.printStackTrace();
        }
    }

    public void handleSocket() throws IOException {
        new Thread(() -> listen2Server(socket), "listen2Server" + System.currentTimeMillis()).start();
        new Thread(()-> {
            signalOut(socket);
        }).start();
    }
    private void signalOut(Socket socket) {
        LOG.debug("监听输入信息.....");
        Scanner scanner = new Scanner(System.in);
        int serial = 0;
        while(!terminate){
//        while(!terminate && scanner.hasNextLine()){
//            String str = "@"+scanner .nextLine().replace("\n","").replace("\r","")+"#";
//            String str = scanner .nextLine().replace("\n","").replace("\r","");
//            LOG.debug("sigal out:{}",str);
//            String[] strs = str.split("--");

            FlowPacketModel packetModel = new FlowPacketModel(serial++,"t100000".getBytes());
            LOG.debug("发送按钮命令serial:{}，data:{}",packetModel.getSeriaNo(),packetModel.getData());
            sendMsgToClient(packetModel);
            try {
                Thread.sleep(1234);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void listen2Server(Socket socket) {
        InputStream is = null;
        OutputStream out = null;
        BufferedInputStream bis=null;
        try {
            is = socket.getInputStream();
            bis= new BufferedInputStream(is);
            out = socket.getOutputStream();
            LOG.debug("=============goodsBoxGroupClientHandler start");
            while (!isTerminate()) {
                byte[] packetModel_bytes = null;
                FlowPacketModel packetModel = null;
                // 如果能读取取数据包
                if (!((packetModel_bytes = FlowSocketUtil.readNextPacketData(bis)) == null)) {
                    packetModel = FlowSocketUtil.bytesTransforModel(packetModel_bytes);
                    String data = new String(packetModel.getData());
                    try {
                        if(data.startsWith("PP505")){
                            LOG.debug("接收到灯转换颜色命令serial:{}，data:{}",packetModel.getSeriaNo(),data);
                            sendMsgToClient(new FlowPacketModel(packetModel.getSeriaNo(),1,
                                    "o".getBytes()));
                        }else if(data.startsWith("O")){
                            LOG.debug("接收到优先指令应答serial:{}，data:{}",packetModel.getSeriaNo(),data);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    close();
                }
            }
            LOG.debug("============= goodsBoxGroup tcpClientHandler end");

        } catch (Exception e) {
            e.printStackTrace();
            close();
        }
    }

    public void sendMsgToClient(FlowPacketModel packetModel)  {
        OutputStream os = null;
        try {
            os = socket.getOutputStream();
            FlowSocketUtil.sendPacketData(os, packetModel);

        } catch (IOException e) {
            LOG.error("指令发送异常");
            close();
        }

    }

    public void close() {
        LOG.info("socketHandler closed");
        try {
            socket.shutdownOutput();
            socket.shutdownInput();
            socket.close();
        } catch (IOException e) {
            LOG.error("关闭连接异常",e);
            e.printStackTrace();
        }
        terminate = true;
        System.gc();
    }

    public boolean isTerminate() {
        return terminate;
    }

    public void setTerminate(boolean terminate) {
        this.terminate = terminate;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        LOG.info("====clientHandler finalize:{}");
    }
}
