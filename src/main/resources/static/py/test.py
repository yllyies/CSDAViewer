import miio
from miio.device import Device

def info():
    s = miio.device.Device(ip='192.168.120.44', token='19bc2330c330c048b5721195523906d7')
    s.info()

from miio.chuangmi_plug import ChuangmiPlug
d.send("get_properties", [{'did': 'MYDID', 'siid': 11, 'piid': 2}])
from miio.device import Device
d = Device(ip="192.168.120.44", token="19bc2330c330c048b5721195523906d7")
d = PowerStripStatus(ip="192.168.120.44", token="19bc2330c330c048b5721195523906d7")
d = ChuangmiPlug(ip="192.168.120.40", token="634690c33dcf8b12ced68510cc5bb8fc")
x=d.status() # 给出设备的状态

cuco.plug.v3 v1.0.3 (68:AB:BC:B0:4D:C3) @ 192.168.120.44 - token: 19bc2330c330c048b5721195523906d7

miiocli -d device --ip "192.168.120.44" --token "19bc2330c330c048b5721195523906d7" raw_command get_properties "[{'did': 'MYDID', 'siid': 11, 'piid': 1 }]"

miiocli -d device --ip YOUR_DEVICE_IP --token YOUR_DEVICE_TOKEN raw_command get_properties "[{'did': 'MYDID', 'siid': 11, 'piid': 1 }]"

from miio import DeviceFactory

dev = DeviceFactory.create("192.168.120.44", "19bc2330c330c048b5721195523906d7")
dev.info()

#方式一
# miiocli -d device --ip "192.168.120.44" --token "19bc2330c330c048b5721195523906d7" raw_command get_properties "[{'did': 'MYDID', 'siid': 11, 'piid': 2 }]"
#方式二
import miio
from miio.device import Device

if __name__ == '__main__':
    s = miio.device.Device(ip='192.168.120.44', token='19bc2330c330c048b5721195523906d7')
    s.info()