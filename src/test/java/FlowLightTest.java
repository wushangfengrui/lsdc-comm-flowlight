import com.lishengzn.comm.pool.ObjectPool;
import com.lishengzn.flowlight.communication.FlowTCPClient;
import com.lishengzn.flowlight.communication.TCPClientFactory;
import com.lishengzn.flowlight.communication.imp.FlowLightReceiveServiceImp;
import com.lishengzn.flowlight.communication.imp.FlowLightSenderServiceImp;
import com.lishengzn.flowlight.entry.FlowLightControlCabinet;
import com.lishengzn.flowlight.instruct.ModeArray;

import java.io.IOException;

public class FlowLightTest {

    public static void main(String[] args) throws IOException {

        FlowLightControlCabinet flowLightController = new FlowLightControlCabinet();
        flowLightController.setIp("192.168.1.254");
        flowLightController.setDeviceId("controller");
        flowLightController.setPort(5003);

        FlowTCPClient<FlowLightControlCabinet, FlowLightSenderServiceImp, FlowLightReceiveServiceImp> client =
                TCPClientFactory.createFlowLightTCPClient(flowLightController);
        ObjectPool.addPoolObj(client);
        client.getMessageSenderService().sendZMode();
        client.getMessageSenderService().sendStartOperation("1000");
        client.getMessageSenderService().sendLightColor("1000", ModeArray.Normally_Audio_yellow);
        client.getMessageSenderService().sendStartOperation("1001");
        client.getMessageSenderService().sendLightColor("1001", ModeArray.Normally_Audio_yellow);
        client.getMessageSenderService().sendStartOperation("1002");
        client.getMessageSenderService().sendLightColor("1002", ModeArray.Normally_Audio_yellow);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        client.getMessageSenderService().sendStartOperation("1002");
        client.getMessageSenderService().sendLightColor("1002", ModeArray.Normally_Audio_blue);
    }


}
