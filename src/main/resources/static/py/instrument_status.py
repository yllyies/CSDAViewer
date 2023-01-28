import json
import sys
from miio.device import Device
from concurrent.futures import ThreadPoolExecutor, as_completed, wait, ALL_COMPLETED


def get_instrument_info(id, ip, token):
    s = Device(ip=ip, token=token)
    power_status = s.send("get_properties", [{'did': 'MYDID', 'siid': 11, 'piid': 2}])
    return {'id': id, 'power': power_status[0].get('value')}


if __name__ == '__main__':
    # 读取json
    params = json.loads(sys.argv[1])
    # 多线程执行获取信息方法
    response = []
    executor = ThreadPoolExecutor(max_workers=2)
    all_task = [executor.submit(get_instrument_info, item['id'], item['ip'], item['token']) for item in params]

    for future in as_completed(all_task):
        response.append(future.result())
    print(response)

# 测试方法
# from miio.device import Device
#
# if __name__ == '__main__':
#     device = Device(ip="192.168.120.44", token="19bc2330c330c048b5721195523906d7")
#     # device = Device(ip=sys.argv[1], token=sys.argv[2])
#     # # siid:11, piid:2 获取当前功率接口
#     power_status = device.send("get_properties", [{'did': 'MYDID', 'siid': 11, 'piid': 2}])
#     print(power_status[0].get('value'))