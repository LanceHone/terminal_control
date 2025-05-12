import time
import json
import logging

logging.basicConfig(
    filename="/var/access/mdb.log",
    filemode="a",  # 追加模式
    format="%(asctime)s - %(message)s",
    datefmt="%Y-%m-%d %H:%M:%S",
    level=logging.INFO
)

class ModBusDataFrame():
    request = None
    def __init__(self, data):
        self.config = None
        self.data = data
        self.config = self.readConfig()
        if len(data) >= 7:
            self.tid = self.readInt(data[0:2])
            self.pid = self.readInt(data[2:4])
            self.len = self.readInt(data[4:6])
            self.uid = data[6]
            self.fid = data[7]
            self.dataLength = 0
            self.dataArray = []
            if self.fid == 6 or self.fid == 5 or self.fid == 0x0F:
                self.addr = self.readInt(data[8:10])
                self.number = self.readInt(data[10:12])
            else:
                if len(data) == 12:
                    self.addr = self.readInt(data[8:10])
                    self.number = self.readInt(data[10:12])
                    ModBusDataFrame.request = self
                else:
                    self.addr = -1
                    self.number = -1
                    if ModBusDataFrame.request:
                        self.addr = ModBusDataFrame.request.addr
                        self.number = ModBusDataFrame.request.number
                    if self.fid == 3 or self.fid == 4:
                        self.dataLength = int(data[8] / 2)
                        self.dataArray = self.parseArray(data[9:])
                    elif self.fid == 2 or self.fid == 1:
                        self.dataLength = data[8]
                        self.dataArray = self.parseXArray(data[9:])
    def build(self):
        if self.fid >= 5:
            return
        buf = []
        if self.fid == 3 or self.fid == 4:
            for k in self.dataArray:
                buf.append(k >> 8 & 0xFF)
                buf.append(k >> 0 & 0xFF)
            return self.data[0:9] + bytes(buf)
        else:
            longstr = "".join(self.dataArray)
            while len(longstr) > 8:
                buf.append(int(longstr[0:8],2))
                longstr = longstr[8:]
            if len(longstr) > 0:
                buf.append(int(longstr,2))
            return self.data[0:9] + bytes(buf)


    def parseArray(self,data):
        buf = []
        for i in range(0,self.dataLength):
            buf.append(self.readInt(data[i*2:i*2+2]))
        return buf
    def parseXArray(self,data):
        buf = []
        for i in range(0,self.dataLength):
            temp = bin(data[i])[2:]
            while len(temp) < 8:
                temp = "0" + temp
            buf += list(temp)
        return buf

    def readInt(self, data):
        return data[0] << 8 | data[1]

    def readConfig(self):
        if self.config and time.time() - self.config["time"] < 5:  # 每5秒读一次
            return
        try:
            with open("/var/access/mdbf.json", "r", encoding="utf-8") as f:
                return {"time": time.time(), "data": json.loads(f.read())}
        except Exception as e:
            return

def write_log(message):
    logging.info(message)

def ModBusFilter(data):
    f = ModBusDataFrame(data)
    log_message = f"TID={f.tid},PID={f.pid},LEN={f.len},UID={f.uid},FUNC={f.fid},ADDR={f.addr},NUMBER={f.number}"
    print(log_message)
    # print(f.config)
    if not f.config:
        return data
    rules = f.config["data"]
    for r in rules:
        if int(r['deviceId']) != f.uid:
            continue
        if f.fid < 5:
            if int(r['functionCode']) == f.fid and int(r['rw']) == 0:
                # 存在改功能码对应的规则
                base_addr = 1
                if f.fid == 2:
                    base_addr += 10000
                elif f.fid == 3:
                    base_addr += 40000
                else:
                    base_addr += 30000
                start = base_addr + f.addr
                end = start + f.dataLength
                r_range = r["registerAddress"].split("~")
                v_range = r["valueRange"].split("~")
                data = f.dataArray
                if start <= int(r_range[1]) and end >= int(r_range[0]):
                    # 存在重叠
                    # 把非法的值替换为0
                    for i in range(len(data)):
                        if int(r_range[0]) <= start + i and int(r_range[1]) >= start + i:
                            data[i] = 0
                        elif data[i] > int(v_range[1]) or data[i] < int(v_range[0]):
                            data[i] = 0
                        write_log(f"{log_message},type=PASS")
                        return data
        else:
            if (int(r['rw']) == 1 and int(r['functionCode']) == 3):
                r_range = r["registerAddress"].split("~")
                v_range = r["valueRange"].split("~")
                if f.addr + 40001 <= int(r_range[1]) and f.addr + 40001 >= int(r_range[0]):
                    if f.number <= int(v_range[1]) and f.number >= int(v_range[0]):
                        write_log(f"{log_message},type=PASS")
                        return data
            if (int(r['rw']) == 1 and int(r['functionCode']) == 1):
                r_range = r["registerAddress"].split("~")
                v_range = r["valueRange"].split("~")
                if f.addr + 1 <= int(r_range[1]) and f.addr + 1 >= int(r_range[0]):
                    if f.number <= int(v_range[1]) and f.number >= int(v_range[0]):
                        write_log(f"{log_message},type=PASS")
                        return data
    write_log(f"{log_message},type=REJECT")

# 确保程序退出时关闭日志文件
import atexit
atexit.register(logging.shutdown)
