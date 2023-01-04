from miio.device import Device

if __name__ == '__main__':
    device = Device(ip="192.168.120.44", token="19bc2330c330c048b5721195523906d7")
    # siid:11, piid:2 获取当前功率接口
    power_status = device.send("get_properties", [{'did': 'MYDID', 'siid': 11, 'piid': 2}])
    print(power_status[0].get('value'))

