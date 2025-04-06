//import net.wimpi.modbus.Modbus;
//import net.wimpi.modbus.ModbusException;
//import net.wimpi.modbus.io.ModbusTCPTransaction;
//import net.wimpi.modbus.msg.ReadInputRegistersRequest;
//import net.wimpi.modbus.msg.ReadInputRegistersResponse;
//import net.wimpi.modbus.net.TCPMasterConnection;
//
//public class ModbusTCPExample {
//
//    public static void main(String[] args) {
//        try {
//            TCPMasterConnection connection = new TCPMasterConnection();
//            connection.setAddress("localhost");
//            connection.setPort(502);
//            connection.connect();
//
//            ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
//            transaction.setRequest(new ReadInputRegistersRequest(0, 10));
//
//            transaction.execute();
//            ReadInputRegistersResponse response = (ReadInputRegistersResponse) transaction.getResponse();
//
//            if (response != null) {
//                int[] values = response.getRegisterValues();
//                for (int value : values) {
//                    System.out.println("Value: " + value);
//                }
//            } else {
//                System.out.println("No response");
//            }
//
//            connection.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//}