from micloud import MiCloud
import json

if __name__ == '__main__':
    mc = MiCloud("15972207686@163.com", "Lifangtc00")
    mc.login()
    device_list = mc.get_devices(country="cn")
    info = json.dumps(device_list)
    f2 = open('socket-info-micloud.json', 'w')
    f2.write(info)
    f2.close()

