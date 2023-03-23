import json
import sys
from miio.device import Device
from concurrent.futures import ThreadPoolExecutor, as_completed

def get_instrument_info(instrumentName, ip, token):
    s = Device(ip=ip, token=token)
    try:
        power_status = s.send("get_properties", [{'did': 'MYDID', 'siid': 11, 'piid': 2}])
    except Exception:
        return {'instrumentName': instrumentName, 'ip': ip, 'token': token, 'power': ""}
    else:
        return {'instrumentName': instrumentName, 'ip': ip, 'token': token, 'power': power_status[0].get('value')}

if __name__ == '__main__':
    # 读取json
    params = json.loads(sys.argv[1])
    # 多线程执行获取信息方法
    response = []
    executor = ThreadPoolExecutor(max_workers=1)
    all_task = [executor.submit(get_instrument_info, item['instrumentName'], item['ip'], item['token']) for item in params]

    for future in as_completed(all_task):
        response.append(future.result())
    print(response)