import sys, json
from miio.gateway import Gateway
from miio.gateway.devices.subdevice import SubDevice, SubDeviceInfo

if __name__ == '__main__':
    # 网关注册
    g = Gateway(ip=sys.argv[1], token=sys.argv[2])
    dev_info = SubDeviceInfo(sys.argv[3], 19, -1, -1, -1)
    # 子设备注册
    device = SubDevice(g, dev_info, {'zigbee_id': 'lumi.weather.v1'})
    info = device.get_property_exp(["temperature", "humidity"])
    print({"temperature": info[0], "humidity": info[1]})