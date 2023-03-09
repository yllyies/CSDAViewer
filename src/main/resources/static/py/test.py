from miio.device import Device
from micloud import MiCloud
from miio.gateway import Gateway
from miio.gateway.devices.subdevice import SubDevice, SubDeviceInfo


def info2():
    s = Device(ip='169.254.234.11', token='3788ccc06c7a52ca1327fb44fa6f30d6')
    msg = s.send("get_properties", [{'did': 'MYDID', 'siid': 11, 'piid': 2}])
    print(msg)

def info3():
    # 网关注册
    g = Gateway(ip='169.254.234.14', token='3245666c3339413237476d694f4f3654')
    dev_info = SubDeviceInfo('lumi.158d0009412d6c', 19, -1, -1, -1)
    # 子设备注册
    device = SubDevice(g, dev_info, {'zigbee_id': 'lumi.weather.v1'})
    info = device.get_property_exp(["temperature", "humidity"])
    print({"temperature": info[0], "humidity": info[1]})

if __name__ == '__main__':
    # info2()
    info3()
# from miio.chuangmi_plug import ChuangmiPlug
# d.send("get_properties", [{'did': 'MYDID', 'siid': 11, 'piid': 2}])
# from miio.device import Device
# d = Device(ip="192.168.120.44", token="19bc2330c330c048b5721195523906d7")
# d = PowerStripStatus(ip="192.168.120.44", token="19bc2330c330c048b5721195523906d7")
# d = ChuangmiPlug(ip="192.168.120.40", token="634690c33dcf8b12ced68510cc5bb8fc")
# x=d.status() # 给出设备的状态

# cuco.plug.v3 v1.0.3 (68:AB:BC:B0:4D:C3) @ 192.168.120.44 - token: 19bc2330c330c048b5721195523906d7
#
# miiocli -d device --ip "192.168.120.44" --token "19bc2330c330c048b5721195523906d7" raw_command get_properties "[{'did': 'MYDID', 'siid': 11, 'piid': 1 }]"
#
# miiocli -d device --ip YOUR_DEVICE_IP --token YOUR_DEVICE_TOKEN raw_command get_properties "[{'did': 'MYDID', 'siid': 11, 'piid': 1 }]"
#
# from miio import DeviceFactory
#
# dev = DeviceFactory.create("192.168.120.44", "19bc2330c330c048b5721195523906d7")
# dev.info()
#
# #方式一
# # miiocli -d device --ip "192.168.120.44" --token "19bc2330c330c048b5721195523906d7" raw_command get_properties "[{'did': 'MYDID', 'siid': 11, 'piid': 2 }]"
# #方式二
# import miio
# from miio.device import Device
#
# def info():
#     mc = MiCloud("15972207686@163.com", "lifang420418")
#     mc.login()
#     device_list = mc.get_devices(country="cn")
#     obj = list(filter(lambda item: item['did'] == 'lumi.158d0009412d6c', device_list))[0]
#     print("总览：", obj['desc'], "，湿度", (int(obj['prop']['humidity']) / 100).__round__(0), "%", "，温度", (int(obj['prop']['temperature']) / 100).__round__(0),"℃")
