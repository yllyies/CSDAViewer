import sys, json
from micloud import MiCloud

if __name__ == '__main__':
    mc = MiCloud(sys.argv[1], sys.argv[2])
    success = mc.login()
    responseList = mc.get_devices(country="cn")
    obj = list(filter(lambda item: item['did'] in sys.argv[3].split(";"), responseList))
    print(json.dumps(obj))

# if __name__ == '__main__':
#     mc = MiCloud("15972207686@163.com", "lifang420418")
#     mc.login()
#     responseList = mc.get_devices(country="cn")
#     obj = list(filter(lambda item: item['did'] in 'lumi.158d0009412d6c'.split(";"), responseList))
#     print(obj[0]['desc'])