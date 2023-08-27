import json
import sys
import numpy as np
from sklearn.cluster import KMeans
from sklearn.metrics import silhouette_score

if __name__ == '__main__':
    instrumentNameToPowersDict = json.loads(sys.argv[1])
    response = {}
    for instrumentName,powerList in instrumentNameToPowersDict.items():
        x = np.array(powerList).reshape(-1, 1)
        # 轮廓系数法确定k值（2）
        model = KMeans(n_init="auto", n_clusters=2, max_iter=100)
        model.fit(x)
        # 质心数值，返回档位功率数组
        c = np.sort(model.cluster_centers_.round(0).flatten()).tolist()
        response[instrumentName] = c
    print(response)