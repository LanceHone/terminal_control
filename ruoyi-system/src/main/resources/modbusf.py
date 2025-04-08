import time
import json
class ModBusDataFrame():
	def __init__(self,data):
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
	def readInt(self,data):
		return data[0] << 8 | data[1]

	def readConfig(self):
		if self.config and time.time() - self.config["time"] < 5: #每5秒读一次
			return
		try:
			with open("/var/access/mdbf.json","r",encoding="utf-8") as f:
				return {"time":time.time(),"data":json.loads(f.read())}
				f.close()

		except Exception as e:

			return

def ModBusFilter(data):
	f = ModBusDataFrame(data)
	print(f"TID={f.tid} PID={f.pid} LEN={f.len} UID={f.uid} FUNC={f.fid} ADDR={f.addr} NUMBER={f.number}")
	print(f.config)

	rules = f.config["data"]
	for r in rules:
		if int(r['deviceId']) != f.uid : continue
		if f.fid < 5 and f.id != 3:
			if int(r['functionCode']) == f.fid:
				return data
		else:
			if (int(r['rw']) == 0): continue 
			r_range = r["registerAddress"].split("~")
			v_range = r["valueRange"].split("~")
			if f.addr + 40000 <= int(r_range[1]) or f.addr + 40000 >= int(r_range[0]):
				if f.number <= int(v_range[1]) or f.number >= int(v_range[0]):
					return data


	# 控制处理读取限制
	# readable = "是否允许读取的配置"
	# writeable = {"enabled":"是否禁止","addr":"寄存器地址限制","range":"范围限制"}

	# if not readable:
	# 	if f.fid <= 4:
	# 		return

	# #控制处理写入限制
	# if writeable and not writeable["enabled"]:
	# 	if f.fid >= 5:
	# 		return
	# 	if f.addr == int(writeable["addr"]):
	# 		if writeable["range"]: #限制写入范围
	# 			ab = writeable["range"].split("~")
	# 			if f.number < int(ab[0]) or f.number > int(ab[1]):
	# 				return

	# 通过验证可以放行数据
	# return data


#测试解析写入指令
# ModBusFilter(b"\x03\x00\x00\x00\x00\x06\x01\x06\x00\x12\x00\xff")
#测试解析读取指令
# ModBusFilter(b"\x03\x00\x00\x00\x00\x06\x01\x03\x00\x00\x00\n")