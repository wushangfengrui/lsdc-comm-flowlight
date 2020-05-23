package com.lishengzn.flowlight.communication;

import com.lishengzn.comm.communication.service.MessageReceiverService;
import com.lishengzn.comm.communication.service.MessageSenderService;
import com.lishengzn.comm.entity.device.ServerDevice;
import com.lishengzn.comm.exception.SimpleException;
import com.lishengzn.comm.pool.ObjectPool;
import com.lishengzn.comm.pool.PoolObj;
import com.lishengzn.flowlight.constants.FlowConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;


public class FlowTCPClient<T extends ServerDevice,Ts extends MessageSenderService<Socket>,Tr extends MessageReceiverService> implements PoolObj {
	private static final Logger LOG = LoggerFactory.getLogger(FlowTCPClient.class);
	private Socket socket;
	private String ip;
	private int port;
	/** 客户端类型，用来区分连接到的目标服务端是小车还是闸口、镜头 */
	private ClientType clientType;
	private int reconnectTimes = 0;

	/** 连接到的设备的信息，（小车基本信息对象/闸口信息对象） */
	private T device;
	private Ts messageSenderService;
	private Tr messageReceiveService;

	public FlowTCPClient(T device, ClientType clientType){
		this.device = device;
		this.ip = device.getIp();
		this.port = device.getPort();
		this.clientType = clientType;
	}
	public void  initialize(Ts messageSenderService, Tr messageReceiveService) throws IOException {
		this.socket = new Socket(device.getIp(), device.getPort());
		socket.setTcpNoDelay(true);
		this.messageSenderService = messageSenderService;
		this.messageReceiveService = messageReceiveService;
		messageSenderService.setDevice(device);
		messageReceiveService.setDevice(device);
		messageSenderService.setMaster(socket);
		messageReceiveService.setMaster(socket);
		messageSenderService.initialize();
		messageReceiveService.initialize();
	}

	public void reconnect(){
		reconnectTimes++;
		try {
			Thread.sleep(1000);
			if(reconnectTimes > FlowConstants.MAC_RECONNECT_TIME){
				throw new SimpleException("灯控制器重新连接失败");
			}
			this.socket = new Socket(device.getIp(), device.getPort());
			socket.setTcpNoDelay(true);
			reconnectTimes = 0;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			reconnect();
		}

	}

	public void close(){
		LOG.info("与设备连接关闭,IP：{},port：{},clientType:{}",ip,port,clientType);
		try {
			if(socket!=null){
				socket.shutdownInput();
				socket.shutdownOutput();
				socket.close();
			}
			messageSenderService.terminate();
			messageReceiveService.terminate();
			ObjectPool.removePoolObj(this);
			device.setOnline(false);
		} catch (IOException e1) {
			LOG.error("关闭连接失败",e1);
			e1.printStackTrace();
			throw new SimpleException("关闭连接失败");
		}
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	public Ts getMessageSenderService() {
		return  messageSenderService;
	}

	public Tr getMessageReceiveService() {
		return messageReceiveService;
	}

	public ClientType getClientType() {
		return clientType;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		LOG.debug("====client finalize:{},{}",ip,clientType);
	}

	@Override
	public String getPoolKey() {
		return this.ip;
	}

	public enum ClientType {
		/** 小车 */
		VEHICLE,

		/** 物料箱 */
		GOODS_BOX,

		/** 工控机 */
		INDUSTRIAL_COMPUTER;
	}
}
