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
    def __init__(self, data):
        self.config = None
        self.config = self.readConfig()
        if len(data) >= 7:
            self.tid = self.readInt(data[0:2])
            self.pid = self.readInt(data[2:4])
            self.len = self.readInt(data[4:6])
            self.uid = data[6]
            self.fid = data[7]
            if self.fid == 6 or self.fid == 5 or self.fid == 0x0F:
                self.addr = self.readInt(data[8:10])
                self.number = self.readInt(data[10:12])
            else:
                self.addr = -1
                self.number = -1

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
    # print(log_message)
    # print(f.config)

    rules = f.config["data"]
    for r in rules:
        if int(r['deviceId']) != f.uid:
            continue
        if f.fid < 5:
            if int(r['functionCode']) == f.fid:
                write_log(f"{log_message},type=PASS")
                return data
        else:
            if (int(r['functionCode']) == 3 and int(r['rw']) == 1):
                r_range = r["registerAddress"].split("~")
                v_range = r["valueRange"].split("~")
                if f.addr + 40001 <= int(r_range[1]) and f.addr + 40001 >= int(r_range[0]):
                    if f.number <= int(v_range[1]) and f.number >= int(v_range[0]):
                        write_log(f"{log_message},type=PASS")
                        return data
    write_log(f"{log_message},type=REJECT")

# 确保程序退出时关闭日志文件
import atexit
atexit.register(logging.shutdown)