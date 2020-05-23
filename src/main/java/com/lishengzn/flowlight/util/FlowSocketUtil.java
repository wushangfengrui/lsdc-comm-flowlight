package com.lishengzn.flowlight.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.primitives.Bytes;
import com.lishengzn.comm.communication.service.MessageSenderService;
import com.lishengzn.comm.entity.sync.RequestSyncObj;
import com.lishengzn.comm.exception.SimpleException;
import com.lishengzn.flowlight.communication.TCPClientFactory;
import com.lishengzn.flowlight.constants.FlowConstants;
import com.lishengzn.flowlight.packet.FlowPacketModel;
import com.lishengzn.flowlight.packet.FlowSeriaNo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/** 通讯协议工具类
 *
 名称			内容	长度（byte）
 报文同步头		 2	    1
 序号					3
 数据区长度				4
 数据区		取决于头部中的数据区长度
 报文同步头		 3	    1
 **/
public class FlowSocketUtil {
	private static final Logger LOG = LoggerFactory.getLogger(FlowSocketUtil.class);

	/**
	 * 读取下一个数据包
	 *
	 * @param in 输入流
	 * @return 从输入流解读出的下一个数据包
	 * @throws IOException
	 */
	public static byte[] readNextPacketData(InputStream in) throws IOException {
		synchronized (in) {
			// 获取开头
			readSyncHead(in, FlowConstants.LED_SYNC_STX);
			// 读取整条信息
			return readSyncBody(in, FlowConstants.LED_SYNC_ETX);
		}
	}

	private static int readSyncHead(InputStream in, byte head) throws IOException {
		synchronized (in) {
			int readData = -1;
			while ((byte) readData != head) {
				readData = in.read();
			}
			return readData;
		}
	}

	private static byte[] readSyncBody(InputStream in, byte end) throws IOException {
		List<Byte> byteList = new ArrayList<>();
		byteList.add(FlowConstants.LED_SYNC_STX);
		synchronized (in) {
			int readData = -1;
			while ((byte) readData != end) {
				readData = in.read();
				byteList.add((byte) readData);
			}
			return Bytes.toArray(byteList);
		}
	}

	public static FlowPacketModel bytesTransforModel(byte[] flowData) {
		byte seriaNo_bytes[] = Arrays.copyOfRange(flowData, 1, 4);
		int seriaNo = BinaryAssemblyUtil.bytes2Int(seriaNo_bytes);// 数据序号
		byte dataLength_bytes[] = Arrays.copyOfRange(flowData, 4, 8);
		int dataLength = BinaryAssemblyUtil.bytes2Int(dataLength_bytes);// 数据序号
		byte data[] = Arrays.copyOfRange(flowData, 8, 8 + dataLength);
		return new FlowPacketModel(seriaNo, dataLength, data);
	}


	/**
	 * 发送 数据包
	 *
	 * @param out
	 * @param packetModel
	 * @throws IOException
	 */
	public static void sendPacketData(OutputStream out, FlowPacketModel packetModel) throws IOException {
		synchronized (out) {
			byte[] sendBytes = packetModel.toBytes();
			out.write(sendBytes);
		}
	}

	/**
	 * 根据包类型获取 应答包的报类型
	 *
	 * @param packetType
	 * @return
	 */
	public static short getResponsePacketType(short packetType) {
		//short responsePacketType = (short)(packetType + SocketConstants.RESPONSE_PACKET_ADDED);
		return 0;//responsePacketType;
	}

	public static void confirmSendCmd(String message, FlowPacketModel packetModel) {
//		LOG.info("指令字节：{}",Arrays.toString(packetModel.toBytes()));
		// TODO
/*		boolean confirm = MainFrame.showConfirmDialog("是否确认发送",message);
		if(!confirm){
			throw new SimpleException("取消发送指令,任务中止");
		}else{
			LOG.info("确认发送指令，任务继续");
		}*/
	}

	public static FlowPacketModel sendRequest(FlowPacketModel packetModel, RequestSyncObj<FlowPacketModel> syncObj, MessageSenderService<Socket> messageSenderService) {
		try {
			OutputStream out = messageSenderService.getMaster().getOutputStream();
			synchronized (syncObj) {
				FlowPacketModel responseFlowPacketModel = syncObj.poll();// 先取一下，保证发送请求的时候，队列真空
				if (syncObj.getSendTimes() == 0 && responseFlowPacketModel != null) {
					LOG.debug("弃置packet...,队列长度：{},seriano:{}", syncObj.size() + 1, responseFlowPacketModel.getSeriaNo());
					responseFlowPacketModel = null;
					syncObj.clear();
				}
				if (responseFlowPacketModel == null) {
					syncObj.setPacketSerialNo(packetModel.getSeriaNo());
					sendPacketData(out, packetModel);
					responseFlowPacketModel = syncObj.poll(FlowConstants.MAX_WAIT_TIME, TimeUnit.SECONDS);
				}

				if (responseFlowPacketModel == null) {
					// 如果packetModel是null，说明服务端没有回应
					if (syncObj.getSendTimes() >= FlowConstants.MAC_RESEND_TIME) {// 超出最大重发次数
						int n = syncObj.getSendTimes();
						syncObj.setSendTimes(0);
						throw new SimpleException("向服务端（" + messageSenderService.getDevice().getDeviceId() + "--" + messageSenderService.getDevice().getIp() + "）发送指令（" + JSONObject.toJSON(packetModel).toString() + "），超出" + n + "次未响应，任务结束");
					}
					syncObj.setSendTimes(syncObj.getSendTimes() + 1);
					LOG.debug("向服务端（{}--{}）发送指令（{}）未响应，重发第{}次", messageSenderService.getDevice().getDeviceId(), messageSenderService.getDevice().getIp(), JSONObject.toJSON(packetModel).toString(), syncObj.getSendTimes());
					packetModel.setSeriaNo(FlowSeriaNo.getNextSerialNo());
					return sendRequest(packetModel, syncObj, messageSenderService);
				} else {
					syncObj.setSendTimes(0);
					while (syncObj.size() > 0) {
						responseFlowPacketModel = syncObj.poll();
					}
					return responseFlowPacketModel;
				}
			}
		} catch (InterruptedException | IOException e) {
			TCPClientFactory.reconnect(messageSenderService.getDevice().getIp());
			LOG.error("发送命令失败！", e);
		}
		return null;
	}

}
